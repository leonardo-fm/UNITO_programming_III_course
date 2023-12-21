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
                case GET_NEW_EMAILS -> getNewEmails(req, res);
                case CHECK_SUPPORTED_EMAIL_ADDRESS -> checkSupportedEmailAddress(req, res);
            }
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(res);
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void sendEmail(ServerRequest req, ServerResponse res){
        serverModel.addLog("Received request: " + MethodType.SEND_EMAIL + " from " + socket.getInetAddress() + ':' + socket.getPort());
        Email email;
        try {
            email = (Email) req.getPayload();
        }
        catch (ClassCastException ex){
            res.setResponseType(ResponseType.INVALID_PAYLOAD);
            res.setResponseDescription("Payload type not valid");
            serverModel.addLog("Request error:" + ResponseType.INVALID_PAYLOAD);
            serverModel.addLog(ex.toString());
            return;
        }

        // Searching for not existing email address
        String mailAddressId = config.getMailAddresses().get(email.getSender());
        if (mailAddressId == null){
            res.setResponseType(ResponseType.INVALID_SENDER_MAIL_ADDRESS);
            res.setResponseDescription("Invalid sender mail address: " + email.getSender());
            serverModel.addLog("Request error: " + ResponseType.INVALID_SENDER_MAIL_ADDRESS + " for " + email.getSender());
            return;
        }
        for (String emailAddress : email.getReceivers()) {
            String id = config.getMailAddresses().get(emailAddress);
            if (id == null){
                res.setResponseType(ResponseType.INVALID_RECEIVER_MAIL_ADDRESS);
                res.setResponseDescription("Invalid receiver mail address: " + emailAddress);
                serverModel.addLog("Request error: " + ResponseType.INVALID_RECEIVER_MAIL_ADDRESS + " for " + emailAddress);
                return;
            }
        }

        // Saving sender email into file
        if (!saveEmailOnFile("mail_data_" + mailAddressId, email)){
            res.setResponseType(ResponseType.SAVING_DATA_ERROR);
            res.setResponseDescription("Error on saving data");
            return;
        }

        // Saving email for receivers
        for (String emailAddress : email.getReceivers()){
            String id = config.getMailAddresses().get(emailAddress);
            if (!saveEmailOnFile("mail_new_data_" + id, email)){
                res.setResponseType(ResponseType.SAVING_DATA_ERROR);
                res.setResponseDescription("Error on saving data");
                return;
            }
        }
        res.setPayload(email.getId());
        res.setResponseType(ResponseType.OK);
    }

    private void deleteEmail(ServerRequest req, ServerResponse res){
        serverModel.addLog("Received request: " + MethodType.DELETE_EMAIL + " from " + socket.getInetAddress() + ':' + socket.getPort());
        DeleteData reqData;
        try {
            reqData = (DeleteData) req.getPayload();
        }
        catch (ClassCastException ex){
            res.setResponseType(ResponseType.INVALID_PAYLOAD);
            res.setResponseDescription("Payload type not valid");
            serverModel.addLog("Request error:" + ResponseType.INVALID_PAYLOAD);
            serverModel.addLog(ex.toString());
            return;
        }

        String mailAddressId = config.getMailAddresses().get(reqData.getEmailAddress());
        if (mailAddressId == null){
            res.setResponseType(ResponseType.INVALID_MAIL_ADDRESS);
            res.setResponseDescription("Invalid mail address: " + reqData.getEmailAddress());
            serverModel.addLog("Request error: " + ResponseType.INVALID_MAIL_ADDRESS + " for " + reqData.getEmailAddress());
            return;
        }

        try {
            List<Email> emails = (List<Email>) FileUtility.readFileObject("data/mail_data_" + mailAddressId);
            System.out.println("Id to remove: " + reqData.getId());
            System.out.println("Before: " + emails);
            boolean removed = emails.removeIf(searchEmail -> searchEmail.getId().compareTo(reqData.getId()) == 0);
            System.out.println("After: " + emails);
            if (!removed){
                res.setResponseType(ResponseType.NOT_FOUND);
                res.setResponseDescription("Email to remove not found: " + reqData.getId());
                return;
            }
            FileUtility.writeFileObject("data/mail_data_" + mailAddressId, emails);
            res.setResponseType(ResponseType.OK);
        }
        catch (IOException | ClassNotFoundException ex){
            res.setResponseType(ResponseType.ERROR);
            res.setResponseDescription("Error on loading file");
            serverModel.addLog("Server error: " + ex);
        }
    }

    private void getAllEmails(ServerRequest req, ServerResponse res){
        serverModel.addLog("Received request: " + MethodType.GET_ALL_EMAILS + " from " + socket.getInetAddress() + ':' + socket.getPort());
        String emailAddress;
        try {
            emailAddress = (String) req.getPayload();
        }
        catch (ClassCastException ex){
            res.setResponseType(ResponseType.INVALID_PAYLOAD);
            res.setResponseDescription("Payload type not valid");
            serverModel.addLog("Request error:" + ResponseType.INVALID_PAYLOAD);
            serverModel.addLog(ex.toString());
            return;
        }

        String mailAddressId = config.getMailAddresses().get(emailAddress);
        if (mailAddressId == null){
            res.setResponseType(ResponseType.INVALID_MAIL_ADDRESS);
            res.setResponseDescription("Invalid mail address: " + emailAddress);
            serverModel.addLog("Request error: " + ResponseType.INVALID_MAIL_ADDRESS + " for " + emailAddress);
            return;
        }

        try {
            List<Email> emails = (List<Email>) FileUtility.readFileObject("data/mail_data_" + mailAddressId);
            res.setPayload(emails);
            res.setResponseType(ResponseType.OK);
        }
        catch (FileNotFoundException ex){
            res.setPayload(new ArrayList<Email>());
            res.setResponseType(ResponseType.OK);
        }
        catch (IOException | ClassNotFoundException ex){
            res.setResponseType(ResponseType.ERROR);
            res.setResponseDescription("Error on loading file");
            serverModel.addLog("Server error: " + ex);
        }
    }
    private void getNewEmails(ServerRequest req, ServerResponse res){
        serverModel.addLog("Received request: " + MethodType.GET_NEW_EMAILS + " from " + socket.getInetAddress() + ':' + socket.getPort());
        String emailAddress;
        try {
            emailAddress = (String) req.getPayload();
        }
        catch (ClassCastException ex){
            res.setResponseType(ResponseType.INVALID_PAYLOAD);
            res.setResponseDescription("Payload type not valid");
            serverModel.addLog("Request error:" + ResponseType.INVALID_PAYLOAD);
            serverModel.addLog(ex.toString());
            return;
        }

        String mailAddressId = config.getMailAddresses().get(emailAddress);
        if (mailAddressId == null){
            res.setResponseType(ResponseType.INVALID_MAIL_ADDRESS);
            res.setResponseDescription("Invalid mail address: " + emailAddress);
            serverModel.addLog("Request error: " + ResponseType.INVALID_MAIL_ADDRESS + " for " + emailAddress);
            return;
        }

        List<Email> oldEmails;
        try {
            oldEmails = (List<Email>) FileUtility.readFileObject("data/mail_data_" + mailAddressId);
        }
        catch (FileNotFoundException ex){
            oldEmails = new ArrayList<>();
        }
        catch (IOException | ClassNotFoundException ex){
            res.setResponseType(ResponseType.ERROR);
            res.setResponseDescription("Error on loading file");
            serverModel.addLog("Server error: " + ex);
            return;
        }

        List<Email> newEmails;
        try {
            newEmails = (List<Email>) FileUtility.readFileObject("data/mail_new_data_" + mailAddressId);
        }
        catch (FileNotFoundException ex){
            newEmails = new ArrayList<>();
        }
        catch (IOException | ClassNotFoundException ex){
            res.setResponseType(ResponseType.ERROR);
            res.setResponseDescription("Error on loading file");
            serverModel.addLog("Server error: " + ex);
            return;
        }

        oldEmails.addAll(newEmails);
        try {
            FileUtility.writeFileObject("data/mail_data_" + mailAddressId, oldEmails);
            FileUtility.writeFileObject("data/mail_new_data_" + mailAddressId, new ArrayList<Email>());

            res.setPayload(newEmails);
            res.setResponseType(ResponseType.OK);
        }
        catch (IOException ex){
            res.setResponseType(ResponseType.ERROR);
            res.setResponseDescription("Error on loading file");
            serverModel.addLog("Server error: " + ex);
        }
    }
    private void checkSupportedEmailAddress(ServerRequest req, ServerResponse res){
        serverModel.addLog("Received request: " + MethodType.CHECK_SUPPORTED_EMAIL_ADDRESS + " from " + socket.getInetAddress() + ':' + socket.getPort());
        String emailAddress;
        try {
            emailAddress = (String) req.getPayload();
        }
        catch (ClassCastException ex){
            res.setResponseType(ResponseType.INVALID_PAYLOAD);
            res.setResponseDescription("Payload type not valid");
            serverModel.addLog("Request error:" + ResponseType.INVALID_PAYLOAD);
            serverModel.addLog(ex.toString());
            return;
        }

        String id = config.getMailAddresses().get(emailAddress);
        if (id == null) {
            res.setResponseType(ResponseType.INVALID_MAIL_ADDRESS);
            res.setResponseDescription("Invalid mail address: " + emailAddress);
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
            serverModel.addLog("Error on reading file: " + filename);
            serverModel.addLog(ex.toString());
            return false;
        }
        emails.add(email);
        try {
            FileUtility.writeFileObject("data/" + filename, emails);
        }
        catch (IOException ex){
            serverModel.addLog("Error on writing file: " + filename);
            serverModel.addLog(ex.toString());
            return false;
        }
        return true;
    }
}
