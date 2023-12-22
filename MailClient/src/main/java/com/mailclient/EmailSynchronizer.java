package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class EmailSynchronizer {

    private boolean startSync;
    private final long defaultTimeBetweenChecks = 5000;
    private final int defaultMaxIncreaseTime = 5;
    private long timeBetweenChecks;
    private int maxIncreaseTime = 5;


    private static EmailSynchronizer instance;
    private EmailSynchronizer() {};
    static {
        instance = new EmailSynchronizer();
    }

    public static EmailSynchronizer getInstance() {
        return instance;
    }

    public void startCheckForNewEmails() {
        startSync = true;
        Thread syncThread = new Thread(() -> {
            try {
                syncEmails();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        syncThread.setDaemon(true);
        syncThread.start();
    }

    public void stopCheckForNewEmails() {
        startSync = false;
    }

    public void syncEmails() throws InterruptedException {
        timeBetweenChecks = defaultTimeBetweenChecks;
        ServerResponse serverResponse = null;
        CommunicationHelper communicationHelper = new CommunicationHelper();
        List<Email> newEmails;
        while (startSync) {
            Thread.sleep(timeBetweenChecks);

            serverResponse = communicationHelper.GetNewEmails();
            if (serverResponse.getResponseType() != ResponseType.OK) {
                if (--maxIncreaseTime <= 0)
                    timeBetweenChecks *= (long) 1.5;
                continue;
            } else {
                timeBetweenChecks = defaultTimeBetweenChecks;
                maxIncreaseTime = defaultMaxIncreaseTime;
            }

            newEmails = (List<Email>) serverResponse.getPayload();
            if (!newEmails.isEmpty()) {
                for (Email email : newEmails)
                    SessionData.getInstance().addNewEmailOnTop(email);

                Utils.Log(newEmails.size() + " new emails fetched and added in the inbox");

                final int numberOfNewEmails = newEmails.size();
                Platform.runLater(() -> {
                    try {
                        URL loadedView = getClass().getResource("inbox-view.fxml");
                        if (loadedView == null)
                            throw new FileNotFoundException("Write page not found!");

                        FXMLLoader fxmlLoader = new FXMLLoader(loadedView);
                        fxmlLoader.load();
                        InboxController inboxController = fxmlLoader.getController();
                        inboxController.reloadInbox();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, numberOfNewEmails + " new email arrived!", ButtonType.OK);
                        alert.showAndWait();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
