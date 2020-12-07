package com.example.wastedfoodteam.seller.order;

import android.os.Bundle;

import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.product.EditProductSellerFragment;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderConfirmAdapter;
import com.example.wastedfoodteam.seller.sellerAdapter.OrderPaymentAdapter;
import com.example.wastedfoodteam.seller.sellerAdapter.SellerOrder;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductOrderSellerFragment extends ListFragment {

    ListView lvOrderConfirm,lvOrderPayment,lvOrderDone;
    ArrayList<SellerOrder> arrOrder,arrOrderPayment,arrOrderDone;
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
        lvOrderPayment = view.findViewById(android.R.id.list);
        lvOrderDone = view.findViewById(R.id.lv_list_product_3);
        imageView = view.findViewById(R.id.iv_list_order_product_picture);
        editProduct = view.findViewById(R.id.btn_editProduct_edit);
        cancelProduct = view.findViewById(R.id.btn_editProduct_stop);
        tvPaymentAlert = view.findViewById(R.id.tv_order_detail_seller_payment);
        tvDoneAlert = view.findViewById(R.id.tv_order_detail_seller_done);
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditProductSellerFragment editProductSellerFragment = new EditProductSellerFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace( R.id.content_main, editProductSellerFragment, "")//TODO check if this work
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
        CommonFunction.setImageViewSrc(getContext(),product.getImage(),imageView);
        arrOrderPayment = new ArrayList<>();
        arrOrderDone = new ArrayList<>();
        orderPaymentAdapter = new OrderPaymentAdapter(getActivity().getApplicationContext(), R.layout.list_seller_payment_order, arrOrderPayment, getResources(),getActivity());
        orderDoneAdapter = new OrderDoneAdapter(getActivity().getApplicationContext(), R.layout.list_seller_done_order, arrOrderDone, getResources(),getActivity());
        lvOrderPayment.setAdapter(orderPaymentAdapter);
        lvOrderDone.setAdapter(orderDoneAdapter);
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
            protected Map<String, String> getParams() {
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
        String urlGetData = Variable.IP_ADDRESS + "seller/getListOrderSeller.php?seller_id=" + Variable.SELLER.getId() + "&product_id=" + Variable.PRODUCT.getId() + "&order_status=" + status;

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
                            if(arrOrderPayment.size()>0)
                                tvPaymentAlert.setVisibility(View.INVISIBLE);
                            if (arrOrderDone.size()>0)
                                tvDoneAlert.setVisibility(View.INVISIBLE);
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