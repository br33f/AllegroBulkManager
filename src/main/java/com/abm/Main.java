package com.abm;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/abm/views/connectionManager.fxml"));
        primaryStage.setTitle("Allegro Bulk Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        primaryStage.show();

    }

    @Override
    public void stop(){
        Platform.exit();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
