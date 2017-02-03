package com.abm.controllers;

import com.abm.adapters.DBA;
import com.abm.models.Product;
import com.abm.models.ProductDAO;
import com.abm.tasks.GetAllProductsTask;
import com.abm.utils.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;


/**
 * Created by br33 on 02.02.2017.
 */
public class PickProductsPanelController {
    private DBA dba = DBA.getInstance();

    @FXML
    private Pane panePickProducts;
    @FXML
    private Pane paneLoad;
    @FXML
    private TableView tvProducts;

    public void btnProceed_clickAction(ActionEvent e) {
        tvProducts_fill();
    }

    public void btnSynchronize_clickAction(ActionEvent e) {
        loadStart();

        GetAllProductsTask getAllProductsTask = new GetAllProductsTask();
        getAllProductsTask.setOnSucceeded(c -> {
            loadStop();
            if(getAllProductsTask.getValue() != 0) {
                Util.alert("Błąd podczas dodawania produktów do tabeli", "Spróbuj ponownie.");
                throw new RuntimeException(getAllProductsTask.getException());
            }
        });
        getAllProductsTask.setOnFailed(c -> {
            Util.alert("UPSS...", "Coś poszło nie tak.");
            loadStop();
            throw new RuntimeException(getAllProductsTask.getException());
        });
        new Thread(getAllProductsTask).start();

    }

    private void tvProducts_fill() {
        ProductDAO productDAO = ProductDAO.getInstance();
        tvProducts.setItems(productDAO.getFullProductList());
        tvProducts.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Product, Long> colId = new TableColumn<>("ID Produktu");
        colId.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));

        TableColumn<Product, String> colName = new TableColumn<>("Nazwa");
        colName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        TableColumn<Product, Long> colCategory = new TableColumn<>("Kategoria");
        colCategory.setCellValueFactory(new PropertyValueFactory<Product, Long>("category"));

        TableColumn<Product, Float> colPrice = new TableColumn<>("Cena");
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));

        tvProducts.getColumns().setAll(colId, colName, colPrice, colCategory);
    }

    private void loadStart() {
        paneLoad.setVisible(true);
        panePickProducts.setDisable(true);
    }

    private void loadStop() {
        panePickProducts.setDisable(false);
        paneLoad.setVisible(false);
    }
}
