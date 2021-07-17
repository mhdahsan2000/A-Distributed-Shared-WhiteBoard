package remote;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import static Board.Paintsettings.CANVAS_HEIGHT;
import static Board.Paintsettings.CANVAS_WIDTH;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;


/**
 * Assignment 2 - COMP90015 (A distributed shared white board)
 * @author Mohammed Ahsan Kollathodi(1048942)
 *
 */


public class SBimage implements Serializable {
    
	//BufferedImage.
    private BufferedImage bffrdimg = null;

    public SBimage() {
        super();
    }

    public SBimage(BufferedImage im) {
        this();
        setImage(im);
    }

    
    // BufferedImage
    public BufferedImage getImage() {
        return bffrdimg;
    }

   
    // image 
    public void setImage(BufferedImage img) {
        this.bffrdimg = img;
    }

   
    // output
    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ImageIO.write(getImage(), "jpg", new MemoryCacheImageOutputStream(out));
    }

  
    // input
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        setImage(ImageIO.read(new MemoryCacheImageInputStream(in)));
    }

    
    // To clear the image.
    public void clear() {
        System.out.println("The canvas has been cleared!");
        bffrdimg = new BufferedImage(CANVAS_WIDTH, CANVAS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }
}
