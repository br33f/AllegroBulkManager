package com.abm.controllers;

import com.abm.adapters.DBA;
import com.abm.models.ConnectionParameter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by br33 on 01.02.2017.
 */
public class ConnectionManagerController implements Initializable {
    @FXML
    private Button btnAddNew;
    @FXML
    private Button btnConnect;
    @FXML
    private Label lblDelete;
    @FXML
    private ListView<ConnectionParameter> lvConnections;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            lvConnections_fill();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnConnect.setDisable(true);
        lblDelete.setDisable(true);
    }

    private void lvConnections_fill() throws SQLException {
        ObservableList<ConnectionParameter> items = FXCollections.observableArrayList();

        ResultSet rs = DBA.getInstance().getResultSet("SELECT * FROM `abm_connections` WHERE 1;");
        while (rs.next()) {
            items.add(new ConnectionParameter(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("api_key")));
        }

        lvConnections.setItems(items);

        lvConnections.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConnectionParameter>() {
            public void changed(ObservableValue<? extends ConnectionParameter> observable, ConnectionParameter oldValue, ConnectionParameter newValue) {
                if (lvConnections.getSelectionModel().getSelectedItem() == null) {
                    btnConnect.setDisable(true);
                    lblDelete.setDisable(true);
                } else {
                    btnConnect.setDisable(false);
                    lblDelete.setDisable(false);
                }
            }
        });
    }

    @FXML
    public void btnAddNew_clickAction(ActionEvent event) {
        Stage newConnectionStage = new Stage();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/abm/views/newConnection.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        newConnectionStage.setTitle("Allegro Bulk Manager");
        newConnectionStage.setScene(new Scene(root));
        newConnectionStage.show();

        newConnectionStage.setResizable(false);

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    public void btnConnect_clickAction(ActionEvent event) {
        System.out.println(lvConnections.getSelectionModel().getSelectedItem());

    }

    @FXML
    public void lblDelete_mouseClickEvent(MouseEvent event) {
        ConnectionParameter connectionParameter = lvConnections.getSelectionModel().getSelectedItem();

        if (!DBA.getInstance().delete("DELETE FROM `abm_connections` WHERE `id` = " + connectionParameter.getId())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Błąd podczas usuwania rekordu z bazy danych");
            alert.setContentText("Spróbuj ponownie.");

            alert.showAndWait();
        }

        try {
            lvConnections_fill();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
