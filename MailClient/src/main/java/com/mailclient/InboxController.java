package com.mailclient;

import com.sharedmodels.Email;
import com.sharedmodels.ServerRequest;
import com.sharedmodels.ServerResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import static com.sharedmodels.MethodType.SEND_EMAIL;

public class InboxController implements Initializable {

    private Scene scene;
    private Parent root;

    @FXML
    private Label username;
    @FXML
    private ScrollPane inboxHolder;
    @FXML
    private VBox inboxHolderVBox;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle) {
        SessionData.getInstance().getCurrentStage().setTitle("Inbox");
        SessionData.getInstance().getCurrentStage().setResizable(false);

        username.setText("Current user: " + SessionData.getInstance().getUserLogged());
        loadAllEmails();
    }

    private void loadAllEmails() {
        List<Email> inboxEmails = new CommunicationHelperMock().GetInboxEmailsMock();
        for (int i = 0; i < inboxEmails.size(); i++) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.TOP_LEFT);
            hBox.setPadding(new Insets(10));
            hBox.setPrefWidth(inboxHolder.getPrefWidth() - 50);

            Button button = generateButton(inboxEmails.get(i));

            Text emailPreview = new Text(
                     "\t" + inboxEmails.get(i).getSender() + " | "
                    + inboxEmails.get(i).getMailObject() + " - "
                    + inboxEmails.get(i).getMainContent());
            emailPreview.setTextAlignment(TextAlignment.CENTER);

            if (emailPreview.getText().length() > 80)
                emailPreview.setText(emailPreview.getText().substring(0, 80) + "...");

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
                root = fxmlLoader.load();

                ReadEmailController readEmailController = fxmlLoader.getController();
                readEmailController.Setup(email);

                scene = new Scene(root);

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
        root = FXMLLoader.load(getClass().getResource("writeEmail-view.fxml"));
        scene = new Scene(root);

        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        currentStage.show();
    }

    public void onLogoutBtnClick(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("login-view.fxml"));
        scene = new Scene(root);

        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        currentStage.show();
    }
}