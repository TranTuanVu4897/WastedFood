package com.example.wastedfoodteam.utils.service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SellerVolley {
    private Context context;
    private RequestQueue requestQueue;
    private String url;

    public SellerVolley(Context context, String url) {
        this.context = context;
        this.url = url;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    /**
     * get seller information
     */
    public void setRequestGetSeller(final SellerResponseCallback callback, final String id) {
        StringRequest getBuyerRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonSellers = new JSONArray(response);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    //TODO check if done
                    Seller seller = gson.fromJson(jsonSellers.getString(0), Seller.class);
                    //Variable.seller = seller;
                    callback.onSuccess(seller);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (!id.isEmpty())
                    params.put("id", id);
                else
                    params = super.getParams();
                return params;
            }

        };
        requestQueue.add(getBuyerRequest);
    }


}

