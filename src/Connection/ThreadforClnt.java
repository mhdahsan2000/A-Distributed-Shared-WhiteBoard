package Connection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import remote.ManangerUsers ;

import static Connection.Connectionsettings.*;

import java.io.IOException;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class ThreadforClnt extends Thread {

    
	// Socket 
    private SocketforConnection client ;

    // To manage the users who are using the whiteBoard. 
    
    private ManangerUsers userManager ;

    public ThreadforClnt(SocketforConnection client, ManangerUsers userManager) {
    	
        this.client = client;
        this.userManager = userManager;
        
    }

    // Method override.
    
    public void run() {
        super.run();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = new JSONObject();

        String request;
        
        while (true) {
            try {
                request = client.accept();
                try {
                    jsonObject = (JSONObject) parser.parse(request);
                } catch (ParseException e) {
                    System.out.println("It's an Invalid request: " + request);
                    interrupt();
                }

                String requestType = (String) jsonObject.get(USER_REQUEST);

                // To send responses.
                
                switch (requestType) {
                    case CREATE_WHITEBOARD:
                    	
                        // Already a manager is present for the board. 
                    	
                        if (userManager.hasManager()) {
                            client.managrpresent();
                            
                        } else {
                        	
                            String uid = userManager.addUser((String) jsonObject.get(USERNAME));
                            userManager.setManagerUID(uid);
                            userManager.addUserSocket(uid, client);
                            client.whiteboardcreatedsuccess(uid);
                        }
                        
                        break;
                        
                     // Request to Join the White Board.    
                    case REQUEST_JOIN_WHITEBOARD:
                    	
                        SocketforConnection managerSocket = userManager.getManagerCommunicationSocket();
                        
                        // Reject due to a manager being already present. 
                        
                        if (!userManager.hasManager()) {
                            client.mangrpresentrspnse();
                            break;
                        }
                        
                        String uid = userManager.addCandidateUser((String) jsonObject.get(USERNAME));
                        userManager.addUserSocket(uid, client);
                        managerSocket.managerapproval(uid);
                        break;
                        
                        // Ask to join the white Board. 
                        
                    case ASK_JOIN_WHITEBOARD_RESULT:
                    	
                        String candidateUID = (String) jsonObject.get(UID);
                        boolean result = Boolean.parseBoolean((String) jsonObject.get(RESULT));
                        SocketforConnection candidateSocket = userManager.getCommunicationSocket(candidateUID);
                        assert candidateSocket != null;
                        
                        if (result) {
                            candidateSocket.joinreqresult(RESPONSE, SUCCESSFUL_JOIN_WHITEBOARD, result, candidateUID);
                            userManager.acceptCandidateUser(candidateUID);
                        } else {
                            candidateSocket.joinreqresult(RESPONSE, REJECTED_BY_MANAGER, result, candidateUID);
                            userManager.rejectCandidateUser(candidateUID);
                        }
                        break;
                        
                       // To leave the white board.  
                    case LEAVE_WHITE_BOARD:
                        String uidToBeRemoved = (String) jsonObject.get(UID);
                        userManager.removeUser(uidToBeRemoved);
                        break;
                        
                        
                        // To close the white board. 
                    case CLOSE_WHITE_BOARD:
                        userManager.broadcastManagerOperation(MANAGER_CLOSE);
                        userManager.clear();
                        break;
                        
                        // To kickout a user 
                        
                    case KICK_OUT_USER:
                        String kickOutUID = (String) jsonObject.get(UID);
                        SocketforConnection kickOutSocket = userManager.getCommunicationSocket(kickOutUID);
                        if (kickOutSocket != null) {
                            System.out.println("Dispatching the kick out request to" + UID);
                            kickOutSocket.kckoutrqstupdate();
                        } else {
                            System.out.println("Dispatching the kick out non-existing user: " + UID);
                        }
                        userManager.removeUser(kickOutUID);
                        break;
                        
                        // TO assign a new manager. 
                        
                    case MANAGER_NEW:
                        userManager.broadcastManagerOperation(MANAGER_NEW);
                        break;
                        
                        // To open a new manager. 
                    case MANAGER_OPEN:
                        userManager.broadcastManagerOperation(MANAGER_OPEN);
                        break;
                    default:
                        System.out.println("Error: It's an Unknown request type: " + request);
                }
            } catch (IOException e) {
                System.out.println("Error in present in the server socket connection ! ");
                break;
            }
        }
    }
}
