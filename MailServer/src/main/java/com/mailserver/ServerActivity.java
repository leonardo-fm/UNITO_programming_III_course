package com.mailserver;

import com.mailserver.model.ConfigModel;
import com.mailserver.model.ServerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActivity implements Runnable {
    private Socket incoming;
    private final ServerModel serverModel;
    private final ConfigModel config;

    public ServerActivity(ServerModel serverModel, ConfigModel config) {
        this.serverModel = serverModel;
        this.config = config;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(config.getHostPort());
            while (true) {
                incoming = serverSocket.accept();
                try {
                    Thread t = new Thread(new EmailActivity(incoming, serverModel, config));
                    t.setDaemon(true);
                    t.start();
                    t.setUncaughtExceptionHandler((t1, e) -> {
                        try{
                            incoming.close();
                        }
                        catch (IOException ex){
                            serverModel.addLog("Error on closing connection: " + ex);
                        }
                    });
                } catch (Exception ex) {
                    serverModel.addLog("Error on starting thread: " + ex);
                    incoming.close();
                }
            }
        }
        catch (IOException ex) {
            serverModel.addLog(ex.toString());
        }
    }
}
