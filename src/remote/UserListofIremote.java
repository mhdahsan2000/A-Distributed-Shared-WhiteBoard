package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public interface UserListofIremote extends Remote {

    
	// To add new user 
    void addUser(String userName) throws RemoteException;

    // To obtain the list of user names. 
    List<String> getUserNames() throws RemoteException;

    
    // To get the manager name.
    String getManagerName() throws RemoteException;

   
    // To set the manager name. 
    void setManagerName(String managerName) throws RemoteException;
}
