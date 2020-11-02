package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.sellerAdapter.ProductSellerAdapter;
import com.example.wastedfoodteam.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ListProductSellerFragment extends Fragment {

    ArrayList<Product> arrProduct;
    ProductSellerAdapter adapter;
    ArrayAdapter<Product> arrayAdapter;
    ListView lvProduct;
    int seller_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_product_seller, container, false);
        //mapping view
        lvProduct = view.findViewById(R.id.listProduct);
        Bundle bundle = getArguments();
        arrProduct = new ArrayList<Product>();
        seller_id = Variable.ACCOUNT_ID;
        String urlGetData = Variable.ipAddress + "seller/getListProductSeller.php?seller_id=" + seller_id;
        adapter = new ProductSellerAdapter( getActivity().getApplicationContext(), R.layout.list_seller_product , arrProduct, getResources());
        lvProduct.setAdapter(adapter);
        getData(urlGetData);
        return view;
    }

    public void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                arrProduct.add(new Product(object.getInt("Id"),
                                        object.getInt("SellerId"),
                                        object.getString("Name"),
                                        object.getString("Image"),
                                        dateFormat.parse(object.getString("StartTime")),
                                        dateFormat.parse(object.getString("EndTime")),
                                        object.getDouble("OriginalPrice"),
                                        object.getDouble("SellPrice"),
                                        object.getInt("OriginalQuantity"),
                                        object.getInt("RemainQuantity"),
                                        object.getString("Description"),
                                        dateFormat.parse(object.getString("SellDate")),
                                        object.getString("Status"),
                                        false));
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}