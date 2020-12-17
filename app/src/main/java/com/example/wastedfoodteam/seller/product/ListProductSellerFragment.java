package com.example.wastedfoodteam.seller.product;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.order.ProductOrderSellerFragment;
import com.example.wastedfoodteam.utils.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class ListProductSellerFragment extends ListFragment {

    ArrayList<Product> arrProduct;
    ProductSellerAdapter adapter;
    Product product;
    ListView lvProduct;
    TextView tv_total_product;
    LoadingDialog loadingDialog;
    int seller_id;
    int totalProduct;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("ListProductSellerFragment", "Show the list view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_product_seller, container, false);
        //mapping view
        lvProduct = view.findViewById(android.R.id.list);
        arrProduct = new ArrayList<>();
        seller_id = Variable.SELLER.getId();
        product = Variable.PRODUCT;
        loadingDialog = new LoadingDialog(getActivity());
        tv_total_product = view.findViewById(R.id.tv_total_product);
        String urlGetData = Variable.IP_ADDRESS + "seller/getListProductSeller.php?seller_id=" + seller_id;
        adapter = new ProductSellerAdapter(getActivity().getApplicationContext(), R.layout.list_seller_product, arrProduct, getResources(),getActivity());
        lvProduct.setAdapter(adapter);
        getData(urlGetData);
        getTotalProduct(Variable.IP_ADDRESS + "seller/getTotalProduct.php" + "?seller_id=" + Variable.SELLER.getId());
        lvProduct.setOnTouchListener(new View.OnTouchListener() {
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
        return view;
    }


    public void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                                        Product.ProductStatus.valueOf(object.getString("Status")),
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

    public void getTotalProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getProductAround = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        try {
                            totalProduct = Integer.parseInt(response);
                            tv_total_product.setText(totalProduct + " sản phẩm");
                        } catch (Exception e) {
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
        super.onListItemClick(l, v, position, id);
        Log.i("ListProductSellerFragment", "On item clicked");
        Variable.PRODUCT = (Product) l.getAdapter().getItem(position);

        ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
        //open seller detail product fragment
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_main, productOrderSellerFragment, "")
                .addToBackStack(null)
                .commit();
    }
}