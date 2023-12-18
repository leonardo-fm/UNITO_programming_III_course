package com.mailclient;

public class SessionData {

    private String userLogged = "";

    private static SessionData instance;
    private SessionData() {};
    static {
        instance = new SessionData();
    }

    public static SessionData getInstance() {
        return instance;
    }

    public void setUserLogged(String userLogged) {
        this.userLogged = userLogged;
    }
    public String getUserLogged() {
        return this.userLogged;
    }
}
