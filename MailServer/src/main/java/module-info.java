module com.example.mailserver {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mailserver to javafx.fxml;
    exports com.mailserver;
}