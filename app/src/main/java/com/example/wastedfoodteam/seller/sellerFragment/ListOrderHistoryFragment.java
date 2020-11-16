package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ListOrderHistoryFragment extends ListFragment {
    ListView lvOrder;
    ArrayList<Order> arrOrder;
    String urlGetData;
    OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_order_history, container, false);
        lvOrder = view.findViewById(android.R.id.list);
        arrOrder = new ArrayList<>();
        orderAdapter = new OrderAdapter(getActivity().getApplicationContext(), R.layout.list_seller_order, arrOrder, getResources());
        lvOrder.setAdapter(orderAdapter);
        getData();
        return view;
    }

    public void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        urlGetData = Variable.IP_ADDRESS + "seller/getListOrderSeller.php?seller_id=" + Variable.SELLER.getId();
        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonOrders = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonOrders.length(); i++) {
                                Order order = (Order) gson.fromJson(jsonOrders.getString(i), Order.class);
                                arrOrder.add((Order) gson.fromJson(jsonOrders.getString(i), Order.class));
                                orderAdapter.notifyDataSetChanged();
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

}