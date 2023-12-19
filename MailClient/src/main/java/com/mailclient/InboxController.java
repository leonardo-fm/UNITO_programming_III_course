package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ResponseType;
import com.sharedmodels.ServerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InboxController implements Initializable {

    @FXML
    private Label username;
    @FXML
    private Label errorLabel;
    @FXML
    private ScrollPane inboxHolder;
    @FXML
    private VBox inboxHolderVBox;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        SessionData.getInstance().getCurrentStage().setTitle("Inbox");
        SessionData.getInstance().getCurrentStage().setResizable(false);

        errorLabel.setText("");
        username.setText("Current user: " + SessionData.getInstance().getUserLogged());
        loadAllEmails();
    }

    private void loadAllEmails() {
        // TODO remove here!
        List<Email> inboxEmails = new CommunicationHelperMock().GetInboxEmailsMock();
/*
        ServerResponse serverResponse = new CommunicationHelper().GetInboxEmails();
        if (serverResponse.getResponseType() == ResponseType.ERROR) {
            errorLabel.setText("Error while retrieving the emails from the server");
            return;
        }
        List<Email> inboxEmails = (List<Email>) serverResponse.getPayload();
*/
        for (Email inboxEmail : inboxEmails) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.TOP_LEFT);
            hBox.setPadding(new Insets(10));
            hBox.setPrefWidth(inboxHolder.getPrefWidth() - 50);

            Button button = generateButton(inboxEmail);
            Text emailPreview = new Text(
                    "\t" + inboxEmail.getSender() + " | "
                            + inboxEmail.getMailObject() + " - "
                            + inboxEmail.getMainContent());
            emailPreview.setTextAlignment(TextAlignment.CENTER);

            if (emailPreview.getText().length() > 75)
                emailPreview.setText(emailPreview.getText().substring(0, 75) + "...");

            hBox.getChildren().add(button);
            hBox.getChildren().add(emailPreview);
            inboxHolderVBox.getChildren().add(hBox);
        }
    }

    private Button generateButton(Email email) {
        Button button = new Button("Read");
        button.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("readEmail-view.fxml"));
                Parent root = fxmlLoader.load();

                ReadEmailController readEmailController = fxmlLoader.getController();
                readEmailController.Setup(email);

                Scene scene = new Scene(root);

                Stage currentStage = SessionData.getInstance().getCurrentStage();
                currentStage.setScene(scene);
                currentStage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        return button;
    }

    public void onWriteBtnClick(ActionEvent event) throws IOException {
        Utils.loadNewScene("writeEmail-view.fxml");
    }

    public void onLogoutBtnClick(ActionEvent event) throws IOException {
        Utils.loadNewScene("login-view.fxml");
    }
}