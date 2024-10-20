module com.example.cw1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cw1 to javafx.fxml;
    exports com.example.cw1;
}