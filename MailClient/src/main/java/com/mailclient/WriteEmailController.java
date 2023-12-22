package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

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
        SessionData.getInstance().getCurrentStage().setTitle("Write - " + SessionData.getInstance().getUserLogged());
        SessionData.getInstance().getCurrentStage().setResizable(false);

        errorLabel.setText("");
    }

    public void setupReply(List<String> receivers, String emailObject) {
        toTextField.setText(String.join(", ", receivers));
        emailObjectTextField.setText(emailObject);
    }

    @FXML
    protected void onCancelBtnClick() throws IOException {
        Utils.loadNewScene("inbox-view.fxml");
    }

    @FXML
    protected void onSendBtnClick() throws IOException {

        if (!isEmailDataCorrect()) return;
        errorLabel.setText("");

        Email emailToSend = generateEmailFromUserData();
        ServerResponse serverResponse = new CommunicationHelper().SendEmail(generateEmailFromUserData());
        if (serverResponse.getResponseType() != ResponseType.OK) {
            errorLabel.setText(serverResponse.getResponseDescription());
            return;
        }

        emailToSend.setId((UUID) serverResponse.getPayload());
        SessionData.getInstance().addNewEmailOnTop(emailToSend);
        Utils.Log("email sent to the server and added to the session inbox");

        Utils.loadNewScene("inbox-view.fxml");
    }

    private boolean isEmailDataCorrect() {
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

    private Email generateEmailFromUserData() {
        String sender = SessionData.getInstance().getUserLogged();
        List<String> receivers = Arrays.stream(toTextField.getText().replaceAll("\\s+", "").split(",", -1)).toList();
        String emailObject = emailObjectTextField.getText();
        String emailContent = emailTextArea.getText();
        return new Email(sender, receivers, emailObject, emailContent);
    }
}