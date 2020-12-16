package com.example.wastedfoodteam.seller.product;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;

import java.util.HashMap;
import java.util.Map;

public class EditProductSellerFragment extends Fragment {
    private EditText name;
    private EditText originalPrice;
    private EditText sellPrice;
    private EditText openTime,closeTime,quantity,remainQuantity;
    private String storageLocation;
    int id;

    CameraStorageFunction cameraStorageFunction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_seller_detail_product, container, false);
        //unit ui
        //ui view
        ImageView iv_detail_product_icon = view.findViewById(R.id.iv_detail_product_icon);
        name = view.findViewById(R.id.editText_detail_product_name);
        originalPrice = view.findViewById(R.id.editText_detail_product_originalPrice);
        sellPrice = view.findViewById(R.id.editText_detail_product_sellPrice);
        openTime = view.findViewById(R.id.editText_detail_product_openTime);
        closeTime = view.findViewById(R.id.editText_detail_product_closeTime);
        quantity = view.findViewById(R.id.etQuantity);
        remainQuantity = view.findViewById(R.id.etRemainQuantity);

        Button btn_detail_product_add = view.findViewById(R.id.btn_detail_product_add);

        //input data
        id = Variable.PRODUCT.getId();
        CommonFunction.setImageViewSrc(getContext(),Variable.PRODUCT.getImage(), iv_detail_product_icon);
        name.setText(Variable.PRODUCT.getName());
        originalPrice.setText(String.valueOf(Variable.PRODUCT.getOriginal_price()));
        sellPrice.setText(String.valueOf(Variable.PRODUCT.getSell_price()));
        openTime.setText(String.valueOf(Variable.PRODUCT.getStart_time()));
        closeTime.setText(String.valueOf(Variable.PRODUCT.getEnd_time()));
        quantity.setText(String.valueOf(Variable.PRODUCT.getOriginal_quantity()));
        remainQuantity.setText(String.valueOf(Variable.PRODUCT.getRemain_quantity()));
        closeTime.setEnabled(false);
        openTime.setEnabled(false);
        quantity.setEnabled(false);
        originalPrice.setEnabled(false);
        sellPrice.setEnabled(false);


        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), iv_detail_product_icon);
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
                if(cameraStorageFunction.getImage_uri() != null)
                {
                    cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                        @Override
                        public void onSuccess(String url) {
                            storageLocation = url;
                            String urlGetData = Variable.IP_ADDRESS + "seller/updateProductByID.php";
                            updateProduct(urlGetData);
                        }
                    });
                }else {
                    storageLocation = " ";
                    String urlGetData = Variable.IP_ADDRESS + "seller/updateProductByID.php";
                    updateProduct(urlGetData);
                }
            }
        });
        return view;
    }

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.i("SellerDetailProductFragment", "handle image pick");
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    //update product data
    private void updateProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Successfully update")) {
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                            //TODO move back to home
                        } else {
                            Toast.makeText(getActivity(), "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("seller_id", String.valueOf(Variable.SELLER.getId()));
                params.put("name", name.getText().toString().trim());
                params.put("originalPrice", originalPrice.getText().toString().trim());
                params.put("sellPrice", sellPrice.getText().toString().trim());
                params.put("openTime", openTime.getText().toString().trim());
                params.put("closeTime", closeTime.getText().toString().trim());
                params.put("remainQuantity",remainQuantity.getText().toString().trim());
                params.put("image",storageLocation);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}