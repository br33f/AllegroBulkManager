package com.abm.models;

import com.abm.adapters.DBA;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                        rs.getString("image"),
                        rs.getInt("offers")
                );
                items.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public ObservableList<Product> getProductList(ArrayList<String[]> params) {
        ObservableList<Product> items = FXCollections.observableArrayList();

        ResultSet rs = DBA.getInstance().getResultSet("SELECT * FROM `abm_products_working` WHERE 1;");
        try {
            while (rs.next()) {
                Product product = new Product(rs.getLong("id"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getString("category"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getInt("offers")
                );

                if(isAcceptable(product, params)) {
                    items.add(product);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    private boolean isAcceptable(Product product, ArrayList<String[]> params) {
        boolean acceptable = true;
        for(String[] param : params) {
            switch (param[0]) {
                case "id":
                    acceptable &= String.valueOf(product.getId()).toLowerCase().contains(param[1].toLowerCase());
                    break;
                case "name":
                    acceptable &= product.getName().toLowerCase().contains(param[1].toLowerCase());
                    break;
                case "category":
                    acceptable &= product.getCategory().toLowerCase().contains(param[1].toLowerCase());
                    break;
            }
        }

        return acceptable;
    }
}
