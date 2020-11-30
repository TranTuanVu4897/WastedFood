package com.example.wastedfoodteam.buyer.followseller;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.BuyHomeActivity;
import com.example.wastedfoodteam.buyer.buy.BuyerProduct;
import com.example.wastedfoodteam.buyer.buy.FragmentDetailProduct;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.service.FollowResponseCallback;
import com.example.wastedfoodteam.utils.service.FollowVolley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentSellerDetail extends ListFragment {
    String urlGetData;
    ArrayList<BuyerProduct> arrProduct;
    ProductAdapterOfSeller adapter;
    FragmentDetailProduct detailProduct;
    Bundle bundleDetail;
    Seller seller;
    TextView tvNameSeller, tvAddress, tvDescription;
    ImageView ivPhotoSeller, ibFollow, ivReport;
    ListView lvProduction;
    String content = "";
    String url;
    String accusedId;
    String reporterId;
    TextView tvAccused;
    EditText etContent;
    Button btnCommit, btnCancel, btnReport;
    CameraStorageFunction cameraStorageFunction;
    private final String GET_FOLLOW_INFORMATION_URL = Variable.IP_ADDRESS + Variable.GET_FOLLOW;
    private final String UPDATE_FOLLOW_URL = Variable.IP_ADDRESS + Variable.UPDATE_FOLLOW;
    private FollowVolley followVolley;

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


        Glide.with(getActivity()).load(seller.getImage()).into(ivPhotoSeller);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogReport();
            }
        });

        //list product
        urlGetData = Variable.IP_ADDRESS + "search/getListProductsOfSeller.php?seller_id=" + seller.getId()
                + "&lat=" + Variable.gps.getLatitude()
                + "&lng=" + Variable.gps.getLongitude();

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
        }, GET_FOLLOW_INFORMATION_URL, Variable.ACCOUNT_ID, seller.getId());


        //mapping view
        lvProduction = view.findViewById(android.R.id.list);

        //setup bundle
        bundleDetail = new Bundle();
        arrProduct = new ArrayList<>();
        adapter = new ProductAdapterOfSeller(getActivity().getApplicationContext(), R.layout.list_seller_product_item, arrProduct, getResources());
        lvProduction.setAdapter(adapter);
        getData(urlGetData);

        lvProduction.setOnTouchListener(new View.OnTouchListener() {
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

    private void mapping(View view) {
        tvNameSeller = view.findViewById(R.id.tvNameSellerFSD);
        tvAddress = view.findViewById(R.id.tvAddressFSD);
        tvDescription = view.findViewById(R.id.tvDescriptionFSD);
        ivPhotoSeller = view.findViewById(R.id.ivPhotoSellerFSD);
        btnReport = view.findViewById(R.id.btnReportFSD);
        ibFollow = view.findViewById(R.id.iBtnFollow);
    }

    public void getData(String urlGetData) {

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

    private void dialogReport() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_report);
        //mapping
        tvAccused = dialog.findViewById(R.id.tvAccusedDR);
        etContent = dialog.findViewById(R.id.etContentDR);
        btnCommit = dialog.findViewById(R.id.btnCommitDR);
        btnCancel = dialog.findViewById(R.id.btnCancelDR);
        ivReport = dialog.findViewById(R.id.ivReport);

        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), ivReport);

        tvAccused.setText(seller.getName());

        //set param
        url = Variable.IP_ADDRESS + "FeedbackReport/report.php";
        reporterId = Variable.ACCOUNT_ID + "";
        accusedId = seller.getId() + "";
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etContent.getText().toString();
                //TODO don't know get url image
                insertData(url, reporterId, accusedId, content, "");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        ivReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
            }
        });

        dialog.show();
    }

    private void insertData(final String url, final String reporter_id, final String accused_id, final String report_text, final String report_image) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                switch (response) {
                    case "ERROR":
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "OK Insert data", Toast.LENGTH_LONG).show();
                        try {

                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "ERROR " + url, Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reporter_id", reporter_id);
                params.put("accused_id", accused_id);
                params.put("report_text", report_text);
                params.put("report_image", report_image);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onPause() {
        boolean isFollow = false;
        if (ibFollow.getTag().equals(R.drawable.followed)) isFollow = true;
        followVolley.setRequestUpdateFollow(new FollowResponseCallback() {
            @Override
            public void onSuccess(String result) {

            }
        }, UPDATE_FOLLOW_URL, Variable.ACCOUNT_ID, seller.getId(), isFollow);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
    }
}
