package com.example.wastedfoodteam.buyer.followseller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.example.wastedfoodteam.buyer.buy.BuyerProduct;
import com.example.wastedfoodteam.buyer.buy.FragmentDetailProduct;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.ReportDialog;
import com.example.wastedfoodteam.utils.service.FollowResponseCallback;
import com.example.wastedfoodteam.utils.service.FollowVolley;
import com.example.wastedfoodteam.utils.service.SellerExtraVolley;
import com.example.wastedfoodteam.utils.service.SellerResponseCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentSellerDetail extends ListFragment {
    String urlGetData;
    ArrayList<BuyerProduct> arrProduct;
    ProductAdapterOfSeller adapter;
    FragmentDetailProduct detailProduct;
    Bundle bundleDetail;
    Seller seller;
    TextView tvNameSeller, tvAddress, tvDescription, tvRatingFSD, tvFollowFSD, tvProductFSD;
    ImageView ivPhotoSeller, ibFollow;
    ListView lvProduction;
    ImageButton ibReport;
    CameraStorageFunction cameraStorageFunction;
    private FollowVolley followVolley;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_get_seller_detail, container, false);
        mapping(view);
        //set up url volley


        seller = (Seller) getArguments().get("SELLER");

        tvNameSeller.setText(seller.getName() + "");
        tvAddress.setText(seller.getAddress() + "");
        tvDescription.setText(seller.getDescription() + "");

        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), null);

        CommonFunction.setImageViewSrc(getActivity().getApplicationContext(), seller.getImage(), ivPhotoSeller);
        ibReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportDialog reportDialog = new ReportDialog(getActivity(), getLayoutInflater(), seller, cameraStorageFunction);
                reportDialog.displayReportDialog();
            }
        });

        //list product
        urlGetData = Variable.IP_ADDRESS + "search/getListProductsOfSeller.php?seller_id=" + seller.getId()
                + "&lat=" + Variable.gps.getLatitude()
                + "&lng=" + Variable.gps.getLongitude();

        followVolley = new FollowVolley(getActivity().getApplicationContext());
        String GET_FOLLOW_INFORMATION_URL = Variable.IP_ADDRESS + Variable.GET_FOLLOW;
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
        }, GET_FOLLOW_INFORMATION_URL, Variable.BUYER.getId(), seller.getId());


        //mapping view
        lvProduction = view.findViewById(android.R.id.list);

        //setup bundle
        bundleDetail = new Bundle();
        arrProduct = new ArrayList<>();
        adapter = new ProductAdapterOfSeller(getActivity().getApplicationContext(), R.layout.list_seller_product_item, arrProduct, getResources());
        lvProduction.setAdapter(adapter);
        getListProduct(urlGetData);
        getSellerExtraInfo();

        lvProduction.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return true;
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

        return view;
    }

    private void getSellerExtraInfo() {
        SellerExtraVolley sellerExtraVolley = new SellerExtraVolley(getActivity(), Variable.IP_ADDRESS + "/getSellerExtraInfo.php");
        sellerExtraVolley.setRequestGetSeller(new SellerResponseCallback() {
            @Override
            public void onSuccess(SellerExtraInfo seller) {
                tvFollowFSD.setText("Số người theo dõi: " + seller.follow_total);
                tvProductFSD.setText("Số sản phẩm đã bán: " + seller.product_total);
            }
        }, seller.getId() + "");
    }

    private void mapping(View view) {
        tvNameSeller = view.findViewById(R.id.tvNameSellerFSD);
        tvAddress = view.findViewById(R.id.tvAddressFSD);
        tvDescription = view.findViewById(R.id.tvDescriptionFSD);
        tvRatingFSD = view.findViewById(R.id.tvRatingFSD);
        tvFollowFSD = view.findViewById(R.id.tvFollowFSD);
        tvProductFSD = view.findViewById(R.id.tvProductFSD);
        ivPhotoSeller = view.findViewById(R.id.ivPhotoSellerFSD);
        ibReport = view.findViewById(R.id.ibReport);
        ibFollow = view.findViewById(R.id.iBtnFollow);
    }

    public void getListProduct(String urlGetData) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonProducts = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonProducts.length(); i++) {
                                arrProduct.add((BuyerProduct) gson.fromJson(jsonProducts.getString(i), BuyerProduct.class));
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("ResponseString", response);
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
        BuyerProduct product = (BuyerProduct) l.getAdapter().getItem(position);


        //put bundle
        bundleDetail.putSerializable("PRODUCT", product);
        detailProduct = new FragmentDetailProduct();
        detailProduct.setArguments(bundleDetail);


        //open detail product fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, detailProduct, "")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPause() {
        boolean isFollow = false;
        if (ibFollow.getTag().equals(R.drawable.followed)) isFollow = true;
        String UPDATE_FOLLOW_URL = Variable.IP_ADDRESS + Variable.UPDATE_FOLLOW;
        followVolley.setRequestUpdateFollow(new FollowResponseCallback() {
            @Override
            public void onSuccess(String result) {

            }
        }, UPDATE_FOLLOW_URL, Variable.BUYER.getId(), seller.getId(), isFollow);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
    }
}
