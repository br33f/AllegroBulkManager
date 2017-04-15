package com.abm.controllers;

import com.abm.models.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.HTMLEditor;

import java.net.URL;
import java.util.ResourceBundle;

public class ProductPanelController implements Initializable {
    private Product product;

    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private TextField inputPrice;
    @FXML
    private TextField inputName;
    @FXML
    private ImageView ivProductImage;

    public ProductPanelController(Product product) {
        this.product = product;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputName.setText(product.getName());
        htmlEditor.setHtmlText(product.getDescription());
        inputPrice.setText(String.valueOf(product.getPrice()));

        ivProductImage.setImage(new Image(product.getImage()));
    }

    public void btnCancel_clickAction(ActionEvent event) {
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public void btnSave_clickAction(ActionEvent event) {
        product.setName(inputName.getText());
        product.setDescription(htmlEditor.getHtmlText());
        product.setPrice(Float.parseFloat(inputPrice.getText()));

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }
}
