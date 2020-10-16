/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package showdomilhao;

import data.Answer;
import data.SelectedQuestion;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


/**
 *
 * @author hermano
 */
public class AnswerDialog extends javax.swing.JDialog {
   
    private SelectedQuestion question;
    private ArrayList<String> students;
    private Answer answer = null;
    private HashMap<String, ButtonGroup> bgs = new HashMap<>();
    
    public AnswerDialog(java.awt.Frame parent, boolean modal, 
                        SelectedQuestion q, ArrayList<String> students) {
        
        super(parent, modal);
        initComponents();
        
        String alternatives[] = {"A", "B", "C", "D", "E"};
        this.question = q;
        this.students = students;
        
        this.setLayout(new GridLayout(students.size()*2+1,1));
        
        for(int i = 0; i < students.size(); i++)
        {
            String student = students.get(i);
            JLabel lbl = new JLabel(student);
            lbl.setFont(new Font("Arial", Font.BOLD, 16));
            this.add(lbl);
            
            ButtonGroup bG = new ButtonGroup();
            bgs.put(student, bG);
            
            JPanel pn = new JPanel();
            pn.setLayout(new FlowLayout());
            pn.setSize(400, 200);
            pn.setPreferredSize(new Dimension(400, 200));
            
            for(int j = 0; j < q.getAlternatives().size(); j++)
            {
                JRadioButton alt = new JRadioButton(alternatives[j]);
                bG.add(alt);
                pn.add(alt);
                
                if(j==0)
                    alt.setSelected(true);
            }
            this.add(pn);
        }
        JButton btnConfirm = new JButton("Responder");
        btnConfirm.setSize(200, 50);
        btnConfirm.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) { 
                
                answer = new Answer();
                answer.setDate(LocalDateTime.now());
                answer.setQuestion(question);
                answer.setStudents(students);
                HashMap<String, Character> alternatives = new HashMap<>();
                
                for(Entry<String, ButtonGroup> entry : bgs.entrySet())
                {
                    String selection = getSelectedButtonText(entry.getValue());
                    alternatives.put(entry.getKey(), selection.charAt(0));
                }
                answer.setAlternatives(alternatives);
                setVisible(false);
            } 
        });
        this.add(btnConfirm);
        
        this.setPreferredSize(new Dimension(400, (students.size()+1)*90));
        this.pack();
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AnswerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(AnswerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(AnswerDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AnswerDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(Component component : this.getComponents())
        {
           SwingUtilities.updateComponentTreeUI(component);
        }
    }
    
    public String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements();
             buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }
        return null;
    }
    
    public Answer getAnswer()
    {
        return answer;
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
        setTitle("Respostas");
        setModal(true);
        setResizable(false);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(20, 0));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        int x=evt.getX();
        int y=evt.getY();
        System.out.println(x+","+y);
    }//GEN-LAST:event_formMouseClicked

  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
