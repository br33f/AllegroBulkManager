package com.abm.models;

import com.abm.adapters.DBA;
import com.abm.utils.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by br33 on 03.02.2017.
 */
public class ConnectionParameterDAO {
    private static ConnectionParameterDAO ourInstance = new ConnectionParameterDAO();

    public static ConnectionParameterDAO getInstance() {
        return ourInstance;
    }

    private ConnectionParameterDAO() {
    }

    public void addNewConnectionParameter(String username, String password, String apiKey) {
        ArrayList<String[]> parameters = new ArrayList<String[]>();
        parameters.add(new String[]{"username", username});
        parameters.add(new String[]{"password", password});
        parameters.add(new String[]{"api_key", apiKey});

        if (!DBA.getInstance().addNewRow("abm_connections", parameters)) {
            Util.alert("Błąd podczas dodawania rekordu do bazy danych", "Spróbuj ponownie.");
        }
    }

    public ObservableList<ConnectionParameter> getConnectionParametersList() {
        ObservableList<ConnectionParameter> items = FXCollections.observableArrayList();

        ResultSet rs = DBA.getInstance().getResultSet("SELECT * FROM `abm_connections` WHERE 1;");
        try {
            while (rs.next()) {
                items.add(new ConnectionParameter(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("api_key")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public void delete(ConnectionParameter connectionParameter) {
        if (!DBA.getInstance().delete("DELETE FROM `abm_connections` WHERE `id` = " + connectionParameter.getId())) {
            Util.alert("Błąd podczas usuwania rekordu z bazy danych", "Spróbuj ponownie.");
        }
    }
}
