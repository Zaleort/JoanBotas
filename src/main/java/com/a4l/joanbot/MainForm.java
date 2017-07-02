package com.a4l.joanbot;

import com.a4l.joanbot.util.DriverHandler;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.openqa.selenium.WebDriver;

public class MainForm extends javax.swing.JFrame {
    private final WebDriver driver;
    
    private Document docTitle, docSubtitle, docNews;
    protected UndoHandler undoHandler = new UndoHandler();
    protected UndoManager undoManager = new UndoManager();
    private UndoAction undoAction = new UndoAction();
    private RedoAction redoAction = new RedoAction();

    public MainForm(WebDriver driver) {
        initComponents();
        this.driver = driver;
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                driver.quit();
                System.exit(0);
            }
        });
        
        docTitle = tTitulo.getDocument();
        docTitle.addUndoableEditListener(undoHandler);
        
        docSubtitle = tSubtitulo.getDocument();
        docSubtitle.addUndoableEditListener(undoHandler);
        
        docNews = tNoticia.getDocument();
        docNews.addUndoableEditListener(undoHandler);
    }
    
    // Calcula la longitud del string sin espacios
    private boolean checkLength(String str, int min){
        String newStr = str.replaceAll(" ", "");
        if (newStr.length() < min)
            return false;
        
        else
            return true;
    }
    
    private boolean checkLength(String str, int min, int max){
        if (str.length() < min || str.length() > max)
            return false;
        
        else
            return true;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bCategorias = new javax.swing.JComboBox<>();
        tTitulo = new javax.swing.JTextField();
        tSubtitulo = new javax.swing.JTextField();
        lCategorias = new javax.swing.JLabel();
        lTitulo = new javax.swing.JLabel();
        lSubtitulo = new javax.swing.JLabel();
        lNoticia = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tNoticia = new javax.swing.JTextArea();
        bEnviar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        tFuentes = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        menuBar = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        mAbrir = new javax.swing.JMenuItem();
        mGuardar = new javax.swing.JMenuItem();
        mSalir = new javax.swing.JMenuItem();
        menuEditar = new javax.swing.JMenu();
        mDeshacer = new javax.swing.JMenuItem();
        mRehacer = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Joan Bot");
        setBackground(new java.awt.Color(51, 51, 51));

        bCategorias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Ciencia", "Deportes", "Economía", "Estilo", "Europa", "Gastronomía", "Internacionales", "Motor", "Ocio y Cultura", "Opinión", "Política", "Salud y Belleza", "Showbiz y TV", "Sociedad", "Sucesos", "Tecnología", "Tendencias", "Viajes" }));

        tTitulo.setToolTipText("Escribe aquí el título de la noticia");

        tSubtitulo.setToolTipText("Escribe aquí el subtítulo de la noticia");
        tSubtitulo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tSubtituloActionPerformed(evt);
            }
        });

        lCategorias.setText("Categoría");

        lTitulo.setText("Títular");

        lSubtitulo.setText("Subtítulo");

        lNoticia.setText("Texto de la noticia");

        tNoticia.setColumns(20);
        tNoticia.setLineWrap(true);
        tNoticia.setRows(5);
        tNoticia.setToolTipText("Escribe tu noticia aquí");
        jScrollPane1.setViewportView(tNoticia);

        bEnviar.setText("Enviar noticia");
        bEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bEnviarActionPerformed(evt);
            }
        });

        jLabel1.setText("Fuentes: ");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        menuArchivo.setMnemonic('A');
        menuArchivo.setText("Archivo");

        mAbrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        mAbrir.setMnemonic('b');
        mAbrir.setText("Abrir archivo");
        mAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mAbrirActionPerformed(evt);
            }
        });
        menuArchivo.add(mAbrir);

        mGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        mGuardar.setMnemonic('G');
        mGuardar.setText("Guardar");
        menuArchivo.add(mGuardar);

        mSalir.setMnemonic('S');
        mSalir.setText("Salir");
        mSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(mSalir);

        menuBar.add(menuArchivo);

        menuEditar.setMnemonic('E');
        menuEditar.setText("Editar");

        mDeshacer.setAction(undoAction);
        mDeshacer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        mDeshacer.setMnemonic('D');
        mDeshacer.setText("Deshacer");
        mDeshacer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mDeshacerActionPerformed(evt);
            }
        });
        menuEditar.add(mDeshacer);

        mRehacer.setAction(redoAction);
        mRehacer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        mRehacer.setMnemonic('R');
        mRehacer.setText("Rehacer");
        menuEditar.add(mRehacer);

        menuBar.add(menuEditar);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tTitulo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tFuentes)
                                .addGap(18, 18, 18)
                                .addComponent(bEnviar))
                            .addComponent(tSubtitulo, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 841, Short.MAX_VALUE))
                        .addGap(7, 7, 7))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lTitulo)
                            .addComponent(lNoticia)
                            .addComponent(lSubtitulo)
                            .addComponent(lCategorias))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(244, 244, 244))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lCategorias)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lSubtitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tSubtitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lNoticia)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bEnviar)
                            .addComponent(tFuentes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addComponent(jSeparator1))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bEnviarActionPerformed
        String categoria, titulo, subtitulo, noticia;
        categoria = (String)bCategorias.getSelectedItem();
        titulo = tTitulo.getText();
        subtitulo = tSubtitulo.getText();
        noticia = tNoticia.getText();
        
        try {
            DriverHandler.login("59234aa881f93@mailbox92.biz", "Vamohacalmarno123", driver);
            Thread.sleep(500);
            driver.get("http://blast.blastingnews.com/news/edit/");
            Thread.sleep(500);
            
            DriverHandler.setCategory(driver, categoria);
            DriverHandler.writeTitle(driver, titulo);
            DriverHandler.writeSubtitle(driver, subtitulo);
            Thread.sleep(250);
            DriverHandler.writeNews(driver, noticia);
            
            DriverHandler.takeScreenshot(driver);
            
        } catch (InterruptedException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bEnviarActionPerformed

    private void mAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mAbrirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mAbrirActionPerformed

    private void mSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mSalirActionPerformed
        driver.quit();
        System.exit(0);
    }//GEN-LAST:event_mSalirActionPerformed

    private void mDeshacerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mDeshacerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mDeshacerActionPerformed

    private void tSubtituloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tSubtituloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tSubtituloActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> bCategorias;
    private javax.swing.JButton bEnviar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lCategorias;
    private javax.swing.JLabel lNoticia;
    private javax.swing.JLabel lSubtitulo;
    private javax.swing.JLabel lTitulo;
    private javax.swing.JMenuItem mAbrir;
    private javax.swing.JMenuItem mDeshacer;
    private javax.swing.JMenuItem mGuardar;
    private javax.swing.JMenuItem mRehacer;
    private javax.swing.JMenuItem mSalir;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu menuEditar;
    private javax.swing.JTextField tFuentes;
    private javax.swing.JTextArea tNoticia;
    private javax.swing.JTextField tSubtitulo;
    private javax.swing.JTextField tTitulo;
    // End of variables declaration//GEN-END:variables

    class UndoHandler implements UndoableEditListener {
      /**
       * Messaged when the Document has created an edit, the edit is added to
       * <code>undoManager</code>, an instance of UndoManager.
       */
      @Override
      public void undoableEditHappened(UndoableEditEvent e){
        undoManager.addEdit(e.getEdit());
        undoAction.update();
        redoAction.update();
      }
    }

    class UndoAction extends AbstractAction{
      public UndoAction(){
        super("Undo");
        setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent e){
        try{
          undoManager.undo();
        }
        catch (CannotUndoException ex){
          // TODO deal with this
          //ex.printStackTrace();
        }

        update();
        redoAction.update();
      }

      protected void update(){
        if (undoManager.canUndo()){
          setEnabled(true);
          putValue(Action.NAME, undoManager.getUndoPresentationName());
        }
        else{
          setEnabled(false);
          putValue(Action.NAME, "Undo");
        }
      }
    }

    class RedoAction extends AbstractAction{
      public RedoAction(){
        super("Redo");
        setEnabled(false);
      }

      @Override
      public void actionPerformed(ActionEvent e){
        try{
          undoManager.redo();
        }
        catch (CannotRedoException ex){
          // TODO deal with this
          ex.printStackTrace();
        }

        update();
        undoAction.update();
      }

      protected void update(){
        if (undoManager.canRedo()){
          setEnabled(true);
          putValue(Action.NAME, undoManager.getRedoPresentationName());
        }

        else{
          setEnabled(false);
          putValue(Action.NAME, "Redo");
        }
      }
    }
}