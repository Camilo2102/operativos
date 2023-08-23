module com.example.operativosui {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.operativosui to javafx.fxml;
    exports com.example.operativosui;
}