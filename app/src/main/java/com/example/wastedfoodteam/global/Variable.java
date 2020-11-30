package com.example.wastedfoodteam.global;

import android.location.Location;

import com.example.wastedfoodteam.model.Account;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Time;


public class Variable {
    //public static final String IP_ADDRESS = "http://192.168.3.4/wastedfoodphp/";//Vutt ip
//    public static final String IP_ADDRESS = "http://10.22.178.239/wastedfoodphp/";//FPT University ip
    //public static final String IP_ADDRESS = "http://192.168.1.253/wastedfoodphp/";//TungPT ip
//     public static final String IP_ADDRESS = "http://192.168.1.10/wastedfoodphp/";//DucHC ip
//      public static final String IP_ADDRESS ="http://192.168.156.2/wastedfoodphp/";//DucHC ip lan
    public static final String IP_ADDRESS ="http://192.168.1.19/wastedfoodphp/";//TungPT ip lan
//    public static final String IP_ADDRESS ="https://wasted-food-service.herokuapp.com/";//heroku server

    //other php variable
    public static final String SEARCH_PRODUCT = "search/getListProducts.php";
    public static final String SEARCH_SELLER_FOLLOW_PRODUCT = "search/getListSellerFollowProducts.php";

    public static final String INSERT_NEW_ORDER = "order/buyerOrder.php";

    public static final String ADD_PRODUCT_SELLER = "seller/SellerCreateProduct.php";
    public static final String GET_SELLER_BY_ID = "getSellerById.php";

    //Order status constraint
    public static final String GET_FOLLOW = "follow/getFollow.php";
    public static final String UPDATE_FOLLOW = "follow/updateFollow.php";
    public static Location gps;
    public static Account ACCOUNT;
    public static final String UPDATE_RATING = "order/buyerUpdateRating.php";
    public static final String ORDER_HISTORY = "order/buyerOrderHistory.php";
    public static String fireBaseUID ;
    public static Seller SELLER;
    public static Buyer buyer;
    public static Product PRODUCT;
    public static int ACCOUNT_ID;
    public static String CURRENT_USER;
    public static BottomNavigationView bottomNavigationViewSeller;

    public static int CHECK_LOGIN;
    public static int TOTAL_NOTIFICATION = 0;

    public static Seller RESISTER_SELLER;

    public static final String noImageUrl = "https://www.actbus.net/fleetwiki/images/8/84/Noimage.jpg";
    public static Time startTime = Time.valueOf("11:00:00"), endTime = Time.valueOf("22:00:00");
    public static String distance = "20", discount = "90";
}
