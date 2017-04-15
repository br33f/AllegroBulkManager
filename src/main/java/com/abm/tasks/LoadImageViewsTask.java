package com.abm.tasks;

import com.abm.models.Product;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * Created by br33 on 03.02.2017.
 */
public class LoadImageViewsTask extends Task<Integer> {
    private ObservableList<Product> productsList;

    public LoadImageViewsTask(ObservableList<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    protected Integer call() throws Exception {
        updateProgress(0, productsList.size());

        int i = 0;
        for (Product product : productsList) {
            try {
                product.buildImageView();
            } catch (Exception e) {
                return 1;
            }
            updateProgress(++i, productsList.size());
        }

        return 0;
    }
}
