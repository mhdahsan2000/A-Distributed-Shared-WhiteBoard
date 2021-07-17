package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */



public interface CanvasofIRemote extends Remote {

   
 
	// To create text 
    void maketext(String t, int x, int y) throws RemoteException;
    
    SBimage getCanvas() throws RemoteException;
    // To create a rectangle. 
    void makerect(int x, int y, int width, int height) throws RemoteException;
    // To create a line.
    void makeline(int x1, int y1, int x2, int y2) throws RemoteException;
    // To create a circle. 
    void makecircle(int x, int y, int width, int height) throws RemoteException;
    // To create an image. 
    void makeimg(SBimage image) throws RemoteException;
    // To clear. 
    void toclear() throws RemoteException;
    
}
