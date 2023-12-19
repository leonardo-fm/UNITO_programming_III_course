package com.mailclient;

import com.sharedmodels.Email;
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

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ReadEmailController implements Initializable {

    private Stage stage;
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
        currentOpenedEmail = email;

        dateText.setText("Sent date: " + email.getMailDate().toString());
        fromText.setText("Sender: " + email.getSender());
        toText.setText("Receivers: " + String.join(", ", email.getReceivers()));
        emailObjectText.setText("Object: " + email.getMailObject());
        emailTextArea.setText(email.getMainContent());
    }

    public void onCancelBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onDeleteBtnClick(ActionEvent event) throws IOException {

        ServerResponse serverResponse = new CommunicationHelper().DeleteEmail(currentOpenedEmail.getId());

        root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void onForwardBtnClick(ActionEvent event) {

        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("");
        textInputDialog.setHeaderText("Forward");
        textInputDialog.setContentText("To: ");
        Optional<String> result = textInputDialog.showAndWait();
        result.ifPresent(forwardTo -> {

            ServerResponse serverResponse = new CommunicationHelper().SendEmail(GenerateForwardEmail(forwardTo));

            try {
                root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("writeEmail-view.fxml"));
        root = fxmlLoader.load();

        WriteEmailController writeEmailController = fxmlLoader.getController();
        List<String> replyTo = new ArrayList<>();
        replyTo.add(currentOpenedEmail.getSender());
        writeEmailController.SetupReply(replyTo);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onReplyAllBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("writeEmail-view.fxml"));
        root = fxmlLoader.load();

        WriteEmailController writeEmailController = fxmlLoader.getController();
        List<String> replyTo = new ArrayList<>();
        replyTo.add(currentOpenedEmail.getSender());
        replyTo.addAll(currentOpenedEmail.getReceivers());
        replyTo.remove(SessionData.getInstance().getUserLogged());
        writeEmailController.SetupReply(replyTo);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}