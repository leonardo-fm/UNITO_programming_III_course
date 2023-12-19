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
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class ReadEmailController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private Email currentOpenedEmail;

    @FXML
    private Text fromText;

    @FXML
    private Text toText;

    @FXML
    private TextArea emailTextArea;

    public void Setup(Email email) {
        currentOpenedEmail = email;

        fromText.setText(email.getSender());
        toText.setText(String.join(", ", email.getReceivers()));
        emailTextArea.setText(email.getMainContent());
    }

    public void onCancelBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onForwardBtnClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("forwardEmail-dialogBox.fxml"));
        DialogPane forwardEmailDialogPane = fxmlLoader.load();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(forwardEmailDialogPane);

        Optional<ButtonType> clickedBtn = dialog.showAndWait();
        if (clickedBtn.get() == ButtonType.OK) {
            root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void onReplyBtnClick(ActionEvent event) throws IOException {
        try {
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
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void onReplyAllBtnClick(ActionEvent event) throws IOException {
        try {
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
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}