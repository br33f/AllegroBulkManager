package com.abm.models;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by br33 on 01.02.2017.
 */
public class ConnectionParameter {
    private int id;
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private SimpleStringProperty apiKey = new SimpleStringProperty();

    public ConnectionParameter(int id, String username, String password, String apiKey) {
        this.id = id;
        this.username.set(username);
        this.password.set(password);
        this.apiKey.set(apiKey);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getApiKey() {
        return apiKey.get();
    }

    public void setApiKey(String apiKey) {
        this.apiKey.set(apiKey);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Login: " + this.username.getValue() + ", Klucz API: " + this.apiKey.getValue() ;
    }
}
