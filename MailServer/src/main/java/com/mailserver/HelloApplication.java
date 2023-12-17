package com.mailserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Thread serverThread;
    @Override
    public void start(Stage stage) throws IOException {
        serverThread = new Thread(new ServerActivity());
        serverThread.setDaemon(true);
        serverThread.start();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        serverThread.interrupt();
    }
    public static void main(String[] args) {
        launch();
    }
}