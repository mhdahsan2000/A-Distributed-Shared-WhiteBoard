package draw ; 

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
 
import javax.swing.JComponent;
 

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


// Board and associated properties 

public class Board extends JComponent {
 
  // To setup the canvas in which we are going to draw. 
  private Image frame;
  
  // The Graphic2D Object on which we are drawing. 
  private Graphics2D graphicobject ;
  
  // Mouse coordinates.
  private int newX, newY, prevX, prevY;
 
  public Board() {
	  
    setDoubleBuffered(false);
    addMouseListener(new MouseAdapter() {
      public void mousePressed(MouseEvent mevnt) {

    	// To updated the coordinates. 
    	  
        prevX = mevnt.getX();
        prevY = mevnt.getY();
        
      }
    });
 
    addMouseMotionListener(new MouseMotionAdapter() {
    	
      public void mouseDragged(MouseEvent mevnt) {
    	  
        // Updated coordinated when the mouse is dragged. 
        newX = mevnt.getX();
        newY = mevnt.getY();
 
        if (graphicobject != null) {
        	
          // Draw a line of the context is not null. 
        	graphicobject.drawLine(prevX, prevY, newX, newY);
        	
          // Draw area has to be reset to paint again or to repaint. 
        	
          repaint();
          
          // To update the coordinates.
          
          prevX = newX;
          prevY = newY;
          
        }
      }
    });
  }
  
 
  protected void paintComponent(Graphics g) {
	  
    if (frame == null) {
    	
      // the image to draw null. 
      frame = createImage(getSize().width, getSize().height);
      graphicobject = (Graphics2D) frame.getGraphics();
      // Anti Aliasing enabled. 
      graphicobject.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      // To reset the drawarea.
      AllClear();
    }
 
    g.drawImage(frame, 0, 0, null);
  }
 
  // To clear the screen.
  
  public void AllClear() {
	  
	  // Background color and the associated background properties . 
	  
	  graphicobject.setPaint(Color.white);
	  graphicobject.fillRect(0, 0, getSize().width, getSize().height);
	  graphicobject.setPaint(Color.black);
	  
    repaint();
    
  }
 
  // red color. 
  public void red() {
	  graphicobject.setPaint(Color.red);
  }
 
  // black color.
  public void black() {
	  graphicobject.setPaint(Color.black);
  }
 
  // magenta color. 
  public void magenta() {
	  graphicobject.setPaint(Color.magenta);
  }
  
  
 // green color. 
  public void green() {
	  graphicobject.setPaint(Color.green);
  }
  
  
 // blue color. 
  public void blue() {
	  graphicobject.setPaint(Color.blue);
  }
 
}