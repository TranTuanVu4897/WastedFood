package com.example.wastedfoodteam.utils.service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.model.Seller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class FollowVolley {
    private Context context;
    private RequestQueue requestQueue;

    public FollowVolley(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);

    }

    public void setRequestGetFollow(final FollowResponseCallback callback, String url, final int buyer, final  int seller){
        url = url + "?buyer_id=" + buyer + "&seller_id=" +seller;
        StringRequest getFollowRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    callback.onSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(getFollowRequest);
    }

    public void setRequestUpdateFollow(final FollowResponseCallback callback,String url, final int buyer, final  int seller, final boolean isFollow){
        StringRequest updateFollowRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("buyer_id",buyer + "");
                params.put("seller_id",seller+"");
                params.put("is_follow",isFollow + "");
                return params;
            }
        };
        requestQueue.add(updateFollowRequest);
    }
}
