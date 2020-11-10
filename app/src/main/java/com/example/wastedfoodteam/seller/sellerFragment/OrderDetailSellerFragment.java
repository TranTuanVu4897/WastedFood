package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.example.wastedfoodteam.seller.sellerAdapter.OrderConfirmAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class OrderDetailSellerFragment extends Fragment {

    ListView lvOrderConfirm,lvOrderPayment,lvOrderDone;
    ArrayList<Order> arrOrder;
    String urlGetData;
    OrderConfirmAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail_seller, container, false);
        lvOrderConfirm = view.findViewById(android.R.id.list);
        lvOrderPayment = view.findViewById(R.id.lv_list_product_2);
        lvOrderDone = view.findViewById(R.id.lv_list_product_3);
        arrOrder = new ArrayList<Order>();
        orderAdapter = new OrderConfirmAdapter(getActivity().getApplicationContext(), R.layout.list_seller_confirm_order, arrOrder, getResources());
        lvOrderConfirm.setAdapter(orderAdapter);
        lvOrderPayment.setAdapter(orderAdapter);
        lvOrderDone.setAdapter(orderAdapter);
        getData("'wait for confirm'");
        return view;
    }

    public void getData(String status) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        urlGetData = Variable.ipAddress + "seller/getListOrderSeller.php?seller_id=" + Variable.SELLER.getId() + "&product_id=" + Variable.PRODUCT.getId() + "&order_status=" + status;

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