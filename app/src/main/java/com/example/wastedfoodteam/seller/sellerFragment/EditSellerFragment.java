package com.example.wastedfoodteam.seller.sellerFragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.VoiceInteractor;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.editSeller.EditSellerActivity;
import com.example.wastedfoodteam.utils.DownloadImageTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class EditSellerFragment extends Fragment {

    public static final int RESULT_OK = -1;
    private int id ;
    private Seller sellerInfomation;

    //ui view
    EditText editText_editSeller_name;
    EditText editText_editSeller_address;
    EditText editText_editSeller_description;
    EditText editText_editSeller_email;
    Button btn_editSeller_edit;
    ImageView iv_editSeller_avatar;

    //permission constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //image pick constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //permission array
    private String[] cameraPermission;
    private  String[] storagePermission;

    //image pick uri
    private Uri image_uri;

    //for editText value
    private String name,address,description;

    //link to php file
    String urlGetData = "";

    //string get from edit text
    String string_editSeller_name ;
    String string_editSeller_address ;
    String string_editSeller_description ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_seller, container, false);

        //init ui view
        editText_editSeller_name = view.findViewById(R.id.editText_editSeller_name);
        editText_editSeller_address = view.findViewById(R.id.editText_editSeller_address);
        editText_editSeller_description = view.findViewById(R.id.editText_editSeller_description);
        btn_editSeller_edit = view.findViewById(R.id.btn_editSeller_edit1);
        iv_editSeller_avatar = view.findViewById(R.id.iv_editSeller_avatar);


        Bundle bundle = getArguments();
        if(bundle!= null){
            id = bundle.getInt("id");
        }

        getSeller(id);


        //string get from edit text
        string_editSeller_name = editText_editSeller_name.getText().toString().trim();
        string_editSeller_address = editText_editSeller_address.getText().toString().trim();
        string_editSeller_description = editText_editSeller_description.getText().toString().trim();
        //string_editSeller_email = editText_editSeller_email.getText().toString().trim();

        //for multiline EditText
        //scroll for EditText
        editText_editSeller_description.setScroller(new Scroller( getActivity().getApplicationContext()));
        editText_editSeller_description.setVerticalScrollBarEnabled(true);

        //Edit Text Line
        editText_editSeller_description.setMinLines(2);
        editText_editSeller_description.setMaxLines(5);

        //init permission arrays
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //click avatar handle
        iv_editSeller_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagePickDialog();
            }
        });

        //click edit button handle
        btn_editSeller_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Flow
                //input data -> validate -> add to db
                try {
                    inputData();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void getSeller(int id) {
        urlGetData = Variable.ipAddress + "getSellerById.php?id=" + id;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonSellers = new JSONArray(response);
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            Seller seller = gson.fromJson(jsonSellers.getString(0), Seller.class);
                            editText_editSeller_name.setText(seller.getName());
                            editText_editSeller_address.setText(seller.getAddress());
                            editText_editSeller_description.setText(seller.getDescription());
                            new DownloadImageTask(iv_editSeller_avatar,getResources()).execute(seller.getImage());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(getSellerRequestString);
    }

    private void inputData() throws ParseException {
        //Get Data In Edit Text
        name = editText_editSeller_name.getText().toString().trim();
        address = editText_editSeller_address.getText().toString();
        description = editText_editSeller_description.getText().toString();



        //Validate
        //TODO Do it later

        //Modify in DB
        String urlGetData = Variable.ipAddress + "seller/sellerEdit.php";
        //updateSeller("http://192.168.1.10/wastedfoodphp/seller/sellerEdit.php");
        updateSeller(urlGetData);

    }

    //start of for camera handle
    private void pickFromGallery(){
        //intent to pick image from gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void showImagePickDialog(){
        //display in dialog
        String[] options = {"Camera","Gallery"};
        //dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle item clicks
                if(which==0){
                    //camera clicked
                    if(checkCameraPermission()){
                        //permission granted
                        pickFromCamera();
                    }else{
                        //permission not granted, request
                        requestCameraPermission();
                    }
                }else{
                    //gallery clicked
                    if(checkStoragePermission()){
                        //permission granted
                        pickFromGallery();
                    }else{
                        //permission not granted, request
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }

    private void pickFromCamera(){
        //intent to pick image from camera

        //use media store to pick high/ori quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE,"Temp_Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Temp_Image_Description");

        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return  result; //return true/false
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(getActivity(),storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean resultCamera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean resultExternalStorage = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return resultCamera && resultExternalStorage;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(getActivity(),cameraPermission,CAMERA_REQUEST_CODE);
    }

    //end of for camera handle

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){
                //image pick from gallery

                //save picked image uri
                image_uri = data.getData();

                //image picked from camera
                iv_editSeller_avatar.setImageURI(image_uri);
            }else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                //image pick from camera
                iv_editSeller_avatar.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }





    //update seller data
    private void updateSeller(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
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
                params.put("username",editText_editSeller_name.getText().toString().trim());
                params.put("address",editText_editSeller_address.getText().toString().trim());
                params.put("description",editText_editSeller_description.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}