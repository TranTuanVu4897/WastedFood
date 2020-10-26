package com.example.wastedfoodteam.global;

import com.example.wastedfoodteam.model.Seller;
import com.google.android.gms.maps.model.LatLng;

public class Variable {
    public static final String ipAddress ="http://192.168.1.253/wastedfoodphp/";//Vutt ip
//    public static final String ipAddress ="http://10.22.178.239/wastedfoodphp/";//FPT University ip
//   public static final String ipAddress ="http://192.168.1.46/wastedfoodphp/";//TungPT ip
    //public static final String ipAddress ="http://192.168.1.10/wastedfoodphp/";//DucHC ip

    //TODO
    //other php variable
    public static final String SEARCH_PRODUCT = "search/getListProducts.php";

    public static final String INSERT_NEW_ORDER = "order/order.php";

    //Order status constraint
    public static final String ORDER_STATUS_ORDERING = "ORDERING";
    
    public static final String noImageUrl ="https://vanhoadoanhnghiepvn.vn/wp-content/uploads/2020/08/112815953-stock-vector-no-image-available-icon-flat-vector.jpg";

    public static LatLng gps;
    public static Seller seller;
}
