package com.example.wastedfoodteam.utils.service;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.buyer.followseller.SellerExtraInfo;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;

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

    public void setRequestGetBuyerBy3rdId(final BuyerResponseCallback callback, String thirdPartyId) {
        url = url + "?thirdPartyID=" + thirdPartyId;
        StringRequest getBuyerRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray buyers = new JSONArray(response);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                    Buyer buyer = gson.fromJson(buyers.getString(0), Buyer.class);
                    callback.onSuccess(buyer);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", error.getMessage());
                callback.onError();
            }
        });
        requestQueue.add(getBuyerRequest);
    }
}
