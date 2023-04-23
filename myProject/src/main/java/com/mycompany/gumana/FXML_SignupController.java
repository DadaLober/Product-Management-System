/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.gumana;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import javax.swing.JOptionPane;

public class FXML_SignupController implements Initializable {

    @FXML
    private Button btn_register;
    @FXML
    private TextField txt_firstName;
    @FXML
    private TextField txt_lastName;
    @FXML
    private TextField txt_username;
    @FXML
    private PasswordField txt_password;
    @FXML
    private Button btn_balik;
    @FXML
    private CheckBox cb_showPassword;
    @FXML
    private TextField txt_showPassword;
    @FXML
    private RadioButton ra_user;
    @FXML
    private ToggleGroup tg_userType;
    @FXML
    private RadioButton ra_admin;
    @FXML
    private Button registerCancelButton;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.actionshowPassword(null);
    } 
    private Connection conn = null;
    private PreparedStatement statement;
    private ResultSet result;
    
    public Connection connectDB(){
        
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost/myproject_db","root","");
            return conn;
        }catch (SQLException e){
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            System.out.println(e.getMessage());
            return null;
        }
            
    }
    private String getPassword(){
        if(txt_showPassword.isVisible()){
            return txt_showPassword.getText();
        } else {
            return txt_password.getText();
        }
    }
    @FXML
    private void btn_register(ActionEvent event) throws NoSuchAlgorithmException {
        conn = connectDB();
        String Username, Password, lastName, firstName, hashedPass, userType;
        firstName = txt_firstName.getText();
        lastName = txt_lastName.getText();
        Username = txt_username.getText();
        Password = getPassword();;
        hashedPass = Encryptor.encryptString(Password);
        
        Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));
        final String INVALID_INPUT = "Invalid Input";
        Boolean invalidInp = false;
        if (txt_firstName.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing First Name\n");
        }
        if (txt_lastName.getText().isBlank()){
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Last Name\n"); 
        }
        if (txt_username.getText().isBlank()){
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Username\n"); 
        }
        if (!txt_password.getText().isBlank()){
            if (txt_password.getText().length() < 8){
                invalidInp = true;
                alrt.setTitle(INVALID_INPUT);
                alrt.setContentText(alrt.getContentText() + "Password needs at least 8 characters!\n");
            }
        } else {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Password\n");
        }
        if (!ra_user.isSelected() && !ra_admin.isSelected()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing User Type\n");        
        }
        if(invalidInp){
            alrt.show();
            return;
        }
        if (ra_user.isSelected()){
            userType = "User";
        } else {
            userType = "Administrator";
        }
        
        new Alert(Alert.AlertType.CONFIRMATION, "Do you want to continue to register?").showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType response) {
                if (response == ButtonType.OK) {
                    try {
                        String sql = "INSERT INTO `tbl_accounts`(`lastName`,`firstName`,`username`, `password`, `userType`) VALUES ('"+lastName+"','"+firstName+"','"+Username+"','"+hashedPass+"','"+userType+"')";
                        statement = conn.prepareStatement(sql);
                        statement.execute();
                        App.setRoot("FXML_Login");
                        new Alert(Alert.AlertType.INFORMATION,"Succesfully registered as "+userType+"").show();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    } catch (SQLException x){
                        new Alert(Alert.AlertType.INFORMATION, x.getMessage()).showAndWait();
                        new Alert(Alert.AlertType.WARNING,"This "+Username+" already exists").show();
                        System.out.println(x.getMessage());
                    }
                }
            }
        });
    }
    @FXML
    private void btn_balik(ActionEvent event) throws IOException{
        App.setRoot("FXML_Login");
    }
    public void registerCancelButtonOnAction (ActionEvent e) {

        Stage stage = (Stage) registerCancelButton.getScene().getWindow();
        stage.close();

    }

    public void registerMinimizeButtonOnAction (ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);

    }
    
    @FXML
    private void actionshowPassword(ActionEvent event) {
        if (cb_showPassword.isSelected()) {
            txt_showPassword.setText(txt_password.getText());
            txt_showPassword.setVisible(true);
            txt_password.setVisible(false);
        } else {
            txt_password.setText(txt_showPassword.getText());
            txt_password.setVisible(true);
            txt_showPassword.setVisible(false);
        }
    }
    
}
