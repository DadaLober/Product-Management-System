module com.mycompany.gumana {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.desktop;
    requires com.jfoenix;
    requires AnimateFX;

    opens com.mycompany.gumana to javafx.fxml;
    exports com.mycompany.gumana;
}
