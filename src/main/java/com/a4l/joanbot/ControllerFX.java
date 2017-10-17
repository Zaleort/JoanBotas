package com.a4l.joanbot;

import com.a4l.joanbot.util.DriverHandler;
import com.a4l.joanbot.util.FileManager;
import com.a4l.joanbot.util.HTMLUtil;
import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import org.openqa.selenium.WebDriver;

public class ControllerFX implements Initializable {
    private WebDriver driver;
    private String[] args;
    private File currentFile = null;
    public static SendNoticia send = null;
    
    public void setDriver(WebDriver driver, String[] args){
        this.driver = driver;
        this.args = args;
    }
    
    @FXML private AnchorPane root;
    @FXML private AnchorPane sessionPane;
    
    @FXML private Button sendBtn;
    @FXML private Button saveBtn;
    @FXML private ComboBox categorias;
    @FXML private TextField tTitulo;
    @FXML private TextField tSubtitulo;
    @FXML private TextField tFuentes;
    @FXML private TextField tEtiquetas;
    @FXML private HTMLEditor tNoticia;
    @FXML private Label tNoticiaCount;
    @FXML private Label tTituloCount;
    @FXML private Label tSubtituloCount;
    
    @FXML private TextField tUser;
    @FXML private TextField tPasswd;
    @FXML private Label lUser;
    @FXML private Label lPasswd;
    @FXML private Button login;
    
    @FXML private MenuItem mSalir;
    
    // Logout items
    private final Button logout = new Button();
    @FXML private Label tSesionIniciada;
    private final Label tNombreSesion = new Label();
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb 
     */
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> options = FXCollections.observableArrayList();
        for (Categorias cat : Categorias.values()){
            options.add(cat.getName());
        }
        
        categorias.getItems().addAll(options);
        categorias.getSelectionModel().selectFirst();
        tTitulo.setTextFormatter(new TextFormatter(maxLength(80)));
        tSubtitulo.setTextFormatter(new TextFormatter(maxLength(150)));
        
        showUiCount(tTitulo, tTituloCount, 30);
        showUiCount(tSubtitulo, tSubtituloCount, 70);
        showUiCount(tNoticia, tNoticiaCount, 2000);
        
