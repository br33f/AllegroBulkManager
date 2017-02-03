package com.abm.models;

import com.abm.adapters.DBA;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by br33 on 03.02.2017.
 */
public class ProductDAO {
    private static ProductDAO ourInstance = new ProductDAO();

    public static ProductDAO getInstance() {
        return ourInstance;
    }

    private ProductDAO() {
    }

    public ObservableList<Product> getFullProductList() {
        ObservableList<Product> items = FXCollections.observableArrayList();

        ResultSet rs = DBA.getInstance().getResultSet("SELECT * FROM `abm_products_working` WHERE 1;");
        try {
            while (rs.next()) {
                Product product = new Product(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("image")
                );
                items.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}
