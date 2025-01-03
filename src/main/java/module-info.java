module com.example.sasatepk {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sasatepk to javafx.fxml;
    exports com.example.sasatepk;
}