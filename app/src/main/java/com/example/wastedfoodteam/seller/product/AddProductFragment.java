package com.example.wastedfoodteam.seller.product;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.home.SellerHomeFragment;
import com.example.wastedfoodteam.seller.order.ProductOrderSellerFragment;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.LoadingDialog;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddProductFragment extends Fragment {

    private int seller_id;
    private String storage_location;


    private EditText etProductName,
            etOriginalPrice, etSellPrice,
            etOpenTime, etCloseTime,
            etDescription, etQuantity;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //for time picker
    private int mHour, mMinute, mSecond, day, month, year;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    CameraStorageFunction cameraStorageFunction;
    LoadingDialog loadingDialog;

    final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get seller id from seller home activity
        seller_id = Variable.SELLER.getId();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_add_product, container, false);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        loadingDialog = new LoadingDialog(getActivity());
        //init ui view
        //ui view
        ImageView ivProduct = view.findViewById(R.id.ivProduct);
        etProductName = view.findViewById(R.id.etProductName);
        etOriginalPrice = view.findViewById(R.id.etOriginalPrice);
        etSellPrice = view.findViewById(R.id.etSellPrice);
        etOpenTime = view.findViewById(R.id.etOpenTime);
        etCloseTime = view.findViewById(R.id.etCloseTime);
        etDescription = view.findViewById(R.id.etDescription);
        etQuantity = view.findViewById(R.id.etQuantity);
        final Button btnAddProductAdd = view.findViewById(R.id.btnAddProductAdd);


        //Date picker handle
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(), ivProduct);

        etOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("DefaultLocale")
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                etOpenTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        ivProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
//                showImagePickDialog();
            }
        });


        etCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        etCloseTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        btnAddProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.startLoadingDialog();
                cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                    @Override
                    public void onSuccess(String url) {
                        storage_location = url;
                        String urlGetData = Variable.IP_ADDRESS + Variable.ADD_PRODUCT_SELLER;
                        addProduct(urlGetData);
                    }
                });
            }
        });
        return view;
    }


    //
    private void addProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Succesfully update")) {
                            loadingDialog.dismissDialog();
                            SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                            //open seller detail product fragment
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.content_main, sellerHomeFragment, "")
                                    .addToBackStack(null)
                                    .commit();
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("seller_id", String.valueOf(seller_id));
                params.put("name", etProductName.getText().toString());
                params.put("image", storage_location);
                params.put("start_time", CommonFunction.getCurrentDate() + " " + etOpenTime.getText().toString());
                params.put("end_time", CommonFunction.getCurrentDate() + " " + etCloseTime.getText().toString());
                params.put("original_price", etOriginalPrice.getText().toString());
                params.put("sell_price", etSellPrice.getText().toString());
                params.put("original_quantity", etQuantity.getText().toString());
                params.put("remain_quantity", etQuantity.getText().toString());
                params.put("description", etDescription.getText().toString());
                params.put("status", Product.ProductStatus.SELLING + "");
                params.put("sell_date", CommonFunction.getCurrentDate() + " " + etOpenTime.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}