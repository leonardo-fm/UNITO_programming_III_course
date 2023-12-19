package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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
        Thread t1 = new Thread(() -> {
            try {
                syncEmails();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        t1.start();
    }

    public void stopCheckForNewEmails() {
        startSync = false;
    }

    public void syncEmails() throws InterruptedException {
        timeBetweenChecks = defaultTimeBetweenChecks;
        ServerResponse serverResponse = null;
        List<Email> newEmails;
        while (startSync) {
            Thread.sleep(timeBetweenChecks);

            serverResponse = new CommunicationHelper().GetNewEmails();
            if (serverResponse.getResponseType() == ResponseType.ERROR) {
                if (--maxIncreaseTime <= 0)
                    timeBetweenChecks *= (long) 1.5;
                continue;
            } else {
                timeBetweenChecks = defaultTimeBetweenChecks;
                maxIncreaseTime = defaultMaxIncreaseTime;
            }

            newEmails = (List<Email>) serverResponse.getPayload();
            if (!newEmails.isEmpty()) {
                SessionData.getInstance().getInboxEmails().addAll(newEmails);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, newEmails.size() + " new email arrived!", ButtonType.OK);
                alert.showAndWait();
            }
        }
    }
}