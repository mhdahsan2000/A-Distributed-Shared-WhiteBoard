import remote.Canvasremote;
import remote.Userlistremote;



/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */

public class ServerSettings {
   
	// This constant can be used to look up for remote list of users 
    public static final String USERLISTOFREMOTE = Userlistremote.class.getName();

   // This constant can be used to setup the remote canvas. 
    public static final String canvasremote = Canvasremote.class.getName();
}
