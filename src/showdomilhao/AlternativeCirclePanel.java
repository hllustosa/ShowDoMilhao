package showdomilhao;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;

/**
 *
 * @author hermano
 */
public class AlternativeCirclePanel extends JPanel{
        
        String letter;
        
        AlternativeCirclePanel(String letter)
        {
            super();
            this.letter = letter;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            
            Graphics2D g2 = (Graphics2D) g;
            Paint whitePaint = new Color(255, 255, 255, 255);
            Paint bluePaint = new Color(47, 2, 244, 255);
            
            RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setRenderingHints(rh);
              
            Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 40, 40);
            g2.setPaint(whitePaint);
            g2.fill(circle);
            
            g2.setPaint(bluePaint);
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            g2.drawString(letter, 10, 32);
           
        }
}
