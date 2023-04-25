module com.mycompany.gumana {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.desktop;
    requires com.jfoenix;
    requires AnimateFX;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.coreui;

    opens com.mycompany.gumana to javafx.fxml;
    exports com.mycompany.gumana;
}
