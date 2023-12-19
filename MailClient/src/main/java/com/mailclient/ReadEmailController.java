package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ReadEmailController implements Initializable {

    private Scene scene;
    private Parent root;
    private Email currentOpenedEmail;

    @FXML
    private Label errorLabel;
    @FXML
    private Text dateText;
    @FXML
    private Text fromText;
    @FXML
    private Text toText;
    @FXML
    private Text emailObjectText;
    @FXML
    private TextArea emailTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionData.getInstance().getCurrentStage().setTitle("Read");
        SessionData.getInstance().getCurrentStage().setResizable(false);

        errorLabel.setText("");
    }

    public void Setup(Email email) {
        dateText.setText("Sent date: " + email.getMailDate().toString());
        fromText.setText("Sender: " + email.getSender());
        toText.setText("Receivers: " + String.join(", ", email.getReceivers()));
        emailObjectText.setText("Object: " + email.getMailObject());
        emailTextArea.setText(email.getMainContent());

        currentOpenedEmail = email;
    }

    public void onCancelBtnClick(ActionEvent event) throws IOException {
        Utils.loadNewScene("inbox-view.fxml");
    }

    public void onDeleteBtnClick(ActionEvent event) throws IOException {
        ServerResponse serverResponse = new CommunicationHelper().DeleteEmail(currentOpenedEmail.getId());
        if (serverResponse.getResponseType() == ResponseType.ERROR) {
            errorLabel.setText("Error while sending the deletion request to the server");
            return;
        }

        Utils.loadNewScene("inbox-view.fxml");
    }


    public void onForwardBtnClick(ActionEvent event) {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("");
        textInputDialog.setHeaderText("Forward");
        textInputDialog.setContentText("To: ");
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(forwardTo -> {

            List<String> receivers = Arrays.stream(forwardTo.replaceAll("\\s+", "").split(",", -1)).toList();
            for (String email : receivers) {
                if (!Utils.isValidEmail(email)) {
                    errorLabel.setText("The email " + email + " is not well formatted");
                    return;
                }
            }

            ServerResponse serverResponse = new CommunicationHelper().SendEmail(GenerateForwardEmail(forwardTo));
            if (serverResponse.getResponseType() == ResponseType.ERROR) {
                errorLabel.setText("Error while forwarding the email request to the server");
                return;
            }

            try {
                Utils.loadNewScene("inbox-view.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Email GenerateForwardEmail(String forwardTo) {
        String sender = SessionData.getInstance().getUserLogged();
        List<String> receivers = Arrays.stream(forwardTo.replaceAll("\\s+", "").split(",", -1)).toList();
        String emailObject = currentOpenedEmail.getMailObject();
        String emailContent = currentOpenedEmail.getMainContent();
        return new Email(sender, receivers, emailObject, emailContent);
    }

    public void onReplyBtnClick(ActionEvent event) throws IOException {
        URL loadedView = getClass().getResource("writeEmail-view.fxml");
        if (loadedView == null)
            throw new FileNotFoundException("Write page not found!");

        FXMLLoader fxmlLoader = new FXMLLoader(loadedView);
        root = fxmlLoader.load();

        WriteEmailController writeEmailController = fxmlLoader.getController();
        List<String> replyTo = new ArrayList<>();
        replyTo.add(currentOpenedEmail.getSender());
        writeEmailController.SetupReply(replyTo);

        scene = new Scene(root);
        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        currentStage.show();
    }

    public void onReplyAllBtnClick(ActionEvent event) throws IOException {
        URL loadedView = getClass().getResource("writeEmail-view.fxml");
        if (loadedView == null)
            throw new FileNotFoundException("Write page not found!");

        FXMLLoader fxmlLoader = new FXMLLoader(loadedView);
        root = fxmlLoader.load();

        WriteEmailController writeEmailController = fxmlLoader.getController();
        List<String> replyTo = new ArrayList<>();
        replyTo.add(currentOpenedEmail.getSender());
        replyTo.addAll(currentOpenedEmail.getReceivers());
        replyTo.remove(SessionData.getInstance().getUserLogged());
        writeEmailController.SetupReply(replyTo);

        scene = new Scene(root);
        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        currentStage.show();
    }
}