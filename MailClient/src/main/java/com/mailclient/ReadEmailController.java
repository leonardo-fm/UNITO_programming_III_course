package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReadEmailController implements Initializable {

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Scene scene;
    private Parent root;
    private Email currentOpenedEmail;

    @FXML
    private Label errorLabel;
    @FXML
    private TextArea emailTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SessionData.getInstance().getCurrentStage().setTitle("Read - " + SessionData.getInstance().getUserLogged());
        SessionData.getInstance().getCurrentStage().setResizable(false);

        errorLabel.setText("");
    }

    public void setup(Email email) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(formatter.format(email.getMailDate()) + "\n");
        stringBuilder.append("From: " + email.getSender() + "\n");
        stringBuilder.append("To: " + String.join(", ", email.getReceivers()) + "\n");
        stringBuilder.append("Object: " + email.getMailObject() + "\n");
        stringBuilder.append("\n" + email.getMainContent());

        emailTextArea.setText(stringBuilder.toString());

        currentOpenedEmail = email;
    }

    @FXML
    protected void onCancelBtnClick() throws IOException {
        // TODO switch window to reload
        Utils.loadNewScene("inbox-view.fxml");
    }

    @FXML
    protected void onDeleteBtnClick() throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you shore to delete this email?", ButtonType.OK, ButtonType.NO);
        alert.initOwner(SessionData.getInstance().getCurrentStage());
        alert.showAndWait();
        if (alert.getResult() == ButtonType.NO) return;

        ServerResponse serverResponse = new CommunicationHelper().DeleteEmail(currentOpenedEmail.getId());
        if (serverResponse.getResponseType() != ResponseType.OK) {
            errorLabel.setText(serverResponse.getResponseDescription());
            return;
        }

        SessionData.getInstance().getInboxEmails().remove(currentOpenedEmail);
        Utils.Log("successfully deleted and removed from session email " + currentOpenedEmail.getId().toString());

        Utils.loadNewScene("inbox-view.fxml");
    }

    @FXML
    protected void onForwardBtnClick() {
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("");
        textInputDialog.setHeaderText("Forward");
        textInputDialog.setContentText("To: ");
        textInputDialog.initOwner(SessionData.getInstance().getCurrentStage());
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(forwardTo -> {

            if (!isEmailDataCorrect(forwardTo)) return;
            errorLabel.setText("");

            ServerResponse serverResponse = new CommunicationHelper().SendEmail(generateForwardEmail(forwardTo));
            if (serverResponse.getResponseType() != ResponseType.OK) {
                errorLabel.setText("Error while forwarding the email request to the server");
                return;
            }

            Utils.Log("successfully forwarded email");

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

    private Email generateForwardEmail(String forwardTo) {
        String sender = SessionData.getInstance().getUserLogged();
        List<String> receivers = Arrays.stream(forwardTo.replaceAll("\\s+", "").split(",", -1)).toList();
        String emailObject = currentOpenedEmail.getMailObject();
        String emailContent = "[Forwarded from " + currentOpenedEmail.getSender() + " to " + String.join(", ", currentOpenedEmail.getReceivers()) + "]\n"
                + currentOpenedEmail.getMainContent();
        return new Email(sender, receivers, emailObject, emailContent);
    }

    @FXML
    protected void onReplyBtnClick() throws IOException {
        String writeEmailView = "writeEmail-view.fxml";
        URL loadedView = getClass().getResource(writeEmailView);
        if (loadedView == null)
            throw new FileNotFoundException("Write page not found!");

        FXMLLoader fxmlLoader = new FXMLLoader(loadedView);
        root = fxmlLoader.load();

        WriteEmailController writeEmailController = fxmlLoader.getController();
        List<String> replyTo = new ArrayList<>();
        replyTo.add(currentOpenedEmail.getSender());
        writeEmailController.setupReply(replyTo, currentOpenedEmail.getMailObject());

        scene = new Scene(root);
        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        SessionData.getInstance().setCurrentView(writeEmailView);
        currentStage.show();
    }

    @FXML
    protected void onReplyAllBtnClick() throws IOException {
        String writeEmailView = "writeEmail-view.fxml";
        URL loadedView = getClass().getResource(writeEmailView);
        if (loadedView == null)
            throw new FileNotFoundException("Write page not found!");

        FXMLLoader fxmlLoader = new FXMLLoader(loadedView);
        root = fxmlLoader.load();

        WriteEmailController writeEmailController = fxmlLoader.getController();
        List<String> replyTo = new ArrayList<>();
        replyTo.add(currentOpenedEmail.getSender());
        replyTo.addAll(currentOpenedEmail.getReceivers());
        replyTo.remove(SessionData.getInstance().getUserLogged());
        writeEmailController.setupReply(replyTo, currentOpenedEmail.getMailObject());

        scene = new Scene(root);
        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        SessionData.getInstance().setCurrentView(writeEmailView);
        currentStage.show();
    }
}