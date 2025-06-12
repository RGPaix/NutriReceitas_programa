module NutriReceitas {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires java.net.http;
    requires java.desktop;

    exports org.example;
    exports org.example.Controllers;
    exports org.example.models;
    exports org.example.Services;

    // Permitir que JavaFX acesse os controllers via reflection
    opens org.example.Controllers to javafx.fxml;
    opens org.example.models to com.fasterxml.jackson.databind;
}