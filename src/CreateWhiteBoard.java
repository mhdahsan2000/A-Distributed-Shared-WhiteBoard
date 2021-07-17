import remote.CanvasofIRemote;
import remote.UserListofIremote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import Board.WhiteBoardApp;
import Connection.Cnncnforclnt;
import Connection.ManagerConnectionThreads;
import Connection.SocketforConnection;

import static Board.Util.popupDialog;
import static Board.Util.popupNoServerConnectionErrorDialog;

import java.io.IOException;
import java.rmi.NotBoundException;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class CreateWhiteBoard {

	// ServerAddress is the address of the server to have access into. 
    private static String Addressofserver;

    // The server port number to which we are connecting to. 
    private static int portofserver;

    // The username of the user. 
    private static String nameofuser;

    public static void main(String args[]) {
    	
        parseArguments(args);

        // initialize the application. 
        
        WhiteBoardApp application = new WhiteBoardApp(true);

        try {
        	
            // Connect to the rmi registry that is running on port 8001 -> By default, the port number is set as 8001. 
            Registry registry = LocateRegistry.getRegistry(8001);

            // find the remote objects. 
            
            UserListofIremote userlistofremoteusers = (UserListofIremote) registry.lookup(ServerSettings.USERLISTOFREMOTE);
            
            CanvasofIRemote canvasthatisremote = (CanvasofIRemote) registry.lookup(ServerSettings.canvasremote);
            
            application.setRemoteUserList(userlistofremoteusers);
            
            application.setRemoteCanvas(canvasthatisremote);

            // create the socket
            SocketforConnection socket = new SocketforConnection(Addressofserver, portofserver);

            // create the whiteboard
            Cnncnforclnt makeconnection = new Cnncnforclnt(socket);
            application.setClientConnection(makeconnection);
            makeconnection.startboard(application, nameofuser);
            System.out.println("The WhiteBoard has been Setup . ");

            // The responses to the join the request.
            
            while (true) {
                String updatedrequest = socket.accept();
                ManagerConnectionThreads Threadjoinofrequest = new ManagerConnectionThreads(application, updatedrequest, socket);
                Threadjoinofrequest.start();
            }
        } catch (RemoteException | NotBoundException e) {
            popupNoServerConnectionErrorDialog();
        } catch (IOException e) {
            System.out.println("Error request in JOIN from the server ! Verify your configuration.");
        }
    }

     //  Parse Arguments. 
    private static void parseArguments(String args[]) {
        if (args.length < 3) {
            popupDialog("The correct format of creating the board is :<serverIPAddress> <portofserver> username");
            System.exit(1);
        }

        Addressofserver    =    args[0];
        portofserver       =    util.parsePort(args[1]);
        nameofuser         =    args[2];
    }
}
