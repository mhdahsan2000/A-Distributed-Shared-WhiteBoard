package Connection;

import org.json.simple.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static Connection.Connectionsettings.*;
import static Board.Util.popupNoServerConnectionErrorDialog;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */
 

public class SocketforConnection {
	
    private Socket port;

  
    // InputDatastream of the client. 
    private DataInputStream streaminput;

    
    // Output datastream of the client. 
    private DataOutputStream streamoutput ;
    
    // Constructor. 
    
    public SocketforConnection(String serverAddress, int serverPort) {
        try {
            this.port = new Socket(serverAddress, serverPort);
            
            // stream input.
            streaminput = new DataInputStream(port.getInputStream());
            // stream output.
            streamoutput = new DataOutputStream(port.getOutputStream());
            
            // IOException.
        } catch (IOException e) {
            popupNoServerConnectionErrorDialog();
        }
    }

    // Try the port connection. 
    
    public SocketforConnection(Socket port) {
        try {
        	
            this.port = port;

            streaminput = new DataInputStream(port.getInputStream());
            streamoutput = new DataOutputStream(port.getOutputStream());
        } catch (IOException e) {
            System.out.println("Error create socket");
        }
    }

   
    // Here we would send the the message through the socket. 
    
    public void Dispatch(String request) throws IOException {
        System.out.println("send: " + request);
        streamoutput.writeUTF(request);
        streamoutput.flush();
    }

   
    // To accept the message from the socket. 
    public String accept() throws IOException {
        String receive = streaminput.readUTF();
        System.out.println("receive: " + receive);
        return receive;
    }

    
    // To end the socket. 
    public void endsocket() throws IOException {
        System.out.println("Close the socket");
        port.close();
        streaminput.close();
        streamoutput.close();
    }

    
    // When there is already a manager for the whiteboard - The response to be sent. 
    public void managrpresent() throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(RESPONSE, ALREADY_HAS_MANAGER);

        Dispatch(jsonObject.toJSONString());
    }

    
    // To send the response when there is no manager yet. 
    
    public void mangrpresentrspnse() throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(RESPONSE, NO_MANAGER_YET);

        Dispatch(jsonObject.toJSONString());
    }

    
    // Send successfully created white-board response.
    public void whiteboardcreatedsuccess(String uid) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(RESPONSE, SUCCESSFUL_CREATE_WHITEBOARD);
        jsonObject.put(UID, uid);

        Dispatch(jsonObject.toJSONString());
    }

    
    // Ask the manager's approval to join the white board. 
    
    public void managerapproval(String candidateUID) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(USER_REQUEST, ASK_JOIN_WHITEBOARD);
        jsonObject.put(UID, candidateUID);

        Dispatch(jsonObject.toJSONString());
    }

    // Send the status of the join request. 
    
    public void joinreqresult(String typeKey, String type, boolean result, String candidateUID) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(typeKey, type);
        jsonObject.put(UID, candidateUID);
        jsonObject.put(RESULT, Boolean.toString(result));

        Dispatch(jsonObject.toJSONString());
    }

    // The manager kicking out a user. 
    
    public void kckoutrqstupdate() throws IOException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(USER_REQUEST, KICK_OUT_USER);
        Dispatch(jsonObject.toJSONString());
    }

    // Update the status if the socket is closed or not.  
    
    public boolean isClosed() {
        return port.isClosed();
    }

    
    public void updatemngroprtn(String operation) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(USER_REQUEST, operation);

        Dispatch(jsonObject.toJSONString());
    }
}
