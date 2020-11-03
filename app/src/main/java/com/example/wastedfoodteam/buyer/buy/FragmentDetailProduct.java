package com.example.wastedfoodteam.buyer.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.order.FragmentOrderDetail;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.service.FollowResponseCallback;
import com.example.wastedfoodteam.utils.service.FollowVolley;
import com.example.wastedfoodteam.utils.service.SellerResponseCallback;
import com.example.wastedfoodteam.utils.service.SellerVolley;
import com.google.android.gms.maps.GoogleMap;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentDetailProduct extends Fragment {
    private GoogleMap mMap;
    private Product product;
    private Seller seller;
    private Buyer buyer;
    private String updateOrderUrl;
    private String getSellerUrl;
    private String getFollowUrl;
    private String updateFollowUrl;
    private int orderQuantity;
    ImageButton ibFollow;
    ImageView ivProduct;
    CircleImageView civSeller;
    TextView tvPriceDiscount,
            tvOpenTime, tvPriceOriginal,
            tvDirect, tvDescription,
            tvBuyQuantity, tvQuantity;
    Button btnIncreate, btnDecreate, btnBuy;
    private FollowVolley followVolley;

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
        View view = inflater.inflate(R.layout.fragment_buyer_product_detail, container, false);
        //mapping view
        mappingViewWithVariable(view);


        //set url
        updateOrderUrl = Variable.ipAddress + Variable.INSERT_NEW_ORDER;
        getSellerUrl = Variable.ipAddress + Variable.GET_SELLER_BY_ID;
        getFollowUrl = Variable.ipAddress + Variable.GET_FOLLOW;
        updateFollowUrl = Variable.ipAddress + Variable.UPDATE_FOLLOW;

        //get bundle values
        Bundle bundle = getActivity().getIntent().getExtras();
        buyer = (Buyer) getArguments().get("BUYER");
        product = (Product) getArguments().get("PRODUCT");

        //set content for views about seller
        SellerVolley sellerVolley = new SellerVolley(getActivity().getApplicationContext(), getSellerUrl);
        sellerVolley.setRequestGetSeller(new SellerResponseCallback() {
                                             @Override
                                             public void onSuccess(Seller seller) {
                                                 CommonFunction.setImageViewSrc(getActivity().getApplicationContext(), seller.getImage(), civSeller);
                                                 Variable.SELLER = seller;
                                             }
                                         },
                product.getSeller_id() + "");

        //set content for views about product
        tvQuantity.setText("Còn: " + product.getRemain_quantity() + "/" + product.getOriginal_quantity());
        tvPriceDiscount.setText(product.getSell_price() + "");
        tvPriceOriginal.setText(product.getOriginal_price() + "");
        tvOpenTime.setText("Mở cửa từ: " + product.getStart_time() + " - " + product.getEnd_time());//TODO edit format date
        tvDescription.setText(product.getDescription());
        tvBuyQuantity.setText(orderQuantity + "");

        //Set button follow
        followVolley = new FollowVolley(getActivity().getApplicationContext());
        followVolley.setRequestGetFollow(new FollowResponseCallback() {
            @Override
            public void onSuccess(String result) {

                if (result.equalsIgnoreCase("TRUE")) {
                    ibFollow.setImageResource(R.drawable.followed);
                    ibFollow.setTag(R.drawable.followed);
                } else {
                    ibFollow.setTag(R.drawable.not_followed);
                    ibFollow.setImageResource(R.drawable.not_followed);
                }

            }
        }, getFollowUrl, Variable.ACCOUNT_ID, product.getSeller_id());

        //set image from url
        Glide.with(getActivity().getApplicationContext()).load(product.getImage()).into(ivProduct);


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


        ibFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ibFollow.getTag() != null)
                    if (ibFollow.getTag().equals(R.drawable.followed)) {
                        ibFollow.setImageResource(R.drawable.not_followed);
                        ibFollow.setTag(R.drawable.not_followed);
                    } else {
                        ibFollow.setImageResource(R.drawable.followed);
                        ibFollow.setTag(R.drawable.followed);
                    }
            }
        });

        civSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentSellerDetail fragmentSellerDetail = new FragmentSellerDetail();

                //put product to next screen
                Bundle bundle = new Bundle();
                bundle.putSerializable("PRODUCT", Variable.SELLER);
                fragmentSellerDetail.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flSearchResultAH, fragmentSellerDetail, "")//TODO check if this work
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    /**
     * buy
     */
    private void btnBuyOnClick() {
        RequestQueue requestInsertOrder = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequestInsert = new StringRequest(Request.Method.POST, updateOrderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Thành công", Toast.LENGTH_LONG);
                    //TODO move to order detail screen
                    moveToFragmentOrderDetail();
                } else if (response.equalsIgnoreCase("NOT ENOUGH QUANTITY")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Số lượng còn lại không đủ", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Có lỗi bất thường xảy ra", Toast.LENGTH_LONG);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Lỗi hệ thống: " + error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("buyer", Variable.ACCOUNT_ID + "");//TODO change to other ways
                params.put("product", product.getId() + "");
                params.put("quantity", orderQuantity + "");
                params.put("status", Variable.ORDER_STATUS_ORDERING);
                params.put("total_cost", (orderQuantity * product.getSell_price()) + "");
                return params;
            }
        };
        requestInsertOrder.add(stringRequestInsert);
    }

    /**
     * Open after buy
     */
    private void moveToFragmentOrderDetail() {
        FragmentOrderDetail fragmentOrderDetail = new FragmentOrderDetail(orderQuantity);

        //put product to next screen
        Bundle bundle = new Bundle();
        bundle.putSerializable("PRODUCT", product);
        fragmentOrderDetail.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, fragmentOrderDetail, "")//TODO check if this work
                .addToBackStack(null)
                .commit();

    }


    /**
     * Set value for view variable
     *
     * @param view
     */
    private void mappingViewWithVariable(View view) {
        ivProduct = view.findViewById(R.id.ivProduct);

        civSeller = view.findViewById(R.id.civSeller);

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
        ibFollow = view.findViewById(R.id.iBtnFollow);
    }

    @Override
    public void onPause() {
        boolean isFollow = false;
        if (ibFollow.getTag().equals(R.drawable.followed)) isFollow = true;
        followVolley.setRequestUpdateFollow(new FollowResponseCallback() {
            @Override
            public void onSuccess(String result) {

            }
        }, updateFollowUrl, Variable.ACCOUNT_ID, product.getSeller_id(), isFollow);
        super.onPause();
    }
}
