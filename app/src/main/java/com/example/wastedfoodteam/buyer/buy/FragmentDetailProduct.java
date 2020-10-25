package com.example.wastedfoodteam.buyer.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.source.model.Buyer;
import com.example.wastedfoodteam.source.model.Product;
import com.example.wastedfoodteam.source.model.Seller;
import com.example.wastedfoodteam.utils.DownloadImageTask;
import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.Map;

public class FragmentDetailProduct extends Fragment {
    private GoogleMap mMap;
    private Product product;
    private Seller seller;
    private Buyer buyer;
    private String url;
    private int orderQuantity;
    ImageView ivProduct;
    TextView tvPriceDiscount, tvOpenTime, tvPriceOriginal, tvDirect, tvDescription, tvBuyQuantity, tvQuantity;
    Button btnIncreate, btnDecreate, btnBuy;
    private final static int MY_PERMISSIONS_REQUEST = 32;

    public FragmentDetailProduct() {
        orderQuantity = 1;
    }

    public FragmentDetailProduct(Product product, Seller seller) {
        orderQuantity = 1;
        this.product = product;
        this.seller = seller;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        //mapping view
        mappingViewWithVariable(view);

        //set url
        url = Variable.ipAddress + Variable.INSERT_NEW_ORDER;

        //get bundle values
        Bundle bundle = getActivity().getIntent().getExtras();
        buyer = (Buyer) bundle.get("BUYER");
        seller = (Seller) bundle.get("SELLER");
        product = (Product) bundle.get("PRODUCT");


        //set content for views
        tvQuantity.setText("Còn: " + product.getRemain_quantity() + "/" + product.getOriginal_quantity());
        tvPriceDiscount.setText(product.getSell_price() + "");
        tvPriceOriginal.setText(product.getOriginal_price() + "");
        tvOpenTime.setText("Mở cửa từ: " + product.getStart_time() + " - " + product.getEnd_time());//TODO edit format date
        tvDescription.setText(product.getDescription());
        tvDirect.setText(seller.getAddress());
        tvBuyQuantity.setText(orderQuantity + "");


        //set image from url
        new DownloadImageTask(ivProduct).execute(product.getImage());

        //set event
        btnDecreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderQuantity > 0) {
                    orderQuantity--;
                    tvBuyQuantity.setText(orderQuantity + "");
                }
            }
        });

        btnIncreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderQuantity < product.getRemain_quantity()) {
                    orderQuantity++;
                    tvBuyQuantity.setText(orderQuantity + "");
                }
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBuyOnClick();
            }
        });
        return view;
    }

    /**
     * buy
     */
    private void btnBuyOnClick() {
        RequestQueue requestInsertOrder = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequestInsert = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equalsIgnoreCase("SUCCESS")){
                    Toast.makeText(getActivity().getApplicationContext(),"Thành công",Toast.LENGTH_LONG);
                    //TODO move to order detail screen
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Có lỗi bất thường xảy ra",Toast.LENGTH_LONG);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),"Lỗi hệ thống: " + error.getMessage(),Toast.LENGTH_LONG);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("buyer", buyer.getAccount_id() + "");
                params.put("product", product.getId() + "");
                params.put("quantity", orderQuantity + "");
                params.put("status", Variable.ORDER_STATUS_ORDERING);
                params.put("total_cost", (orderQuantity * product.getSell_price()) +"");
                return params;
            }
        };
        requestInsertOrder.add(stringRequestInsert);
    }


    /**
     * Set value for view variable
     *
     * @param view
     */
    private void mappingViewWithVariable(View view) {
        ivProduct = view.findViewById(R.id.ivProduct);

        tvQuantity = view.findViewById(R.id.tvQuantity);
        tvPriceDiscount = view.findViewById(R.id.tvPriceDiscount);
        tvPriceOriginal = view.findViewById(R.id.tvPriceOriginal);
        tvDirect = view.findViewById(R.id.tvDirect);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvBuyQuantity = view.findViewById(R.id.tvBuyQuantity);
        tvOpenTime = view.findViewById(R.id.tvOpenTime);

        btnIncreate = view.findViewById(R.id.btnIncreate);
        btnDecreate = view.findViewById(R.id.btnDecreate);
        btnBuy = view.findViewById(R.id.btnBuy);

    }


}
