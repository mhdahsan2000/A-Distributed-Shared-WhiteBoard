package Connection;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class Connectionsettings {
    
	// The user's request 
    public static final String USER_REQUEST = "request";

    
    // To create a white board 
    public static final String CREATE_WHITEBOARD = "Create Whiteboard";

   
    // To request for a white board 
    public static final String REQUEST_JOIN_WHITEBOARD = "Join Whiteboard";

  
    // To ask to join a White Board. 
    public static final String ASK_JOIN_WHITEBOARD = "Ask Join Whiteboard";

    /**
     * ask join whiteboard result
     */
    public static final String ASK_JOIN_WHITEBOARD_RESULT = "Ask Join Whiteboard result";

   
    // The manager who would close the white board result 
    public static final String CLOSE_WHITE_BOARD = "Manager close Whiteboard";

  
    // User leaving the WhiteBoard 
    public static final String LEAVE_WHITE_BOARD = "User leave Whiteboard";

    
    
    // Manager kicking out a user 
    public static final String KICK_OUT_USER = "Kick out user";

    /**
     * manager close the whiteboard
     */
    public static final String MANAGER_CLOSE = "Manager closed the whiteboard";

    
    // Manager closing the White Board 
    public static final String MANAGER_NEW = "Manager new a whiteboard";

    // Manager opening a saved white board 
    public static final String MANAGER_OPEN = "Manager open a whiteboard";

    
    // The result message s
    public static final String RESULT = "result";

    
    // Key for username 
    public static final String USERNAME = "username";

    
    // Key for response 
    public static final String RESPONSE = "response";

    // Exception when there is already a manager. 
    public static final String ALREADY_HAS_MANAGER = "already has manager";

    
    // Excpeption when there is no manager for the whiteboard yet. 
    public static final String NO_MANAGER_YET = "No whiteboard created by manager";

    // When the creation of whiteBoard is a success
    public static final String SUCCESSFUL_CREATE_WHITEBOARD = "Successful create Whiteboard";

    // When the manager would reject the exception 
    public static final String REJECTED_BY_MANAGER = "rejected by the manager to join";

    // When joining the whiteBoard is a success 
    public static final String SUCCESSFUL_JOIN_WHITEBOARD = "Successful join Whiteboard";

    // Each user would have a unique user id which is essentially the UID. 
    
    public static final String UID = "uid";
}
