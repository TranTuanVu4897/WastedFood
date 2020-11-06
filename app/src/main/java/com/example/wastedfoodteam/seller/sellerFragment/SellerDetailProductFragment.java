package com.example.wastedfoodteam.seller.sellerFragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.DownloadImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class SellerDetailProductFragment extends Fragment {
    //ui view
    private ImageView iv_detail_product_icon;
    private EditText name;
    private EditText originalPrice;
    private EditText sellPrice;
    private EditText openTime;
    private EditText closeTime;
    private EditText saleDate;
    private Button btn_detail_product_add;
    int id;

    CameraStorageFunction cameraStorageFunction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_detail_product, container, false);
        //unit ui
        iv_detail_product_icon = view.findViewById(R.id.iv_detail_product_icon);
        name = view.findViewById(R.id.editText_detail_product_name);
        originalPrice = view.findViewById(R.id.editText_detail_product_originalPrice);
        sellPrice = view.findViewById(R.id.editText_detail_product_sellPrice);
        openTime = view.findViewById(R.id.editText_detail_product_openTime);
        closeTime = view.findViewById(R.id.editText_detail_product_closeTime);
        saleDate = view.findViewById(R.id.editText_detail_product_saleDate);
        btn_detail_product_add = view.findViewById(R.id.btn_detail_product_add);

        //input data
        id = Variable.PRODUCT.getId();
        Glide.with(getContext()).load(Variable.PRODUCT.getImage().isEmpty() ? Variable.noImageUrl : Variable.PRODUCT.getImage()).into(iv_detail_product_icon);
        name.setText(Variable.PRODUCT.getName());
        originalPrice.setText(String.valueOf(Variable.PRODUCT.getOriginal_price()));
        sellPrice.setText(String.valueOf(Variable.PRODUCT.getSell_price()));
        openTime.setText(String.valueOf(Variable.PRODUCT.getStart_time()));
        closeTime.setText(String.valueOf(Variable.PRODUCT.getEnd_time()));
        saleDate.setText(String.valueOf(Variable.PRODUCT.getSell_date()));


        cameraStorageFunction = new CameraStorageFunction(getActivity(),getContext());
        //
        iv_detail_product_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
            }
        });

        btn_detail_product_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlGetData = Variable.ipAddress + "seller/updateProductByID.php";
                updateProduct(urlGetData);
            }
        });
        return view;
    }

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data ) {
        Log.i("SellerDetailProductFragment","handle image pick");
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data );
    }

    //update product data
    private void updateProduct(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Successfully update")){
                            Toast.makeText(getActivity(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            //TODO move back to home
                        }else{
                            Toast.makeText(getActivity(),"Lỗi cập nhật",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("seller_id",String.valueOf(Variable.SELLER.getId()));
                params.put("name",name.getText().toString().trim());
                params.put("originalPrice", originalPrice.getText().toString().trim());
                params.put("sellPrice",sellPrice.getText().toString().trim());
                params.put("openTime",openTime.getText().toString().trim());
                params.put("closeTime",sellPrice.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


}