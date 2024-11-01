module lk.ijse.oopcoursework {
    requires com.jfoenix;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens lk.ijse.oopcoursework.controller to javafx.fxml;
    exports lk.ijse.oopcoursework;
}