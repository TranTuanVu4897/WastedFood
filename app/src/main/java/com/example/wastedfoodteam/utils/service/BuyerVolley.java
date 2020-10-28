package com.example.wastedfoodteam.utils.service;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.model.Buyer;

public class BuyerVolley {
    private Context context;
    private RequestQueue requestQueue;
    private String url;
    private Buyer buyer;


    public BuyerVolley(Context context, String url) {
        this.context = context;
        this.url = url;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void setRequestGetBuyer() {
        StringRequest getBuyerRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(getBuyerRequest);
    }
}
