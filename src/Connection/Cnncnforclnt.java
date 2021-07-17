package Connection;

import Board.WhiteBoardApp ;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

import static Connection.Connectionsettings.*;
import static Board.Util.popupNoServerConnectionErrorDialog;

/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class Cnncnforclnt {
   
	
	// The server conncnsckt. 
	
    private SocketforConnection conncnsckt;

    public Cnncnforclnt(SocketforConnection conncnsckt) {
        this.conncnsckt = conncnsckt;
    }

    
  
    public void startboard(WhiteBoardApp app, String username) {
        JSONObject obj = new JSONObject();
        JSONParser parser = new JSONParser();

        if (app.mngrtrue()) {
        	
            // request to create a white board
            obj.put(USER_REQUEST, CREATE_WHITEBOARD);
            obj.put(USERNAME, username);
            
        } else {
        	
            // request to join a white board
            obj.put(USER_REQUEST, REQUEST_JOIN_WHITEBOARD);
            obj.put(USERNAME, username);
        }

        String response = "";
        try {
            conncnsckt.Dispatch(obj.toJSONString());
            
            // grant access to whiteboard or rejected
            
            response = conncnsckt.accept();
            obj = (JSONObject) parser.parse(response);

            String responseType = (String) obj.get(RESPONSE);

            switch (responseType) {
                case ALREADY_HAS_MANAGER:
                    app.error(responseType);
                    break;
                case SUCCESSFUL_CREATE_WHITEBOARD:
                    app.start((String) obj.get(UID));
                    break;
                case REJECTED_BY_MANAGER:
                    app.error(responseType);
                    break;
                case SUCCESSFUL_JOIN_WHITEBOARD:
                    app.start((String) obj.get(UID));
                    break;
                case NO_MANAGER_YET:
                    app.error(responseType);
                    break;
                default:
                    System.out.println("Unknown response: " + response);
            }
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        } catch (ParseException e) {
            System.out.println("Invalid response: " + response);
        }
    }

   
    
    
    public void disconnect(boolean isManager, String uid) {

        JSONObject obj = new JSONObject();

        if (isManager) {
            // manager close
            obj.put(USER_REQUEST, CLOSE_WHITE_BOARD);
            obj.put(UID, uid);
        } else {
            // user leave
            obj.put(USER_REQUEST, LEAVE_WHITE_BOARD);
            obj.put(UID, uid);
        }

        try {
            conncnsckt.Dispatch(obj.toJSONString());
            conncnsckt.endsocket();
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    
    
    public void kickOut(String uid) {
        JSONObject obj = new JSONObject();

        obj.put(USER_REQUEST, KICK_OUT_USER);
        obj.put(UID, uid);

        try {
            conncnsckt.Dispatch(obj.toJSONString());
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    
    
    public void notifyUserWithManagerOperation(String type) {
        JSONObject obj = new JSONObject();

        obj.put(USER_REQUEST, type);

        try {
            conncnsckt.Dispatch(obj.toJSONString());
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }
}
