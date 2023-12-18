package com.mailserver.controller;

import com.mailserver.model.ServerModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ServerController {
    public TextArea logTextField;
    private ServerModel model;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void initModel(ServerModel model){
        if (this.model != null)
            throw new IllegalStateException("Model can only be initialized once");
        if (model == null)
            throw new IllegalStateException("Model cannot be null");
        this.model = model;

        this.logTextField.textProperty().bind(this.model.logProperty());
    }
}