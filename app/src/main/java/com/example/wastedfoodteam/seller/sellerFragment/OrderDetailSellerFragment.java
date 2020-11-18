package com.example.wastedfoodteam.seller.sellerFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.wastedfoodteam.seller.sellerAdapter.OrderAdapter;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderConfirmAdapter;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderDoneAdapter;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderPaymentAdapter;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailSellerFragment extends Fragment {

    ListView lvOrderConfirm,lvOrderPayment,lvOrderDone;
    ArrayList<Order> arrOrder,arrOrderPayment,arrOrderDone;
    Button editProduct,cancelProduct;
    OrderConfirmAdapter orderAdapter;
    OrderPaymentAdapter orderPaymentAdapter;
    OrderDoneAdapter orderDoneAdapter;
    ImageView imageView;
    Product product;
    TextView tvConfirmAlert,tvPaymentAlert,tvDoneAlert;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_detail_seller, container, false);
        lvOrderConfirm = view.findViewById(android.R.id.list);
        lvOrderPayment = view.findViewById(R.id.lv_list_product_2);
        lvOrderDone = view.findViewById(R.id.lv_list_product_3);
        imageView = view.findViewById(R.id.iv_list_order_product_picture);
        editProduct = view.findViewById(R.id.btn_editProduct_edit);
        cancelProduct = view.findViewById(R.id.btn_editProduct_stop);
        tvConfirmAlert = view.findViewById(R.id.tv_order_detail_seller_confirm);
        tvPaymentAlert = view.findViewById(R.id.tv_order_detail_seller_payment);
        tvDoneAlert = view.findViewById(R.id.tv_order_detail_seller_done);
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellerDetailProductFragment sellerDetailProductFragment = new SellerDetailProductFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace( R.id.content_main, sellerDetailProductFragment , "")//TODO check if this work
                        .addToBackStack(null)
                        .commit();
            }
        });
        cancelProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelProduct.getText().equals("NGỪNG BÁN")){
                    updateProductStatus(Variable.IP_ADDRESS + "seller/setActiveForProduct.php","stop",product.getId());
                    cancelProduct.setText("MỞ LẠI BÁN");
                }else {
                    updateProductStatus(Variable.IP_ADDRESS + "seller/setActiveForProduct.php","selling",product.getId());
                    cancelProduct.setText("NGỪNG BÁN");
                }
            }
        });
        product = Variable.PRODUCT;
        Glide.with(view.getContext()).load(product.getImage().isEmpty() ? "https://i.pinimg.com/originals/95/ee/86/95ee8696f8ed1abb3767928c4d0daf65.jpg" : product.getImage()).into(imageView);
        arrOrder = new ArrayList<Order>();
        arrOrderPayment = new ArrayList<Order>();
        arrOrderDone = new ArrayList<Order>();
        orderAdapter = new OrderConfirmAdapter(getActivity().getApplicationContext(), R.layout.list_seller_confirm_order, arrOrder, getResources(),getActivity());
        orderPaymentAdapter = new OrderPaymentAdapter(getActivity().getApplicationContext(), R.layout.list_seller_payment_order, arrOrderPayment, getResources(),getActivity());
        orderDoneAdapter = new OrderDoneAdapter(getActivity().getApplicationContext(), R.layout.list_seller_done_order, arrOrderDone, getResources(),getActivity());
        lvOrderPayment.setAdapter(orderPaymentAdapter);
        lvOrderDone.setAdapter(orderDoneAdapter);
        lvOrderConfirm.setAdapter(orderAdapter);
        //setListViewHeightBasedOnItems(lvOrderDone);
        //("'wait for confirm'");
        //getData("'wait for payment'");
        //getData("'done'");


        getData(Order.Status.BUYING);
        getData(Order.Status.SUCCESS);
        return view;
    }

    //update product status
    public void updateProductStatus(String url, final String status , final int id){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            Toast.makeText(getContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            //TODO move back to home
                        }else{
                            Toast.makeText( getContext(),"Lỗi cập nhật",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("seller_id", String.valueOf(product.getSeller_id()));
                params.put("status",  status );
                params.put("id" ,  String.valueOf(id) );
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    public void getData(final Order.Status status) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String urlGetData = Variable.ipAddress + "seller/getListOrderSeller.php?seller_id=" + Variable.SELLER.getId() + "&product_id=" + Variable.PRODUCT.getId() + "&order_status=" + status;

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonOrders = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                            for (int i = 0; i < jsonOrders.length(); i++) {
                                switch (status) {
                                    case BUYING:
                                        arrOrderPayment.add(gson.fromJson(jsonOrders.getString(i), SellerOrder.class));
                                        orderPaymentAdapter.notifyDataSetChanged();
                                        break;
                                    case SUCCESS:
                                        arrOrderDone.add(gson.fromJson(jsonOrders.getString(i), SellerOrder.class));
                                        orderDoneAdapter.notifyDataSetChanged();
                                        break;
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