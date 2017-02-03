package com.abm.controllers;

import com.abm.models.ConnectionParameterDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by br33 on 01.02.2017.
 */
public class NewConnectionController {
    @FXML
    TextField inputUsername;
    @FXML
    PasswordField inputPassword;
    @FXML
    TextField inputApiKey;
    @FXML
    Label lblValidator;


    @FXML
    public void btnCancel_clickAction(ActionEvent event) {
        goToConnectionManager(event);
    }

    @FXML
    public void btnCreate_clickAction(ActionEvent event) {
        if (!isValidData())
            return;

        ConnectionParameterDAO.getInstance().addNewConnectionParameter(inputUsername.getText(), inputPassword.getText(), inputApiKey.getText());

        goToConnectionManager(event);
    }

    private void goToConnectionManager(ActionEvent event) {
        Stage connectionManagerState = new Stage();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/com/abm/views/connectionManager.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        connectionManagerState.setScene(new Scene(root));
        connectionManagerState.show();

        connectionManagerState.setTitle("Allegro Bulk Manager");
        connectionManagerState.setResizable(false);

        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    private boolean isValidData() {
        boolean returnedValue = true;

        returnedValue &= isTextValid(inputUsername, 5);
        returnedValue &= isTextValid(inputPassword, 5);
        returnedValue &= isTextValid(inputApiKey, 5);

        lblValidator.setVisible(!returnedValue);

        return returnedValue;
    }

    private boolean isTextValid(TextField input, int size) {
        boolean returnedValue = true;

        if (input.getText().length() < size) {
            if (!input.getStyleClass().contains("invalidField"))
                input.getStyleClass().add("invalidField");
            returnedValue = false;
        } else
            input.getStyleClass().remove("invalidField");

        return returnedValue;
    }

}
