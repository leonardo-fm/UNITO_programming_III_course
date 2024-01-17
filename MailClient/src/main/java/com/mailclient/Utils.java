package com.mailclient;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.S");

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean isValidEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.matches();
    }

    /**
     * It's mandatory to leave this class in this folder /com.mailclient, otherwise the resources folder can't be found
     */
    public static URL getResourceViewPath(String viewName) throws FileNotFoundException {
        URL loadedView = Utils.class.getResource(viewName);
        if (loadedView == null)
            throw new FileNotFoundException(viewName + " page not found!");

        return loadedView;
    }

    public static void loadNewScene(String viewName) throws IOException {
        URL loadedView = getResourceViewPath(viewName);

        Parent root = FXMLLoader.load(loadedView);
        Scene scene = new Scene(root);

        Stage currentStage = SessionData.getInstance().getCurrentStage();
        currentStage.setScene(scene);
        SessionData.getInstance().setCurrentView(viewName);
        currentStage.show();
    }

    public static void Log(String log) {
        System.out.println("[" + LocalDateTime.now().format(formatter) + "] - " + log);
    }
}
