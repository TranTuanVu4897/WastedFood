package com.example.wastedfoodteam.global;

import com.example.wastedfoodteam.model.Seller;
import com.google.android.gms.maps.model.LatLng;

public class Variable {
    public static final String ipAddress ="http://192.168.3.4/wastedfoodphp/";//Vutt ip
//    public static final String ipAddress ="http://10.22.178.239/wastedfoodphp/";//FPT University ip
//    public static final String ipAddress ="http://192.168.1.46/wastedfoodphp/";//TungPT ip

    //TODO
    //other php variable
    public static final String searchNormal = "search/getListProducts.php";


    public static final String INSERT_NEW_ORDER = "order/order.php";


    //Order status constraint
    public static final String ORDER_STATUS_ORDERING = "ORDERING";

    public static LatLng gps;
    public static Seller seller;
}
