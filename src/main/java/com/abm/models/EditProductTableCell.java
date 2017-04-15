package com.abm.models;

import com.abm.controllers.ProductPanelController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by br33 on 15.04.2017.
 */
public class EditProductTableCell extends TableCell<Product, Product> {
    private Button editButton = new Button("Edytuj");

    @Override
    protected void updateItem(Product item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            setGraphic(editButton);
            editButton.setOnAction(event -> showProductEditForm(item));
        }
    }

    private void showProductEditForm(Product product) {
        Stage productPanelStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/abm/views/productPanel.fxml"));

        ProductPanelController productPanelController = new ProductPanelController(product);
        loader.setController(productPanelController);

        Pane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productPanelStage.setTitle("ABM - " + product.getName());
        productPanelStage.setScene(new Scene(root));
        productPanelStage.show();

        productPanelStage.setResizable(false);
    }
}
