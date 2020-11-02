package com.example.wastedfoodteam.global;

import com.example.wastedfoodteam.model.Account;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.model.Seller;
import com.google.android.gms.maps.model.LatLng;

public class Variable {
    public static final String ipAddress = "http://192.168.3.4/wastedfoodphp/";//Vutt ip
//    public static final String ipAddress ="http://10.22.178.239/wastedfoodphp/";//FPT University ip
//   public static final String ipAddress ="http://192.168.1.20/wastedfoodphp/";//TungPT ip
    //public static final String ipAddress ="http://192.168.1.10/wastedfoodphp/";//DucHC ip

        // 192.168.1.46

    //TODO
    //other php variable
    public static final String  SEARCH_PRODUCT = "search/getListProducts.php";

    public static final String INSERT_NEW_ORDER = "order/order.php";

    public static final String ADD_PRODUCT_SELLER = "seller/SellerCreateProduct.php";
    public static final String GET_SELLER_BY_ID = "getSellerById.php";

    //Order status constraint
    public static final String ORDER_STATUS_ORDERING = "ORDERING";
    public static final String GET_FOLLOW = "follow/getFollow.php";
    public static final String UPDATE_FOLLOW = "follow/updateFollow.php";

    public static LatLng gps;
    public static Account ACCOUNT;

    public static Seller seller;
    public static Buyer buyer;
    public static int ACCOUNT_ID;

    public static int CHECK_LOGIN;
}
