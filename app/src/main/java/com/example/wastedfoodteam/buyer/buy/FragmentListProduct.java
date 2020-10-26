package com.example.wastedfoodteam.buyer.buy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.source.model.Product;
import com.example.wastedfoodteam.source.model.Seller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FragmentListProduct extends ListFragment {
    String urlGetData;
    ArrayList<Product> arrProduct;
    ProductAdapter adapter;
    ListView lvProduction;
    FragmentDetailProduct detailProduct;
    Bundle bundleDetail;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);
        //set up url volley
        urlGetData = Variable.ipAddress + Variable.searchNormal;

        //mapping view
        lvProduction = view.findViewById(android.R.id.list);

        //setup bundle
        bundleDetail = new Bundle();

        //set up list display
        arrProduct = new ArrayList<>();
        adapter = new ProductAdapter(getActivity().getApplicationContext(), R.layout.list_product_item, arrProduct,getResources());
        lvProduction.setAdapter(adapter);
        getData();
        return view;
    }


    public void getData() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //TODO
        //require edit latitude
        urlGetData = urlGetData + "?lat=" + Variable.gps.latitude + "&lng=" + Variable.gps.longitude;

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            JSONArray jsonProducts = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonProducts.length(); i++) {
//                                arrProduct.add(new Product(object.getInt("Id"),
//                                        object.getInt("SellerId"),
//                                        object.getString("Name"),
//                                        object.getString("Image"),
//                                        dateFormat.parse(object.getString("StartTime")),
//                                        dateFormat.parse(object.getString("EndTime")),
//                                        object.getDouble("OriginalPrice"),
//                                        object.getDouble("SellPrice"),
//                                        object.getInt("OriginalQuantity"),
//                                        object.getInt("RemainQuantity"),
//                                        object.getString("Description"),
//                                        dateFormat.parse(object.getString("SellDate")),
//                                        object.getString("Status"),
//                                        false));
                                arrProduct.add((Product) gson.fromJson(jsonProducts.getString(i),Product.class));
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(getProductAround);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Product product = (Product) l.getAdapter().getItem(position);
        getSeller(product.getSeller_id());

        //put bundle
        bundleDetail.putSerializable("PRODUCT", product);
        detailProduct = new FragmentDetailProduct();
        detailProduct.setArguments(bundleDetail);


        //open detail product fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.flSearchResultAH, detailProduct, "")//TODO check if this work
                .addToBackStack(null)
                .commit();
    }

    /**
     * get a seller by Id
     *
     * @param id
     */
    private void getSeller(int id) {
        urlGetData = Variable.ipAddress + "getSellerById.php?id=" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonSellers = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

//                                Seller seller = new Seller(jsonSellers.getInt("AccountId"),
//                                        jsonSellers.getString("Name"),
//                                        jsonSellers.getString("Image"),
//                                        jsonSellers.getString("Address"),
//                                        jsonSellers.getDouble("Latitude"),
//                                        jsonSellers.getDouble("Longitude"),
//                                        jsonSellers.getString("Description"), null);
                            //TODO check if done
                            Seller seller = gson.fromJson(jsonSellers.getString(0), Seller.class);
                            Variable.seller = seller;
                            bundleDetail.putSerializable("SELLER", seller);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(getSellerRequestString);
    }
}
