/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.gumana;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class ProtoController implements Initializable {


    @FXML
    private TextField lbl_username;
    @FXML
    private PasswordField lbl_password;
    @FXML
    private CheckBox cb_showPassword;
    @FXML
    private TextField txt_showPassword;
    @FXML
    private Button loginCancelButton;
    @FXML
    private Label loginCheckLabel;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.actionshowPassword(null);
    }    
    
    //Database Tools
    private Connection conn = null;
    private PreparedStatement statement;
    private ResultSet result;
    
    public Connection connectDB(){
        
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/myproject_db","root","");
            return conn;
        }catch (Exception e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            System.out.println(e.getMessage());
        }
            return null;
    }
    public void loginLoginButtonOnAction (ActionEvent e){

        if (lbl_username.getText().isBlank() == false && getPassword().isBlank() == false) {
            //loginCheckLabel.setText("You try to Login!");
            btn_login();
        }else {
            loginCheckLabel.setText("Please fill out all fields!");
        }
    }
    private String getPassword(){
        if(txt_showPassword.isVisible()){
            return txt_showPassword.getText();
        } else {
            return lbl_password.getText();
        }
    }

    @FXML
    public void btn_login() {
        conn = connectDB();
        try{
            PreparedStatement statement = conn.prepareStatement("select * from tbl_accounts");
            ResultSet result = statement.executeQuery();
            
            while (result.next()){
                String encryptedPassword = result.getString("password");
                String Username = result.getString("username");;
                String user = result.getString("userType");
                if(lbl_username.getText().equals(Username) && Encryptor.encryptString(getPassword()).equals(encryptedPassword) && user.contains("Administrator")){
                    App.setCurrUser(new UserModel(result.getString("firstName") ,result.getString("userType")));
                    App.setRoot("FXML_ViewProduct");
                } if(lbl_username.getText().equals(Username) && Encryptor.encryptString(getPassword()).equals(encryptedPassword) && user.contains("User")){
                    App.setCurrUser(new UserModel(result.getString("firstName"),result.getString("userType")));
                    App.setRoot("FXML_ViewProduct");
                }else {
                    loginCheckLabel.setText("Invalid Username or Password!");
                }
    
            }
        }catch (IOException | SQLException | NoSuchAlgorithmException e){
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
                System.out.println(e.getMessage());
                System.out.println("catch nagrun");
                e.printStackTrace();
        }
    }
    public void loginCancelButtonOnAction (ActionEvent e) {

        Stage stage = (Stage) loginCancelButton.getScene().getWindow();
        stage.close();

    }

    public void loginMinimizeButtonOnAction (ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);

    }
    public void loginRegisterHyperlinkOnAction (ActionEvent e) throws IOException {
        App.setRoot("FXML_Signup");
    }

    @FXML
    private void actionshowPassword(ActionEvent event) {
        if (cb_showPassword.isSelected()) {
            txt_showPassword.setText(lbl_password.getText());
            txt_showPassword.setVisible(true);
            lbl_password.setVisible(false);
        } else {
            lbl_password.setText(txt_showPassword.getText());
            lbl_password.setVisible(true);
            txt_showPassword.setVisible(false);
        }
    }
}
