package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class InboxController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label username;

    @FXML
    private VBox inboxHolder;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        username.setText(SessionData.getInstance().getUserLogged());

        List<Email> inboxEmails = new CommunicationHelperMock().GetInboxEmailsMock();
        for (int i = 0; i < inboxEmails.size(); i++) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 5, 5, 5));

            Text from = new Text(inboxEmails.get(i).getSender() + "\t");
            Text emailText = new Text(inboxEmails.get(i).getMainContent());

            hBox.getChildren().add(from);
            hBox.getChildren().add(emailText);
            inboxHolder.getChildren().add(hBox);
        }
    }

    public void onWriteBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("writeEmail-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onLogoutBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onReadBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("readEmail-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}