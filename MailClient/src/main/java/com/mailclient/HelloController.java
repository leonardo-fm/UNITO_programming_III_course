package com.mailclient;

import com.sharedmodels.*;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.UUID;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
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
    }
}