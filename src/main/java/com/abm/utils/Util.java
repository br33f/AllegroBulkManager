package com.abm.utils;

import javafx.scene.control.Alert;

import java.util.ArrayList;

/**
 * Created by br33 on 02.02.2017.
 */
public class Util {
    public Util() {
    }

    public static void alert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(title);
        alert.setContentText(msg);

        alert.showAndWait();
    }

    public static long[] convert(ArrayList<Long> list) {
        Long[] arrayObject = list.toArray(new Long[0]);

        long[] arrayPrimitive = new long[arrayObject.length];
        for(int i = 0; i < arrayObject.length; i++) {
            arrayPrimitive[i] = arrayObject[i];
        }

        return arrayPrimitive;
    }

}
