package com.mailserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerActivity implements Runnable {
    private ServerSocket serverSocket;
    private Socket incoming;
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(8189);
            while (true) {
                incoming = serverSocket.accept();
                try {
                    Thread t = new Thread(new ActionActivity(incoming));
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
