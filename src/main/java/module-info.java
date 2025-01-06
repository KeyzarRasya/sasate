module com.example.sasatepk {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.sasatepk to javafx.fxml;
    exports com.example.sasatepk;
}