package com.example.wastedfoodteam.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buy.detailproduct.FragmentDetailProduct;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.source.model.Product;
import com.example.wastedfoodteam.source.model.Seller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class FragmentListProduct extends ListFragment {
    String urlGetData;
    ArrayList<Product> arrProduct;
    ProductAdapter adapter;
    ListView lvProduction;
    Seller seller;
    Button btn;
    FragmentDetailProduct detailProduct;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);
        urlGetData = Variable.ipAddress + Variable.searchNormal;


        lvProduction = view.findViewById(android.R.id.list);
        arrProduct = new ArrayList<>();
        adapter = new ProductAdapter(getActivity().getApplicationContext(), R.layout.list_product_item, arrProduct);
        lvProduction.setAdapter(adapter);
        getData();
        return view;
    }


    public void getData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        urlGetData = urlGetData + "?lat=" + Variable.gps.latitude + "&lng=" + Variable.gps.longitude;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetData, null,
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
                                        false, null));
                            } catch (JSONException | ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT);

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Product product = (Product)l.getAdapter().getItem(position);
        getSeller(product.getSeller_id());
        detailProduct = new FragmentDetailProduct(product);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.flSearchResultAH, detailProduct, "")
                .addToBackStack(null)
                .commit();
    }

    private void getSeller(int id) {
        urlGetData = Variable.ipAddress +"getSellerById.php?id=" +  id;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, urlGetData, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Variable.seller = new Seller(object.getInt("AccountId"),
                                        object.getString("Name"),
                                        object.getString("Image"),
                                        object.getString("Address"),
                                        object.getDouble("Latitude"),
                                        object.getDouble("Longitude"),
                                        object.getString("Description"),null);
                            } catch (JSONException  e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT);

                    }
                });
        requestQueue.add(jsonArrayRequest);
    }
}
