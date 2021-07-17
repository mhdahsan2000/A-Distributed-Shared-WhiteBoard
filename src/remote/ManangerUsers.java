package remote;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Connection.SocketforConnection;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


// To allow multiple users and to allow concurrency the keyword Synchronized is used so that multiple users could connect. 

public class ManangerUsers {

	// To keep track of the number of users 
    private int userCounter = 0;

    
    // Mapping with a HashMap , the user and the associated socket. 
    private HashMap<String, SocketforConnection> scktusrs;

    // The user id of the manager. 
    private String useridofmanager = null;

    // List of remote users. 
    private UserListofIremote userlistremote;

    // List of candidate users. 
    private List<String> cndnusr;

    
    public ManangerUsers() {
    	
        scktusrs = new HashMap<>();
        cndnusr = new ArrayList<>();
    }

   // To add new user 
    public synchronized String addUser(String userName) {
        String uid = String.format("%s (%d)", userName, userCounter);

        try {
            if (useridofmanager == null) {
                useridofmanager = uid;
                userlistremote.setManagerName(useridofmanager);
            } else {
                userlistremote.addUser(uid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        userCounter++;
        return uid;
    }

    // To add a candidate user. 
    
    public synchronized String addCandidateUser(String userName) {
        String uid = String.format("%s (%d)", userName, userCounter);
        cndnusr.add(uid);

        userCounter++;

        return uid;
    }

    
    // To accept a candidate user. 
    public synchronized void acceptCandidateUser(String uid) {
        if (cndnusr.contains(uid)) {
            cndnusr.remove(uid);
            try {
                userlistremote.addUser(uid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    // To reject a candidate user. 
    public synchronized void rejectCandidateUser(String uid) {
        if (cndnusr.contains(uid)) {
            cndnusr.remove(uid);
            scktusrs.remove(uid);
        }
    }

   
    // To set the remote user list. 
    public void setRemoteUserList(UserListofIremote userlistremote) {
        this.userlistremote = userlistremote;
    }

   
    // To remove an existing user.
    public synchronized void removeUser(String uid) {
        try {
            if (userlistremote.getUserNames().contains(uid)) {
                userlistremote.getUserNames().remove(uid);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (scktusrs.containsKey(uid)) {
            try {
                scktusrs.get(uid).endsocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
            scktusrs.remove(uid);
        }
    }
    
    
    // To add a new user socket. 
    public synchronized void addUserSocket(String uid, SocketforConnection socket) {
        if (!scktusrs.containsKey(uid)) {
            scktusrs.put(uid, socket);
        } else {
            System.out.println("Error uid already has sockets: " + uid);
        }
    }

    // To add a socket for connection
    public synchronized SocketforConnection getManagerCommunicationSocket() {
        return scktusrs.get(useridofmanager);
    }

    
    public synchronized SocketforConnection getCommunicationSocket(String uid) {
        return scktusrs.getOrDefault(uid, null);
    }

    // To get the manager user id. 
    public synchronized String getManagerUID() {
        return useridofmanager;
    }

    // To set the manager user id. 
    public synchronized void setManagerUID(String useridofmanager) {
        this.useridofmanager = useridofmanager;
    }

    
    // To see if a manager is present or not. 
    public synchronized boolean hasManager() {
        return useridofmanager != null;
    }

    
    // To clear the user manager. 
    public synchronized void clear() {
        System.out.println("clearing the UserManager");
        userCounter = 0;
        scktusrs.clear();
        useridofmanager = null;
        try {
            userlistremote.getUserNames().clear();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        cndnusr.clear();
    }

    // To update the user manager operations.
  
    public synchronized void broadcastManagerOperation(String operation) {
        for (Map.Entry<String, SocketforConnection> entry: scktusrs.entrySet()) {
            String uid = entry.getKey();
            SocketforConnection communicationSocket = entry.getValue();

            if (communicationSocket.isClosed()) {
                System.out.println("UserID" + uid + "The socket has already been closed");
            } else if (!uid.equals(useridofmanager)) {
                System.out.println("UserID" + uid + "have sent with the manager operation: " + operation);
                try {
                    communicationSocket.updatemngroprtn(operation);
                } catch (IOException e) {
                    System.out.println("There exists a Socket error! ");
                }
            }
        }
    }
}
