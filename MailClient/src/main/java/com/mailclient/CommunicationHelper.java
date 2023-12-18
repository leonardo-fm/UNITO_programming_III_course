package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import static com.sharedmodels.MethodType.*;

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
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Email> GetInboxEmails() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(GET_ALL_EMAILS);
            outputStream.writeObject(req);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();

            // Is not good :\
            return (List<Email>) serverResponse.getPayload();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
