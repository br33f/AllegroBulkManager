package com.abm.models;

import javafx.beans.property.*;

/**
 * Created by br33 on 03.02.2017.
 */
public class Product {
    private LongProperty id;
    private StringProperty name;
    private FloatProperty price;
    private StringProperty category;
    private StringProperty description;
    private StringProperty image;


    public Product() {
    }

    public Product(long id, String name, float price, String category, String description, String image) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleFloatProperty(price);
        this.category = new SimpleStringProperty(category);
        this.description = new SimpleStringProperty(description);
        this.image = new SimpleStringProperty(image);
    }

    public long getId() {
        return id.get();
    }

    public LongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public float getPrice() {
        return price.get();
    }

    public FloatProperty priceProperty() {
        return price;
    }

    public void setPrice(float price) {
        this.price.set(price);
    }

    public String getCategory() {
        return category.get();
    }

    public StringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getImage() {
        return image.get();
    }

    public StringProperty imageProperty() {
        return image;
    }

    public void setImage(String image) {
        this.image.set(image);
    }
}