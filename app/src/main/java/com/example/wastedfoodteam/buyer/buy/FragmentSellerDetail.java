package com.example.wastedfoodteam.buyer.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.FragmentListSellerFollow;
import com.example.wastedfoodteam.buyer.FragmentReport;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;

import java.util.ArrayList;

public class FragmentSellerDetail extends Fragment {
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
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bundle = new Bundle();
                bundle.putSerializable("SELLER", seller);
                report = new FragmentReport();
                report.setArguments(bundleDetail);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flSearchResultAH, report, "")//TODO check if this work
                        .addToBackStack(null)
                        .commit();
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

//    public void getData() {
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//        //TODO
//        //require edit latitude
//        urlGetData = urlGetData + "?lat=" + Variable.gps.latitude + "&lng=" + Variable.gps.longitude;
//
//        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        try {
//                            JSONArray jsonProducts = new JSONArray(response);
//                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                            for (int i = 0; i < jsonProducts.length(); i++) {
//                                arrProduct.add((Product) gson.fromJson(jsonProducts.getString(i),Product.class));
//                                adapter.notifyDataSetChanged();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                    }
//                });
//        requestQueue.add(getProductAround);
//    }
}
