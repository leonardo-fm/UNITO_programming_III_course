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

        Parent root = FXMLLoader.load(landingPageUrl);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}