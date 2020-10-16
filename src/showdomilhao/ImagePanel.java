package showdomilhao;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ImagePanel extends JPanel implements Runnable{

    private static final GraphicsConfiguration GFX_CONFIG = GraphicsEnvironment
                                                    .getLocalGraphicsEnvironment()
                                                    .getDefaultScreenDevice()
                                                    .getDefaultConfiguration();
    private int MAX = 250;
    private BufferedImage image;
    private BufferedImage imageBlinking;
    private boolean blinking = true;
    private Timer timer;
    
    public ImagePanel(String imagePath) {
       try {                
          image = ImageIO.read(new File(imagePath));
       } catch (IOException ex) {
          System.err.println("Caught IOException: " + ex.getMessage());
       }
    }
    
    public ImagePanel(String imagePath, boolean isResource) {
       try{
           
           if(isResource){
               InputStream is = ImagePanel.class.getResourceAsStream(imagePath);
               image = ImageIO.read(is);
           }
           else{
               image = ImageIO.read(new File(imagePath));
           }
           
       } catch (IOException ex) {
            System.err.println("Caught IOException: " + ex.getMessage());
       }
    }
    
     public ImagePanel(String imagePath, String imagePathBlinking, boolean isResource) {
       try{
           
           if(isResource){
               InputStream is = ImagePanel.class.getResourceAsStream(imagePath);
               InputStream is2 = ImagePanel.class.getResourceAsStream(imagePathBlinking);
               image = toCompatibleImage(ImageIO.read(is));
               imageBlinking = toCompatibleImage(ImageIO.read(is2));
           }
           else{
               image = ImageIO.read(new File(imagePath));
           }
           
       } catch (IOException ex) {
            System.err.println("Caught IOException: " + ex.getMessage());
       }
    }
     
    public static BufferedImage toCompatibleImage(final BufferedImage image) {
        /*
         * if image is already compatible and optimized for current system settings, simply return it
         */
        if (image.getColorModel().equals(GFX_CONFIG.getColorModel())) {
            return image;
        }

        // image is not optimized, so create a new image that is
        final BufferedImage new_image = GFX_CONFIG.createCompatibleImage(image.getWidth(), image.getHeight(), image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        final Graphics2D g2d = (Graphics2D) new_image.getGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return new_image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        //super.paintComponent(g);
        if(blinking)
        {
            g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters  
        }
        else
        {
            g.drawImage(imageBlinking, 0, 0, this);
        }
    }
    
    @Override
    public void run() {
            MAX = 20;
            timer = new Timer(100, new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    blinking = !blinking;
                    repaint();
                    if(MAX-- == 0) timer.stop();
                }
            });
            timer.setCoalesce(true);
            timer.start();
            blinking=true;
            repaint();
    }

}