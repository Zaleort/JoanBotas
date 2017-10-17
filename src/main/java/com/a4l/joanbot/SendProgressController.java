package com.a4l.joanbot;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.openqa.selenium.WebDriver;

public class SendProgressController implements Initializable {
    @FXML private Label lActionOnProgress;
    @FXML private Button bCancelProgress;
    
    public Stage progressStage;
    private Stage parentStage;
    private boolean cancelAction;
    
    // Task variables
    private static Task<Integer> task;
    private String categoria, titulo, subtitulo, fuentes;
    private String noticia;
    private String[] etiquetas;
    private boolean publish;
    
    private WebDriver driver;
    
    public void setNoticiaProperties(String categoria, String titulo, String subtitulo, String noticia, String[] etiquetas, String fuentes, WebDriver driver, boolean publish){
        this.categoria = categoria;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.noticia = noticia;
        this.etiquetas = etiquetas;
        this.fuentes = fuentes;
        this.driver = driver;
        this.publish = publish;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
    }
    
    public void show(Stage stage){
        try {
            progressStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sendProgress.fxml"));
            Parent parent = loader.load();
            Scene scene = new Scene(parent);
            
            progressStage.setTitle("Enviando noticia...");
            progressStage.setResizable(false);
            progressStage.initModality(Modality.WINDOW_MODAL);
            progressStage.initStyle(StageStyle.DECORATED);
            progressStage.setScene(scene);
            progressStage.initOwner(stage);
            progressStage.show();
            
            progressStage.setOnCloseRequest((WindowEvent e) -> {
                if (task != null){
                    cancelAction = task.cancel();
                    System.out.println(cancelAction);
                }
            });
            
        } catch (IOException ex) {
            Logger.getLogger(SendProgressController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    @FXML private void cancelProgress(ActionEvent e){
        if (task != null){
            cancelAction = task.cancel();
            System.out.println(cancelAction);
        }
    }
    
    public void initializeTask(){
        task = new SendNoticia(categoria, titulo, subtitulo, noticia, etiquetas, fuentes, driver, publish);
        
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }
    
    public void setProgressHandlers(){
        task.setOnSucceeded((WorkerStateEvent t) -> {
            int exitCode = task.getValue();
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
            
            progressStage.close();
        });

        task.setOnCancelled((WorkerStateEvent t) -> {
            progressStage.close();
        });

        task.setOnFailed((WorkerStateEvent t) -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error desconocido");
            alert.setHeaderText(null);
            alert.setContentText("Ha ocurrido un error");
            alert.showAndWait();
            progressStage.close();
        });
    }
    
    public boolean getCancelAction(){
        return cancelAction;
    }
}