package com.example.wastedfoodteam.model;

import java.io.Serializable;
import java.sql.Date;

/**
 * Order class
 * author Vutt
 */
public class Order implements Serializable {
    int id;
    int buyer_id;
    String buyerName;
    String nameProduct;
    int product_id;
    int quantity;
    String status;
    double total_cost;
    int buyer_rating;
    String buyer_comment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

    public int getBuyer_rating() {
        return buyer_rating;
    }

    public void setBuyer_rating(int buyer_rating) {
        this.buyer_rating = buyer_rating;
    }

    public String getBuyer_comment() {
        return buyer_comment;
    }

    public void setBuyer_comment(String buyer_comment) {
        this.buyer_comment = buyer_comment;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public Order() {
    }

    public Order(int id, int buyer_id, int product_id, int quantity, String status, double total_cost, int buyer_rating, String buyer_comment, Date modified_date) {
        this.id = id;
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
        this.buyer_rating = buyer_rating;
        this.buyer_comment = buyer_comment;
    }

    public Order(int id, int buyer_id, String buyerName, String nameProduct, int product_id, int quantity, String status, double total_cost, int buyer_rating, String buyer_comment) {
        this.id = id;
        this.buyer_id = buyer_id;
        this.buyerName = buyerName;
        this.nameProduct = nameProduct;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
        this.buyer_rating = buyer_rating;
        this.buyer_comment = buyer_comment;
    }

    public Order(int buyer_id, int product_id, int quantity, String status, double total_cost) {
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
    }
}
