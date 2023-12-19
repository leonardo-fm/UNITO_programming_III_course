package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
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
    private TextArea emailTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionData.getInstance().getCurrentStage().setTitle("Read");
        SessionData.getInstance().getCurrentStage().setResizable(false);

        errorLabel.setText("");
    }

    public void Setup(Email email) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(email.getMailDate().toString() + "\n");
        stringBuilder.append("From: " + email.getSender() + "\n");
        stringBuilder.append("To: " + String.join(", ", email.getReceivers()) + "\n");
        stringBuilder.append("Object: " + email.getMailObject() + "\n");
        stringBuilder.append("\n" + email.getMainContent());

        emailTextArea.setText(stringBuilder.toString());

        currentOpenedEmail = email;
    }

    @FXML
    protected void onCancelBtnClick() throws IOException {
        Utils.loadNewScene("inbox-view.fxml");
    }

    @FXML
    protected void onDeleteBtnClick() throws IOException {
        ServerResponse serverResponse = new CommunicationHelper().DeleteEmail(currentOpenedEmail.getId());
        if (serverResponse.getResponseType() == ResponseType.ERROR) {
            errorLabel.setText("Error while sending the deletion request to the server");
            return;
        }

        Utils.loadNewScene("inbox-view.fxml");
    }

    @FXML
    protected void onForwardBtnClick() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("");
        textInputDialog.setHeaderText("Forward");
        textInputDialog.setContentText("To: ");
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(forwardTo -> {

            if (!isEmailDataCorrect(forwardTo)) return;
            errorLabel.setText("");

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

    private boolean isEmailDataCorrect(String receiversText) {
        List<String> receivers = Arrays.stream(receiversText.split(",", -1)).toList();
        for (String email : receivers) {
            if (!Utils.isValidEmail(email)) {
                errorLabel.setText("The email " + email + " is not well formatted");
                return false;
            }
        }

        return true;
    }

    private Email GenerateForwardEmail(String forwardTo) {
        String sender = SessionData.getInstance().getUserLogged();
        List<String> receivers = Arrays.stream(forwardTo.replaceAll("\\s+", "").split(",", -1)).toList();
        String emailObject = currentOpenedEmail.getMailObject();
        String emailContent = currentOpenedEmail.getMainContent();
        return new Email(sender, receivers, emailObject, emailContent);
    }

    @FXML
    protected void onReplyBtnClick() throws IOException {
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

    @FXML
    protected void onReplyAllBtnClick() throws IOException {
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