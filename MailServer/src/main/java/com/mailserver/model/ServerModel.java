package com.mailserver.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerModel {
    public ServerModel() {
        this.setLog("");
    }

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.S");
    private final StringProperty log = new SimpleStringProperty();
    public final StringProperty logProperty() {
        return this.log;
    }

    public final String getLog() {
        return this.logProperty().get();
    }

    public final void setLog(String log) {
        this.logProperty().set(log);
    }
    public final void addLog(String log){
        String newLog = this.getLog() + LocalDateTime.now().format(formatter) + " - " + log + "\n";
        this.setLog(newLog);
    }
}
