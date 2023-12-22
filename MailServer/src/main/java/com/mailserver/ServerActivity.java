package com.mailserver;

import com.mailserver.model.ConfigModel;
import com.mailserver.model.ServerModel;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActivity implements Runnable {
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
                Socket incoming = serverSocket.accept();
                try {
                    Thread t = new Thread(new EmailActivity(incoming, serverModel, config));
                    t.setDaemon(true);
                    t.start();
                    t.setUncaughtExceptionHandler((t1, e) -> {
                        try{
                            incoming.close();
                            serverModel.addLog("Thread Uncaught Exception : " + e);
                        }
                        catch (Exception ex){
                            serverModel.addLog("Error on closing connection: " + ex);
                        }
                    });
                } catch (Exception ex) {
                    serverModel.addLog("Error on starting thread: " + ex);
                    incoming.close();
                }
            }
        }
        catch (Exception ex){
            Platform.runLater(new Runnable() {
                @Override public void run() {
                    new Alert(Alert.AlertType.ERROR, ex.getMessage()).showAndWait();
                    Platform.exit();
                }
            });
        }
    }
}
