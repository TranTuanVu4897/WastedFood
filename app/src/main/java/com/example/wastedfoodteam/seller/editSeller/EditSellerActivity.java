package com.example.wastedfoodteam.seller.editSeller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
//import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditSellerActivity extends AppCompatActivity {

    //ui view
    EditText editText_editSeller_name;
    EditText editText_editSeller_address;
    EditText editText_editSeller_description;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_seller);

        //init ui view
        editText_editSeller_name = findViewById(R.id.editText_editSeller_name);
        editText_editSeller_address = findViewById(R.id.editText_editSeller_address);
        editText_editSeller_description = findViewById(R.id.editText_editSeller_description);
        btn_editSeller_edit = findViewById(R.id.btn_editSeller_edit);
        iv_editSeller_avatar = findViewById(R.id.iv_editSeller_avatar);

        //for multiline EditText
        //scroll for EditText
        editText_editSeller_description.setScroller(new Scroller(getApplicationContext()));
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
    }




    private void inputData() throws ParseException {
        //Get Data In Edit Text
        name = editText_editSeller_name.getText().toString().trim();
        address = editText_editSeller_address.getText().toString().trim();
        description = editText_editSeller_description.getText().toString();



        //Validate
        //TODO Do it later WARNING

        //TODO Modify in DB

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(intent,IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return  result; //return true/false
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean resultExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return resultCamera && resultExternalStorage;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_REQUEST_CODE);
    }

    //end of for camera handle

    //handle image pick result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

    //get Data after edit
    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(EditSellerActivity.this,"OK",Toast.LENGTH_LONG).show();//TODO get data
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditSellerActivity.this,"lỗi " + urlGetData,Toast.LENGTH_LONG).show();//TODO get data
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

}