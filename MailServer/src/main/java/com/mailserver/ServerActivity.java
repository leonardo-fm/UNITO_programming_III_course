package com.mailserver;

import com.mailserver.model.ConfigModel;
import com.mailserver.model.ServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActivity implements Runnable {
    private ServerSocket serverSocket;
    private Socket incoming;
    private ServerModel serverModel;
    private final ConfigModel config;

    public ServerActivity(ServerModel serverModel, ConfigModel config) {
        this.serverModel = serverModel;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(config.getHostPort());
            while (true) {
                incoming = serverSocket.accept();
                try {
                    Thread t = new Thread(new EmailActivity(incoming, serverModel, config));
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
