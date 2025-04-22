module com.example.fxproject {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fxproject to javafx.fxml;
    opens com.example.fxproject.controller to javafx.fxml;
    
    exports com.example.fxproject;
    exports com.example.fxproject.controller;
    exports com.example.fxproject.model;
}