package Board;

import remote.CanvasofIRemote;
import remote.UserListofIremote;

import javax.swing.*;
import javax.swing.JComponent;

import Connection.Cnncnforclnt;

import static Board.Paintsettings.*;
import static Board.Util.popupDialog;
import static Board.Util.popupErrorDialog;
import static Board.Util.popupNoServerConnectionErrorDialog;
import static Board.WBSettings.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class WhiteBoardApp extends JComponent {
    
	// The boolean returns true if the current user is the Manager. 
    private boolean mngrtrue;

    
    // The window Object. 
    private JFrame window;

    // A unique user id for each user.
    private String idforuser;

    // To update the user thread from the remote object. 
    private Thread userlistthrdupdt;
    
 // A remote user list 
    private UserListofIremote userlistrmt;
    
    // The Client's connection to the server. 
    private Cnncnforclnt conncnclient;

   
  
    // Did the user get kicked out by the manager. 
    private boolean isKickedOut = false;

    // To paint on the Canvas Manager. 
    private PaintManager paintManager;

    
    // FileMenu
    private MenuOptions fileMenu;

    
    public WhiteBoardApp(boolean mngrtrue) {
        this.mngrtrue = mngrtrue;

        // initialize paintManager
        this.paintManager = new PaintManager();

        // setup window
        window = new JFrame();
        window.setLayout(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // When close the window, it should remove its information in the system.
        window.addWindowListener(new WindowAdapter() {
        	// Method override
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                if (mngrtrue) {
                    paintManager.clearCanvas();
                }
                closeFrame(e);
            }
        });
        
        window.setSize(1000, 800);
        window.setResizable(false);

        String appTitle;
        if (this.mngrtrue) {
        	
            // setup manager's app name
            appTitle = TITLEOFAPPLCN + " (Manager)";
            window.setTitle(appTitle);

            // setup the file menu for the manager
            JMenuBar menuBar = new JMenuBar();
            window.setJMenuBar(menuBar);
            fileMenu = new MenuOptions(window, paintManager);
            menuBar.add(fileMenu);

            // setup the label for kickout user
            JLabel kickOutLabel = new JLabel("Kick out an user:");
            kickOutLabel.setSize(kickOutLabel.getPreferredSize());
            kickOutLabel.setLocation(700, 450);
            window.add(kickOutLabel);

            // setup the inputting file for user to be kickedout
            JTextField kickOutTextField = new JTextField();
            kickOutTextField.setBounds(700, 470, 250, 50);
            window.add(kickOutTextField);

            // setup button to confirm the kickout of the user
            JButton kickOutButton = new JButton("Kick out");
            kickOutButton.setBounds(700, 530, 250, 30);
            kickOutButton.addActionListener(new ActionListener() {
            	// Method override. 
                @Override
                public void actionPerformed(ActionEvent e) {
                    // kickout a normal user
                    String kickOutUID = kickOutTextField.getText();
                    System.out.println("kick out: " + kickOutTextField.getText());
                    try {
                        if (kickOutUID.equals(userlistrmt.getManagerName())) {
                            popupDialog("Can't kick out yourself");
                        } else {
                            conncnclient.kickOut(kickOutUID);
                        }
                    } catch (RemoteException e1) {
                        popupNoServerConnectionErrorDialog();
                    }
                }
            });
            window.add(kickOutButton);
        } else {
            // setup normal user's app name
            appTitle = TITLEOFAPPLCN + " (User)";
            window.setTitle(appTitle);
        }

        // set up  users display label
        JLabel jLabel = new JLabel("Users: username (idforuser)");
        jLabel.setSize(jLabel.getPreferredSize());
        jLabel.setLocation(700, 5);
        window.add(jLabel);

        // paint tool selection setup
        // Circle Button
        JRadioButton circleButton = new JRadioButton(CIRCLE);
        circleButton.setBounds(10, 5, 60, 40);
        circleButton.setActionCommand(CIRCLE);
        circleButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);
        circleButton.doClick();

        // Rectangle Button 
        JRadioButton rectangleButton = new JRadioButton(RECTANGLE);
        rectangleButton.setBounds(80, 5, 100, 40);
        rectangleButton.setActionCommand(RECTANGLE);
        rectangleButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        // Radio Button 
        JRadioButton lineButton = new JRadioButton(LINE);
        lineButton.setBounds(190, 5, 60, 40);
        lineButton.setActionCommand(LINE);
        lineButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        // Text Button
        JRadioButton textButton = new JRadioButton(TEXT);
        textButton.setBounds(260, 5, 60, 40);
        textButton.setActionCommand(TEXT);
        textButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        // Pen Button. 
        JRadioButton penButton = new JRadioButton(PEN);
        penButton.setBounds(330, 5, 60, 40);
        penButton.setActionCommand(PEN);
        penButton.addActionListener(paintManager.PAINT_TOOL_ACTION_LISTENER);

        ButtonGroup group = new ButtonGroup();
        group.add(circleButton);
        group.add(rectangleButton);
        group.add(lineButton);
        group.add(textButton);
        group.add(penButton);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setBounds(0, 0, 450, 40);
        buttonPanel.add(circleButton);
        buttonPanel.add(rectangleButton);
        buttonPanel.add(lineButton);
        buttonPanel.add(textButton);
        buttonPanel.add(penButton);

        window.add(buttonPanel);

        // canvas setup
        WBPanelCanvas whiteboardCanvasPanel = new WBPanelCanvas(paintManager);
        whiteboardCanvasPanel.setBounds(10, 50, CANVAS_WIDTH, CANVAS_HEIGHT);
        window.add(whiteboardCanvasPanel);
    }

   
    // To set the client connection. 
    public void setClientConnection(Cnncnforclnt conncnclient) {
        this.conncnclient = conncnclient;
        if (this.mngrtrue) {
            this.fileMenu.setClientConnection(conncnclient);
        }
    }

    // To set the remote canvas. 
    public void setRemoteCanvas(CanvasofIRemote remoteCanvas) {
        paintManager.setRemoteCanvas(remoteCanvas);
    }

   
    // To set the remote user list. 
    public void setRemoteUserList(UserListofIremote userlistrmt) {
        JTextArea jTextArea = new JTextArea();
        jTextArea.setEditable(false);
        jTextArea.setBounds(700, 30, 250, 400);
        window.add(jTextArea);

        this.userlistrmt = userlistrmt;

        // thread to update users in whiteboard
        userlistthrdupdt = new Thread() {
        	// method override
            @Override
            public void run() {
                super.run();

                while (true) {

                    StringBuilder newText = new StringBuilder();

                    try {
                        // has manager
                        if (userlistrmt.getManagerName() != null) {
                            newText.append("(*) ").append(userlistrmt.getManagerName()).append("\n");
                            for (String s : userlistrmt.getUserNames()) {
                                newText.append("(-) ").append(s).append("\n");
                            }
                        }

                        if (!newText.toString().equals(jTextArea.getText())) {
                            jTextArea.setText(newText.toString());
                        }

                    } catch (RemoteException e) {
                        popupNoServerConnectionErrorDialog();
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

        userlistthrdupdt.start();
    }

    
    // The unique user id.  
    public void start(String idforuser) {
        this.idforuser = idforuser;

        window.setVisible(true);
    }

    
    // Error message. 
    public void error(String msg) {
        popupErrorDialog(msg);
        stop();
    }

    
    // To stop the background application. 
    public void stop() {
        userlistthrdupdt.interrupt();
    }

    
    // To see if the user is a manager or not 
    public boolean mngrtrue() {
        return mngrtrue;
    }
    
    public boolean askAcceptCandidate(String candidateUID) {
        Timeoptions dialog = new Timeoptions();
        return dialog.showDialog(window, candidateUID + WHITEBOARDSHARE, TIMEAUTOREJECT);
    }

   
    // To kickout a user 
    public void kickedOut() {
        window.setTitle(window.getTitle() + "Kicked out");
        stop();
        popupDialog("You have been Kicked out by the Manager");
        isKickedOut = true;
        closeFrame();
    }

    
    // To close the window.
    private void closeFrame() {
        if (!isKickedOut) {
            conncnclient.disconnect(mngrtrue, idforuser);
        }
        window.dispose();
        System.exit(0);
    }

    
    // To close an existing frame. 
    
    private void closeFrame(WindowEvent e) {
        if (!isKickedOut) {
            conncnclient.disconnect(mngrtrue, idforuser);
        }
        stop();
        e.getWindow().dispose();
        System.exit(0);
    }

   
    // When the whiteboard gets closed by the manager. 
    public void closeByManager() {
        window.setTitle(window.getTitle() + " - [Closed]");
        stop();
        popupDialog("Closed by the manager");
        isKickedOut = true;
        closeFrame();
    }

    
    // A popup message for the user. 
    public void notifyUser(String msg) {
        popupDialog(msg);
    }
}