        // Hace que tabule el área de texto
        tNoticia.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode().equals(KeyCode.TAB)) {
                Node node = (Node) event.getSource();
                if (node instanceof HTMLEditor) {
                    HTMLEditorSkin skin = (HTMLEditorSkin) ((HTMLEditor)node).getSkin();
                    if (event.isShiftDown()) {
                        skin.getBehavior().traversePrevious();
                    }
                    else {
                        skin.getBehavior().traverseNext();
                    }
                }
                event.consume();
            }
        });
        
        cargarHTMLEditor();
        cargarLogOutItems();
    }
    
    private void cargarLogOutItems(){
        logout.setText("Cerrar sesión");
        logout.applyCss();
        logout.setStyle("-fx-font: 14 System;");
        logout.layoutXProperty().set(200);
        logout.layoutYProperty().set(210);
        logout.cursorProperty().set(Cursor.HAND);
        logout.setOnAction((ActionEvent e) -> {
            if (DriverHandler.isClosed(driver)){
            driver = DriverHandler.launchDriver(args);
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cierre inesperado de ChromeDriver");
                alert.setHeaderText(null);
                alert.setContentText("ChromeDriver se había cerrado de forma inesperada");
                alert.showAndWait();
            }
            
            driver.get("http://blaster.blastingnews.com/services/logout/");
            setSessionPane(true);
        });
        
        tNombreSesion.layoutXProperty().set(85);
        tNombreSesion.layoutYProperty().set(90);
        tNombreSesion.setStyle("-fx-font: 14 System;");
        tNombreSesion.setText("¡Bienvenido de nuevo!");
    }
    
    private void setSessionPane(boolean mode){
        if (mode){
            if (sessionPane.getChildren().contains(logout)){
                tSesionIniciada.setText("Iniciar sesión");
                sessionPane.getChildren().removeAll(logout, tNombreSesion);
                sessionPane.getChildren().addAll(login, tUser, tPasswd, lUser, lPasswd);
            }
        }
        
        else {
            if (sessionPane.getChildren().contains(login)){
                tSesionIniciada.setText("Sesión iniciada");
                // TODO: Nombre de sesión plox
                sessionPane.getChildren().addAll(logout, tNombreSesion);
                sessionPane.getChildren().removeAll(login, tUser, tPasswd, lUser, lPasswd);
            }
        }
    }
    
    private void cargarHTMLEditor(){
        tNoticia.lookup(".top-toolbar").setManaged(false);
        tNoticia.lookup(".top-toolbar").setVisible(false);
        
        tNoticia.setHtmlText(HTMLUtil.defaultHtml);
    }
    
    // Verdadero publica, Falso solo guarda
    private void sendNoticia(boolean publish){
        if (DriverHandler.isClosed(driver)){
            driver = DriverHandler.launchDriver(args);
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cierre inesperado de ChromeDriver");
                alert.setHeaderText(null);
                alert.setContentText("ChromeDriver se había cerrado, por favor vuelva a iniciar sesión");
                alert.showAndWait();
            setSessionPane(true);
            return;
        }

        if(DriverHandler.isLogged(driver)){
            final String categoria, titulo, subtitulo, noticia, fuentes;
            final String[] etiquetas;
            categoria = (String)categorias.getValue();
            titulo = tTitulo.getText();
            subtitulo = tSubtitulo.getText();
            noticia = tNoticia.getHtmlText();
            fuentes = tFuentes.getText();
            etiquetas = getEtiquetas(tEtiquetas.getText());

            if (isCorrect(titulo, subtitulo, noticia, etiquetas, fuentes, categoria)){
                SendProgressController progress = new SendProgressController(); 
                progress.setNoticiaProperties(categoria, titulo, subtitulo, noticia, etiquetas, fuentes, driver, publish);
                progress.show(MainFX.stage);
                progress.initializeTask();
                progress.setProgressHandlers();
            }

            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Rellena correctamente todos los campos");

                alert.showAndWait();
            }
        }

        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Debes iniciar sesión primero");

            alert.showAndWait();
        }
    }
    
    // Botón "Enviar Noticia"
    @FXML private void sendNoticia(ActionEvent event){
        sendNoticia(false);
    }
    
    // Botón "Enviar y publicar"
    @FXML private void publishNoticia(ActionEvent event){
        sendNoticia(true);
    }
    
    @FXML private void logIn(ActionEvent e) throws InterruptedException{
        if (DriverHandler.isClosed(driver)){
            driver = DriverHandler.launchDriver(args);
            Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cierre inesperado de ChromeDriver");
                alert.setHeaderText(null);
                alert.setContentText("ChromeDriver se había cerrado de forma inesperada");
                alert.showAndWait();
        }
        
        if (DriverHandler.isLogged(driver)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Ya has iniciado sesión");

            alert.showAndWait();
        }
        
        else {
            String user = tUser.getText();
            String passwd = tPasswd.getText();

            if (DriverHandler.login(user, passwd, driver)){
                DriverHandler.closePopUp(driver);
                setSessionPane(false);
            }
        }
    }
    
    @FXML private void newNoticia(ActionEvent e){
        if (currentFile != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Deseas guardar antes de abrir una nueva noticia?");
            Button exitButton = (Button) alert.getDialogPane().lookupButton(
                    ButtonType.OK
            );
            exitButton.setText("Guardar");
            alert.setHeaderText(null);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(root.getScene().getWindow());

            Optional<ButtonType> closeResponse = alert.showAndWait();
            if (ButtonType.OK.equals(closeResponse.get())) {
                saveBtn.fireEvent(new ActionEvent());
            }
        }
        
        categorias.getSelectionModel().selectFirst();
        tTitulo.clear();
        tSubtitulo.clear();
        
        tNoticia.setHtmlText(HTMLUtil.defaultHtml);
        tEtiquetas.clear();
        tFuentes.clear();
        
        currentFile = null;
    }
    @FXML private void saveNoticia(ActionEvent e){
        String categoria, titulo, subtitulo, noticia, fuentes, etiquetas;
        categoria = (String)categorias.getValue();
        titulo = tTitulo.getText();
        subtitulo = tSubtitulo.getText();
        noticia = tNoticia.getHtmlText();
        fuentes = tFuentes.getText();
        etiquetas = tEtiquetas.getText();
        
        Noticias nuevaNoticia = new Noticias(titulo, subtitulo, noticia, categoria, etiquetas, fuentes);
        
        if (currentFile == null){
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Selecciona el destino de la nueva noticia");
            chooser.setInitialFileName("NuevaNoticia.dat");
            chooser.setInitialDirectory(new File("./"));
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Noticias (*.dat)", "*.dat");
            chooser.getExtensionFilters().add(extFilter);
            currentFile = chooser.showSaveDialog(root.getScene().getWindow());
            
            if (currentFile != null)
                FileManager.writeFile(currentFile, nuevaNoticia);
        }
        
        else {
            FileManager.modifyFile(currentFile, nuevaNoticia);
        }
    }
    
    @FXML private void saveAsNoticia(ActionEvent e){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecciona el destino de la nueva noticia");
        chooser.setInitialFileName("NuevaNoticia.dat");
        chooser.setInitialDirectory(new File("./"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Noticias (*.dat)", "*.dat");
        chooser.getExtensionFilters().add(extFilter);
        
        currentFile = chooser.showSaveDialog(root.getScene().getWindow());
        
        if (currentFile != null){
            String categoria, titulo, subtitulo, noticia, fuentes, etiquetas;
            categoria = (String)categorias.getValue();
            titulo = tTitulo.getText();
            subtitulo = tSubtitulo.getText();
            noticia = tNoticia.getHtmlText();
            fuentes = tFuentes.getText();
            etiquetas = tEtiquetas.getText();
        
            Noticias nuevaNoticia = new Noticias(titulo, subtitulo, noticia, categoria, etiquetas, fuentes);
            
            FileManager.modifyFile(currentFile, nuevaNoticia);
        }
    }
    
    @FXML private void openNoticia(ActionEvent e){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecciona una noticia a abrir");
        chooser.setInitialDirectory(new File("./"));
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Noticias (*.dat)", "*.dat");
        chooser.getExtensionFilters().add(extFilter);
        File file = chooser.showOpenDialog(root.getScene().getWindow());
        
        if (file != null){
            Noticias noticia = FileManager.readNoticia(file);

            tTitulo.setText(noticia.getTitulo());
            tSubtitulo.setText(noticia.getSubtitulo());
            tNoticia.setHtmlText(noticia.getNoticia());
            tEtiquetas.setText(noticia.getEtiquetas());
            tFuentes.setText(noticia.getFuentes());

            if (noticia.getCategoria() != null)        
                categorias.setValue(noticia.getCategoria());
            else
                categorias.getSelectionModel().selectFirst();
            
            currentFile = file;
        }
    }

    @FXML private void mSalirAction(ActionEvent e){
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Estás seguro de que quieres salir?"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText("Salir");
        closeConfirmation.setHeaderText(null);
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.OK.equals(closeResponse.get())) {
            Platform.exit();
        }
    }
    
    private void setSendProgressHandlers(SendProgressController progress){
        send.setOnSucceeded((WorkerStateEvent t) -> {
            int exitCode = send.getValue();
            switch (exitCode) {
                //Éxito
                case 0:{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Éxito");
                    alert.setHeaderText(null);
                    alert.setContentText("Noticia enviada con éxito");
                    alert.showAndWait();
                    break;
                }
                
                // No se encuentra una imagen u otro error, pero la noticia se puede enviar
                case 1:{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ha ocurrido un error");
                    alert.setHeaderText(null);
                    alert.setContentText("Se ha enviado la noticia, pero con errores. Avisa al administrador");
                    alert.showAndWait();
                    break;
                }
                
                // Título parecido
                case 2:{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Revise el título");
                    alert.setHeaderText(null);
                    alert.setContentText("El título es demasiado parecido a uno ya existente");
                    alert.showAndWait();
                    break;
                }
                
                // Etiqueta no permitida
                case 3: {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Etiqueta no permitida");
                    alert.setHeaderText(null);
                    alert.setContentText("Una de las etiquetas elegidas no están permitidas, por favor elija otra");
                    alert.showAndWait();
                    break;
                }
                
                // Excepción WebDriver
                case -1:{
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ChromeDriver Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Ha ocurrido un error");
                    alert.showAndWait();
                    break;
                }
                default:
                    break;
            }
            
            progress.progressStage.close();
        });

        send.setOnCancelled((WorkerStateEvent t) -> {
            progress.progressStage.close();
        });

        send.setOnFailed((WorkerStateEvent t) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error desconocido");
            alert.setHeaderText(null);
            alert.setContentText("Ha ocurrido un error");
            alert.showAndWait();
            progress.progressStage.close();
        });
    }
    
    private UnaryOperator<TextFormatter.Change> maxLength(int len){
        UnaryOperator<TextFormatter.Change> rejectChange = c -> {
            // check if the change might effect the validating predicate
            if (c.isContentChange()) {
                // check if change is valid
                String res = c.getControlNewText();
                if (countStringLength(res, true) > len) {
                    // invalid change
                    // return null to reject the change
                    Toolkit.getDefaultToolkit().beep();
                    return null;
                }
            }
            // valid change: accept the change by returning it
            return c;
        };
        
        return rejectChange;
    }
    
    // Si space = true cuenta los espacios no consecutivos, a no ser que esté al final o principio 
    // de la cadena
    private int countStringLength(String str, boolean space){
        if (!space){
            String res = HTMLUtil.deleteHTML(str);
            res = res.replaceAll("\\s", "");
            return res.length();
        }
        
        else {
            String res = str.replaceAll("\\s+", " ");
            res = res.trim();
            return res.length();
        }
    }
    
    private void showUiCount (TextField text, Label count, int min){
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            int caracteres = countStringLength(newValue, true);
            if (caracteres == 0){ 
                if (count.getOpacity() != 0)
                    count.setOpacity(0);
            }
            else if (caracteres == 1){
                if (count.getOpacity() != 1)
                    count.setOpacity(1);
                if (!count.getStyle().equals("-fx-text-fill: #d32f2f;"))
                    count.setStyle("-fx-text-fill: #d32f2f;");
                
                count.setText("Has escrito " + caracteres + " carácter");
                
            }
            else if (caracteres < min){
                if (count.getOpacity() != 1)
                    count.setOpacity(1);
                if (!count.getStyle().equals("-fx-text-fill: #d32f2f;"))
                    count.setStyle("-fx-text-fill: #d32f2f;");
                
                count.setText("Has escrito " + caracteres + " caracteres");
            }
            
            else if (caracteres >= min){
                if (count.getOpacity() != 1)
                    count.setOpacity(1);
                if (!count.getStyle().equals("-fx-text-fill: #777;"))
                    count.setStyle("-fx-text-fill: #777;");
                
                count.setText("Has escrito " + caracteres + " caracteres");
            }
        });
    }
    
    private void showUiCount (HTMLEditor html, Label count, int min){
        int caracteres = countStringLength(html.getHtmlText(), false);
        if (caracteres == 0){ 
            if (count.getOpacity() != 0)
                count.setOpacity(0);
        }
        else if (caracteres == 1){
            if (count.getOpacity() != 1)
                count.setOpacity(1);
            if (!count.getStyle().equals("-fx-text-fill: #d32f2f;"))
                count.setStyle("-fx-text-fill: #d32f2f;");

            count.setText("Has escrito " + caracteres + " carácter");

        }
        else if (caracteres < min){
            if (count.getOpacity() != 1)
                count.setOpacity(1);
            if (!count.getStyle().equals("-fx-text-fill: #d32f2f;"))
                count.setStyle("-fx-text-fill: #d32f2f;");

            count.setText("Has escrito " + caracteres + " caracteres");
        }

        else if (caracteres >= min){
            if (count.getOpacity() != 1)
                count.setOpacity(1);
            if (!count.getStyle().equals("-fx-text-fill: #777;"))
                count.setStyle("-fx-text-fill: #777;");

            count.setText("Has escrito " + caracteres + " caracteres");
        }
    }
    
    @FXML private void onHtmlKey(KeyEvent e){
        showUiCount(tNoticia, tNoticiaCount, 2000);
    }
    
    @FXML private void onHtmlKeyRelease(KeyEvent e){
        showUiCount(tNoticia, tNoticiaCount, 2000);
    }
    
    private String[] getEtiquetas(String etiquetas){
      String[] res = etiquetas.split(",");
      
      for (int i = 0; i < res.length; i++){
          res[i] = res[i].trim();
      }
      
      return res;
    }
    
    private boolean isCorrect(String titulo, String subtitulo, String noticia, String[] etiquetas, String fuentes, String categoria){
        return  (countStringLength(titulo, true) >= 30 &&
                countStringLength(subtitulo, true) >= 70 &&
                countStringLength(noticia, false) >= 2000 &&
                !categoria.isEmpty() &&
                etiquetas.length >= 2 &&
                etiquetas.length <= 3 &&
                !fuentes.isEmpty()); 
    }
}