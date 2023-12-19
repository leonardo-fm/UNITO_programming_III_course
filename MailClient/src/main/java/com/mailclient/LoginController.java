package com.mailclient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private TextField usernameTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Pane loginPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionData.getInstance().getCurrentStage().setTitle("User selection");
        SessionData.getInstance().getCurrentStage().setResizable(false);

        errorLabel.setText("");
        loginPane.requestFocus();
    }

    @FXML
    protected void onLoginBtnClick() throws IOException {
        String selectedUsername = usernameTextField.getText();
        if (!Utils.isValidEmail(selectedUsername)) {
            errorLabel.setText("The username is not an email!");
            return;
        }

        SessionData.getInstance().setUserLogged(selectedUsername);

        Utils.loadNewScene("inbox-view.fxml");
    }
}