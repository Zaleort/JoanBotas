package com.a4l.joanbot;

import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.openqa.selenium.WebDriver;

public class MainFX extends Application {
    public static WebDriver driver;
    public static Stage stage;
    public static String[] args;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        primaryStage.setOnCloseRequest(confirmCloseEventHandler);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainFXML.fxml"));
        Parent root = loader.load();
        ControllerFX controller = loader.getController();
        controller.setDriver(driver, args);
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("News Sender - 0.10");
        primaryStage.getIcons().add(new Image("/JoanBotIcon.png"));
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private final EventHandler<WindowEvent> confirmCloseEventHandler = event -> {
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
        closeConfirmation.initOwner(stage);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
            event.consume();
        }
    };
    
    @Override
    public void stop(){
        driver.quit();
    }
}