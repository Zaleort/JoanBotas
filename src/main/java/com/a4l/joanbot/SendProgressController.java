package com.a4l.joanbot;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class SendProgressController implements Initializable {
    @FXML private Label lActionOnProgress;
    @FXML private Button bCancelProgress;
    
    public Stage progressStage;
    private Stage parentStage;
    private final SendNoticia send = ControllerFX.send;

    private boolean cancelAction;
    
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
            
            progressStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    if (send != null){
                        cancelAction = ControllerFX.send.cancel();
                        System.out.println(cancelAction);
                    }
                }
            });
            
        } catch (IOException ex) {
            Logger.getLogger(SendProgressController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    @FXML private void cancelProgress(ActionEvent e){
        if (send != null){
            cancelAction = ControllerFX.send.cancel();
            System.out.println(cancelAction);
        }
        
    }
    
    public boolean getCancelAction(){
        return cancelAction;
    }
    
    public void setActionOnProgress(String action){
        lActionOnProgress.setText(action);
    }
}
