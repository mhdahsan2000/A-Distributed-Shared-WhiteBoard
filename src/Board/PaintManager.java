package Board;

import remote.CanvasofIRemote;
import remote.SBimage;

import static Board.Util.popupDialog;
import static Board.Util.popupNoServerConnectionErrorDialog;
import static Board.Util.ImageSave;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */

public class PaintManager {
	
    // The tool that the user is using.
    private String selectedToolName;

    // remote canvas object that's being called. 
    private CanvasofIRemote remoteCanvas;

    // The end point for the drawing lines. 
    private Point lastPoint;

    // The starting point for drawing lines. 
    private Point firstPoint;

    // The last updated time of the user. 
    private long lastUpdateTime;

    // The listener that can be used for changing the tools. 
    public final ActionListener PAINT_TOOL_ACTION_LISTENER = new ActionListener() {
        
    	// Method overriding. 
        public void actionPerformed(ActionEvent e) {
            System.out.println("[" + PaintManager.class.getSimpleName() + "] " + selectedToolName + "Change TOOL to" + e.getActionCommand());
            selectedToolName = e.getActionCommand();
            clearPoints();  // Clear all of the Points. 
        }
    };

    public PaintManager() {
    }

    // To obtain the selected tool name. 
    public String getSelectedToolName() {
        return selectedToolName;
    }
    

    // To set the Remote Canvas. 
    public void setRemoteCanvas(CanvasofIRemote remoteCanvas) {
        this.remoteCanvas = remoteCanvas;
    }
    
    // To get the remote canvas. 
    public CanvasofIRemote getRemoteCanvas() {
        return remoteCanvas;
    }

    // To clear all of the drawing points. 
    public void clearPoints() {
        System.out.println("To Clear all of the drawing points");
        firstPoint = null;
        lastPoint = null;
    }

    
    // To clear the canvas. 
    
    public void clearCanvas() {
        try {
            remoteCanvas.toclear();
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    // To draw the Text. 
    
    public void drawText(Point p, String text) {
        try {
            remoteCanvas.maketext(text, p.x, p.y);
        } catch (RemoteException e) {
            System.out.println("Error to draw to remote.");
            popupNoServerConnectionErrorDialog();
        }
    }

    // To draw a rectangle. 
    
    public void drawRectangle(Point newPoint) {
        if (firstPoint == null) {
            firstPoint = newPoint;
            System.out.println("    | Input firstPoint: " + firstPoint.toString());
        } else {
            lastPoint = newPoint;

            Dimension rectSize = null;
            
            //To Calculate the size of the rectangle.
            if (firstPoint.x <= lastPoint.x && firstPoint.y <= lastPoint.y) {
                rectSize = new Dimension(
                        lastPoint.x - firstPoint.x,
                        lastPoint.y - firstPoint.y
                );
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y >= lastPoint.y) {
                rectSize = new Dimension(
                        firstPoint.x - lastPoint.x,
                        firstPoint.y - lastPoint.y
                );
                firstPoint = new Point(lastPoint.x, lastPoint.y);
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y <= lastPoint.y) {
                rectSize = new Dimension(
                        firstPoint.x - lastPoint.x,
                        lastPoint.y - firstPoint.y
                );
                firstPoint = new Point(lastPoint.x, firstPoint.y);
            } else if (firstPoint.x <= lastPoint.x && firstPoint.y >= lastPoint.y) {
                rectSize = new Dimension(
                        lastPoint.x - firstPoint.x,
                        firstPoint.y - lastPoint.y
                );
                firstPoint = new Point(firstPoint.x, lastPoint.y);
            } else {
                System.out.println(" unknown rectangle position");
                this.clearPoints();
                return;
            }

            // Draw the rectangle to remote.
            try {
                remoteCanvas.makerect(firstPoint.x, firstPoint.y, rectSize.width, rectSize.height);
            } catch (RemoteException e) {
                popupNoServerConnectionErrorDialog();
            }

            System.out.println("complete drawing");
            this.clearPoints();
        }
    }

    /**
     * @param newPoint point of a line
     */
    public void drawLine(Point newPoint) {
        if (firstPoint == null) {
            firstPoint = newPoint;
            System.out.println("    | Input firstPoint: " + firstPoint.toString());
        } else {
            lastPoint = newPoint;

            try {
                remoteCanvas.makeline(firstPoint.x, firstPoint.y, lastPoint.x, lastPoint.y);
            } catch (RemoteException e) {
                popupNoServerConnectionErrorDialog();
            }

            System.out.println("    | draw lastPoint: " + lastPoint.toString());
            // now lastPoint become the start point of next line
            firstPoint = lastPoint;
        }
    }

    /**
     * @param newPoint point of a circle
     */
    public void drawCircle(Point newPoint) {
        if (firstPoint == null) {
            firstPoint = newPoint;
            System.out.println("    | Input firstPoint: " + firstPoint.toString());
        } else {
            lastPoint = newPoint;

            int radius = (int) Math.round(firstPoint.distance(lastPoint));
            Point topLeft = null;
            // Calculate the circle center
            if (firstPoint.x <= lastPoint.x && firstPoint.y <= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y >= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else if (firstPoint.x >= lastPoint.x && firstPoint.y <= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else if (firstPoint.x <= lastPoint.x && firstPoint.y >= lastPoint.y) {
                topLeft = new Point(firstPoint.x - radius, firstPoint.y - radius);
            } else {
                System.out.println("    | unknown rectangle position");
                this.clearPoints();
                return;
            }


            // Draw the rectangle to remote.
            
            try {
                remoteCanvas.makecircle(topLeft.x, topLeft.y, 2 * radius, 2 * radius);
            } catch (RemoteException e) {
                popupNoServerConnectionErrorDialog();
            }

            System.out.println("Complete drawing");
            this.clearPoints();
        }
    }

    
    // To drawPen. 
    
    public void drawPen(Point newPoint) {
        long curTime = System.currentTimeMillis();
        
        // Limit how regularly new line segments are created to avoid saturating the network with line segments.
        if ((curTime - lastUpdateTime) < 50) {
            return;
        }
        lastUpdateTime = curTime;
        if (!newPoint.equals(lastPoint)) {
            drawLine(newPoint);
        }
    }

    
    // To set Image. 
    public void setImage(BufferedImage image) {
        try {
            remoteCanvas.makeimg(new SBimage(image));
        } catch (RemoteException e) {
            popupNoServerConnectionErrorDialog();
        }
        clearPoints();
    }

    // To save the white Board. 
    
    public void saveWhiteBoard(String fileName) {
        try {
            ImageSave(this.getRemoteCanvas().getCanvas().getImage(), fileName);
            popupDialog("Successful save whiteboard to image: " + fileName);
            System.out.println("    | Successful ave whiteboard to image: " + fileName);
        } catch (RemoteException e1) {
            popupNoServerConnectionErrorDialog();
        }
    }
}
