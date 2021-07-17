package Board;

import javax.swing.*;
import javax.swing.JComponent ; 
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.rmi.RemoteException;

import static Board.Paintsettings.*;
import static Board.Util.popupNoServerConnectionErrorDialog;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;


public class WBPanelCanvas extends JPanel implements MouseListener, MouseMotionListener {

	/**
	 * Assignment 2 - COMP90015 (A distributed shared white board)
	 * @author Mohammed Ahsan Kollathodi(1048942)
	 *
	 */
	
    private PaintManager paintManager;
    private Graphics2D g2 ; 
    private int currentX, currentY, oldX, oldY; 
        
    public WBPanelCanvas(PaintManager paintManager) {
    	
    	setDoubleBuffered(false);
        this.paintManager = paintManager;
        setBackground(Color.WHITE);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // Method override. 
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // To update the canvas.
        
        try {
            g.drawImage(paintManager.getRemoteCanvas().getCanvas().getImage(),
                            0,
                            0,
                            getWidth(),
                            getHeight(),
                            null);
            this.repaint();
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        } catch (NullPointerException e) {
            System.out.println("    | canvas panel <paintComponent> null pointer");
        }
    }

    
    
    // This is called upon when the user would click the mouse button. 
    // Method overriding. 
    
    @Override
    public void mouseClicked(MouseEvent e) {
        Point newPoint = e.getPoint();

        String toolSelected = paintManager.getSelectedToolName();

        // Different drawing shapes are available. 
        
        switch (toolSelected) {
        
            case LINE:
                if (isLeftMouseButton(e)) {
                	
                    // Set the origin or the end-point.
                	
                    paintManager.drawLine(newPoint);
                    
                } else if (isRightMouseButton(e)) {
                	
                    // To Cancel the drawing.
                    paintManager.clearPoints();
                } else {
                    System.out.println("Unknown button command exists to draw the rectangle!");
                }
                break;
            case CIRCLE:
                if (isLeftMouseButton(e)) {
                    // Set origin or the end point.
                    paintManager.drawCircle(newPoint);
                } else if (isRightMouseButton(e)) {
                    // To Cancel the drawing.
                    paintManager.clearPoints();
                } else {
                    System.out.println("Unknown button command to draw the rectangle");
                }
                break;
            case RECTANGLE:
                if (isLeftMouseButton(e)) {
                	
                    // Set the origin or the end point.
                    paintManager.drawRectangle(newPoint);
                } else if (isRightMouseButton(e)) {
                    // To Cancel the drawing.
                    paintManager.clearPoints();
                } else {
                    System.out.println("Unknown button command to draw the rectangle");
                }
                break;
            case TEXT:
                String data = showInputDialog("Please input the text:");
                if (data != null && !data.isEmpty()) {
                    System.out.println(data);
                    paintManager.drawText(newPoint, data);
                } else {
                    System.out.println("User cancels to input text or input nothing");
                }
                break;
            case PEN:
                System.out.println("Pen clicked");
                break;
            default:
                System.out.println("Drawing tool is not implemented:" + toolSelected);
        }
    }

    // This is invoked when the mouse has been pressed on a component. 
    // Method overrride. 
    @Override
    public void mousePressed(MouseEvent e) {

    }

    // This is invoked when the mouse is released from a component. 
    // Method override. 
    @Override
    public void mouseReleased(MouseEvent e) {
        String toolSelected = paintManager.getSelectedToolName();
        switch (toolSelected) {
            case PEN:
                System.out.println("    | Pen released");
                paintManager.clearPoints();
                break;
            default:
                System.out.println("Unknown tool dragged");
        }
    }

    // This is invoked when the mouse enters a component.
    // Method override. 
    
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    // Method override. 
    @Override
    public void mouseExited(MouseEvent e) {

    }

    // This is invoked when the mouse has been dragged. 
    @Override
    public void mouseDragged(MouseEvent e) {
        Point newPoint = e.getPoint();
        String toolSelected = paintManager.getSelectedToolName();
        switch (toolSelected) {
            case PEN:
                System.out.println("    | Pen dragged");
                paintManager.drawPen(newPoint);
                break;
            default:
                System.out.println("Unknown tool dragged");
        }
    }

    
    // This is invoked when the mouse cursor is moved to a new component. 
    // However, when there are no new buttons pushed. 
    // Method override. 
    
    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
