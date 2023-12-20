package com.mailserver;

import com.mailserver.model.ConfigModel;
import com.sharedmodels.*;

import com.mailserver.model.ServerModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmailActivity implements Runnable {

    private final Socket socket;
    private final ServerModel serverModel;
    private final ConfigModel config;
    public EmailActivity(Socket socket, ServerModel serverModel, ConfigModel config) {
        this.socket = socket;
        this.serverModel = serverModel;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ServerRequest req = (ServerRequest) in.readObject();
            ServerResponse res = new ServerResponse();
            switch (req.getMethodType()){
                case SEND_EMAIL -> sendEmail(req, res);
                case DELETE_EMAIL -> deleteEmail(req, res);
                case GET_ALL_EMAILS -> getAllEmails(req, res);
                case GET_NEW_EMAILS -> getNewEmails(req, res);
                case CHECK_SUPPORTED_EMAIL_ADDRESS -> checkSupportedEmailAddress(req, res);
            }
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(ServerRequest req, ServerResponse res){
        Email email = (Email) req.getPayload();
        serverModel.addLog("Received request: SEND EMAIL from " + socket.getInetAddress() + ':' + socket.getPort());

        // Searching for not existing email address
        String mailAddressId = config.getMailAddresses().get(email.getSender());
        if (mailAddressId == null){
            serverModel.addLog("Invalid request: INVALID SENDER MAIL ADDRESS of " + email.getSender());
            res.setResponseType(ResponseType.INVALID_SENDER_MAIL_ADDRESS);
            res.setResponseDescription("INVALID SENDER MAIL ADDRESS of " + email.getSender());
            return;
        }
        for (String emailAddress : email.getReceivers()) {
            String id = config.getMailAddresses().get(emailAddress);
            if (id == null) {
                serverModel.addLog("Invalid request: INVALID RECEIVER MAIL ADDRESS - " + emailAddress);
                res.setResponseType(ResponseType.INVALID_RECEIVER_MAIL_ADDRESS);
                res.setResponseDescription("INVALID RECEIVER MAIL ADDRESS - " + emailAddress);
                return;
            }
        }

        // Saving sender email into file
        if (!saveEmailOnFile("mail_data_" + mailAddressId, email)){
            res.setResponseType(ResponseType.SAVING_DATA_ERROR);
            res.setResponseDescription("Error on saving email");
            return;
        }

        // Saving email for receivers
        for (String emailAddress : email.getReceivers()){
            String id = config.getMailAddresses().get(emailAddress);
            if (!saveEmailOnFile("mail_new_data_" + id, email)){
                res.setResponseType(ResponseType.SAVING_DATA_ERROR);
                res.setResponseDescription("Error on saving email");
                return;
            }
        }
        res.setResponseType(ResponseType.OK);
    }

    private void deleteEmail(ServerRequest req, ServerResponse res){
        Email email = (Email) req.getPayload();
        try {
            List<Email> emails = (List<Email>) FileUtility.readFileObject("data/mail_data_" + email.getSender());
            boolean removed = emails.removeIf(searchEmail -> searchEmail.getId() == email.getId());
            if (!removed){
                res.setResponseType(ResponseType.NOT_FOUND);
                return;
            }
            FileUtility.writeFileObject("data/mail_data_" + email.getSender(), emails);
            res.setResponseType(ResponseType.OK);
        }
        catch (ClassNotFoundException ex){
            res.setResponseType(ResponseType.INVALID_PAYLOAD);
            res.setResponseDescription("Invalid payload");
            serverModel.addLog("Error on casting type from file" + ex);
            return;
        }
        catch (IOException ex){
            res.setResponseType(ResponseType.LOADING_DATA_ERROR);
            res.setResponseDescription("Error on loading email");
            serverModel.addLog("Error on reading file: " + ex);
            return;
        };
    }

    private void getAllEmails(ServerRequest req, ServerResponse res){
        String emailAddress = (String) req.getPayload();
        serverModel.addLog("Received request: GET ALL EMAILS from " + socket.getInetAddress() + ':' + socket.getPort());
        String id = config.getMailAddresses().get(emailAddress);
        if (id == null) {
            serverModel.addLog("Invalid request: INVALID MAIL ADDRESS - " + emailAddress);
            res.setResponseType(ResponseType.INVALID_MAIL_ADDRESS);
            res.setResponseDescription("Invalid mail address - " + emailAddress);
            return;
        }

        try {
            List<Email> emails = (List<Email>) FileUtility.readFileObject("data/mail_data_" + id);
            res.setPayload(emails);
            res.setResponseType(ResponseType.OK);
        }
        catch (ClassNotFoundException ex){
            res.setResponseType(ResponseType.INVALID_PAYLOAD);
            res.setResponseDescription("Invalid payload");
            serverModel.addLog("Error on casting type from file" + ex);
            return;
        }
        catch (IOException ex){
            res.setResponseType(ResponseType.LOADING_DATA_ERROR);
            res.setResponseDescription("Error on loading email");
            serverModel.addLog("Error on reading file: " + "data/mail_data_" + id + "\n" + ex);
            return;
        };
    }
    private void getNewEmails(ServerRequest req, ServerResponse res){
        String emailAddress = (String) req.getPayload();
        serverModel.addLog("Received request: GET NEW EMAILS from " + socket.getInetAddress() + ':' + socket.getPort());
        String id = config.getMailAddresses().get(emailAddress);
        if (id == null) {
            serverModel.addLog("Invalid request: INVALID MAIL ADDRESS - " + emailAddress);
            res.setResponseType(ResponseType.INVALID_MAIL_ADDRESS);
            res.setResponseDescription("Invalid mail address - " + emailAddress);
            return;
        }

        try {
            List<Email> emails = (List<Email>) FileUtility.readFileObject("data/mail_new_data_" + id);
            List<Email> oldEmails = (List<Email>) FileUtility.readFileObject("data/mail_data_" + id);
            oldEmails.addAll(emails);
            FileUtility.writeFileObject("data/mail_data_" + id, oldEmails);
            FileUtility.writeFileObject("data/mail_new_data_" + id, new ArrayList<Email>());

            res.setPayload(emails);
            res.setResponseType(ResponseType.OK);
        }
        catch (ClassNotFoundException ex){
            res.setResponseType(ResponseType.ERROR);
            serverModel.addLog("Error on casting type from file" + ex);
            return;
        }
        catch (IOException ex){
            res.setResponseType(ResponseType.LOADING_DATA_ERROR);
            res.setResponseDescription("Error on loading email");
            serverModel.addLog("Error on reading file: " + ex);
            return;
        };
    }
    private void checkSupportedEmailAddress(ServerRequest req, ServerResponse res){
        String emailAddress = (String) req.getPayload();
        serverModel.addLog("Received request: CHECK SUPPORTED EMAIL ADDRESS from " + socket.getInetAddress() + ':' + socket.getPort());
        String id = config.getMailAddresses().get(emailAddress);
        if (id == null) {
            res.setResponseType(ResponseType.INVALID_MAIL_ADDRESS);
            return;
        }
        res.setResponseType(ResponseType.OK);
    }

    private boolean saveEmailOnFile(String filename, Email email){
        List<Email> emails;
        try {
            emails = (List<Email>)FileUtility.readFileObject("data/" + filename);
        }
        catch (FileNotFoundException ex){
            emails = new ArrayList<>();
        }
        catch (IOException | ClassNotFoundException ex){
            serverModel.addLog("Error on reading file: " + filename + "\n" + ex);
            return false;
        }
        emails.add(email);
        try {
            FileUtility.writeFileObject("data/" + filename, emails);
        }
        catch (IOException ex){
            serverModel.addLog("Error on writing file: " + filename + "\n" + ex);
            return false;
        }
        return true;
    }
}
