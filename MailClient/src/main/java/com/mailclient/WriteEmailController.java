package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class WriteEmailController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField toTextField;

    @FXML
    private TextField emailObjectTextField;

    @FXML
    private TextArea emailTextArea;

    @FXML
    private Label errorLabel;

    public void SetupReply(List<String> receivers) {
        toTextField.setText(String.join(", ", receivers));
    }

    public void onCancelBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSendBtnClick(ActionEvent event) throws IOException {

        if (!IsEmailDataCorrect()) return;
        ServerResponse serverResponse = new CommunicationHelper().SendEmail(GenerateEmailFromUserData());

        root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private boolean IsEmailDataCorrect() {
        String receiversText = toTextField.getText().replaceAll("\\s+", "");
        if (receiversText.isEmpty()) {
            errorLabel.setText("Must put at least one receiver");
            return false;
        }

        String emailText = emailTextArea.getText().replaceAll("\\s+", "");
        if (emailText.isEmpty()) {
            errorLabel.setText("No email body");
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