import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Board.WhiteBoardApp;
import Connection.Cnncnforclnt;
import Connection.ManagerConnectionThreads ;
import Connection.SocketforConnection;
import Connection.ThrdforUsr;
import remote.CanvasofIRemote;
import remote.UserListofIremote;

import static Board.Util.popupDialog;
import static Board.Util.popupNoServerConnectionErrorDialog;

import java.io.IOException;
import java.rmi.NotBoundException;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */

public class JoinWhiteBoard {
    
	// The Address of the server. 
    private static String Addressofserver;

    
    // The port to which the user is connecting to. 
    private static int portofserver;

  
    // User name of the client. 
    private static String nameoftheuser;

    public static void main(String args[]) {
        parseArguments(args);

        // Start the application. 
        WhiteBoardApp application = new WhiteBoardApp(false);

        try {
            // Get the rmi registry that is running on the port 8001. -> By default the port is 8001. 
            Registry registry = LocateRegistry.getRegistry(8001);
            

            // find the remote objects.
            
            UserListofIremote userlistofremoteusers = (UserListofIremote) registry.lookup(ServerSettings.USERLISTOFREMOTE);
            CanvasofIRemote canvasofremoteusers = (CanvasofIRemote) registry.lookup(ServerSettings.canvasremote);
            application.setRemoteUserList(userlistofremoteusers);
            application.setRemoteCanvas(canvasofremoteusers);

            // Make the socket.
            SocketforConnection socket = new SocketforConnection(Addressofserver, portofserver);
            
            // Make the WhiteBoard. 
            Cnncnforclnt connection = new Cnncnforclnt(socket);
            // set the client connection. 
            application.setClientConnection(connection);
            // Start a new thread 
            Thread join = new Thread() {
                // override the method.
                public void run() {
                    super.run();
                    popupDialog("Mananger's Permission Pending for the request !");
                }
            };
            join.start();
            connection.startboard(application, nameoftheuser);

            System.out.println("You have Succesfully joined the whiteboard.");
            
            // Process to kick out the user. 
            
            while (true) {
                String kickoutreq = socket.accept();
                ThrdforUsr kickOutThread = new ThrdforUsr(application, kickoutreq);
                kickOutThread.start();
            }
            // Catch the exceptions. 
        } catch (RemoteException | NotBoundException e) {
            popupNoServerConnectionErrorDialog();
        } catch (IOException e) {
            System.out.println("There is an error in the kickout request error that's received.");
        }
    }

    // Check if it's the correct format or not. 
    
    private static void parseArguments(String args[]) {
        if (args.length < 3) {
            popupDialog("The correct format for the request is: <serverIPAddress> <portofserver> username");
            System.exit(1);
        }
        
        // Address of the server is the first argument
        Addressofserver = args[0];
        // Port of the server is the second argument
        portofserver = util.parsePort(args[1]);
        // Name of the user is the third argument as part of the input. 
        nameoftheuser = args[2];
    }
}
