package com.mailserver;

import com.mailserver.controller.ServerController;
import com.mailserver.model.ConfigModel;
import com.mailserver.model.ServerModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

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

        ConfigModel config = new ConfigModel(8189);
        List<String> mailConfigs = FileUtility.readFileLines("../Data/emails.txt");
        for (String mailConfig : mailConfigs){
            String[] mail = mailConfig.split(";");
            config.addMailAddress(mail[0], mail[1]);
        }
        serverModel.addLog(config.toString());

        serverThread = new Thread(new ServerActivity(serverModel, config));
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