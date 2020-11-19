package com.example.wastedfoodteam.seller.sellerFragment;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProductFragment extends Fragment {

    private int seller_id;
    private String storage_location;

    private CameraStorageFunction cameraStorageFunction;
    //ui view
    private ImageView ivProduct;
    private EditText etProductName,
            etOriginalPrice, etSellPrice,
            etOpenTime, etCloseTime,
            etDescription, etQuantity;
    private Button btnAddProductAdd;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission array
    private String[] cameraPermission;
    private String[] storagePermission;

    //image pick uri
    private Uri image_uri;

    //
    private StorageTask uploadTask;

    //for time picker
    private int mHour, mMinute;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    final Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //get seller id from seller home activity
        seller_id = Variable.ACCOUNT_ID;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_add_product, container, false);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //init ui view
        ivProduct = view.findViewById(R.id.ivProduct);
        etProductName = view.findViewById(R.id.etProductName);
        etOriginalPrice = view.findViewById(R.id.etOriginalPrice);
        etSellPrice = view.findViewById(R.id.etSellPrice);
        etOpenTime = view.findViewById(R.id.etOpenTime);
        etCloseTime = view.findViewById(R.id.etCloseTime);
        etDescription = view.findViewById(R.id.etDescription);
        etQuantity = view.findViewById(R.id.etQuantity);
        btnAddProductAdd = view.findViewById(R.id.btnAddProductAdd);

        //Date picker handle
//        year = calendar.get(Calendar.YEAR);
//        month = calendar.get(Calendar.MONTH);
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        editTextAddProductSaleDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        String dateString = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
//                        editTextAddProductSaleDate.setText(dateString);
//                    }
//                }, year, month, day);
//                datePickerDialog.show();
//            }
//        });
//        final Calendar calendar1 = Calendar.getInstance();

        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        mMinute = calendar.get(Calendar.MINUTE);

        //init permission arrays
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext());
        etOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
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
            }
        });


        etCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

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

                cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                    @Override
                    public void onSuccess(String url) {
                        storage_location = url;
                        String urlGetData = Variable.IP_ADDRESS + Variable.ADD_PRODUCT_SELLER;
                        addProduct(urlGetData);
                    }

                    @Override
                    public void onError() {

                    }
                });

            }
        });


        return view;
    }


    private void addProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Succesfully update")) {
                            Toast.makeText(getActivity(), "Cập nhật thành công", Toast.LENGTH_SHORT);
                            //TODO move back to home
                        } else {
                            Toast.makeText(getActivity(), "Lỗi cập nhật", Toast.LENGTH_SHORT);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AddProductFunction", error.getMessage());
                        Toast.makeText(getActivity(), "Xảy ra lỗi, vui lòng thử lại", Toast.LENGTH_SHORT);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
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


    //start of for camera handle
    private void pickFromGallery() {
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void showImagePickDialog() {
        //display in dialog
        String[] options = {"Camera", "Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if (which == 0) {
                    //camera clicked
                    if (checkCameraPermission()) {
                        //permission granted
                        pickFromCamera();
                    } else {
                        //permission not granted, request
                        requestCameraPermission();
                    }
                } else {
                    //gallery clicked
                    if (checkStoragePermission()) {
                        //permission granted
                        pickFromGallery();
                    } else {
                        //permission not granted, request
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }

    private void pickFromCamera() {
        //intent to pick image from camera

        //use media store to pick high/ori quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image_Description");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result; //return true/false
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean resultExternalStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return resultCamera && resultExternalStorage;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), cameraPermission, CAMERA_REQUEST_CODE);
    }

    //end of for camera handle

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == -1) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image pick from gallery

                //save picked image uri
                image_uri = data.getData();

                //image picked from camera
                ivProduct.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image pick from camera
                ivProduct.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    // UploadImage method
    private void uploadImage() {
        // Defining the child of storageReference
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());

        // adding listeners on upload
        // or failure of image
        ref.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(
                    UploadTask.TaskSnapshot taskSnapshot) {
                // Image uploaded successfully
                Toast.makeText(getActivity(), "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        storage_location = uri.toString();

                        //Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Error, Image not uploaded
                Toast.makeText(getActivity(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}