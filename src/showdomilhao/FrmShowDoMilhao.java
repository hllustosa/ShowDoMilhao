package showdomilhao;

import data.SelectedQuestion;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;


public class FrmShowDoMilhao extends javax.swing.JFrame {
    
    private final String FONT_NAME = "Arial";
    private final String BACKGROUND = "/resources/backallg.jpg";
    private final String BACKGROUND_QUESTION = "/resources/backquestion.png";
    private final String BACKGROUND_ALTERNATIVE = "/resources/backalt.png";
    private final String BACKGROUND_ALTERNATIVE_B = "/resources/backalt_green.png";
    private final String THEME_SONG = "/resources/tema.wav";
    private final String[] SILVIOS = {"/resources/silvio1.jpg", 
                                      "/resources/silvio2.jpg",
                                      "/resources/silvio3.jpg"};
    
    private final AudioClip CAN_I_ASK = Applet.newAudioClip(getClass().getResource(
                                                "/resources/posso_perguntar.wav"));
    private final AudioClip APPLAUSE = Applet.newAudioClip(getClass().getResource(
                                                "/resources/applause.wav"));
    private final AudioClip RIGHT_ANSWER = Applet.newAudioClip(getClass().getResource(
                                                "/resources/certa.wav"));
    
    
    private final AudioClip WRONG = Applet.newAudioClip(getClass().getResource(
                                                "/resources/wronganswer.wav"));
    
    private final AudioClip CLIP = Applet.newAudioClip(getClass().getResource(
                                                "/resources/tema.wav"));
    
    private final Dimension IMG1_SIZE = new Dimension(960, 720);
    private final Dimension IMG2_SIZE = new Dimension(480, 720);
    
    //private final Dimension QUESTION_SIZE = new Dimension(868, 271);
    private final Dimension QUESTION_SIZE = new Dimension(844, 203);
    private final Point QUESTION_POS = new Point(-50, -1);
    private final Dimension ALT_SIZE = new Dimension(843, 104);
    private final Point[] ALT_POS = {new Point(-70, 200),
                                     new Point(-70, 200+104*1),
                                     new Point(-70, 200+104*2),
                                     new Point(-70, 200+104*3),
                                     new Point(-70, 200+104*4),
                                    };
    
    private final Dimension QUESTION_TXT_SIZE = new Dimension(770, 250);
    private final Point QUESTION_TXT_POS = new Point(10, 10);
    private final Dimension ALT_TXT_SIZE = new Dimension(680, 104);
    private final Point[] ALT_TXT_POS = {new Point(60, 213),
                                         new Point(60, 213+105*1),
                                         new Point(60, 213+105*2),
                                         new Point(60, 213+105*3),
                                         new Point(60, 213+105*4),
                                        };
    
    private final Dimension ALT_ZOOMIN_BTN_SIZE = new Dimension(30, 30);
    private final Point[] ALT_ZOOMIN_BTN_POS = {new Point(754, 220),
                                                new Point(754, 220+105*1),
                                                new Point(754, 220+105*2),
                                                new Point(754, 220+105*3),
                                                new Point(754, 220+105*4),
                                               };
    
    private final Dimension ALT_ZOOMOUT_BTN_SIZE = new Dimension(30, 30);
    private final Point[] ALT_ZOOMOUT_BTN_POS = {new Point(754, 250),
                                                new Point(754, 250+105*1),
                                                new Point(754, 250+105*2),
                                                new Point(754, 250+105*3),
                                                new Point(754, 250+105*4),
                                               };
    
    private final Dimension ALT_CIRCLE_SIZE = new Dimension(200, 200);
    private final Point[] ALT_CIRCLE_POS =   {new Point(5, 230),
                                              new Point(5, 230+105*1),
                                              new Point(5, 230+105*2),
                                              new Point(5, 230+105*3),
                                              new Point(5, 230+105*4),
                                             };

    private final String BTN_ANSWER_TXT[] = {"Abrir Contagem", "Responder"};
    private final String[] ALT_LETTERS = {"A", "B", "C", "D", "E"};
    
    private int screenState = 0;
    private JButton btnAnswer; JTextArea txtQuestion; 
    private SelectedQuestion question;
    private ArrayList<String> winners;
    private ArrayList<String> presentStudents;
    private data.Class currentClass;
    private data.Answer answer;
    private boolean resultsAvailable = false;
    
