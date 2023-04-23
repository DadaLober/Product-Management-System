package com.mycompany.gumana;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static UserModel currUser = null;
    private double xOffset = 0;
    private double yOffset = 0;

    public static UserModel getCurrUser() {
        return currUser;
    }

    public static void setCurrUser(UserModel currUser) {
        App.currUser = currUser;
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("FXML_Login.fxml"));
        scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });
        stage.setScene(scene);
        stage.show();
        new animatefx.animation.FadeInDown(root).play();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(root);
        new animatefx.animation.FadeIn(root).play();
        scene.getWindow().sizeToScene();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
    

}