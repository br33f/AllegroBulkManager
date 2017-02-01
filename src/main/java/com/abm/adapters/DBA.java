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

            for(String[] param : rowData) {
                String colName = param[0];

                strInsert += "`" + colName + "`";
                if(rowData.get(rowData.size() - 1)[0] != colName) //if not last column
                    strInsert += ", ";
            }

            strInsert += ") VALUES (";

            for(String[] param : rowData) {
                String value = param[1];

                strInsert += "'" + value + "'";
                if(rowData.get(rowData.size() - 1)[1] != value) //if not last column
                    strInsert += ", ";
            }

            strInsert += ");";

            if(super.debug == "1")
                System.out.println("Zapytanie: " + strInsert);

            PreparedStatement s = this.dbConnection.prepareStatement(strInsert);
            returnedValue = !(s.execute());
        } catch (SQLException e) {
            if(super.debug == "1")
                e.printStackTrace();
            returnedValue = false;
        } catch (Exception e) {
            if(super.debug == "1")
                e.printStackTrace();
            System.out.println("Nie prawidłowe dane wejściowe [addNewRow(...)]");
            returnedValue = false;
        }

        return returnedValue;
    }

    public boolean delete(String strDelete)
    {
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

    /*
    public boolean updateRow(ArrayList<String> rowData) {
        boolean returnedValue = true;

        try {
            String strUpdate = "UPDATE `zamojski_damian_lekcje_tb` SET `nazwa` = '" + rowData.get(0) + "', `dziedzina_wiedzy` = '" + rowData.get(1) + "', `rodzaj_lekcji` = '" + rowData.get(2) + "', `nauczyciel` = '" + rowData.get(3) + "', `sposob_zaliczenia` = '" + rowData.get(4) + "', `czy_odrabialna` = '" + rowData.get(5) + "' WHERE `id_lekcji` = '" + rowData.get(6) + "'";
            PreparedStatement s = this.dbConnection.prepareStatement(strUpdate);
            returnedValue = !(s.execute());
        } catch (SQLException e) {
            returnedValue = false;
        } catch (Exception e) {
            System.out.println("Nie prawidłowe dane wejściowe [updateRow(...)]");
            e.printStackTrace();
            returnedValue = false;
        }

        return returnedValue;
    }*/



}
