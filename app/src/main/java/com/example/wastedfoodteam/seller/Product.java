package com.example.wastedfoodteam.seller;

public class Product {
    private String productName;
    private String productPrice;
    private String productDiscount;

    public Product() {
    }

    public Product(String productName, String productPrice, String productDiscount) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        this.productDiscount = productDiscount;
    }

    @Override
    public String toString() {
        return productName + " " + productPrice + " " + productDiscount ;
    }
}
