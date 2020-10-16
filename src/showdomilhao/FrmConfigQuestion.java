package showdomilhao;

import data.Question;
import data.QuestionParser;
import data.SelectedQuestion;
import java.awt.CardLayout;
import java.awt.Component;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


public class FrmConfigQuestion extends javax.swing.JFrame {

    private data.Class currentClass = null;
    private SelectedQuestion selectedQuestion = null;
    private WheelOfFortuneWheelPanel wheelOfFortuneWheelPanel;
    private ArrayList<String> presentStudents = new ArrayList<>();
    private ArrayList<String> contenders = new ArrayList<>();
    private ArrayList<String> winners = new ArrayList<>();
    
    Random rand = new SecureRandom();
    
    public FrmConfigQuestion(data.Class currentClass) {
                
        initComponents();
        this.currentClass = currentClass;
        
        try{
            
            ClipboardKeyAdapter kl = new ClipboardKeyAdapter(tablePresence);
            tablePresence.addKeyListener(kl);

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for(Component component : this.getComponents())
            {
               SwingUtilities.updateComponentTreeUI(component);
            }
            
            tableQuestions.getColumnModel().getColumn(0).setPreferredWidth(700);
            tableQuestions.getColumnModel().getColumn(1).setPreferredWidth(100);
            tablePresence.getColumnModel().getColumn(0).setPreferredWidth(800);
            tablePresence.getColumnModel().getColumn(1).setPreferredWidth(100);
            
            /*Setting sorters*/
            RowSorter<TableModel> sorter 
                    = new TableRowSorter<>(tableQuestions.getModel());
            RowSorter<TableModel> sorter2 
                    = new TableRowSorter<>(tablePresence.getModel());
            
            tableQuestions.setRowSorter(sorter);
            tablePresence.setRowSorter(sorter2);
            
            /*Setting Wheel of fortune.*/
            wheelOfFortuneWheelPanel = new WheelOfFortuneWheelPanel(lblWinners);
            wheelOfFortuneWheelPanel.setLocation(0, 268);
            pnWheelPlaceHolder.add(wheelOfFortuneWheelPanel);
            
            /*Setting handler for table selection change.*/
            tableQuestions.getSelectionModel().addListSelectionListener(
                                               new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent lse) {
                    previewQuestion();
                }
            });
            
