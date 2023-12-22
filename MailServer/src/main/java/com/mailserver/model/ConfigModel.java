package com.mailserver.model;

import java.util.HashMap;
import java.util.Map;

public class ConfigModel {
    private final int hostPort;
    private final Map<String, String> mailAddresses;

    public ConfigModel(int hostPort) {
        this.hostPort = hostPort;
        this.mailAddresses = new HashMap<>();
    }

    public int getHostPort() {
        return hostPort;
    }

    public Map<String, String> getMailAddresses() {
        return mailAddresses;
    }

    public void addMailAddress(String id, String mailAddress) {
        mailAddresses.put(id, mailAddress);
    }

    @Override
    public String toString() {
        return "Server host port: " + this.getHostPort() + " " +
                "Emails supported: " + this.getMailAddresses();
    }
}
