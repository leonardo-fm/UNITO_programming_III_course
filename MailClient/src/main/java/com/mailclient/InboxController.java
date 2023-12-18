package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class InboxController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label username;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        username.setText(SessionData.getInstance().getUserLogged());
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