package com.mailserver.controller;

import com.mailserver.model.ServerModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ServerController {
    @FXML
    public TextArea logTextArea;
    private ServerModel model;

    public void initModel(ServerModel model) {

        if (this.model != null)
            throw new IllegalStateException("Model can only be initialized once");
        if (model == null)
            throw new IllegalStateException("Model cannot be null");
        this.model = model;

        logTextArea.textProperty().bind(this.model.logProperty());
        // Automatic scroll to bottom when text change
        model.logProperty().addListener((observable, oldValue, newValue) -> logTextArea.setScrollTop(Double.MAX_VALUE));
    }
}