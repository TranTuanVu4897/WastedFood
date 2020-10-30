package com.example.wastedfoodteam.model;

import java.sql.Date;

/**
 * Buyer class
 * Author Vutt
 */
public class Buyer {
    int account_id;
    String name;
    Date date_of_birth;
    String image;
    boolean gender;

    public Buyer() {
    }

    public Buyer(int account_id,String name, Date date_of_birth, String image, boolean gender) {
        this.account_id = account_id;
        this.name = name;
        this.date_of_birth = date_of_birth;
        this.image = image;
        this.gender = gender;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

}
