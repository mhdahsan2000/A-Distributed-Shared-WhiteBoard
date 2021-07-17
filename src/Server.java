import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import ServerGUI.ServerGUI;
import java.net.ServerSocket;
import remote.*;
import java.rmi.Remote ; 
import javax.net.ServerSocketFactory;

import Connection.ThreadforClnt;
import Connection.SocketforConnection;

import java.rmi.AlreadyBoundException;

import static Board.Util.popupDialog;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */

public class Server{

    
	// The server port number , by default I have given it as 8001 while creating the socket. 
	
    private static int portnumberserver ;

    public static void main(String args[]) {
    	
        parseArguments(args);

        ManangerUsers Manangeruser = new ManangerUsers();
        
        try {
            
        	// Start the remote Objects. 
            UserListofIremote usersList = new Userlistremote();
            CanvasofIRemote canvas = new Canvasremote();
            
            // The program would automatically create a registry in the specified port, so there is no need to create a seperate registry from the command line. 
            // Create Registry and update the registry. 
            Registry registry = LocateRegistry.createRegistry(8001);  
            Registry updatedregistry = LocateRegistry.getRegistry(8001);  

            
            // update the registry by using the bind() functionality. 
            updatedregistry.bind(ServerSettings.USERLISTOFREMOTE, usersList);
            updatedregistry.bind(ServerSettings.canvasremote, canvas);
            
            
            System.out.println("Remote method Invocation is Ready! ");
            
            Manangeruser.setRemoteUserList(usersList);

            // Initialize the graphical User Interface, 
            
            ServerGUI graphicalUIserver = new ServerGUI(usersList);
            
            System.out.println("Graphical User Interface is ready !");
            
            // Exceptions within the RMI setup which are AlreadyBoundException, AccessException and RemoteExceptions. 
            
        } catch (AlreadyBoundException e) {
            popupDialog("The RMI object is already bound to the server");
            System.exit(1);
        } catch (AccessException e) {
            popupDialog("Remote method Invocation access failure ! ");
            System.exit(1);
        } catch (RemoteException e) {
            popupDialog("Remote Method Invocation connection failure as a result of remote exceptions ! ");
            System.exit(1);
        }

        // start server
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        try(ServerSocket server = new ServerSocket(8002)) {
            System.out.println("The Socket connection is now ready! Starting the SERVER now. ");
            while (true) {
            	
                // start new thread for the request.
            	
                Socket client = server.accept();
                
                ThreadforClnt clientRequestsThread = new ThreadforClnt(new SocketforConnection(client), Manangeruser);
                
                clientRequestsThread.start();
            }
            
            // Catch the IO exception.
            
        } catch (IOException e) {
            System.out.println("Creation of socket has been a failure ! ");
        }
    }
    
    // Parse the Arguments. 
    private static void parseArguments(String args[]) {
        if (args.length < 2) {
            popupDialog("Make sure it's the corerct format which is <server address><server port>");
            System.exit(1);
        }

        portnumberserver = util.parsePort(args[1]);
    }
}
