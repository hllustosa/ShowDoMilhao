package showdomilhao;

import data.Database;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ResultsDialog extends javax.swing.JDialog {

    private data.Class currentClass;
    private data.Answer answer;
    private ArrayList<String> present;
                                              
    public ResultsDialog(java.awt.Frame parent, boolean modal, 
                                      data.Class currentClass, 
                                      data.Answer answer,
                                      ArrayList<String> present
    ) {
        super(parent, modal);
        initComponents();
        
        this.currentClass = currentClass;
        this.answer = answer;
        this.present = present;
        
        ClipboardKeyAdapter kl = new ClipboardKeyAdapter(tableScores);
        tableScores.addKeyListener(kl);
        
        RowSorter<TableModel> sorter 
                    = new TableRowSorter<>(tableScores.getModel());
        tableScores.setRowSorter(sorter);
        
        loadScores();
    }

    void loadScores()
    {
        double presentScore = 0.1; double rightAnswerScore = 0.5;
        boolean allRight = true;
        
        String rightAnswer = answer.getQuestion().getStringAnswer();
        for(Character c : answer.getAlternatives().values())
        {
            if(!answer.getQuestion().getStringAnswer().equals(c.toString()))
            {
                allRight = false;
                break;
            }
        }
        
        DefaultTableModel dtm = (DefaultTableModel) this.tableScores.getModel();
        
        for(String s : currentClass.getStudents())
        {
            if(allRight)
            {
                if(answer.getStudents().contains(s))
                {
                    dtm.addRow(new Object[]{s, rightAnswerScore});
                }
                else if(present.contains(s))
                {
                    dtm.addRow(new Object[]{s, presentScore});
                }
                else 
                {
                    dtm.addRow(new Object[]{s, 0.0});
                }
            }
            else
            {
                if(answer.getStudents().contains(s))
                {
                    if(answer.getAlternatives().get(s).toString().equals(rightAnswer))
                        dtm.addRow(new Object[]{s, rightAnswerScore});
                    else
                        dtm.addRow(new Object[]{s, 0.0});
                }
                else
                {
                    dtm.addRow(new Object[]{s, 0.0});
                }
            }
        }
        
    }
   
    void saveAll()
    {
        for(String s: answer.getStudents())
        {
            currentClass.getLivePool().remove(s);
            currentClass.getDeadPool().add(s);
        }
        
        currentClass.getAnswers().put(answer.getQuestion().getId()
                                     ,answer);
        Database database = Database.getInstance();
        database.saveClass(currentClass);
        
        JOptionPane.showMessageDialog(this, "Dados salvos com sucesso"
                                  ,"Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        this.dispose();
        
    }
    
    void leave()
    {
            Object[] options = {"Sim", "Não"};
            int dialogResult = JOptionPane.showOptionDialog(this,
                               "Tem certeza que deseja sair e descartar "
                                +"os resultados da última pergunta?",
                               "Atenção",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[1]);
            
            if(dialogResult == JOptionPane.YES_OPTION){
                this.dispose();
            }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableScores = new javax.swing.JTable();
        btnSalvar = new showdomilhao.JGradientButton();
        btnSair = new showdomilhao.JGradientButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Resultados");

        tableScores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Alunos", "Pontos"
            }
        ));
        tableScores.setCellSelectionEnabled(true);
        jScrollPane1.setViewportView(tableScores);

        btnSalvar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSalvar.setText("Salvar");
        btnSalvar.setFocusable(false);
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        btnSair.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSair.setText("Sair");
        btnSair.setFocusable(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 439, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        saveAll();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        leave();
    }//GEN-LAST:event_btnSairActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private showdomilhao.JGradientButton btnSair;
    private showdomilhao.JGradientButton btnSalvar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tableScores;
    // End of variables declaration//GEN-END:variables
}
