package com.mailclient;

import com.sharedmodels.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.UUID;

import static com.sharedmodels.MethodType.*;

public class CommunicationHelper {

    private Socket socket;

    public CommunicationHelper() { }

    public ServerResponse SendEmail(Email email) {
        try {
            openCommunication();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(SEND_EMAIL, email);
            outputStream.writeObject(req);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();
            closeCommunication();
            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            closeCommunication();
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse checkSupportedEmailAddress(String emailAddress) {
        try {
            openCommunication();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(CHECK_SUPPORTED_EMAIL_ADDRESS, emailAddress);
            outputStream.writeObject(req);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();
            closeCommunication();
            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            closeCommunication();
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse GetInboxEmails() {
        try {
            openCommunication();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(GET_ALL_EMAILS, SessionData.getInstance().getUserLogged());
            outputStream.writeObject(req);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();
            closeCommunication();
            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            closeCommunication();
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse GetNewEmails() {
        try {
            openCommunication();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ServerRequest req = new ServerRequest(GET_NEW_EMAILS, SessionData.getInstance().getUserLogged());
            outputStream.writeObject(req);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();
            closeCommunication();
            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            closeCommunication();
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    public ServerResponse DeleteEmail(UUID emailUUID) {
        try {
            openCommunication();
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            DeleteData deleteData = new DeleteData(emailUUID, SessionData.getInstance().getUserLogged());
            ServerRequest req = new ServerRequest(DELETE_EMAIL, deleteData);
            outputStream.writeObject(req);

            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ServerResponse serverResponse = (ServerResponse) inputStream.readObject();
            closeCommunication();
            return serverResponse;
        } catch (IOException | ClassNotFoundException e) {
            closeCommunication();
            return new ServerResponse(ResponseType.ERROR, "Error while communicating with the server", null);
        }
    }

    private void openCommunication() throws IOException {
        socket = new Socket(SessionData.getInstance().getHost(), SessionData.getInstance().getPort());
    }

    private void closeCommunication() {
        try {
            if (socket != null)
                socket.close();
        } catch (IOException e) {
            System.out.println("Error while closing socket: " + e);
        }
    }
}
