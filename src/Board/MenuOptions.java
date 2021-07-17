package Board;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import Connection.Cnncnforclnt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static Board.Util.*;
import static Connection.Connectionsettings.*;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */

public class MenuOptions extends JMenu {
    
	// To Make the connections to the server. 
    private Cnncnforclnt clientConnection;

    // File Menu Options.
    
    public MenuOptions(Frame frame, PaintManager paintManager) {
    	
        super("Press for file options(Alt+F)");
        this.clientConnection = null;
        setMnemonic(KeyEvent.VK_F);

        // To Create a new canvas.
        JMenuItem newMenuItem = new JMenuItem("NEW", KeyEvent.VK_N);
        newMenuItem.addActionListener(new ActionListener() {
        	
            // Method override. 
            public void actionPerformed(ActionEvent e) {
            	
                System.out.println("The key NEW has been pressed.");
                
                //To clear the canvas.
                
                paintManager.clearCanvas();
                paintManager.clearPoints();
                
                //To send custom notifications.
                if (clientConnection != null) {
                    clientConnection.notifyUserWithManagerOperation(MANAGER_NEW);
                }
            }
        });
        
        // To add a new Menu Item.
        
        add(newMenuItem);
        
        // To Open a new canvas.
        JMenuItem open = new JMenuItem("OPEN", KeyEvent.VK_O);
        
        open.addActionListener(new ActionListener() {
        	
            // Method overriding. 
        	
            public void actionPerformed(ActionEvent e) {
                System.out.println("OPEN has been clicked now.");

                // To Select a canvas.
                
                final JFileChooser selectfile = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("FILE IMAGE", TYPEOFIMG);
                selectfile.setFileFilter(filter);
                
                // To Open the dialog using null as parent component if you are outside a Java Swing application otherwise provide the parent comment instead.
                
                int returnVal = selectfile.showOpenDialog(frame);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                	
                    // Retrieve the selected file.
                	
                    File file = selectfile.getSelectedFile();
                    try (FileInputStream fis = new FileInputStream(file)) {
                    } catch (FileNotFoundException e1) {
                        popupDialog("File not found.");
                        return;
                    } catch (IOException e1) {
                        popupDialog("Read file fail.");
                        return;
                    }
                    // read canvas and update
                    BufferedImage img = null;
                    try {
                        img = ImageIO.read(file);
                        paintManager.setImage(img);
                        popupDialog("Successful open image: " + file.getName());
                        if (clientConnection != null) {
                            clientConnection.notifyUserWithManagerOperation(MANAGER_OPEN);
                        }
                    } catch (IOException e1) {
                        popupDialog("Read image fail.");
                    }
                }
            }
        });
        add(open);

        // save a canvas to a auto generated file
        JMenuItem save = new JMenuItem("Save", KeyEvent.VK_S);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save pressed");
                String fileName = generateAutoFileName();
                paintManager.saveWhiteBoard(fileName);
            }
        });
        add(save);

        // save a canvas to a specified file
        JMenuItem saveAs = new JMenuItem("Save As", KeyEvent.VK_A);
        saveAs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Save As pressed");
                // select the location to be saved
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("IMAGE FILES", TYPEOFIMG);
                fc.setFileFilter(filter);
                int returnVal = fc.showSaveDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    // Retrieve the selected file
                    File file = fc.getSelectedFile();
                    String fileName = file.getPath();
                    if (!fileName.endsWith("." + TYPEOFIMG)) {
                        fileName += "." + TYPEOFIMG ;
                    }
                    // setup save to file
                    file = new File(fileName);
                    boolean result = false;
                    try {
                        result = file.createNewFile();
                    } catch (IOException e1) {
                        popupDialog("File creation fail: " + file);
                        return;
                    }
                    if (result) {
                        System.out.println("    | Successful create image file: " + file);
                    }
                    // save to file
                    paintManager.saveWhiteBoard(fileName);
                }
            }
        });
        add(saveAs);

        // close the application
        JMenuItem close = new JMenuItem("Close", KeyEvent.VK_C);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Close pressed");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
        add(close);
    }

    
    // To set the Client Connection. 
    public void setClientConnection(Cnncnforclnt clientConnection) {
        this.clientConnection = clientConnection;
    }
}
