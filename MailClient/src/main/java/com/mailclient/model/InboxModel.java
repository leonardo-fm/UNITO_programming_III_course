package com.mailclient.model;

import com.sharedmodels.Email;

import java.util.List;

public class InboxModel {

    private List<Email> emails;

    public List<Email> getEmails() {
        return emails;
    }

    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }
}
