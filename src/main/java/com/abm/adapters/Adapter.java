package com.abm.adapters;

import java.io.*;
import java.sql.*;
import java.util.Properties;

/**
 * Created by br33 on 11.04.2016.
 */
public class Adapter {
    //attributes
    protected String host, user, password, prefix, debug;
    protected Connection dbConnection = null;

    //methods
    public Adapter(String file) {
        this.readConfig(file);
        this.connect();
    }

    /**
     * Getter for dbConnection
     *
     * @return this.dbConnection - current connection established based on configuration file
     */
    public Connection getDbConnection() {
        return this.dbConnection;
    }

    /**
     * Function returns string made from Adapter's attributes (read from configuration file)
     *
     * @return String - host, dbName, user, password
     */
    public String toString() {
        return "host: " + this.host + "\nuser: " + this.user + "\npassword: " + this.password;
    }

    /**
     * Function returns ResultSet from mysql query string
     *
     * @param str - mysql query string
     * @return ResultSet - result from mysql query
     */
    public ResultSet getResultSet(String str) {
        ResultSet returnedValue = null;

        try {
            Statement s = this.dbConnection.createStatement();
            returnedValue = s.executeQuery(str);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnedValue;
    }

    /**
     * Function reads file and returns Properties object to be further processed.
     *
     * @param file - name of a configuration file
     * @return Properties - Properties object read from file
     */
    protected Properties loadConfig(String file) {
        Properties p = new Properties();
        try {
            File jarPath=new File(Adapter.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            String propertiesPath=jarPath.getParentFile().getAbsolutePath();
            System.out.println(propertiesPath + "\\config\\" + file);
            InputStream in = new FileInputStream(propertiesPath + "\\config\\" + file);
            p.load(in);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * Function executes loadConfig function and then sets Adapters attributes according to Properties.
     *
     * @param file - name of a configuration file
     */
    private void readConfig(String file) {
        Properties p = this.loadConfig(file);

        this.host = (p.getProperty("host") != null) ? p.getProperty("host") : "notDefined";
        this.user =  p.getProperty("user");
        this.password = p.getProperty("password");
        this.prefix = p.getProperty("prefix");
        this.debug = p.getProperty("debug");
    }

    /**
     * Function establishes a connection based on data previously read from configuration file.
     * If connection was not established with success it throws an Expection becouse connections with databases are
     * vital for this application.
     */
    protected void connect() {
        String url = this.host;
        try {
            if(this.user == null)
                this.dbConnection = DriverManager.getConnection(url);
            else
                this.dbConnection = DriverManager.getConnection(url, this.user, this.password);
            System.out.println("Connected to: " + url);
        } catch (SQLException e) {
        	e.printStackTrace();
            throw new IllegalStateException("\nCouldn't connect to: " + url + "\n(user: " + this.user + " password:" + this.password + ")");
        }
    }

    public void disconnect() {
        try {
            this.dbConnection.close();
            System.out.println("Disconnected");
            this.dbConnection = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}