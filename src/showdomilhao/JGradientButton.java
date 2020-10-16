package showdomilhao;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
 
/**
 * An implementation of a "push" button which support gradient background with
 * two color. It extends JButton. So we can use all the feature of JButton. It has a method named 
 * setGradient(Color color1, Color color2) by which we can set the colors of the gradient background.
 * And also setGradient(Color color1, Color color2, int align) which allows us to set the alignment of these colors.
 * 
 * @author Mashuk
 */
public class JGradientButton extends JButton implements MouseListener {
 
    /**
     * first color of the Gradient background.
     */
    private Color color1;
    /**
     * second color of the Gradient background.
     */
    private Color color2;
    /**
     * determine the alignment of the gradient colors.
     */
    private int align;
    /**
     * 
     */
    private Color oldForeground;
    /**
     * 
     */
    private Color borderColor;
    /**
     * The first color of the Gradient background which acts as Default.
     */
    private static final Color DEFAULT_COLOR_1 = new Color(245, 245, 245);
    /**
     * The second color of the Gradient background which acts as Default.
     */
    private static final Color DEFAULT_COLOR_2 = new Color(230, 230, 230);
    /**
     * This allows to set colors alignment as color1 start from top and color2 start from bottom. 
     * we can use it as a value of parameter like as: GradientButton.TOP_LEFT_TO_BOTTOM_RIGHT which returns a int value 0.
     */
    public static final int TOP_TO_BOTTOM = 0;
    /**
     * This allows to set colors alignment as color1 start from left and color2 start from right. 
     * we can use it as a value of parameter like as: GradientButton.TOP_LEFT_TO_BOTTOM_RIGHT which returns a int value 1.
     */
    public static final int LEFT_TO_RIGHT = 1;
    /**
     * This allows to set colors alignment as color1 start from top-left corner and color2 start from bottom-right corner. 
     * we can use it as a value of parameter like as: GradientButton.TOP_LEFT_TO_BOTTOM_RIGHT which returns a int value 2.
     */
    public static final int TOP_LEFT_TO_BOTTOM_RIGHT = 2;
    /**
     * This allows to set colors alignment as color1 start from bottom-left corner and color2 start from top-right corner. 
     * we can use it as a value of parameter like as: GradientButton.TOP_LEFT_TO_BOTTOM_RIGHT which returns a int value 3.
     */
    public static final int BOTTOM_LEFT_TO_TOP_RIGHT = 3;
    /**
     * This sets the default alignment of the colors.
     */
    private static final int DEFAULT_ALIGN = TOP_TO_BOTTOM;
    /**
     * 
     */
    private int mouse_action;
    /**
     *
     */
    private Graphics2D g2 = null;
 
