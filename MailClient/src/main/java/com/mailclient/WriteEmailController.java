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
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.UUID;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class WriteEmailController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void onCancelBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSendBtnClick(ActionEvent event) throws IOException {

        try{
            Socket socket = new Socket("127.0.0.1", 8189);
            ObjectOutputStream out =
                    new ObjectOutputStream(socket.getOutputStream());

            Email e = new Email();
            e.setId(UUID.randomUUID());
            e.setMailDate(new Date());

            ServerRequest req = new ServerRequest();
            req.setMethodType(SEND_EMAIL);
            req.setPayloadType(Email.class);
            req.setPayload(e);

            out.writeObject(req);
            System.out.println("Send request");

            ObjectInputStream in =
                    new ObjectInputStream(socket.getInputStream());
            ServerResponse nuova = (ServerResponse) in.readObject();
            System.out.println("Received Response");
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }

        root = FXMLLoader.load(getClass().getResource("inbox-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}