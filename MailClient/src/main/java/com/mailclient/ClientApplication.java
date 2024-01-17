package com.mailclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

    private String landingPage = "login-view.fxml";

    @Override
    public void start(Stage stage) throws IOException {

        SessionData.getInstance().setCurrentStage(stage);

        Parent root = FXMLLoader.load(Utils.getResourceViewPath(landingPage));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        SessionData.getInstance().setCurrentView(landingPage);
        stage.show();

        Utils.Log("application started successfully");
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                Integer portNumber = Integer.parseInt(args[0]);
                SessionData.getInstance().setPort(portNumber);
                Utils.Log("server port set to " + portNumber);
            } else {
                Utils.Log("server port " + SessionData.getInstance().getPort());
            }

            if (args.length > 1) {
                SessionData.getInstance().setHost(args[1]);
                Utils.Log("server address set to " + args[1]);
            } else {
                Utils.Log("server address " + SessionData.getInstance().getHost());
            }
        } catch (NumberFormatException ex) {
            Utils.Log("something wrong with the arguments, server address is: " + SessionData.getInstance().getHost() + ":" + SessionData.getInstance().getPort());
        }

        launch();
    }
}