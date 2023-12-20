package com.sharedmodels;

import java.io.Serializable;
import java.util.UUID;

public class DeleteData implements Serializable {

    private UUID id;
    private String emailAddress;

    public DeleteData(UUID id, String emailAddress) {
        this.id = id;
        this.emailAddress = emailAddress;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
