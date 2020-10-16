package showdomilhao;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class WheelOfFortuneWheelPanel extends JPanel {
        
    private final AudioClip SPINNING_WHEEL_CLIP;
    
    private final int LEN_X = 560;
    private final int LEN_Y = 368; //568;
    private final int MARK_SIZE = 20;
    private final int MIN_RADIUS = 300;
    private final int RADIUS_OFFSET = 140;
    
    private final int INITIAL_TIMER_TICKS = 25;
    private final int FAST_INCREMENT = 11;
    private final int SLOW_INCREMENT = 1;
    private final int PREAMBLE = 6;
    private final int SLOW_AT_DREGREES = 1440;
    
    private JLabel label;
    
    private Timer wheelTimer;
    private ArrayList<String> contenders;
    private ArrayList<String> contendersFomatted;
    private ArrayList<String> winners;
    private int totalRotation = 1; 
    private int timerResets = 1; 
    private int rotateDegrees = 0;
    private int numElements = 40;
    private int winner = 0;
    private double degreesEach = 360.0/(double)numElements;
    private boolean isInFinalQuarter = false;
    private boolean renderWheel = false;
    private boolean running = false;
    
    /*Double buffer variables*/
    private Image offScreenImage = null;
    private Graphics offScreenGraphics = null;
    private BufferedImage offScreenImageDrawed = null;
    private HashMap<Integer, BufferedImage> offScreenGraphicsDrawed;
    
    public WheelOfFortuneWheelPanel(JLabel label) {
        super();
        
        SPINNING_WHEEL_CLIP = Applet.newAudioClip(getClass().getResource(
                                                    "/resources/roullete.wav"));
        setDoubleBuffered(true);
        
        wheelTimer = new Timer(INITIAL_TIMER_TICKS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int rotationIncrement = FAST_INCREMENT;
                
                /*Calculating position of the winner.*/
                double winnerPosition=(-1*totalRotation+(-degreesEach)*(winner))%360;  
                
                /*Cheking if its time to stop.*/
                boolean timeToStop = isInFinalQuarter && (winnerPosition <= -90 
                                                      && winnerPosition >= -100);

                if(timeToStop)
                {
                    wheelTimer.stop();
                    synchronized(wheelTimer){
                        wheelTimer.notifyAll();
                    }
                    running = false;
                }
                
                /*Cheking if is in final quarter of spinning.*/
                if(!isInFinalQuarter)
                {
                    isInFinalQuarter = winnerPosition >=-5 && winnerPosition <=0
                                       && rotateDegrees >= SLOW_AT_DREGREES;
                }
                
               
                if(isInFinalQuarter)
                {
                    rotationIncrement = SLOW_INCREMENT;
                    
                    
                    /*Slowing down sound*/
                    if(isTimeToPlayClip(timerResets, rotationIncrement))
                        SPINNING_WHEEL_CLIP.play();

                    /*Increment delays*/
                    wheelTimer.setDelay(wheelTimer.getDelay()+1);
                }
                else
                {
                    SPINNING_WHEEL_CLIP.play();
                }
                
                rotateDegrees+=rotationIncrement;
                
                timerResets++;
                totalRotation+=rotationIncrement;
                revalidate();
                repaint();
            }
        });

        this.label = label;
        setSize(new Dimension(LEN_X, LEN_Y));
        //wheelTimer.start();
    }

    private boolean isTimeToPlayClip(int timerResets, int rotationIncrement)
    {
        return timerResets%(PREAMBLE-rotationIncrement)*2 == 0;
    }
    
    private void updatedFormatedNames()    
    {
        contendersFomatted = new ArrayList<>();
        for(String c : contenders)
        {
            c = c.trim();
            String[] split = c.split(" ");
            String formated="";
            
            if(split.length > 2)
            {
                formated = split[0]+" "+split[1].charAt(0);
            }
            else
            {
                formated = split[0];
            }
            contendersFomatted.add(formated);
        }
    }
            
    public void starWheel(ArrayList<String> contenders, ArrayList<String> winners)
    {
        this.contenders = contenders;
        this.winners = winners;
        offScreenGraphicsDrawed = new HashMap<>();
        
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                
                label.setText("");
                
                for(int i = 0; i < winners.size(); i++)
                {
                    String w = winners.get(i);
                    totalRotation = 1; 
                    timerResets = 1; 
                    rotateDegrees = 0;
                    winner = contenders.indexOf(w);
                    numElements = contenders.size();
                    updatedFormatedNames();
                    
                    degreesEach = 360.0/(double)numElements;
                    
                    isInFinalQuarter = false;
                    renderWheel = true;
                    wheelTimer.setDelay(INITIAL_TIMER_TICKS);
                    wheelTimer.start();
                    running = true;
                    
                    synchronized(wheelTimer)
                    {
                        try {
                            wheelTimer.wait();
                        } catch (InterruptedException ex) {
                        }
                    }
                    
                    String labelText = "<html>";
                    for(int j = 0; j <= i; j++)
                    {
                        labelText += winners.get(j)+"<br>";
                    }
                    labelText += "</html>";
                    
                    label.setText(labelText);
                    
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                    }
                    
                    if(i < winners.size()-1)
                    {
                        renderWheel = false;
                        contenders.remove(w);
                    }
                }
            }
        });
        
        t.start();    
    }      
    
    public void interruptWheel(){
        wheelTimer.stop();
        renderWheel = false;
    }
    
    public boolean isRunning(){
        return running;
    }
        
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        int divisor;
        
        if(renderWheel)
        {
            final Dimension d = getSize();

            if (offScreenImageDrawed == null) {   
            
                // Double-buffer: clear the offscreen image.                
                offScreenImageDrawed = (BufferedImage) createImage(d.width, d.height);   
            }          
            
           
            
            //Graphics2D g2D = (Graphics2D)g.create();
            Graphics2D g2D = (Graphics2D) offScreenImageDrawed.getGraphics().create();
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2D.setColor(Color.gray);
            g2D.clearRect(0, 0, d.width, d.height);
            
            //g2D.setRenderingHint(
            g2D.setFont(new Font("Arial", Font.BOLD, 16));

            /*Declaring colors*/
            Color color1 = Color.BLUE;
            Color color2 = Color.WHITE;
            
            /*Move center to the middle for x, and to edge for y.*/
            g2D.translate(LEN_X/2, LEN_Y);

            /*rotate -rot*/
            g2D.rotate(Math.toRadians(-1*totalRotation));

            
            boolean plusOne = false;
            if(numElements % 2 == 0) 
            {
                divisor = 2;
            }
            else
            {   if(numElements%3 == 1)
                    plusOne = true;
            
                divisor = 3;
            }
            
            
            /*Render slices of the Wheel*/
            for(int i = 0; i < numElements; i++)
            {
                int remainder = i%divisor;
                //remainder = (remainder == 1 && i == numElements-1)?
                
                if(remainder==0)
                {
                    if(i == (numElements-1) && plusOne)
                    {
                        color1 = Color.RED;
                    }
                    else
                    {
                        color1 = Color.BLACK;
                    }
                    
                    color2 = Color.WHITE;      
                }
                else if(remainder==1)
                {
                    color1 = Color.RED;
                    color2 = Color.WHITE; 
                }
                else
                {
                    color1 = Color.WHITE; 
                    color2 = Color.BLACK;
                }
                
                g2D.setColor(color1);
                Arc2D.Double arc = new Arc2D.Double(-MIN_RADIUS, -MIN_RADIUS, MIN_RADIUS*2, 
                               MIN_RADIUS*2, -degreesEach/2, degreesEach, Arc2D.PIE);
                g2D.setPaint(color1);
                g2D.fill(arc);
                
                //g2D.fillArc(-MIN_RADIUS, -MIN_RADIUS, MIN_RADIUS*2, 
                //           MIN_RADIUS*2, -degreesEach/2, degreesEach);

                g2D.setColor(color2);
                g2D.drawString(contendersFomatted.get(i),MIN_RADIUS-RADIUS_OFFSET,0);
                
                //g2D.setColor(Color.BLACK);
                //g2D.drawArc(-MIN_RADIUS, -MIN_RADIUS, MIN_RADIUS*2, 
                //          MIN_RADIUS*2, -degreesEach/2, degreesEach);
                
                
                
                g2D.rotate(Math.toRadians(-degreesEach));
            }


            /*Render double buffered image.*/
            g.drawImage(offScreenImageDrawed, 0, 0, null);
            
            /*Render marker at the top.*/
            g.fillOval(LEN_X/2-MARK_SIZE, (LEN_Y/2)-MARK_SIZE-100, MARK_SIZE, MARK_SIZE);
            
        }
    }


}