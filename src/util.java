import static Board.Util.popupDialog;


public class util {

   
	/**
	 * Assignment 2 - COMP90015 (A distributed shared white board)
	 * @author Mohammed Ahsan Kollathodi(1048942)
	 *
	 */
	
    public static int parsePort(String port) {
        int portnum = 8001 ;   // by default it is set as 8001. 

        try {
            portnum = Integer.parseInt(port);

            if (portnum < 1024 || portnum > 65535) {
                popupDialog("The server port number has to be in the range of: 1024-65535");
                System.exit(1);
            }
              // Catch the exception. 
        } catch (NumberFormatException e) {
            popupDialog("The server port number has to be an Integer.");
            System.exit(1);
        }

        return portnum;
    }
}
