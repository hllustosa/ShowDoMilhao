package showdomilhao;

import data.Answer;
import data.Database;
import data.Question;
import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;


public class FrmMain extends javax.swing.JFrame {

    private final int NROWS = 50;
    
    public FrmMain() {
                
        initComponents();
        ClipboardKeyAdapter kl = new ClipboardKeyAdapter(tableAlunos);
        tableAlunos.addKeyListener(kl);
        ClipboardKeyAdapter kl2 = new ClipboardKeyAdapter(tableLivepool);
        tableLivepool.addKeyListener(kl2);
        ClipboardKeyAdapter kl3 = new ClipboardKeyAdapter(tableDeadpool);
        tableDeadpool.addKeyListener(kl3);
        
        try {
            
            this.setIconImage(ImageIO.read(
                               FrmMain.class.getResource("/resources/icon.png"))
            );
             
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            for(Component component : this.getComponents())
            {
               SwingUtilities.updateComponentTreeUI(component);
            }
            
            Database database = Database.getInstance();
            database.loadClasses();
            cbClass.addItem("");
                    
            for(String c : database.getClasses().keySet())
            {
                cbClass.addItem(c);
            }
            
            cbClass.setSelectedItem("");
            tableAnswers.getColumnModel().getColumn(1).setPreferredWidth(700);
           
            
        } catch (ClassNotFoundException | IllegalAccessException | IOException
                 | InstantiationException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public <T> ArrayList<T> intersection(List<T> list1, List<T> list2) {
        ArrayList<T> list = new ArrayList<>();
        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
    
    public <T> void subtract(List<T> list1, List<T> list2) {
        for (T t : list2) {
            if(list1.contains(t)) {
                list1.remove(t);
            }
        }
    }
    
    void loadClass(String name)
    {
        Database database = Database.getInstance();
        database.loadClasses();
        data.Class currentClass = database.getClasses().get(name);
        
        if(currentClass != null)
        {
            txtDatabase.setText(currentClass.getQuestionsDatabase());
            
            DefaultTableModel dtm = (DefaultTableModel) tableAlunos.getModel();
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();
            
            if(!currentClass.getStudents().isEmpty()){
                for(String student : currentClass.getStudents())
                    if(!student.isEmpty())
                        dtm.addRow(new Object[]{student});
            }
            else{
                dtm.addRow(new Object[]{""});
            }
            
            dtm = (DefaultTableModel) tableLivepool.getModel();
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();
            
            if(!currentClass.getLivePool().isEmpty()){
                for(String student : currentClass.getLivePool())
                if(!student.isEmpty())
                    dtm.addRow(new Object[]{student});
            }
            else{
                dtm.addRow(new Object[]{""});
            }
            

            dtm = (DefaultTableModel) tableDeadpool.getModel();
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();
            
            if(!currentClass.getDeadPool().isEmpty()){
                for(String student : currentClass.getDeadPool())
                    if(!student.isEmpty())
                        dtm.addRow(new Object[]{student});
            }
            else{
                dtm.addRow(new Object[]{""});
            }
            
            dtm = (DefaultTableModel) tableAnswers.getModel();
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();
            
            for(Entry<Integer, Answer> entry:currentClass.getAnswers().entrySet())
                dtm.addRow(new Object[]{entry.getValue().getDate().toString(),
                                        entry.getValue().toString()});
        }
        
    }
    
    void setDatabase()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha o diretório com as questões:");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        int fileChoosereturnStatus = fileChooser.showSaveDialog(this);
        
        if (fileChoosereturnStatus == JFileChooser.APPROVE_OPTION) 
        {
            File fileToSave = fileChooser.getSelectedFile();
            txtDatabase.setText(fileToSave.getAbsolutePath());
        }
    }
    
    private void reloadForm() {
        if(cbClass.getSelectedItem() == null
            || cbClass.getSelectedItem().toString().isEmpty())
        {
            clearForm();
        }
        else
        {
            loadClass(cbClass.getSelectedItem().toString());
        }
    }
    
    private void clearForm(){
        DefaultTableModel dtm = (DefaultTableModel) tableAlunos.getModel();
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();

        for(int i = 0; i < NROWS; i++)
            dtm.addRow(new Object[]{""});

        dtm = (DefaultTableModel) tableLivepool.getModel();
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();

        for(int i = 0; i < NROWS; i++)
            dtm.addRow(new Object[]{""});

        dtm = (DefaultTableModel) tableDeadpool.getModel();
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();

        for(int i = 0; i < NROWS; i++)
            dtm.addRow(new Object[]{""});
        
        dtm = (DefaultTableModel) tableAnswers.getModel();
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();
        
        for(int i = 0; i < NROWS/10; i++)
            dtm.addRow(new Object[]{""});
        
        
        txtDatabase.setText("");
    }
    
    void addLines(JTable table)
    {
        int rowNo = table.getRowCount();
        if(table.getSelectedRow() == rowNo-1)
        {
           DefaultTableModel dtm = (DefaultTableModel) table.getModel();
           dtm.addRow(new Object[]{""});
        }
    }
    
    HashMap<Integer, Question>  loadQuestions()
    {
        String path = txtDatabase.getText();
        if(path.isEmpty()) return null;
        
        File dir = new File(path);
        LinkedList<File> files = new LinkedList<File>();
        files.addAll(Arrays.asList(dir.listFiles()));
        
        Integer idCounter = 0;
        HashMap<Integer, Question> questions = new HashMap<>();
        
        while(!files.isEmpty()) {
            
            File fileEntry = files.get(0);
            files.removeFirst();
            
            if (fileEntry.isDirectory()) {
                files.addAll(Arrays.asList(fileEntry.listFiles()));
            }
            else{
                Question q = new Question(idCounter, fileEntry.getAbsolutePath());
                questions.put(idCounter++, q);
            }
        }
        
        return questions;
    }
    
    void loadStudents(DefaultTableModel dtm, ArrayList<String> list)
    {
        Integer nRows = dtm.getRowCount();
        for (Integer i = 0 ; i < nRows ; i++)
        {
            Object value = dtm.getValueAt(i, 0);
            if(value != null && !value.toString().isEmpty())
            {
                if(!list.contains((String)value))
                    list.add((String)value);
            }
        }
    }
    
    void moveStudents(DefaultTableModel origin, DefaultTableModel destiny
                                               , boolean all, int index)
    {
        Integer nRows = origin.getRowCount();
        
        if(all)
        {
            for (Integer i = 0 ; i < nRows ; i++)
            {
                Object value = origin.getValueAt(i, 0);
                if(value != null && !value.toString().isEmpty())
                {
                    destiny.insertRow(0, new Object[]{value});
                }
            }
            
            if (origin.getRowCount() > 0) {
                for (int i = origin.getRowCount() - 1; i > -1; i--) {
                    origin.removeRow(i);
                }
            }
        }
        else
        {
           Object value = origin.getValueAt(index, 0);
           //destiny.addRow(new Object[]{value});
            destiny.insertRow(0, new Object[]{value});
           origin.removeRow(index);
        }
    }

    
    void save()
    {
        try
        {
            Database database = Database.getInstance();

            data.Class currentClass;
            
            if(cbClass.getSelectedItem() == null)
            {
                throw new Exception("Nome da classe não informado.");
            }
            String className = cbClass.getSelectedItem().toString();
            if(database.getClasses().containsKey(className)){
                currentClass = database.getClasses().get(className);
            }
            else{
                currentClass = new data.Class();
                currentClass.setName(className);
            }
            
            HashMap<Integer, Question> questions = loadQuestions();
            if(questions == null)
            {
                throw new RuntimeException("Erro durante leitura da base"
                                           + " de perguntas.");
            }
            currentClass.setQuestionsDatabase(txtDatabase.getText());
            currentClass.setQuestions(questions);
             
            currentClass.clearStudents();
            
            DefaultTableModel dtm = (DefaultTableModel) tableAlunos.getModel();
            loadStudents(dtm, currentClass.getStudents());

            dtm = (DefaultTableModel) tableLivepool.getModel();
            loadStudents(dtm, currentClass.getLivePool());

            currentClass.setLivePool(intersection(currentClass.getStudents(), 
                                     currentClass.getLivePool()));

            dtm = (DefaultTableModel) tableDeadpool.getModel();
            loadStudents(dtm, currentClass.getDeadPool());

            currentClass.setDeadPool(intersection(currentClass.getStudents(), 
                                     currentClass.getDeadPool()));

            subtract(currentClass.getLivePool(), currentClass.getDeadPool());

            if(!database.saveClass(currentClass))
            {
                throw new RuntimeException("Não foi possível salvar a turma "
                                           + " no arquivo XML.");
            }
            
            String n = currentClass.getName();
            if(((DefaultComboBoxModel)cbClass.getModel()).getIndexOf(n) == -1){
               cbClass.addItem(n);
            }
            
            JOptionPane.showMessageDialog(this, "Turma salva com sucesso"
                                   ,"Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
        catch(Exception ex)
        {
           JOptionPane.showMessageDialog(this, "Problema ao salvar Turma\n" + 
                            ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE); 
        }
        
    }
    
    void remove()
    {
        if(cbClass.getSelectedItem() != null
           && !cbClass.getSelectedItem().toString().isEmpty())
        {
            
            String className = cbClass.getSelectedItem().toString();
            Object[] options = {"Sim", "Não"};
            int dialogResult = JOptionPane.showOptionDialog(this,
                               "Tem certeza que deseja remover a turma "
                                + className + " e todo seu conteúdo?",
                               "Atenção",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[1]);
            
            if(dialogResult == JOptionPane.YES_OPTION){
                Database database = Database.getInstance();
                database.removeClass(className);
                cbClass.removeItem(className);
            }
        }
    }
    
    void startShow()
    {
        if(cbClass.getSelectedItem() != null
           && !cbClass.getSelectedItem().toString().isEmpty())
        {
            GraphicsEnvironment ge = GraphicsEnvironment
                                                .getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();
            
            if(gs.length > 1)
            {
                 Object[] options = {"Sim", "Não"};
                 int dialogResult = JOptionPane.showOptionDialog(this,
                               "Segundo monitor detectado. Deseja continuar mesmo assim?",
                               "Atenção",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[1]);
            
                if(dialogResult == JOptionPane.NO_OPTION)
                    return;
            }
            
            String className = cbClass.getSelectedItem().toString();
            Database database = Database.getInstance();
            if(database.getClasses().containsKey(className))
            {
                data.Class currentClass = database.getClasses().get(className);
                FrmConfigQuestion frm = new FrmConfigQuestion(currentClass);
                frm.setVisible(true);
                frm.setLocationRelativeTo(null);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tableLivepool = new javax.swing.JTable();
        cbClass = new javax.swing.JComboBox<>();
        txtDatabase = new javax.swing.JTextField();
        btnFromDead2LiveSingle = new showdomilhao.JGradientButton();
        btnFromDead2LiveAll = new showdomilhao.JGradientButton();
        btnFromLive2DeadSingle = new showdomilhao.JGradientButton();
        btnFromLive2DeadAll = new showdomilhao.JGradientButton();
        btnSair = new showdomilhao.JGradientButton();
        btnPergunta = new showdomilhao.JGradientButton();
        btnRemover = new showdomilhao.JGradientButton();
        btnSalvar = new showdomilhao.JGradientButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableAnswers = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableAlunos = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tableDeadpool = new javax.swing.JTable();
        btnSetDatabase = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Show do Milhão");
        setBackground(new java.awt.Color(242, 242, 242));
        setResizable(false);
        setSize(new java.awt.Dimension(1240, 721));

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tableLivepool.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tableLivepool.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Livepool"
            }
        ));
        tableLivepool.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableLivepool.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableLivepoolKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tableLivepool);

        cbClass.setBackground(java.awt.Color.white);
        cbClass.setEditable(true);
        cbClass.setForeground(new java.awt.Color(0, 0, 0));
        cbClass.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbClassItemStateChanged(evt);
            }
        });

