package com.example.wastedfoodteam.model;

import java.io.Serializable;

/**
 * Order class
 * author Vutt
 */
public class Order implements Serializable {
    int id;
    int buyer_id;
    int product_id;
    int quantity;
    OrderStatus orderStatus;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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


    public Order() {
    }


    public Order(int buyer_id, int product_id, int quantity, OrderStatus orderStatus, double total_cost) {
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.total_cost = total_cost;
    }

    public Order(int id, int buyer_id, int product_id, int quantity, OrderStatus orderStatus, double total_cost, int buyer_rating, String buyer_comment) {
        this.id = id;
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.orderStatus = orderStatus;
        this.total_cost = total_cost;
        this.buyer_rating = buyer_rating;
        this.buyer_comment = buyer_comment;
    }

    public enum OrderStatus {
        BUYING,
        SUCCESS,
        CANCEL,

    }
}
