package com.mailclient;

import com.mailclient.SessionData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public class LoginController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField usernameTextField;

    @FXML
    private Label errorLabel;

    public void onLoginBtnClick(ActionEvent event) throws IOException {

        URL inboxView = getClass().getResource("inbox-view.fxml");
        if (inboxView == null)
            throw new FileNotFoundException("Inbox page not found!");

        String selectedUsername = usernameTextField.getText();
        if (selectedUsername.trim().equals("")) {
            errorLabel.setText("The username can't be empty!");
            errorLabel.setDisable(false);
            return;
        }
        SessionData.getInstance().setUserLogged(selectedUsername);

        root = FXMLLoader.load(inboxView);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}