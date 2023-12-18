package com.mailserver;

import com.mailserver.controller.ServerController;
import com.mailserver.model.ServerModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    private Thread serverThread;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader serverLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(serverLoader.load(), 320, 240);

        ServerModel serverModel = new ServerModel();
        ServerController serverController = serverLoader.getController();
        serverController.initModel(serverModel);

        stage.setTitle("Mail Server");
        stage.setScene(scene);
        stage.show();

        serverThread = new Thread(new ServerActivity(serverModel));
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @Override
    public void stop(){
        serverThread.interrupt();
    }
    public static void main(String[] args) {
        launch();
    }
}