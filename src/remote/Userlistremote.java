package remote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */



public class Userlistremote extends UnicastRemoteObject implements UserListofIremote {

    
	// Name of the manager. 
    private String managerName = null;

    // List of user names. 
    private List<String> usernames;

    public Userlistremote() throws RemoteException {
        usernames = new ArrayList<>();
    }

    
    // username of the user 
    @Override
    public void addUser(String userName) throws RemoteException{
        usernames.add(userName);
        System.out.println("added: " + userName);
        System.out.println("   |"+ Arrays.toString(usernames.toArray()));
    }

   
    // list of usernames 
    @Override
    public List<String> getUserNames() throws RemoteException {
        return usernames;
    }

    
    //getter method for the name of the whiteboard's manager 
    @Override
    public String getManagerName() throws RemoteException{
        return managerName;
    }

  
    //setter method for the name of the whiteboard's manager. 
    @Override
    public void setManagerName(String managerName) throws RemoteException{
        this.managerName = managerName;
    }
}
