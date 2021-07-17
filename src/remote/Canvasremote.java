package remote;

import static Board.Paintsettings.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */




public class Canvasremote extends UnicastRemoteObject implements CanvasofIRemote {
    
    private SBimage frame;

    public Canvasremote() throws RemoteException {
        frame = new SBimage(new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB));
    }

    
    public SBimage getCanvas() throws RemoteException {
        return frame;
    }

    @Override
    // method override from canvasofIRemote class. 
    public void maketext(String t, int x, int y) throws RemoteException {
        Graphics g = frame.getImage().getGraphics();
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString(t, x, y);
    }

 
    @Override
    // method override from canvasofIRemote class. 
    public void makerect(int x, int y, int width, int height) throws RemoteException {
        Graphics g = frame.getImage().getGraphics();
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);
    }

   
    @Override
    // method override from canvasofIRemote class. 
    public void makeline(int x1, int y1, int x2, int y2) throws RemoteException {
        Graphics g = frame.getImage().getGraphics();
        g.setColor(Color.RED);
        g.drawLine(x1, y1, x2, y2);
    }

   
    @Override
    // method override from canvasofIRemote class. 
    public void makecircle(int x, int y, int width, int height) throws RemoteException {
        Graphics g = frame.getImage().getGraphics();
        g.setColor(Color.BLUE);
        g.drawOval(x, y, width, height);
    }

  
    @Override
    // method override from canvasofIRemote class. 
    public void makeimg(SBimage image) throws RemoteException {
        frame = image;
    }

   
    @Override
    // method override from canvasofIRemote class.
    public void toclear() throws RemoteException {
        frame.clear();
    }
}
