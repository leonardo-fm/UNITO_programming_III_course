package com.mailserver;

import com.model.Email;
import com.model.ServerRequest;
import com.model.ServerResponse;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ActionActivity implements Runnable {

    private final Socket socket;
    public ActionActivity(Socket socket) {
        this.socket = socket;
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
        Email e = (Email) req.getPayload();
        System.out.println("SEND EMAIL");
        System.out.println("Email id: " + e.getId());
        System.out.println("Date: " + e.getMailDate());
    }

    private void deleteEmail(ServerResponse res){
        System.out.println("DELETE EMAIL");
    }

    private void getAllEmails(ServerResponse res){
        System.out.println("GET ALL EMAILS");
    }
}
