package com.mailserver;

import com.mailserver.model.ServerModel;
import javafx.beans.Observable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActivity implements Runnable {
    private ServerSocket serverSocket;
    private Socket incoming;
    private ServerModel serverModel;

    public ServerActivity(ServerModel serverModel) {
        this.serverModel = serverModel;
    }

    @Override
    public void run() {
        try {
            int port = 8189;
            serverSocket = new ServerSocket(port);
            serverModel.addLog("Server hosted in port " + port);
            while (true) {
                incoming = serverSocket.accept();
                try {
                    Thread t = new Thread(new ActionActivity(incoming, this.serverModel));
                    t.setDaemon(true);
                    t.start();
                    t.join();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                finally {
                    incoming.close();
                }
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
