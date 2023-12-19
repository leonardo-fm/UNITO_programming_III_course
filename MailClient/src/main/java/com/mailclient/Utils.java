package com.mailclient;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    public static void loadNewScene(String viewName) throws IOException {
        URL loadedView = Utils.class.getResource(viewName);
        if (loadedView == null)
            throw new FileNotFoundException(viewName + " page not found!");

        Parent root = FXMLLoader.load(loadedView);
        Scene scene = new Scene(root);

        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        currentStage.show();
    }
}
