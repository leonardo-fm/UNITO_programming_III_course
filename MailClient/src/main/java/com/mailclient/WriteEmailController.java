package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class WriteEmailController implements Initializable {

    @FXML
    private TextField toTextField;
    @FXML
    private TextField emailObjectTextField;
    @FXML
    private TextArea emailTextArea;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionData.getInstance().getCurrentStage().setTitle("Write");
        SessionData.getInstance().getCurrentStage().setResizable(false);

        errorLabel.setText("");
    }

    public void SetupReply(List<String> receivers) {
        toTextField.setText(String.join(", ", receivers));
    }

    public void onCancelBtnClick(ActionEvent event) throws IOException {
        Utils.loadNewScene("inbox-view.fxml");
    }

    public void onSendBtnClick(ActionEvent event) throws IOException {

        if (!IsEmailDataCorrect()) return;
        ServerResponse serverResponse = new CommunicationHelper().SendEmail(GenerateEmailFromUserData());
        if (serverResponse.getResponseType() == ResponseType.ERROR) {
            errorLabel.setText("Error while sending the emails to the server");
            return;
        }

        Utils.loadNewScene("inbox-view.fxml");
    }

    private boolean IsEmailDataCorrect() {
        String receiversText = toTextField.getText().replaceAll("\\s+", "");
        if (receiversText.isEmpty()) {
            errorLabel.setText("Must put at least one receiver");
            return false;
        }

        List<String> receivers = Arrays.stream(receiversText.split(",", -1)).toList();
        for (String email : receivers) {
           if (!Utils.isValidEmail(email)) {
               errorLabel.setText("The email " + email + " is not well formatted");
               return false;
           }
        }

        String emailText = emailTextArea.getText().replaceAll("\\s+", "");
        if (emailText.isEmpty()) {
            errorLabel.setText("Email body must contain something");
            return false;
        }

        return true;
    }

    private Email GenerateEmailFromUserData() {
        String sender = SessionData.getInstance().getUserLogged();
        List<String> receivers = Arrays.stream(toTextField.getText().replaceAll("\\s+", "").split(",", -1)).toList();
        String emailObject = emailObjectTextField.getText();
        String emailContent = emailTextArea.getText();
        return new Email(sender, receivers, emailObject, emailContent);
    }
}