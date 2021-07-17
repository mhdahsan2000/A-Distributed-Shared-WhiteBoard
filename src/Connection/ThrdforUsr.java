package Connection;

import Board.WhiteBoardApp ;
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


public class ThrdforUsr extends Thread {

    // Initialise the white board application. 
    private final WhiteBoardApp app;

    // The request for the server. 
    private final String srvrrqst ;

    public ThrdforUsr(WhiteBoardApp app, String srvrrqst) {
        this.app = app;
        this.srvrrqst = srvrrqst;
    }

    //Method override. s
    public void run() {
        super.run();

        JSONObject jsonObject = new JSONObject();
        JSONParser jsonParser = new JSONParser();

        // To send responses.
        
        try {
        	
            jsonObject = (JSONObject) jsonParser.parse(srvrrqst);

            String requestType = (String) jsonObject.get(USER_REQUEST);
            
            switch (requestType) {
            
                case KICK_OUT_USER:
                    app.kickedOut();
                    break;
                case MANAGER_CLOSE:
                    app.closeByManager();
                    break;
                case MANAGER_NEW:
                    app.notifyUser(MANAGER_NEW);
                    break;
                case MANAGER_OPEN:
                    app.notifyUser(MANAGER_OPEN);
                    break;
                default:
                    System.out.println("Error:Unknown request:" + srvrrqst);
            }
        } catch (ParseException e) {
            System.out.println("Error:Invalid request:" + srvrrqst);
        }
    }
}
