package draw;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
 

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */

// The Paint Manager. 
public class PaintManager {
 
  // DrawArea of the board.
  Board drawArea;
  
  // Buttons 
  JButton clearBtn, blackBtn, blueBtn, greenBtn, redBtn, magentaBtn;

  // ActionListener.
  ActionListener actionListener = new ActionListener() {
 
  // Performed Actions. 
  public void actionPerformed(ActionEvent e) {
      if (e.getSource() == clearBtn) {
        drawArea.AllClear();
      } else if (e.getSource() == blackBtn) {
        drawArea.black();
      } else if (e.getSource() == blueBtn) {
        drawArea.blue();
      } else if (e.getSource() == greenBtn) {
        drawArea.green();
      } else if (e.getSource() == redBtn) {
        drawArea.red();
      } else if (e.getSource() == magentaBtn) {
        drawArea.magenta();
      }
    }
  };
 
  // Main method. 
  
  public static void main(String[] args) {
	  
    new PaintManager().display();
    
  }
  
  // display. 
  public void display() {
	  
    // create main frame.
    JFrame frame = new JFrame("Swing Paint");
    Container frm = frame.getContentPane();
    
    // set layout on content pane.
    frm.setLayout(new BorderLayout());
    
    // create draw area.
    drawArea = new Board();
 
    // add to the content pane.
    frm.add(drawArea, BorderLayout.CENTER);
 
    // create controls to apply colors and call clear feature.
    JPanel actions = new JPanel();
 
    clearBtn = new JButton("Clear");
    clearBtn.addActionListener(actionListener);
    blackBtn = new JButton("Black");
    blackBtn.addActionListener(actionListener);
    blueBtn = new JButton("Blue");
    blueBtn.addActionListener(actionListener);
    greenBtn = new JButton("Green");
    greenBtn.addActionListener(actionListener);
    redBtn = new JButton("Red");
    redBtn.addActionListener(actionListener);
    magentaBtn = new JButton("Magenta");
    magentaBtn.addActionListener(actionListener);
 
    
    //Update the existing panel.
    actions.add(greenBtn);
    actions.add(blueBtn);
    actions.add(blackBtn);
    actions.add(redBtn);
    actions.add(magentaBtn);
    actions.add(clearBtn);
 
    // Update the content pane.
    frm.add(actions, BorderLayout.NORTH);
 
    // set the frame size. 
    frame.setSize(800, 800);
    
    //To close the frame.
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    //To display the swing paint result. 
    frame.setVisible(true);
 
  }
 
}