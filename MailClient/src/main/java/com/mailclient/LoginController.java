package com.mailclient;

import com.mailclient.SessionData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private Scene scene;
    private Parent root;

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
        scene = new Scene(root);

        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        currentStage.show();
    }
}