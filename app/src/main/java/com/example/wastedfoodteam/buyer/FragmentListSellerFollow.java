package com.example.wastedfoodteam.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

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
import com.example.wastedfoodteam.buyer.buy.FragmentSellerDetail;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FragmentListSellerFollow extends ListFragment {
    String urlGetData;
    SellerFollowAdapter adapter;
    ArrayList<Seller> listSellers;
    ListView lvSeller;
    Bundle bundleDetail;
    FragmentSellerDetail restaurant;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_seller_follow, container, false);
        urlGetData = Variable.ipAddress + "information/getListSellerFollow.php?buyer_id=" + Variable.ACCOUNT_ID;
        lvSeller = view.findViewById(android.R.id.list);
        listSellers = new ArrayList<>();
        adapter = new SellerFollowAdapter(getActivity().getApplicationContext(), R.layout.list_seller_follow_item, listSellers, getResources());
        lvSeller.setAdapter(adapter);
        getData(urlGetData);
        return view;
    }

    public void getData(final String urlGetData) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            JSONArray jsonProducts = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonProducts.length(); i++) {
                                listSellers.add((Seller) gson.fromJson(jsonProducts.getString(i), Seller.class));
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
                        Toast.makeText(getActivity(), "lỗi kết nỗi" + urlGetData, Toast.LENGTH_LONG).show();
                    }
                });
        requestQueue.add(getProductAround);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Seller seller = (Seller) l.getAdapter().getItem(position);


        //put bundle
        bundleDetail = new Bundle();
        bundleDetail.putSerializable("SELLER", seller);
        restaurant = new FragmentSellerDetail();
        restaurant.setArguments(bundleDetail);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, restaurant, "")
                .addToBackStack(null)
                .commit();
    }
}