            loadQuestions();
            loadStudents();
            
        } catch (ClassNotFoundException | IllegalAccessException 
                 | InstantiationException | UnsupportedLookAndFeelException ex){
            System.err.println(ex);
        }
        
    }
    
    public <T> ArrayList<T> intersection(List<T> list1, List<T> list2) {
        ArrayList<T> list = new ArrayList<T>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
    
    private void loadQuestions()
    {
        String spuriousFilesEnd = "~";
        DefaultTableModel dtm = (DefaultTableModel) tableQuestions.getModel();
        
        for(Entry<Integer, Question> entry : currentClass.getQuestions().entrySet())
        {
            Question q = entry.getValue();
            boolean hasBeenAsked = false;
            
            if(currentClass.getAnswers().containsKey(entry.getKey()))
            {
                hasBeenAsked = true;
            }
            else
            {
                hasBeenAsked = false;
            }
            
            if(!q.getFile().endsWith(spuriousFilesEnd))
               dtm.addRow(new Object[]{q, ((hasBeenAsked)?"Sim":"Não")});        
        }
    }
    
    private void loadStudents()
    {
        DefaultTableModel dtm = (DefaultTableModel) tablePresence.getModel();
        
        for(String student : currentClass.getStudents())
        { 
            dtm.addRow(new Object[]{student, "P"});        
        }
    }
    
    private void previewQuestion()
    {
        if(tableQuestions.getSelectedRow() != -1)
        {
            DefaultTableModel dtm = (DefaultTableModel)tableQuestions.getModel();
            int selectedRow = tableQuestions.getSelectedRow();
            int modelRow = tableQuestions.convertRowIndexToModel(selectedRow);
            Question q = (Question)dtm.getValueAt(modelRow, 0);
            QuestionParser parser = new QuestionParser();
            selectedQuestion = parser.parser(q);
            
            if(selectedQuestion != null)
                txtQuestionPreview.setText(selectedQuestion.toString());
        }
    }
    
    private void startWheel()
    {
        lblWinners.setText("");
        if(wheelOfFortuneWheelPanel.isRunning())
            wheelOfFortuneWheelPanel.interruptWheel();
        
        presentStudents = new ArrayList<>();
        contenders = new ArrayList<>();
        winners = new ArrayList<>();
        
        DefaultTableModel dtm = (DefaultTableModel) tablePresence.getModel();
        int numRows = dtm.getRowCount();
        
        for(int i = 0; i < numRows; i++)
        { 
            if(dtm.getValueAt(i, 1).toString().toLowerCase().equals("p"))
            {
                 presentStudents.add(dtm.getValueAt(i, 0).toString());
            }     
        }
        
        contenders = intersection(presentStudents, currentClass.getLivePool());
        
        int runs = Integer.parseInt(spnRuns.getValue().toString());
        
        if(contenders.size() < runs)
        {
            JOptionPane.showMessageDialog(this, "Não existem alunos suficientes " + 
                                         "no Livepool", "Erro", JOptionPane.ERROR_MESSAGE); 
            return;
        }
        
        ArrayList<String> auxContenders = new ArrayList<>();
        auxContenders.addAll(contenders);
        
        for(int i=0; i < runs; i++)
        {
            int winner = rand.nextInt(auxContenders.size());
            winners.add(auxContenders.get(winner));
            auxContenders.remove(winner);
        }
        
        wheelOfFortuneWheelPanel.starWheel(contenders, winners);
        pnWheelPlaceHolder.repaint();
    }
    
    private void startShowDoMilhao()
    {
        if(selectedQuestion != null
           && winners.size() > 0)
        {
            if(wheelOfFortuneWheelPanel.isRunning())
                wheelOfFortuneWheelPanel.interruptWheel();
            
            FrmShowDoMilhao frm = new FrmShowDoMilhao(currentClass,
                                                      selectedQuestion, 
                                                      presentStudents, 
                                                      winners);
            
            frm.setVisible(true);
            frm.setLocationRelativeTo(null);
            wheelOfFortuneWheelPanel.interruptWheel();
            wheelOfFortuneWheelPanel.setVisible(false);
            this.setVisible(false);
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

        pnMain = new javax.swing.JPanel();
        pnQuestionSelection = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableQuestions = new javax.swing.JTable();
        btnCancel = new showdomilhao.JGradientButton();
        btnProximo = new showdomilhao.JGradientButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtQuestionPreview = new javax.swing.JTextArea();
        pnConfig = new javax.swing.JPanel();
        btnProximo1 = new showdomilhao.JGradientButton();
        btnCancel1 = new showdomilhao.JGradientButton();
        btnPrevious = new showdomilhao.JGradientButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablePresence = new javax.swing.JTable();
        pnWheelPlaceHolder = new javax.swing.JPanel();
        spnRuns = new javax.swing.JSpinner();
        btnStartWheel = new javax.swing.JButton();
        lblWinners = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Show do Milhão");
        setBackground(new java.awt.Color(242, 242, 242));
        setResizable(false);
        setSize(new java.awt.Dimension(1240, 721));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnMain.setLayout(new java.awt.CardLayout());

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tableQuestions.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tableQuestions.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Pergunta", "Respondida?"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableQuestions.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableQuestions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableQuestionsMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableQuestions);

        btnCancel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCancel.setText("Cancelar");
        btnCancel.setFocusable(false);
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnProximo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnProximo.setText("Próximo");
        btnProximo.setFocusable(false);
        btnProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximoActionPerformed(evt);
            }
        });

        txtQuestionPreview.setEditable(false);
        txtQuestionPreview.setColumns(20);
        txtQuestionPreview.setLineWrap(true);
        txtQuestionPreview.setRows(5);
        jScrollPane2.setViewportView(txtQuestionPreview);

        javax.swing.GroupLayout pnQuestionSelectionLayout = new javax.swing.GroupLayout(pnQuestionSelection);
        pnQuestionSelection.setLayout(pnQuestionSelectionLayout);
        pnQuestionSelectionLayout.setHorizontalGroup(
            pnQuestionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnQuestionSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnQuestionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnQuestionSelectionLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnProximo, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnQuestionSelectionLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnQuestionSelectionLayout.setVerticalGroup(
            pnQuestionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnQuestionSelectionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnQuestionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(pnQuestionSelectionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProximo, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnMain.add(pnQuestionSelection, "cardQuestionSel");

        btnProximo1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnProximo1.setText("Próximo");
        btnProximo1.setFocusable(false);
        btnProximo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProximo1ActionPerformed(evt);
            }
        });

        btnCancel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCancel1.setText("Cancelar");
        btnCancel1.setFocusable(false);
        btnCancel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancel1ActionPerformed(evt);
            }
        });

        btnPrevious.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPrevious.setText("Anterior");
        btnPrevious.setFocusable(false);
        btnPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPreviousActionPerformed(evt);
            }
        });

        jScrollPane4.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        tablePresence.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tablePresence.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Vítimas", "Presença"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePresence.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePresence.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePresenceMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablePresence);

        pnWheelPlaceHolder.setPreferredSize(new java.awt.Dimension(576, 576));

        spnRuns.setModel(new javax.swing.SpinnerNumberModel(1, 1, 10, 1));

        btnStartWheel.setText("Sortear");
        btnStartWheel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartWheelActionPerformed(evt);
            }
        });

        lblWinners.setFont(new java.awt.Font("Arial", 1, 24)); // NOI18N
        lblWinners.setForeground(java.awt.Color.black);
        lblWinners.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblWinners.setToolTipText("");

        javax.swing.GroupLayout pnWheelPlaceHolderLayout = new javax.swing.GroupLayout(pnWheelPlaceHolder);
        pnWheelPlaceHolder.setLayout(pnWheelPlaceHolderLayout);
        pnWheelPlaceHolderLayout.setHorizontalGroup(
            pnWheelPlaceHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWheelPlaceHolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnWheelPlaceHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnWheelPlaceHolderLayout.createSequentialGroup()
                        .addGap(0, 291, Short.MAX_VALUE)
                        .addComponent(spnRuns, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStartWheel, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblWinners, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnWheelPlaceHolderLayout.setVerticalGroup(
            pnWheelPlaceHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnWheelPlaceHolderLayout.createSequentialGroup()
                .addGroup(pnWheelPlaceHolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spnRuns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStartWheel))
                .addGap(18, 18, 18)
                .addComponent(lblWinners)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnConfigLayout = new javax.swing.GroupLayout(pnConfig);
        pnConfig.setLayout(pnConfigLayout);
        pnConfigLayout.setHorizontalGroup(
            pnConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnConfigLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnConfigLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnProximo1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnConfigLayout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 622, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnWheelPlaceHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pnConfigLayout.setVerticalGroup(
            pnConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnConfigLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(pnConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                    .addComponent(pnWheelPlaceHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(pnConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnProximo1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrevious, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnMain.add(pnConfig, "cardConfig");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void btnProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProximoActionPerformed
        if(selectedQuestion != null)
        {
            CardLayout cardLayout = (CardLayout) this.pnMain.getLayout();
            cardLayout.show(this.pnMain, "cardConfig");
        }
    }//GEN-LAST:event_btnProximoActionPerformed

    private void tableQuestionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableQuestionsMouseClicked
       //previewQuestion();
    }//GEN-LAST:event_tableQuestionsMouseClicked

    private void btnProximo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProximo1ActionPerformed
       startShowDoMilhao();
    }//GEN-LAST:event_btnProximo1ActionPerformed

    private void btnCancel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancel1ActionPerformed
        if(wheelOfFortuneWheelPanel.isRunning())
                wheelOfFortuneWheelPanel.interruptWheel();
        this.dispose();
    }//GEN-LAST:event_btnCancel1ActionPerformed

    private void btnPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPreviousActionPerformed
        CardLayout cardLayout = (CardLayout) this.pnMain.getLayout();
        cardLayout.show(this.pnMain, "cardQuestionSel");
        if(wheelOfFortuneWheelPanel.isRunning())
                wheelOfFortuneWheelPanel.interruptWheel();
    }//GEN-LAST:event_btnPreviousActionPerformed

    private void tablePresenceMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePresenceMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablePresenceMouseClicked

    private void btnStartWheelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartWheelActionPerformed
        startWheel();
        btnStartWheel.setEnabled(false);
    }//GEN-LAST:event_btnStartWheelActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if(wheelOfFortuneWheelPanel.isRunning())
                wheelOfFortuneWheelPanel.interruptWheel();
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmConfigQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmConfigQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmConfigQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmConfigQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmConfigQuestion(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private showdomilhao.JGradientButton btnCancel;
    private showdomilhao.JGradientButton btnCancel1;
    private showdomilhao.JGradientButton btnPrevious;
    private showdomilhao.JGradientButton btnProximo;
    private showdomilhao.JGradientButton btnProximo1;
    private javax.swing.JButton btnStartWheel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel lblWinners;
    private javax.swing.JPanel pnConfig;
    private javax.swing.JPanel pnMain;
    private javax.swing.JPanel pnQuestionSelection;
    private javax.swing.JPanel pnWheelPlaceHolder;
    private javax.swing.JSpinner spnRuns;
    private javax.swing.JTable tablePresence;
    private javax.swing.JTable tableQuestions;
    private javax.swing.JTextArea txtQuestionPreview;
    // End of variables declaration//GEN-END:variables

    
}
