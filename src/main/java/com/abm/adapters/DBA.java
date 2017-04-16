package com.abm.adapters;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by br33 on 10.11.2016.
 */
public class DBA extends Adapter {
    public static DBA ourInstance = new DBA();

    public static DBA getInstance() {
        return ourInstance;
    }

    private DBA() {
        super("sqlite.properties");
    }

    public boolean addNewRow(String tableName, ArrayList<String[]> rowData) {
        boolean returnedValue = true;

        try {
            String strInsert = "INSERT INTO `" + tableName + "` (";

            for (String[] param : rowData) {
                String colName = param[0];

                strInsert += "`" + colName + "`";
                if (rowData.get(rowData.size() - 1)[0] != colName) //if not last column
                    strInsert += ", ";
            }

            strInsert += ") VALUES (";

            for (String[] param : rowData) {
                String value = param[1];

                strInsert += "?";
                if (rowData.get(rowData.size() - 1)[1] != value) //if not last column
                    strInsert += ", ";
            }

            strInsert += ");";

            if (super.debug == "1")
                System.out.println("Zapytanie: " + strInsert);

            PreparedStatement s = this.dbConnection.prepareStatement(strInsert);

            int i = 1;
            for (String[] param : rowData) {
                String value = param[1];
                s.setString(i, value);

                i++;
            }

            returnedValue = !(s.execute());
        } catch (SQLException e) {
            if (super.debug == "1")
                e.printStackTrace();
            returnedValue = false;
        } catch (Exception e) {
            if (super.debug == "1")
                e.printStackTrace();
            System.out.println("Nie prawidłowe dane wejściowe [addNewRow(...)]");
            returnedValue = false;
        }

        return returnedValue;
    }

    public boolean delete(String strDelete) {
        boolean returnedValue = false;

        PreparedStatement s = null;
        try {
            s = this.dbConnection.prepareStatement(strDelete);
            returnedValue = !s.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnedValue;
    }

    public boolean deleteAll(String table) {
        return this.delete("DELETE FROM `" + table + "` WHERE 1;");
    }

    public boolean updateRow(String tableName, ArrayList<String[]> rowData, String[] identifier) {
        boolean returnedValue = true;

        try {
            String strInsert = "UPDATE `" + tableName + "` SET ";

            for (String[] param : rowData) {
                String colName = param[0];

                strInsert += "`" + colName + "` = ?";
                if (rowData.get(rowData.size() - 1)[0] != colName) //if not last column
                    strInsert += ", ";
            }

            strInsert += " WHERE `" + identifier[0] + "` = ?;";

            PreparedStatement s = this.dbConnection.prepareStatement(strInsert);

            int i = 1;
            for (String[] param : rowData) {
                String value = param[1];
                s.setString(i, value);

                i++;
            }
            s.setString(i, identifier[1]);

            returnedValue = !(s.execute());
        } catch (SQLException e) {

            e.printStackTrace();
            returnedValue = false;
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Nie prawidłowe dane wejściowe [updateRow(...)]");
            returnedValue = false;
        }

        return returnedValue;
    }


}
