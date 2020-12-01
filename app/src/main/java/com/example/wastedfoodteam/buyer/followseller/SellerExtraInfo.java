package com.example.wastedfoodteam.buyer.followseller;

import com.example.wastedfoodteam.model.Seller;

import java.sql.Date;

public class SellerExtraInfo {
    String follow_total;
    String product_total;

    public SellerExtraInfo(String follow_total, String product_total) {
        this.follow_total = follow_total;
        this.product_total = product_total;
    }
}
