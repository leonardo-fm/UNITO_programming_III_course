package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import static com.sharedmodels.MethodType.*;

public class CommunicationHelper {

    private Socket socket;

    public CommunicationHelper() {
        try {
            socket = new Socket("127.0.0.1", 8189);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public ServerResponse SendEmail(Email email) {
        try {
            if (socket == null) throw new IOException();

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(SEND_EMAIL, email);
            outputStream.writeObject(req);

            System.out.println("Sent email to the server");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return (ServerResponse) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse checkSupportedEmailAddress(String emailAddress) {
        try {
            if (socket == null) throw new IOException();

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(CHECK_SUPPORTED_EMAIL_ADDRESS, emailAddress);
            outputStream.writeObject(req);

            System.out.println("Sent check for email address to the server");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return (ServerResponse) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse GetInboxEmails() {
        try {
            if (socket == null) throw new IOException();

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(GET_ALL_EMAILS, SessionData.getInstance().getUserLogged());
            outputStream.writeObject(req);

            System.out.println("Get emails from the server");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();

            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse GetNewEmails() {
        try {
            if (socket == null) throw new IOException();

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(GET_NEW_EMAILS);
            outputStream.writeObject(req);

            System.out.println("Get new emails from the server");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();

            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse DeleteEmail(UUID emailUUID) {
        try {
            if (socket == null) throw new IOException();

            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(DELETE_EMAIL, emailUUID);
            outputStream.writeObject(req);

            System.out.println("Deleted email on the server");

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            return (ServerResponse) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }
}
