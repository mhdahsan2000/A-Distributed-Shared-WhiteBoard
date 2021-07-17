package Connection;

import Board.WhiteBoardApp;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static Connection.Connectionsettings.*;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class ManagerConnectionThreads extends Thread {

	// Corresponding to the application. 
	
    private final WhiteBoardApp apps ;

   
    // The request from the server.
    private final String srvrrqt;

    
    // The socket corresponding to the server.
    private final SocketforConnection conncnsckt;

    public ManagerConnectionThreads(WhiteBoardApp apps, String srvrrqt, SocketforConnection conncnsckt) {
        this.apps = apps ;
        this.srvrrqt = srvrrqt;
        this.conncnsckt = conncnsckt;
    }

    // Method override. 
    
    public void run() {
        super.run();

        // JsonObject and JsonParser. 
        
        JSONObject jsonObject = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        // send responses
        
        try {
            jsonObject = (JSONObject) jsonParser.parse(srvrrqt);

            String srvrrqtType = (String) jsonObject.get(USER_REQUEST);
            switch (srvrrqtType) {
            
                case ASK_JOIN_WHITEBOARD:
                	
                    String useridforcndte = (String) jsonObject.get(UID);
                    boolean result = apps.askAcceptCandidate(useridforcndte);
                    conncnsckt.joinreqresult(USER_REQUEST, ASK_JOIN_WHITEBOARD_RESULT, result, useridforcndte);
                    break;
                default:
                    System.out.println("The request is Unknown: " + srvrrqt);
            }
        } catch (ParseException e) {
            System.out.println("Error : Invalid request: " + srvrrqt);
        } catch (IOException e) {
            System.out.println("Error in message that is send to the server.");
        }
    }
}
