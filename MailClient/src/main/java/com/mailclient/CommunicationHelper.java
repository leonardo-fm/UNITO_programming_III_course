package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class CommunicationHelper {

    private Socket socket;

    public CommunicationHelper() {
        try {
            socket = new Socket("127.0.0.1", 8189);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ServerResponse SendEmail(Email email) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(SEND_EMAIL, email, Email.class);
            outputStream.writeObject(req);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return (ServerResponse) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
