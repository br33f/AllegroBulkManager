package com.abm.controllers;

import com.abm.models.Product;
import com.abm.tasks.LoadImageViewsTask;
import com.abm.utils.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void initializeEditTable() {
        tvEdit.setItems(getEditProducts());

        TableColumn<Product, Long> colId = new TableColumn<>("ID Produktu");
        colId.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));

        TableColumn<Product, ImageView> colImage = new TableColumn<>("Zdjęcie");
        colImage.setCellValueFactory(new PropertyValueFactory<Product, ImageView>("imageView"));

        TableColumn<Product, String> colName = new TableColumn<>("Nazwa");
        colName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        TableColumn<Product, Long> colDescription = new TableColumn<>("Opis");
        colDescription.setCellValueFactory(new PropertyValueFactory<Product, Long>("description"));

        TableColumn<Product, Float> colPrice = new TableColumn<>("Cena");
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));

        TableColumn<Product, Integer> colOffers = new TableColumn<>("Ofert kupna");
        colOffers.setCellValueFactory(new PropertyValueFactory<Product, Integer>("offers"));

        tvEdit.getColumns().setAll(colId, colImage, colName, colDescription, colPrice, colOffers);

        lbLoaded.setText(String.valueOf(getEditProducts().size()));
    }

    private void loadTable() {
        pbLoading.setVisible(true);

        LoadImageViewsTask loadImageViewsTask = new LoadImageViewsTask(editProducts);
        pbLoading.progressProperty().bind(loadImageViewsTask.progressProperty());

        EventHandler<WorkerStateEvent> successHandler = event -> {
            pbLoading.setVisible(false);
            if (loadImageViewsTask.getValue() != 0) {
                Util.alert("Błąd podczas generowania obrazów.", "Spróbuj ponownie.");
                throw new RuntimeException(loadImageViewsTask.getException());
            } else {
                initializeEditTable();
            }
        };
        loadImageViewsTask.setOnSucceeded(successHandler);

        EventHandler<WorkerStateEvent> failHandler = event -> {
            Util.alert("UPSS...", "Coś poszło nie tak.");
            pbLoading.setVisible(false);

            throw new RuntimeException(loadImageViewsTask.getException());
        };
        loadImageViewsTask.setOnFailed(failHandler);

        new Thread(loadImageViewsTask).start();
    }

    public ObservableList<Product> getEditProducts() {
        return editProducts;
    }

    public void setEditProducts(ObservableList<Product> editProducts) {
        this.editProducts = editProducts;
    }

    public void btnFindReplace_clickAction(ActionEvent event) {
        loadTable();
    }
}
