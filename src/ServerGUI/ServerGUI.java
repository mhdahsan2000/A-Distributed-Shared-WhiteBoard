package ServerGUI;

import remote.UserListofIremote;

import javax.swing.*;

import static Board.Paintsettings.REMOTE_OBJECT_UI_UPDATE_RATE;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class ServerGUI {

    private JFrame frame;

    private Thread updateUIThread;

    public ServerGUI(UserListofIremote userList) {
        frame = new JFrame();
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
        	// Method override. 
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Closed");
                if (updateUIThread != null) {
                    updateUIThread.interrupt();
                }
                System.exit(0);
            }
        });

        // To setup the frame.
        frame.setSize(500, 400);
        frame.setResizable(false);
        frame.setTitle(" COMP90015 (1048942) A Distributed Whiteboard Server - Server Panel");

        JLabel managerLabel = new JLabel("MANAGER:");
        managerLabel.setBounds(0, 0, 70, 20);
        frame.add(managerLabel);

        JTextArea managerText = new JTextArea();
        managerText.setEditable(false);
        managerText.setBounds(70, 5, 400, 20);
        frame.add(managerText);

        // userlabel
        JLabel userLabel = new JLabel("USERS:");
        userLabel.setBounds(0, 30, 70, 20);
        frame.add(userLabel);

        // usertext
        JTextArea userText = new JTextArea();
        userText.setEditable(false);
        userText.setBounds(70, 30, 400, 250);
        frame.add(userText);

        // tread to update user list GUI periodically
        updateUIThread = new Thread() {
            @Override
            public void run() {
                super.run();

                while (true) {
                    try {
                        String managerName = userList.getManagerName();
                        if (managerName!= null && !managerName.equals(managerText.getText())) {
                            managerText.setText(managerName);
                        }

                        StringBuilder newText = new StringBuilder();
                        for (String s: userList.getUserNames()) {
                            newText.append(s).append("\n");
                        }

                        if (!newText.toString().equals(userText.getText())) {
                            userText.setText(newText.toString());
                        }

                    } catch (RemoteException e) {
                        System.out.println("Remote object error");
                        break;
                    }

                    try {
                        Thread.sleep(REMOTE_OBJECT_UI_UPDATE_RATE);
                    } catch (InterruptedException e) {
                        System.out.println("Thread sleep error");
                        break;
                    }
                }
            }
        };
        
        updateUIThread.start();
        frame.setVisible(true);
    }
}
