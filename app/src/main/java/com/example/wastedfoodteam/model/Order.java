package com.example.wastedfoodteam.model;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.sql.Date;

/**
 * Order class
 * author Vutt
 */
public class Order implements Serializable {
    int id;
    int buyer_id;
    int product_id;
    int quantity;
    Status status;
    double total_cost;
    int buyer_rating;
    String buyer_comment;
    Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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


    public Order() {
    }

    public Order(int id, int buyer_id, int product_id, int quantity, Status status, double total_cost, int buyer_rating, String buyer_comment, Date modified_date) {
        this.id = id;
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
        this.buyer_rating = buyer_rating;
        this.buyer_comment = buyer_comment;
    }

    public Order(int buyer_id, int product_id, int quantity, Status status, double total_cost) {
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
    }

    public Order(int id, int buyer_id, int product_id, int quantity, Status status, double total_cost, int buyer_rating, String buyer_comment, Product product) {
        this.id = id;
        this.buyer_id = buyer_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.status = status;
        this.total_cost = total_cost;
        this.buyer_rating = buyer_rating;
        this.buyer_comment = buyer_comment;
        this.product = product;
    }

    public enum Status {
        BUYING(0),
        SUCCESS(1),
        CANCEL(2);

        private final int key;

        Status(int key) {
            this.key = key;
        }

        public int getKey() {
            return this.key;
        }

        @Nullable
        public static Status fromKey(int key) {
            for (Status type : Status.values()) {
                if (type.getKey() == key) {
                    return type;
                }
            }
            return null;
        }
    }
}
