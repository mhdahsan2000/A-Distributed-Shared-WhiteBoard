package Board;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;



/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */

public class Util {
    public static final String TYPEOFIMG = "jpg";

    
    // Error message. 
    
    public static void popupErrorDialog(String output) {
        JOptionPane.showConfirmDialog(
                null,
                output,
                "Error",
                JOptionPane.OK_CANCEL_OPTION
        );
        System.exit(1);
    }

    
    // Message when error happens. 
    
    public static void popupDialog(String output) {
        JOptionPane.showConfirmDialog(
                null,
                output,
                "Info",
                JOptionPane.OK_CANCEL_OPTION
        );
    }

    
    // Error when there is no connection to the Server. 
    public static void popupNoServerConnectionErrorDialog() {
        popupErrorDialog("There exists no connection to the server !.");
    }

    
    // To save the Image. 
    
    public static void ImageSave(BufferedImage bufferedImage, String filePath) {
        File outfile = new File(filePath);
        try {
            ImageIO.write(bufferedImage, TYPEOFIMG, outfile);
        } catch (IOException e) {
            popupDialog("Failed to save to: "+ filePath + "." + TYPEOFIMG);
        }
    }

    /**
     * @return auto generated file name to be saved to
     */
    public static String generateAutoFileName() {
        return "shared-whiteboard-auto-save-" +
                new SimpleDateFormat("yyyy-MM-dd-HH-mm").format(new Date()) +
                "." + TYPEOFIMG;
    }
}
