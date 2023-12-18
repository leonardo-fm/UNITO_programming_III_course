package com.sharedmodels;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Email implements Serializable {
    private UUID id;
    private String sender;
    private List<String> receivers;
    private String mailObject;
    private String mainContent;
    private Date mailDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public String getMailObject() {
        return mailObject;
    }

    public void setMailObject(String mailObject) {
        this.mailObject = mailObject;
    }

    public String getMainContent() {
        return mainContent;
    }

    public void setMainContent(String mainContent) {
        this.mainContent = mainContent;
    }

    public Date getMailDate() {
        return mailDate;
    }

    public void setMailDate(Date mailDate) {
        this.mailDate = mailDate;
    }
}
