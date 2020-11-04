package com.example.wastedfoodteam.buyer.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.FragmentListSellerFollow;
import com.example.wastedfoodteam.buyer.FragmentReport;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FragmentSellerDetail extends ListFragment {
    String urlGetData;
    ArrayList<Product> arrProduct;
    ProductAdapter adapter;
    FragmentDetailProduct detailProduct;
    Bundle bundleDetail;
    Seller seller;
    TextView tvNameSeller, tvAddress, tvDescription;
    ImageView ivPhotoSeller;
    Button btnReport;
    Bundle bundle;
    FragmentReport report;
    ListView lvProduction;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_detail, container, false);
        mapping(view);
        //set up url volley
//        urlGetData = Variable.ipAddress + Variable.SEARCH_PRODUCT;


        seller = (Seller) getArguments().get("SELLER");

        tvNameSeller.setText(seller.getName() + "");
        tvAddress.setText(seller.getAddress() + "");
        tvDescription.setText(seller.getDescription() + "");

        Glide.with(getActivity()).load(seller.getImage()).into(ivPhotoSeller);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putSerializable("SELLER", seller);
                report = new FragmentReport();
                report.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flSearchResultAH, report, "")//TODO check if this work
                        .addToBackStack(null)
                        .commit();
            }
        });

        //list product

        urlGetData = Variable.ipAddress + "search/getListProductsOfSeller.php?seller_id=" + seller.getId();

        //mapping view
        lvProduction = view.findViewById(android.R.id.list);

        //setup bundle
        bundleDetail = new Bundle();
        arrProduct = new ArrayList<>();
        adapter = new ProductAdapter(getActivity().getApplicationContext(), R.layout.list_buyer_product_item, arrProduct, getResources());
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


//        getData();
        return view;
    }
    private void mapping(View view){
        tvNameSeller = view.findViewById(R.id.tvNameSellerFSD);
        tvAddress = view.findViewById(R.id.tvAddressFSD);
        tvDescription = view.findViewById(R.id.tvDescriptionFSD);
        ivPhotoSeller = view.findViewById(R.id.ivPhotoSellerFSD);
        btnReport = view.findViewById(R.id.btnReportFSD);
    }
    public void getData(String urlGetData) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //TODO
        //require edit latitude

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            JSONArray jsonProducts = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonProducts.length(); i++) {
                                arrProduct.add((Product) gson.fromJson(jsonProducts.getString(i), Product.class));
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
        Product product = (Product) l.getAdapter().getItem(position);


        //put bundle
        bundleDetail.putSerializable("PRODUCT", product);
        detailProduct = new FragmentDetailProduct();
        detailProduct.setArguments(bundleDetail);


        //open detail product fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, detailProduct, "")//TODO check if this work
                .addToBackStack(null)
                .commit();
    }


}