    private ArrayList<ImagePanel> alternativeBackgrounds = new ArrayList<>();
    private Font font = new Font(FONT_NAME, Font.BOLD | Font.ITALIC, 22);
    private ArrayList<JTextArea> textAreas = new ArrayList<>();
    private HashMap<JButton, JTextArea> buttonMap = new HashMap<>();
    private AlternativeCirclePanel circle; ImagePanel backAlternative;
    
    public FrmShowDoMilhao(data.Class currentClasse,
                           SelectedQuestion question, 
                           ArrayList<String> presentStudents,
                           ArrayList<String> winners) {
       
        Stack<Component> components = new Stack<>();
        initComponents();
        this.currentClass = currentClasse;
        this.question = question;
        this.winners = winners;
        this.presentStudents = presentStudents;
        
        try
        {
            ImagePanel img1 = new ImagePanel(BACKGROUND, true);
            img1.setSize(IMG1_SIZE);
            img1.setLocation(0,0);
            components.push(img1);

            int silvioNum = ThreadLocalRandom.current().nextInt(0, 3);
            ImagePanel img2 = new ImagePanel(SILVIOS[silvioNum], true);
            img2.setSize(IMG2_SIZE);
            img2.setLocation(770,0);
            components.push(img2);

            ImagePanel backQuestion = new ImagePanel(BACKGROUND_QUESTION, true);
            backQuestion.setSize(QUESTION_SIZE);
            backQuestion.setLocation(QUESTION_POS);
            backQuestion.setOpaque(false);
            components.push(backQuestion);

            for(int i = 0; i < question.getAlternatives().size(); i++)
            {
                backAlternative = new ImagePanel(BACKGROUND_ALTERNATIVE,
                                                            BACKGROUND_ALTERNATIVE_B, 
                                                            true);
                backAlternative.setSize(ALT_SIZE);
                backAlternative.setLocation(ALT_POS[i]);
                backAlternative.setOpaque(false);
                alternativeBackgrounds.add(backAlternative);
                components.push(backAlternative);
            }

            txtQuestion = new JTextArea(20, 100);
            txtQuestion.setLineWrap(true);
            txtQuestion.setWrapStyleWord(true);
            txtQuestion.setSize(QUESTION_TXT_SIZE);
            txtQuestion.setForeground(Color.WHITE);
            txtQuestion.setLocation(QUESTION_TXT_POS);
            txtQuestion.setFont(font);
            txtQuestion.setOpaque(false);
            txtQuestion.setText(question.getQuestion());
            txtQuestion.setEditable(false);
            components.push(txtQuestion);

            JButton btnZoomInQ = new JButton("+");
            btnZoomInQ.setFocusable(false);
            btnZoomInQ.setMargin(new Insets(0, 0, 0, 0));
            btnZoomInQ.setBackground(Color.red);
            btnZoomInQ.setForeground(Color.BLACK);
            btnZoomInQ.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
            btnZoomInQ.setBorder(null);
            btnZoomInQ.setLocation(773, 140);
            btnZoomInQ.setSize(ALT_ZOOMIN_BTN_SIZE);
            btnZoomInQ.setOpaque(false);
            btnZoomInQ.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) {
                    JTextArea txtArea = buttonMap.get(e.getSource());
                    int size = txtArea.getFont().getSize()+2;
                    txtArea.setFont(new Font(font.getFamily(), Font.BOLD | Font.ITALIC, size));
                    txtArea.repaint();
                } 
            });
            buttonMap.put(btnZoomInQ, txtQuestion);
            components.push(btnZoomInQ);

            JButton btnZoomOutQ = new JButton("−");
            btnZoomOutQ.setFocusable(false);
            btnZoomOutQ.setMargin(new Insets(0, 0, 0, 0));
            btnZoomOutQ.setBackground(Color.red);
            btnZoomOutQ.setForeground(Color.BLACK);
            btnZoomOutQ.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
            btnZoomOutQ.setBorder(null);
            btnZoomOutQ.setLocation(773, 170);
            btnZoomOutQ.setSize(ALT_ZOOMIN_BTN_SIZE);
            btnZoomOutQ.addActionListener(new ActionListener() { 
                public void actionPerformed(ActionEvent e) { 
                    JTextArea txtArea = buttonMap.get(e.getSource());
                    int size = txtArea.getFont().getSize()-2;
                    txtArea.setFont(new Font(font.getFamily(),  Font.BOLD | Font.ITALIC, size));
                    txtArea.repaint();
                } 
            });
            buttonMap.put(btnZoomOutQ, txtQuestion);
            components.push(btnZoomOutQ);

            for(int i = 0; i < question.getAlternatives().size(); i++)
            {
                JTextArea txtAlternative = new JTextArea(1, 100);
                txtAlternative.setLineWrap(true);
                txtAlternative.setWrapStyleWord(true);
                txtAlternative.setSize(ALT_TXT_SIZE);
                txtAlternative.setForeground(Color.WHITE);
                txtAlternative.setLocation(ALT_TXT_POS[i]);
                txtAlternative.setFont(font);
                txtAlternative.setOpaque(false);
                txtAlternative.setText(question.getAlternatives().get(i));
                txtAlternative.setEditable(false);
                textAreas.add(txtAlternative);
                components.push(txtAlternative);

                JButton btnZoomIn = new JButton("+");
                btnZoomIn.setFocusable(false);
                btnZoomIn.setMargin(new Insets(0, 0, 0, 0));
                btnZoomIn.setBackground(Color.red);
                btnZoomIn.setForeground(Color.BLACK);
                btnZoomIn.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
                btnZoomIn.setBorder(null);
                btnZoomIn.setLocation(ALT_ZOOMIN_BTN_POS[i]);
                btnZoomIn.setSize(ALT_ZOOMIN_BTN_SIZE);
                btnZoomIn.addActionListener(new ActionListener() { 
                    public void actionPerformed(ActionEvent e) { 
                        JTextArea txtArea = buttonMap.get(e.getSource());
                        int size = txtArea.getFont().getSize()+2;
                        txtArea.setFont(new Font(font.getFamily(),  Font.BOLD | Font.ITALIC, size));
                        txtArea.repaint();
                        System.out.println("size "+txtArea.getPreferredSize().toString());
                        System.out.println("lines "+getHeight(txtArea));

                    } 
                });

                buttonMap.put(btnZoomIn, txtAlternative);
                components.push(btnZoomIn);

                JButton btnZoomOut = new JButton("−");
                btnZoomOut.setFocusable(false);
                btnZoomOut.setMargin(new Insets(0, 0, 0, 0));
                btnZoomOut.setBackground(Color.BLACK);
                btnZoomOut.setForeground(Color.BLACK);
                btnZoomOut.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
                btnZoomOut.setBorder(null);
                btnZoomOut.setLocation(ALT_ZOOMOUT_BTN_POS[i]);
                btnZoomOut.setSize(ALT_ZOOMOUT_BTN_SIZE);
                btnZoomOut.addActionListener(new ActionListener() { 
                    @Override
                    public void actionPerformed(ActionEvent e) { 
                        JTextArea txtArea = buttonMap.get(e.getSource());
                        int size = txtArea.getFont().getSize()-2;
                        txtArea.setFont(new Font(font.getFamily(),  Font.BOLD | Font.ITALIC, size));
                        txtArea.repaint();
                    } 
                });

                buttonMap.put(btnZoomOut, txtAlternative);
                components.push(btnZoomOut);

                circle = new AlternativeCirclePanel(ALT_LETTERS[i]);
                circle.setLocation(ALT_CIRCLE_POS[i]);
                circle.setSize(ALT_CIRCLE_SIZE);
                components.push(circle);
            }
            
            btnAnswer = new JButton("Responder");
            btnAnswer.setFocusable(false);
            btnAnswer.setMargin(new Insets(0, 0, 0, 0));
            btnAnswer.setBackground(Color.BLACK);
            btnAnswer.setForeground(Color.BLACK);
            btnAnswer.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
            btnAnswer.setBorder(null);
            btnAnswer.setLocation(1050,671);
            btnAnswer.setSize(160, 40);
            btnAnswer.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) { 
                    btnAnswer.setEnabled(false);
                    Answer();
                } 
            });
            components.push(btnAnswer);
            
            /*Adding components to JFrame*/
            while(!components.isEmpty())
            {
                this.add(components.pop());
            }
            
            this.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent e)
                {
                    showResults();
                }
            });
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for(Component component : this.getComponents())
            {
               SwingUtilities.updateComponentTreeUI(component);
            }
            
            adjustText();
            
            /*Playing song*/
            CLIP.loop();
        }
        catch(Exception ex)
        {
            System.err.println(ex.getMessage());
        }    
    }
    
    public int getLineCountAsSeen(JTextComponent txtComp) {
	Font font = txtComp.getFont();
        FontMetrics fontMetrics = txtComp.getFontMetrics(font);
        int fontHeight = fontMetrics.getHeight();
        int lineCount;
        try {
            int height = txtComp.modelToView(txtComp.getDocument().getEndPosition().getOffset() - 1).y;
            lineCount = height / fontHeight + 1;
        } catch (Exception e) { 
            lineCount = 0;
        }      
        return lineCount;
    }
    
    public int getHeight(JTextComponent txtComp)
    {
        Font font = txtComp.getFont();
        FontMetrics fontMetrics = txtComp.getFontMetrics(font);
        int fontHeight = fontMetrics.getHeight();
        int height;
        try {
             height = txtComp.modelToView(txtComp.getDocument().getEndPosition().getOffset() - 1).y;
            //lineCount = height / fontHeight + 1;
        } catch (Exception e) { 
            height = 0;
        }      
        return height;
    }
    
   
    private void adjustText()
    {
        int MIN = 12;
        int MAX_HEIGHT = 60;
        int MAX_HEIGHT_Q = 147;
        for(JTextArea txtArea : textAreas)
        {
            int size = MIN;
            while(size <= 22)
            {
                txtArea.setFont(new Font(font.getFamily(),  Font.BOLD | Font.ITALIC, size));
                txtArea.repaint(); 
                
                if(getHeight(txtArea) > MAX_HEIGHT)
                {
                    txtArea.setFont(new Font(font.getFamily(),  Font.BOLD | Font.ITALIC, size-1));
                    txtArea.repaint();
                    break;
                }       
                size+=1;
            }
            
            int lines = getLineCountAsSeen(txtArea);
            if(lines == 1)
            {
                txtArea.setText("\n"+txtArea.getText());
            }
        }
        
        int size = MIN;
        while(size <= 24)
        {
            txtQuestion.setFont(new Font(font.getFamily(), Font.BOLD | Font.ITALIC, size));
            txtQuestion.repaint(); 

            if(getHeight(txtQuestion) > MAX_HEIGHT_Q)
            {
                txtQuestion.setFont(new Font(font.getFamily(),  Font.BOLD | Font.ITALIC, size-1));
                txtQuestion.repaint();
                break;
            }       
            size+=1;
        }
    }
    
    void Answer()
    {
        Character alternatives[] = {'A', 'B', 'C', 'D', 'E'};
        CLIP.stop();
        AnswerDialog answerDialog = new AnswerDialog(this, true, 
                                                     question, winners);
        answerDialog.setLocationRelativeTo(null);
        answerDialog.setVisible(true);
        answer = answerDialog.getAnswer();
        
        CAN_I_ASK.play();
        
        try {
            Thread.sleep(3200);
        } catch (InterruptedException ex) {
            System.err.print(ex.getMessage());
        }
        
        boolean right = true;
        for(Character c : answer.getAlternatives().values())
        {
            if(!c.equals(alternatives[question.getAnswer()-1]))
            {
                right = false;
                break;
            }
        }
        
        if(right)
        {
            APPLAUSE.play();
            RIGHT_ANSWER.play();
        }
        else
        {
            WRONG.play();
        }
        
        ImagePanel img = alternativeBackgrounds.get(question.getAnswer()-1);
        Thread t = new Thread(img);
        t.start();
        resultsAvailable = true;
    }
    
    public void showResults()
    {
        if(resultsAvailable)
        {
            ResultsDialog resultsDialog = new ResultsDialog(this, true, 
                                                        currentClass, 
                                                        answer, 
                                                        presentStudents);
            resultsDialog.setLocationRelativeTo(null);
            resultsDialog.setVisible(true);
            this.dispose();
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Show do Milhão");
        setPreferredSize(new java.awt.Dimension(1240, 720));
        setResizable(false);
        setSize(new java.awt.Dimension(1240, 840));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1440, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 720, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        showResults();
    }//GEN-LAST:event_formMouseClicked

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
