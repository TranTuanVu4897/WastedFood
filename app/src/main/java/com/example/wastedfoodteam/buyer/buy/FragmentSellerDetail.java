package com.example.wastedfoodteam.buyer.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentSellerDetail extends ListFragment {
    String urlGetData;
    ArrayList<Product> arrProduct;
    ProductAdapter adapter;
    ListView lvProduction;
    FragmentDetailProduct detailProduct;
    Bundle bundleDetail;
    Seller seller;
    TextView tvNameSeller, tvAddress, tvDescription;
    ImageView ivPhotoSeller;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_detail, container, false);
        mapping(view);
        //set up url volley
//        urlGetData = Variable.ipAddress + Variable.SEARCH_PRODUCT;


        seller = (Seller) getArguments().get("SELLER");

        tvNameSeller.setText(seller.getName());
        tvAddress.setText(seller.getAddress());
        tvDescription.setText(seller.getDescription());


//        getData();
        return view;
    }
    private void mapping(View view){
        tvNameSeller = view.findViewById(R.id.tvNameSellerFSD);
        tvAddress = view.findViewById(R.id.tvAddressFSD);
        tvDescription = view.findViewById(R.id.tvDescriptionFSD);
        ivPhotoSeller = view.findViewById(R.id.ivPhotoSellerFSD);
    }
//    public void getData() {
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        //TODO
//        //require edit latitude
//        urlGetData = urlGetData + "?lat=" + Variable.gps.latitude + "&lng=" + Variable.gps.longitude;
//
//        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        try {
//                            JSONArray jsonProducts = new JSONArray(response);
//                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                            for (int i = 0; i < jsonProducts.length(); i++) {
//                                arrProduct.add((Product) gson.fromJson(jsonProducts.getString(i),Product.class));
//                                adapter.notifyDataSetChanged();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
//        requestQueue.add(getProductAround);
//    }
}
