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
                case DELETE_EMAIL -> deleteEmail(res);
                case GET_ALL_EMAILS -> getAllEmails(res);
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
        String mailAddressId = config.getMailAddresses().get(email.getSender());
        if (mailAddressId == null){
            res.setResponseType(ResponseType.INVALID_SENDER_MAIL_ADDRESS);
            serverModel.addLog("Invalid request: INVALID SENDER MAIL ADDRESS of " + email.getSender());
            res.setResponseDescription("INVALID SENDER MAIL ADDRESS of " + email.getSender());
            return;
        }
        // Saving sender email into file
        List<Email> emails;
        try {
            emails = (List<Email>)FileUtility.readFileObject("Data/mail_data_" + mailAddressId);
        }
        catch (FileNotFoundException ex){
            emails = new ArrayList<>();
        }
        catch (IOException | ClassNotFoundException ex){
            serverModel.addLog("Error on reading file: " + "Data/mail_data_" + mailAddressId + "\n" + ex);
            return;
        }
        emails.add(email);
        try {
            FileUtility.writeFileObject("Data/mail_data_" + mailAddressId, emails);
        }
        catch (IOException ex){
            serverModel.addLog("Error on writing file: " + "Data/mail_data_" + mailAddressId + "\n" + ex);
        }
        res.setResponseType(ResponseType.OK);
    }

    private void deleteEmail(ServerResponse res){
        System.out.println("DELETE EMAIL");
    }

    private void getAllEmails(ServerResponse res){
        System.out.println("GET ALL EMAILS");
    }
}
