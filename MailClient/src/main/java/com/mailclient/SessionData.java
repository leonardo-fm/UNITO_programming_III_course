package com.mailclient;

import javafx.stage.Stage;

public class SessionData {

    private String userLogged = "";
    private Stage currentStage = null;

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

    public Stage getCurrentStage() {
        return currentStage;
    }
    public void setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
    }

}
