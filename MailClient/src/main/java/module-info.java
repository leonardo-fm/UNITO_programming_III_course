module com.example.mailclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.mailclient to javafx.fxml;
    exports com.mailclient;
}