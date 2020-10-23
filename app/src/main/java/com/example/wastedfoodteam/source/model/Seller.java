package com.example.wastedfoodteam.source.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Seller class
 * author Vutt
 */
public class Seller implements Serializable {
    int account_id;
    String name;
    String password;
    String image;
    String address;
    double latitude;
    double longitude;
    String description;

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Seller() {
    }

    public Seller(int account_id, String name, String image, String address, double latitude, double longitude, String description, Date modified_date) {
        this.account_id = account_id;
        this.name = name;
        this.image = image;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

    //TODO temporary
    public Seller(int account_id, String name, String password , String image, String address, double latitude, double longitude, String description  ) {
        this.account_id = account_id;
        this.name = name;
        this.image = image;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.password = password;
    }
}
