package com.mailclient;

import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

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
        String emailAddress = usernameTextField.getText();
        if (!Utils.isValidEmail(emailAddress)) {
            errorLabel.setText("The username is not an email!");
            return;
        }

        ServerResponse serverResponse = new CommunicationHelper().checkSupportedEmailAddress(emailAddress);
        if (serverResponse.getResponseType() != ResponseType.OK) {
            errorLabel.setText("The username is not in the list of valid email addresses!");
            return;
        }

        SessionData.getInstance().setUserLogged(emailAddress);

        Utils.loadNewScene("inbox-view.fxml");
    }
}