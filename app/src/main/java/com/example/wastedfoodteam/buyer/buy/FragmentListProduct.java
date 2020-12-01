package com.example.wastedfoodteam.buyer.buy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.utils.FilterDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.ArrayList;

public class FragmentListProduct extends ListFragment {
    String urlGetData;
    ArrayList<BuyerProduct> arrProduct;
    ProductAdapter adapter;
    ListView lvProduction;
    FragmentDetailProduct detailProduct;
    Bundle bundleDetail;
    ImageButton ibFilter;
    EditText etSearch;
    Button btnNear, btnAll, btnFollowSeller;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buyer_list_product, container, false);
        Log.i("FragmentListProduct", "Show the list view");
        //set up url volley
        urlGetData = Variable.IP_ADDRESS + Variable.SEARCH_PRODUCT;

        //mapping view
        lvProduction = view.findViewById(android.R.id.list);
        etSearch = view.findViewById(R.id.etSearchBHA);
        ibFilter = view.findViewById(R.id.ibFilter);
        btnNear = view.findViewById(R.id.btnNearProduct);
        btnAll = view.findViewById(R.id.btnAllProduct);
        btnFollowSeller = view.findViewById(R.id.btnFollowSellerProduct);
        getBuyerData();
        //setup bundle
        bundleDetail = new Bundle();

        setUpArrayProduct();

        adapter = new ProductAdapter(getActivity().getApplicationContext(), R.layout.list_buyer_product_item, arrProduct, getResources());
        lvProduction.setAdapter(adapter);
        getProduct();

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


        ibFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filterDialog = new FilterDialog(getLayoutInflater(), getActivity());
                filterDialog.showFilterDialog(new FilterDialog.ModifyFilter() {
                    @Override
                    public void onClear() {
                        Variable.startTime = null;
                        Variable.endTime = null;
                        Variable.distance = "20";
                        Variable.discount = null;

                        createNewArrayProduct();
                        getProduct();
                    }

                    @Override
                    public void onChange() {
                        createNewArrayProduct();
                        getProduct();
                    }
                });
            }
        });

        btnNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Variable.distance = "20";
                createNewArrayProduct();
                getProduct();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Variable.distance = "";
                createNewArrayProduct();
                getProduct();
            }
        });

        btnFollowSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewArrayProduct();
                getSellerFollowProduct();

            }
        });

        return view;
    }

    private void getBuyerData() {
        final String url = Variable.IP_ADDRESS + "information/informationBuyerFirebase.php?account_id=" + Variable.ACCOUNT_ID;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();
                try {
                    JSONArray object = new JSONArray(response);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    Buyer buyer = new Buyer();
                    buyer = gson.fromJson(object.getString(0), Buyer.class);
                    Variable.BUYER = buyer;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi kết nỗi" + url, Toast.LENGTH_LONG).show();

            }
        }
        );
        requestQueue.add(stringRequest);
    }


    public void getProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //require edit latitude
        urlGetData = Variable.IP_ADDRESS + Variable.SEARCH_PRODUCT + "?lat=" + Variable.gps.getLatitude() + "&lng=" + Variable.gps.getLongitude()
                + "&distance=" + Variable.distance
                + "&start_time=" + Variable.startTime + "&end_time=" + Variable.endTime
                + "&discount=" + Variable.discount
                + "&search_text=" + etSearch.getText();

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonProducts = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonProducts.length(); i++) {
                                arrProduct.add(gson.fromJson(jsonProducts.getString(i), BuyerProduct.class));
                                adapter.setProductList(arrProduct);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.e("ListProduct", response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG);
                    }
                });
        requestQueue.add(getProductAround);
    }

    public void getSellerFollowProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        //require edit latitude
        urlGetData = Variable.IP_ADDRESS + Variable.SEARCH_SELLER_FOLLOW_PRODUCT + "?lat=" + Variable.gps.getLatitude() + "&lng=" + Variable.gps.getLongitude()
                + "&distance=" + Variable.distance
                + "&start_time=" + Variable.startTime + "&end_time=" + Variable.endTime
                + "&discount=" + Variable.discount
                + "&search_text=" + etSearch.getText()
                + "&buyer_id=" +Variable.ACCOUNT_ID;

        StringRequest getProductAround = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonProducts = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            for (int i = 0; i < jsonProducts.length(); i++) {
                                arrProduct.add(gson.fromJson(jsonProducts.getString(i), BuyerProduct.class));
                                adapter.setProductList(arrProduct);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (Exception e) {
                            Log.e("ListProduct", response);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG);
                    }
                });
        requestQueue.add(getProductAround);
    }


    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        BuyerProduct product = (BuyerProduct) l.getAdapter().getItem(position);
        Log.i("FragmentListProduct", "On item clicked");

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

    public void createNewArrayProduct() {
        arrProduct = new ArrayList<>();
        if (adapter != null) {
            adapter.getProductList().clear();
            adapter.setProductList(arrProduct);
            adapter.notifyDataSetChanged();
        }
    }

    private void setUpArrayProduct() {
        if (arrProduct == null) createNewArrayProduct();
    }


}
