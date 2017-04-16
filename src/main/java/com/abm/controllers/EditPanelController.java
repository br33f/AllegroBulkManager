package com.abm.controllers;

import com.abm.models.EditProductTableCell;
import com.abm.models.Product;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class EditPanelController implements Initializable {
    private ObservableList<Product> editProducts = FXCollections.observableArrayList();

    @FXML
    private Button btnFindReplace;
    @FXML
    private TableView tvEdit;
    @FXML
    private ProgressBar pbLoading;
    @FXML
    private Label lbLoaded;

    public EditPanelController(ObservableList<Product> editProducts) {
        this.editProducts = editProducts;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeEditTable();
    }

    private void initializeEditTable() {
        tvEdit.setItems(getEditProducts());

        TableColumn<Product, Long> colId = new TableColumn<>("ID Produktu");
        colId.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));

        TableColumn<Product, String> colName = new TableColumn<>("Nazwa");
        colName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        TableColumn<Product, Float> colPrice = new TableColumn<>("Cena");
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));

        TableColumn<Product, Integer> colOffers = new TableColumn<>("Ofert kupna");
        colOffers.setCellValueFactory(new PropertyValueFactory<Product, Integer>("offers"));

        TableColumn<Product, Long> colCategory = new TableColumn<>("Kategoria");
        colCategory.setCellValueFactory(new PropertyValueFactory<Product, Long>("category"));

        TableColumn<Product, Product> colEdit = new TableColumn<>("Edycja");
        colEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        colEdit.setCellFactory(param -> new EditProductTableCell());

        tvEdit.getColumns().setAll(colId, colName, colCategory, colPrice, colOffers, colEdit);

        lbLoaded.setText(String.valueOf(getEditProducts().size()));
    }

    public ObservableList<Product> getEditProducts() {
        return editProducts;
    }

    public void btnFindReplace_clickAction(ActionEvent event) {
    }
}
