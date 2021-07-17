package Board;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

import static Board.WBSettings.useraccept ;
import static Board.WBSettings.userreject;
import java.awt.event.ActionListener;



/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class Timeoptions {
    
	// The time it takes 
    private int timesecs = 0;

    
    // output displayotpt. 
    private JLabel displayotpt = new JLabel();

    
    // Dialog options 
    private JDialog commncn = null;

   
    // when the user would get rejected.ss
    private boolean output = userreject ;

    
    
    // master - > The master frame. 
    // info -> The output to be displayed. 
    // sec -> time seconds it would take (as part of the count down).
    // The function would return if the user has been accepted or is rejected. 
    
    public boolean showDialog(JFrame master, String info, int sec) {
    	
        timesecs = sec;
        displayotpt.setText(info);
        displayotpt.setBounds(80,6, 400, 40);

  
        JButton confirm = new JButton("USER ACCEPTED");
        
        confirm.setBounds(100,40,80,20);
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output = useraccept ;
                master.setEnabled(true);
                Timeoptions.this.commncn.dispose();
            }
        });


        JButton cancel = new JButton("USER REJECTED");
        cancel.setBounds(270,40,80,20);
        cancel.addActionListener(new ActionListener() {
        	
          // Method override
            public void actionPerformed(ActionEvent e) {
                output = userreject ;
                master.setEnabled(true);
                Timeoptions.this.commncn.dispose();
            }
        });

        // To configure the dialog options.
        
        commncn = new JDialog(master, true);
        master.setEnabled(false);
        commncn.setTitle("The request would be auto rejected after"+" "+timesecs+"seconds");
        commncn.setLayout(null);
        commncn.add(displayotpt);
        commncn.add(confirm);
        commncn.add(cancel);

        // scheduler for counting down and to update the Graphical UI.
        // To select cancel for the auto timeout options.
        
        ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
        s.scheduleAtFixedRate(new Runnable() {
        	
            // Method Over ride. 
        	
            public void run() {
                Timeoptions.this.timesecs--;
                if (Timeoptions.this.timesecs == 0) {
                    cancel.doClick();
                }else {
                	commncn.setTitle("The request will be auto rejected after"+" "+ timesecs +"seconds of time");
                }
            }
        }, 1, 1, TimeUnit.SECONDS);

        commncn.pack();
        commncn.setSize(new Dimension(500,150));
        commncn.setLocationRelativeTo(master);
        commncn.setVisible(true);

        return output;
    }
}
