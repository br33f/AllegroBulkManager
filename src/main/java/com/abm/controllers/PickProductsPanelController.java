package com.abm.controllers;

import com.abm.models.Product;
import com.abm.models.ProductDAO;
import com.abm.tasks.GetAllProductsTask;
import com.abm.utils.Util;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class PickProductsPanelController implements Initializable {
    private Timer searchTimer = null;
    private IntegerProperty selectedCount = new SimpleIntegerProperty();
    private ObservableList<Product> selected = FXCollections.observableArrayList();

    @FXML
    private Pane paneInfos;
    @FXML
    private Pane paneOptions;
    @FXML
    private TableView tvProducts;
    @FXML
    private TableView tvProductsSelected;
    @FXML
    private ProgressBar pbLoading;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRemove;
    @FXML
    private Tab tabAll;
    @FXML
    private TextField inputID;
    @FXML
    private TextField inputName;
    @FXML
    private TextField inputCategory;
    @FXML
    private Label lbSelected;

    public void tab_change(Event e) {
        if (btnAdd != null && btnRemove != null) {
            boolean isAllTabSelected = tabAll.isSelected();
            btnAdd.setVisible(isAllTabSelected);
            btnRemove.setVisible(!isAllTabSelected);
            updateSelectedCounter();
        }
    }

    public void inputSearch_keyTyped(InputEvent e) {
        if (searchTimer != null)
            searchTimer.cancel();

        TimerTask searchTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ArrayList<String[]> params = new ArrayList<String[]>();
                    if (inputID.getText().length() > 0)
                        params.add(new String[]{"id", inputID.getText()});
                    if (inputName.getText().length() > 0)
                        params.add(new String[]{"name", inputName.getText()});
                    if (inputCategory.getText().length() > 0)
                        params.add(new String[]{"category", inputCategory.getText()});

                    tv_fill(tvProducts, getProducts(params));
                });
            }
        };

        searchTimer = new Timer();
        searchTimer.schedule(searchTask, 400);
    }

    public void lbSelectAll_click(MouseEvent e) {
        TableView tableView = tvProducts;
        if (!tabAll.isSelected())
            tableView = tvProductsSelected;

        if (tableView.getSelectionModel() != null) {
            tableView.getSelectionModel().selectAll();
        }
        updateSelectedCounter();
    }

    public void lbDeselectAll_click(MouseEvent e) {
        TableView tableView = tvProducts;
        if (!tabAll.isSelected())
            tableView = tvProductsSelected;

        if (tableView.getSelectionModel() != null) {
            tableView.getSelectionModel().clearSelection();
        }
        updateSelectedCounter();
    }

    public void btnProceed_clickAction(ActionEvent e) {

    }

    public void btnAdd_clickAction(ActionEvent e) {
        for (Product p : (ObservableList<Product>) tvProducts.getSelectionModel().getSelectedItems()) {
            if (!selected.contains(p)) {
                selected.add(p);
            }
        }

        tv_fill(tvProductsSelected, selected);
    }

    public void btnRemove_clickAction(ActionEvent e) {
        selected.removeAll(tvProductsSelected.getSelectionModel().getSelectedItems());

        tv_fill(tvProductsSelected, selected);
    }

    public void btnSynchronize_clickAction(ActionEvent e) {
        loadStart();

        GetAllProductsTask getAllProductsTask = new GetAllProductsTask();
        pbLoading.progressProperty().bind(getAllProductsTask.progressProperty());

        EventHandler<WorkerStateEvent> successHandler = event -> {
            loadStop();
            if (getAllProductsTask.getValue() != 0) {
                Util.alert("Błąd podczas dodawania produktów do tabeli", "Spróbuj ponownie.");
                throw new RuntimeException(getAllProductsTask.getException());
            } else {
                tv_fill(tvProducts, getProducts(null));
            }
        };
        getAllProductsTask.setOnSucceeded(successHandler);

        EventHandler<WorkerStateEvent> failHandler = event -> {
            Util.alert("UPSS...", "Coś poszło nie tak.");
            loadStop();

            throw new RuntimeException(getAllProductsTask.getException());
        };
        getAllProductsTask.setOnFailed(failHandler);

        new Thread(getAllProductsTask).start();
    }


    private void tv_fill(TableView table, ObservableList<Product> list) {
        table.getItems().clear();

        table.setItems(list);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        TableColumn<Product, Long> colId = new TableColumn<>("ID Produktu");
        colId.setCellValueFactory(new PropertyValueFactory<Product, Long>("id"));

        TableColumn<Product, String> colName = new TableColumn<>("Nazwa");
        colName.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));

        TableColumn<Product, Long> colCategory = new TableColumn<>("Kategoria");
        colCategory.setCellValueFactory(new PropertyValueFactory<Product, Long>("category"));

        TableColumn<Product, Float> colPrice = new TableColumn<>("Cena");
        colPrice.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));

        TableColumn<Product, Integer> colOffers = new TableColumn<>("Ofert kupna");
        colOffers.setCellValueFactory(new PropertyValueFactory<Product, Integer>("offers"));

        table.getColumns().setAll(colId, colName, colCategory, colPrice, colOffers);
    }

    private ObservableList<Product> getProducts(ArrayList<String[]> params) {
        ProductDAO productDAO = ProductDAO.getInstance();
        if (params == null)
            return productDAO.getFullProductList();
        else
            return productDAO.getProductList(params);
    }


    private void loadStart() {
        paneInfos.setVisible(true);
        paneOptions.setDisable(true);
    }

    private void loadStop() {
        paneOptions.setDisable(false);
        paneInfos.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tv_fill(tvProducts, getProducts(null));
        tvProducts.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            updateSelectedCounter();
        });

        (new Timer()).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    updateSelectedCounter();
                });
            }
        }, 500, 500);

        lbSelected.textProperty().bind(selectedCount.asString());
    }

    private void updateSelectedCounter() {
        if (tabAll.isSelected()) {
            updateProcedure(tvProducts, btnAdd);
        } else {
            updateProcedure(tvProductsSelected, btnRemove);
        }
    }

    private void updateProcedure(TableView table, Button btn) {
        if (table.getSelectionModel().getSelectedItems() != null) {
            selectedCount.set(table.getSelectionModel().getSelectedItems().size());
        }

        btn.setDisable((selectedCount.get() == 0));
    }
}
