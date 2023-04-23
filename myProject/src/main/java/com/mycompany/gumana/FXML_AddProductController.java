
package com.mycompany.gumana;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import com.jfoenix.controls.JFXComboBox;

/**
 * FXML Controller class
 *
 * @author Luis
 */
public class FXML_AddProductController implements Initializable {

    @FXML
    private TextField txt_productName;
    @FXML
    private Button addCancelButton;
    @FXML
    private TextField txt_productPrice;
    @FXML
    private TextField txt_productModelnum;
    @FXML
    private TextField txt_productBrand;
    @FXML
    private TextField txt_highestPrice;
    @FXML
    private JFXComboBox<String> cb_category;
    @FXML
    private TextField txt_productQuantity;

    private ObservableList<String> Category = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Category.add("Keyboard");
        Category.add("Mouse");
        Category.add("Headset");
        Category.add("Speaker");
        Category.add("Monitor");
        Category.add("Phone");
        Category.add("Laptop");
        Category.add("Webcam");
        Category.add("Miscellaneous");
        cb_category.setItems(Category);

        txt_productPrice.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if (change.getControlNewText().matches("[0-9]{1,}")) {
                    return change;
                }
                return null;
            }
            return change;
        }));
        txt_productQuantity.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if (change.getControlNewText().matches("[0-9]{1,}")) {
                    return change;
                }
                return null;
            }
            return change;
        }));
        txt_highestPrice.setTextFormatter(new TextFormatter<>(change -> {
            if (change.isAdded()) {
                if (change.getControlNewText().matches("[0-9]{1,}")) {
                    return change;
                }
                return null;
            }
            return change;
        }));

    }
    private Connection conn = null;
    private PreparedStatement statement;

    public Connection connectDB() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/myproject_db", "root", "");
            return conn;
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void addCancelButtonOnAction (ActionEvent e) {

        Stage stage = (Stage) addCancelButton.getScene().getWindow();
        stage.close();

    }

    public void addMinimizeButtonOnAction (ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);

    }
    
    public void clearTxtfld()
        {
          txt_productName.setText("");
          txt_productPrice.setText("");
            txt_highestPrice.setText("");
          txt_productModelnum.setText("");
          txt_productBrand.setText("");
          txt_productQuantity.setText("");
          cb_category.setValue("");
         }

    private void addProductButton(ActionEvent event) throws IOException {
        App.setRoot("FXML_AddProduct");
    }
    @FXML
    private void btn_addProduct(ActionEvent event) {

        Alert alrt = new Alert(Alert.AlertType.NONE, "", new ButtonType("Try Again"));
        final String INVALID_INPUT = "Invalid Input";
        Boolean invalidInp = false;
        if (txt_productName.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Product Name\n");
        }
        if (txt_productPrice.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Product Lowest Price\n");
        }if (txt_highestPrice.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Product Highest Price\n");
        }
        if (txt_productQuantity.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Product Quantity\n");
        }
        if (txt_productModelnum.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Product Number\n");
        }
        if (txt_productBrand.getText().isBlank()) {
            invalidInp = true;
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Product Brand\n");
        }
        if (cb_category.getValue() == null) {
            alrt.setTitle(INVALID_INPUT);
            alrt.setContentText(alrt.getContentText() + "Missing Category\n");
        }
        if (invalidInp) {
            alrt.show();
            return;
        }

        conn = connectDB();
        String productName, productModel, productBrand,productCategory;
        Integer productPrice, productQuantity, productHigh;
        productName = txt_productName.getText();
        productPrice = Integer.valueOf(txt_productPrice.getText());
        productHigh = Integer.valueOf(txt_highestPrice.getText());
        productQuantity = Integer.valueOf(txt_productQuantity.getText());
        productModel = txt_productModelnum.getText();
        productBrand = txt_productBrand.getText();
        productCategory = cb_category.getValue();

        new Alert(Alert.AlertType.CONFIRMATION, "Do you want to add this product?").showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType response) {
                if (response == ButtonType.OK) {
                    try {
                        String sql = "INSERT INTO `tbl_products`(`productName`,`productPrice`,`productHigh`,`productQuantity`,`productModel`, `productBrand`, `productCategory`) VALUES ('" + productName + "','" + productPrice + "','" + productHigh + "','" + productQuantity + "','" + productModel + "','" + productBrand + "','" + productCategory + "')";
                        statement = conn.prepareStatement(sql);
                        statement.executeUpdate();
                        new Alert(Alert.AlertType.INFORMATION, "Succesfully added!").show();
                        clearTxtfld();
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
                            System.out.println(e.getMessage());
                    }
                }
            }
        });
    }

}
