package com.example.wastedfoodteam.source.model;

import java.sql.Date;

/**
 * Buyer class
 * Author Vutt
 */
public class Buyer {
    int account_id;
    Date date_of_birth;
    String image;
    boolean gender;
    Date modified_date;

    public Buyer() {
    }

    public Buyer(int account_id, Date date_of_birth, String image, boolean gender, Date modified_date) {
        this.account_id = account_id;
        this.date_of_birth = date_of_birth;
        this.image = image;
        this.gender = gender;
        this.modified_date = modified_date;
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

    public Date getModified_date() {
        return modified_date;
    }

    public void setModified_date(Date modified_date) {
        this.modified_date = modified_date;
    }
}
