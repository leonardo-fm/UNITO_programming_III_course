module com.example.mailclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires SharedModel;

    opens com.mailclient to javafx.fxml;
    exports com.mailclient;
    exports com.mailclient.controller;
    opens com.mailclient.controller to javafx.fxml;
}