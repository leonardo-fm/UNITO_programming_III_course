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
            }
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(res);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(ServerRequest req, ServerResponse res){
        Email email = (Email) req.getPayload();
        System.out.println(email);
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
                serverModel.addLog("Invalid request: INVALID RECEIVER MAIL ADDRESS - " + email.getSender());
                res.setResponseType(ResponseType.INVALID_RECEIVER_MAIL_ADDRESS);
                res.setResponseDescription("INVALID RECEIVER MAIL ADDRESS - " + email.getSender());
                return;
            }
        }

        // Saving sender email into file
        if (!saveEmailOnFile(mailAddressId, email)){
            res.setResponseType(ResponseType.SAVING_DATA_ERROR);
            res.setResponseDescription("Error on saving email");
            return;
        }

        // Saving email for receivers
        for (String emailAddress : email.getReceivers()){
            String id = config.getMailAddresses().get(emailAddress);
            if (!saveEmailOnFile(id, email)){
                res.setResponseType(ResponseType.SAVING_DATA_ERROR);
                res.setResponseDescription("Error on saving email");
                return;
            }
        }
        res.setResponseType(ResponseType.OK);
    }

    private void deleteEmail(ServerRequest req, ServerResponse res){
        System.out.println("DELETE EMAIL");
    }

    private void getAllEmails(ServerRequest req, ServerResponse res){
        String emailAddress = (String) req.getPayload();
        serverModel.addLog("Received request: GET ALL EMAILS from " + socket.getInetAddress() + ':' + socket.getPort());

    }

    private boolean saveEmailOnFile(String id, Email email){
        List<Email> emails;
        try {
            emails = (List<Email>)FileUtility.readFileObject("Data/mail_data_" + id);
        }
        catch (FileNotFoundException ex){
            emails = new ArrayList<>();
        }
        catch (IOException | ClassNotFoundException ex){
            serverModel.addLog("Error on reading file: " + "Data/mail_data_" + id + "\n" + ex);
            return false;
        }
        emails.add(email);
        try {
            FileUtility.writeFileObject("Data/mail_data_" + id, emails);
        }
        catch (IOException ex){
            serverModel.addLog("Error on writing file: " + "Data/mail_data_" + id + "\n" + ex);
            return false;
        }
        return true;
    }
}
