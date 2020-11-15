package com.example.wastedfoodteam.buyer.order;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentOrderHistory extends ListFragment {

    String urlGetData;
    ArrayList<Order> orderArrayList;
    OrderAdapter adapter;
    ListView lvOrder;
    FragmentOrderDetail orderDetail;
    Bundle bundleDetail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_order_history, container, false);

        //set up url volley

        urlGetData = Variable.ipAddress + Variable.ORDER_HISTORY + "?buyer_id=" + Variable.ACCOUNT_ID;

        //mapping view
        lvOrder = view.findViewById(android.R.id.list);

        //setup bundle
        bundleDetail = new Bundle();

        //set up list display
        orderArrayList = new ArrayList<>();
        adapter = new OrderAdapter(getActivity().getApplicationContext(), R.layout.list_buyer_product_item, orderArrayList, getResources());
        lvOrder.setAdapter(adapter);
        getData();


        return view;
    }

    private void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            JSONArray jsonOrders = new JSONArray(response);
                            GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss");
                            Gson gson = gsonBuilder.create();


                            for (int i = 0; i < jsonOrders.length(); i++) {
                                orderArrayList.add((Order) gson.fromJson(jsonOrders.getString(i), Order.class));
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
        Order order = (Order) l.getAdapter().getItem(position);


        //put bundle
        bundleDetail.putSerializable("PRODUCT", order);
        orderDetail = new FragmentOrderDetail(order);//TODO check again
        orderDetail.setArguments(bundleDetail);


        //open detail order fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, orderDetail, "")//TODO check if this work
                .addToBackStack(null)
                .commit();
    }
}