    /**
     * Creates a Gradientbutton with no set text or icon.
     */
    public JGradientButton() {
        super();
        this.color1 = DEFAULT_COLOR_1;
        this.color2 = DEFAULT_COLOR_2;
        this.align = DEFAULT_ALIGN;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton where properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a the <code>Action</code> used to specify the new button
     *
     * @since 1.3
     */
    public JGradientButton(Action a) {
        super(a);
        this.color1 = DEFAULT_COLOR_1;
        this.color2 = DEFAULT_COLOR_2;
        this.align = DEFAULT_ALIGN;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton with an icon.
     *
     * @param icon the Icon image to display on the button
     */
    public JGradientButton(Icon icon) {
        super(icon);
        this.color1 = DEFAULT_COLOR_1;
        this.color2 = DEFAULT_COLOR_2;
        this.align = DEFAULT_ALIGN;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton with text.
     *
     * @param text the text of the button
     */
    public JGradientButton(String text) {
        super(text);
        this.color1 = DEFAULT_COLOR_1;
        this.color2 = DEFAULT_COLOR_2;
        this.align = DEFAULT_ALIGN;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton with initial text and an icon.
     *
     * @param text the text of the button
     * @param icon the Icon image to display on the button
     */
    public JGradientButton(String text, Icon icon) {
        super(text, icon);
        this.color1 = DEFAULT_COLOR_1;
        this.color2 = DEFAULT_COLOR_2;
        this.align = DEFAULT_ALIGN;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton with specific gradient colors and alignment.
     *
     * @param color1 First color of the gradient.
     * @param color2 Second color of the gradient.
     * @param align Determine the position of the colors. Here has four options.
     * Such as TOP_TO_BOTTOM which is default, LEFT_TO_RIGHT,
     * TOP_LEFT_TO_BOTTOM_RIGHT, BOTTOM_LEFT_TO_TOP_RIGHT.
     */
    public JGradientButton(Color color1, Color color2, int align) {
        super();
        this.color1 = color1;
        this.color2 = color2;
        this.align = align;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton with specific gradient colors ,alignment and initial
     * icon.
     *
     * @param color1 First color of the gradient.
     * @param color2 Second color of the gradient.
     * @param align Determine the position of the colors. Here has four options.
     * Such as TOP_TO_BOTTOM which is default, LEFT_TO_RIGHT,
     * TOP_LEFT_TO_BOTTOM_RIGHT, BOTTOM_LEFT_TO_TOP_RIGHT.
     * @param icon the Icon image to display on the button
     */
    public JGradientButton(Color color1, Color color2, int align, Icon icon) {
        super(icon);
        this.color1 = color1;
        this.color2 = color2;
        this.align = align;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton with specific gradient colors ,alignment and initial
     * text.
     *
     * @param color1 First color of the gradient.
     * @param color2 Second color of the gradient.
     * @param align Determine the position of the colors. Here has four options.
     * Such as TOP_TO_BOTTOM which is default, LEFT_TO_RIGHT,
     * TOP_LEFT_TO_BOTTOM_RIGHT, BOTTOM_LEFT_TO_TOP_RIGHT.
     * @param text the text of the button
     */
    public JGradientButton(Color color1, Color color2, int align, String text) {
        super(text);
        this.color1 = color1;
        this.color2 = color2;
        this.align = align;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * Creates a Gradientbutton with specific gradient colors ,alignment and initial
     * text and icon.
     *
     * @param color1 First color of the gradient.
     * @param color2 Second color of the gradient.
     * @param align Determine the position of the colors. Here has four options.
     * Such as TOP_TO_BOTTOM which is default, LEFT_TO_RIGHT,
     * TOP_LEFT_TO_BOTTOM_RIGHT, BOTTOM_LEFT_TO_TOP_RIGHT.
     * @param text the text of the button
     * @param icon the Icon image to display on the button
     */
    public JGradientButton(Color color1, Color color2, int align, String text, Icon icon) {
        super(text, icon);
        this.color1 = color1;
        this.color2 = color2;
        this.align = align;
        this.borderColor = Color.BLACK;
        addMouseListener(this);
    }
 
    /**
     * This method is use to set the colors of the gradient background.
     *
     * @param color1 First color of the gradient.
     * @param color2 Second color of the gradient.
     * 
     */
    public void setGradient(Color color1, Color color2) {
        this.color1 = color1;
        this.color2 = color2;
    }
 
    /**
     * This method is use to set the colors of the gradient background. It also allows
     * to set the alignment of these colors.
     *
     * @param color1 First color of the gradient.
     * @param color2 Second color of the gradient.
     * @param align Determine the position of the colors. Here has four options.
     * Such as TOP_TO_BOTTOM which is default, LEFT_TO_RIGHT,
     * TOP_LEFT_TO_BOTTOM_RIGHT, BOTTOM_LEFT_TO_TOP_RIGHT.
     */
    public void setGradient(Color color1, Color color2, int align) {
        this.color1 = color1;
        this.color2 = color2;
        this.align = align;
    }
 
    @Override
    public void mouseClicked(MouseEvent e) {
    }
 
    @Override
    public void mousePressed(MouseEvent e) {
        this.oldForeground = this.getForeground();
        /*Color blue = new Color(125, 99, 171);
        if (this.oldForeground == blue) {
            this.setForeground(Color.BLACK);
        } else {
            this.setForeground(blue);
        }
        this.borderColor = blue;
        */
        mouse_action = 1;
    }
 
    @Override
    public void mouseReleased(MouseEvent e) {
        //this.setForeground(this.oldForeground);
        mouse_action = 0;
        //this.borderColor = Color.GREEN;
    }
 
    @Override
    public void mouseEntered(MouseEvent e) {
        //mouse_action = 1;
        //this.borderColor = Color.GREEN;
    }
 
    @Override
    public void mouseExited(MouseEvent e) {
        //mouse_action = 0;
        //this.borderColor = Color.BLACK;
    }
    /*
     * Overriden method to make the gradient background.
     */
 
    /*@Override
    protected void paintComponent(Graphics g) {
        setContentAreaFilled(false);
        if (ui != null) {
            Graphics scratchGraphics = (g == null) ? null : g.create();
            g2 = (Graphics2D) scratchGraphics;
 
            if (this.align == TOP_TO_BOTTOM) {
                if (mouse_action == 0) {
                    g2.setPaint(new GradientPaint(new Point(0, 0), this.color1, new Point(0, getHeight()), this.color2));
                }
                if (mouse_action == 1) {
                    g2.setPaint(new GradientPaint(new Point(0, 0), this.color2, new Point(0, getHeight()), this.color1));
                }
            }
            if (this.align == LEFT_TO_RIGHT) {
                if (mouse_action == 0) {
                    g2.setPaint(new GradientPaint(new Point(0, 0), this.color1, new Point(getWidth(), 0), this.color2));
                }
                if (mouse_action == 1) {
                    g2.setPaint(new GradientPaint(new Point(0, 0), this.color2, new Point(getWidth(), 0), this.color1));
                }
            }
            if (this.align == TOP_LEFT_TO_BOTTOM_RIGHT) {
                if (mouse_action == 0) {
                    g2.setPaint(new GradientPaint(new Point(0, 0), this.color1, new Point(getWidth(), getHeight()), this.color2));
                }
                if (mouse_action == 1) {
                    g2.setPaint(new GradientPaint(new Point(0, 0), this.color2, new Point(getWidth(), getHeight()), this.color1));
                }
            }
            if (this.align == BOTTOM_LEFT_TO_TOP_RIGHT) {
                if (mouse_action == 0) {
                    g2.setPaint(new GradientPaint(new Point(0, getHeight()), this.color1, new Point(getWidth(), 0), this.color2));
                }
                if (mouse_action == 1) {
                    g2.setPaint(new GradientPaint(new Point(0, getHeight()), this.color2, new Point(getWidth(), 0), this.color1));
                }
            }
 
            //g2.fillRoundRect(0, 0, getWidth(), getHeight(), 2, 2);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 7, 7);
            g2.setPaint(this.borderColor);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 7, 7);
            try {
                ui.update(g2, this);
            } finally {
                scratchGraphics.dispose();
                g2.dispose();
            }
        }
    }*/
}