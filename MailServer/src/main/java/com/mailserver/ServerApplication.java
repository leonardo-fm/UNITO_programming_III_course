package com.mailserver;

import com.mailserver.controller.ServerController;
import com.mailserver.model.ConfigModel;
import com.mailserver.model.ServerModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ServerApplication extends Application {
    private Thread serverThread;
    private static int hostPort = 8189;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader serverLoader = new FXMLLoader(ServerApplication.class.getResource("server-view.fxml"));
        Scene scene = new Scene(serverLoader.load(), 570, 400);

        ServerModel serverModel = new ServerModel();
        ServerController serverController = serverLoader.getController();
        serverController.initModel(serverModel);

        stage.setTitle("Mail Server");
        stage.setScene(scene);
        stage.show();

        ConfigModel config = new ConfigModel(hostPort);
        File f = new File("data/emails.txt");
        if (!f.exists() || f.isDirectory()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, f.getAbsolutePath() + " not found. Reopen the server with that.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMaxHeight(Double.MAX_VALUE);
            alert.getDialogPane().setMaxWidth(Double.MAX_VALUE);
            alert.showAndWait();
            Platform.exit();
            return;
        }

        List<String> mailConfigs = FileUtility.readFileLines("data/emails.txt");
        for (String mailConfig : mailConfigs) {
            String[] mail = mailConfig.split(";");
            config.addMailAddress(mail[0], mail[1]);
        }

        serverModel.addLog("Server host port: " + config.getHostPort());
        serverModel.addLog("Emails supported: " + config.getMailAddresses());

        serverThread = new Thread(new ServerActivity(serverModel, config));
        serverThread.setDaemon(true);
        serverThread.start();
    }

    @Override
    public void stop() {
        if (serverThread != null)
            serverThread.interrupt();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                hostPort = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                System.out.println("Host port number not valid. Using default 8189");
            }
        }
        launch();
    }
}