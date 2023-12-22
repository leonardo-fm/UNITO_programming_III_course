package com.mailclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class ClientApplication extends Application {

    private String landingPage = "login-view.fxml";

    @Override
    public void start(Stage stage) throws IOException {

        URL landingPageUrl = getClass().getResource(landingPage);
        if (landingPageUrl == null)
            throw new FileNotFoundException("Landing page not found!");

        SessionData.getInstance().setCurrentStage(stage);

        Parent root = FXMLLoader.load(landingPageUrl);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        SessionData.getInstance().setCurrentView(landingPage);
        stage.show();

        Utils.Log("application started successfully");
    }

    public static void main(String[] args) {
        try {
            if (args.length > 0)
                SessionData.getInstance().setPort(Integer.parseInt(args[0]));

            if (args.length > 1)
                SessionData.getInstance().setHost(args[1]);
        } catch (NumberFormatException ex) {
            System.out.println("Host port number not valid. Using default 8189");
        }

        launch();
    }
}