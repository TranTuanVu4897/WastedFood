package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderConfirmAdapter;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderDoneAdapter;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderPaymentAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class OrderDetailSellerFragment extends Fragment {

    ListView lvOrderConfirm,lvOrderPayment,lvOrderDone;
    ArrayList<Order> arrOrder,arrOrderPayment,arrOrderDone;
    String urlGetData;
    OrderConfirmAdapter orderAdapter;
    OrderPaymentAdapter orderPaymentAdapter;
    OrderDoneAdapter orderDoneAdapter;
    ImageView imageView;
    Product product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail_seller, container, false);
        lvOrderConfirm = view.findViewById(android.R.id.list);
        lvOrderPayment = view.findViewById(R.id.lv_list_product_2);
        lvOrderDone = view.findViewById(R.id.lv_list_product_3);
        imageView = view.findViewById(R.id.iv_list_order_product_picture);
        product = Variable.PRODUCT;
        Glide.with(view.getContext()).load(product.getImage().isEmpty() ? "https://i.pinimg.com/originals/95/ee/86/95ee8696f8ed1abb3767928c4d0daf65.jpg" : product.getImage()).into(imageView);
        arrOrder = new ArrayList<Order>();
        arrOrderPayment = new ArrayList<Order>();
        arrOrderDone = new ArrayList<Order>();
        orderAdapter = new OrderConfirmAdapter(getActivity().getApplicationContext(), R.layout.list_seller_confirm_order, arrOrder, getResources());
        orderPaymentAdapter = new OrderPaymentAdapter(getActivity().getApplicationContext(), R.layout.list_seller_payment_order, arrOrderPayment, getResources());
        orderDoneAdapter = new OrderDoneAdapter(getActivity().getApplicationContext(), R.layout.list_seller_done_order, arrOrderDone, getResources());
        lvOrderConfirm.setAdapter(orderAdapter);
        lvOrderPayment.setAdapter(orderPaymentAdapter);
        lvOrderDone.setAdapter(orderDoneAdapter);
        setListViewHeightBasedOnItems(lvOrderDone);
        getData("'wait for confirm'");
        getData("'wait for payment'");
        getData("'done'");
        return view;
    }

    public void getData(final String status) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        urlGetData = Variable.IP_ADDRESS + "seller/getListOrderSeller.php?seller_id=" + Variable.SELLER.getId() + "&product_id=" + Variable.PRODUCT.getId() + "&order_status=" + status;

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonOrders = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonOrders.length(); i++) {
                                if(status.equals("'wait for confirm'")){
                                    arrOrder.add((Order) gson.fromJson(jsonOrders.getString(i), Order.class));
                                    orderAdapter.notifyDataSetChanged();
                                }else if (status.equals("'wait for payment'")){
                                    arrOrderPayment.add((Order) gson.fromJson(jsonOrders.getString(i), Order.class));
                                    orderPaymentAdapter.notifyDataSetChanged();
                                }else if(status.equals("'done'")){
                                    arrOrderDone.add((Order) gson.fromJson(jsonOrders.getString(i), Order.class));
                                    orderDoneAdapter.notifyDataSetChanged();
                                }

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

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
}