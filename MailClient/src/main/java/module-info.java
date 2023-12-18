module com.example.mailclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires SharedModel;

    opens com.mailclient to javafx.fxml;
    exports com.mailclient;
}