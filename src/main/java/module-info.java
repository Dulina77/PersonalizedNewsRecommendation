module com.example.cw1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires okhttp3;
    requires org.jsoup;
    requires java.sql;
    requires java.desktop;


    opens com.example.cw1 to javafx.fxml;
    exports com.example.cw1;
}