        txtDatabase.setToolTipText("");

        btnFromDead2LiveSingle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnFromDead2LiveSingle.setFocusable(false);
        btnFromDead2LiveSingle.setLabel("<");
        btnFromDead2LiveSingle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromDead2LiveSingleActionPerformed(evt);
            }
        });

        btnFromDead2LiveAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnFromDead2LiveAll.setText("<<");
        btnFromDead2LiveAll.setFocusable(false);
        btnFromDead2LiveAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromDead2LiveAllActionPerformed(evt);
            }
        });

        btnFromLive2DeadSingle.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnFromLive2DeadSingle.setText(">");
        btnFromLive2DeadSingle.setFocusable(false);
        btnFromLive2DeadSingle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromLive2DeadSingleActionPerformed(evt);
            }
        });

        btnFromLive2DeadAll.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnFromLive2DeadAll.setText(">>");
        btnFromLive2DeadAll.setFocusable(false);
        btnFromLive2DeadAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFromLive2DeadAllActionPerformed(evt);
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

        btnPergunta.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnPergunta.setText("Show do Milhão");
        btnPergunta.setFocusable(false);
        btnPergunta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPerguntaActionPerformed(evt);
            }
        });

        btnRemover.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnRemover.setText("Remover");
        btnRemover.setFocusable(false);
        btnRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoverActionPerformed(evt);
            }
        });

        btnSalvar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSalvar.setText("Salvar");
        btnSalvar.setFocusable(false);
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });

        jLabel1.setText("Turma");

        jLabel2.setText("Banco de Dados");

        tableAnswers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Data", "Conteúdo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tableAnswers);

        jScrollPane3.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tableAlunos.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tableAlunos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Alunos"
            }
        ));
        tableAlunos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAlunos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableAlunosKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tableAlunos);

        jScrollPane6.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tableDeadpool.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        tableDeadpool.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Deadpool"
            }
        ));
        tableDeadpool.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableDeadpool.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tableDeadpoolKeyPressed(evt);
            }
        });
        jScrollPane6.setViewportView(tableDeadpool);

        btnSetDatabase.setText("...");
        btnSetDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSetDatabaseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(283, 283, 283))
                                    .addComponent(cbClass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(29, 29, 29)
                                        .addComponent(jLabel2)
                                        .addGap(0, 697, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSetDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(39, 482, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(btnFromDead2LiveSingle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(btnFromDead2LiveAll, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                                                    .addComponent(btnFromLive2DeadSingle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(btnFromLive2DeadAll, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(12, 12, 12)
                                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE))))))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDatabase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSetDatabase))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(btnFromDead2LiveSingle, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFromDead2LiveAll, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFromLive2DeadSingle, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnFromLive2DeadAll, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSair, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPergunta, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRemover, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalvar, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSetDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSetDatabaseActionPerformed
        setDatabase();
    }//GEN-LAST:event_btnSetDatabaseActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        save();
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void cbClassItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbClassItemStateChanged
       if (evt.getStateChange() == ItemEvent.SELECTED) {
           reloadForm();
       } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
           clearForm();
       }
    }//GEN-LAST:event_cbClassItemStateChanged

    private void btnRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoverActionPerformed
       remove();
    }//GEN-LAST:event_btnRemoverActionPerformed

    private void btnFromDead2LiveSingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromDead2LiveSingleActionPerformed
        if(tableDeadpool.getSelectedRow() != -1)
        {
            DefaultTableModel origin = (DefaultTableModel) tableDeadpool.getModel();
            DefaultTableModel destiny = (DefaultTableModel) tableLivepool.getModel();
            moveStudents(origin, destiny, false, tableDeadpool.getSelectedRow());
        }
    }//GEN-LAST:event_btnFromDead2LiveSingleActionPerformed

    private void btnFromLive2DeadSingleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromLive2DeadSingleActionPerformed
        if(tableLivepool.getSelectedRow() != -1)
        {
            DefaultTableModel destiny = (DefaultTableModel) tableDeadpool.getModel();
            DefaultTableModel origin = (DefaultTableModel) tableLivepool.getModel();
            moveStudents(origin, destiny, false, tableLivepool.getSelectedRow());
        }
    }//GEN-LAST:event_btnFromLive2DeadSingleActionPerformed

    private void btnFromDead2LiveAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromDead2LiveAllActionPerformed
        DefaultTableModel origin = (DefaultTableModel) tableDeadpool.getModel();
        DefaultTableModel destiny = (DefaultTableModel) tableLivepool.getModel();
        moveStudents(origin, destiny, true, tableDeadpool.getSelectedRow());
    }//GEN-LAST:event_btnFromDead2LiveAllActionPerformed

    private void btnFromLive2DeadAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFromLive2DeadAllActionPerformed
        DefaultTableModel destiny = (DefaultTableModel) tableDeadpool.getModel();
        DefaultTableModel origin = (DefaultTableModel) tableLivepool.getModel();
        moveStudents(origin, destiny, true, tableLivepool.getSelectedRow());
    }//GEN-LAST:event_btnFromLive2DeadAllActionPerformed

    private void btnPerguntaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPerguntaActionPerformed
        startShow();
    }//GEN-LAST:event_btnPerguntaActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSairActionPerformed

    private void tableAlunosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableAlunosKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            addLines((JTable)evt.getSource());
    }//GEN-LAST:event_tableAlunosKeyPressed

    private void tableLivepoolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableLivepoolKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            addLines((JTable)evt.getSource());
    }//GEN-LAST:event_tableLivepoolKeyPressed

    private void tableDeadpoolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableDeadpoolKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER)
            addLines((JTable)evt.getSource());
    }//GEN-LAST:event_tableDeadpoolKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private showdomilhao.JGradientButton btnFromDead2LiveAll;
    private showdomilhao.JGradientButton btnFromDead2LiveSingle;
    private showdomilhao.JGradientButton btnFromLive2DeadAll;
    private showdomilhao.JGradientButton btnFromLive2DeadSingle;
    private showdomilhao.JGradientButton btnPergunta;
    private showdomilhao.JGradientButton btnRemover;
    private showdomilhao.JGradientButton btnSair;
    private showdomilhao.JGradientButton btnSalvar;
    private javax.swing.JButton btnSetDatabase;
    private javax.swing.JComboBox<String> cbClass;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTable tableAlunos;
    private javax.swing.JTable tableAnswers;
    private javax.swing.JTable tableDeadpool;
    private javax.swing.JTable tableLivepool;
    private javax.swing.JTextField txtDatabase;
    // End of variables declaration//GEN-END:variables

}
