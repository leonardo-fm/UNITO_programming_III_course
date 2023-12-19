package com.mailclient;

import com.sharedmodels.Email;
import javafx.stage.Stage;

import java.util.List;

public class SessionData {

    private String userLogged = "";
    private Stage currentStage = null;
    private List<Email> inboxEmails = null;
    private boolean isInboxLoaded = false;

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
        isInboxLoaded = false;
        EmailSynchronizer.getInstance().stopCheckForNewEmails();
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

    public List<Email> getInboxEmails() {
        return inboxEmails;
    }
    public void setInboxEmails(List<Email> inboxEmails) {
        this.inboxEmails = inboxEmails;
        isInboxLoaded = true;
        EmailSynchronizer.getInstance().startCheckForNewEmails();
    }

    public boolean isInboxLoaded() {
        return isInboxLoaded;
    }
}
