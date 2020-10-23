package com.example.wastedfoodteam.seller;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.seller.sellerFragment.AddProductFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    //set up firebase
    //tool -> firebase -> authentication

    //ui view
    private ImageView productIconIv;
    private EditText editTextName;
    private EditText editTextPrice;
    private EditText editTextDiscount;
    private EditText editTextQuantity;
    private EditText editTextOpenDate;
    private EditText editTextCloseDate;
    private Button btnAddProduct;

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

    //firebase connect
    //private FirebaseAuth firebaseAuth;

    //progress dialog
    private ProgressDialog progressDialog;


    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference mStorageReference;

    //
    private StorageTask uploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //init ui view
        productIconIv = findViewById(R.id.productIconIv);
        editTextName = findViewById(R.id.editTextName);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextDiscount = findViewById(R.id.editTextDiscount);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        editTextOpenDate = findViewById(R.id.editTextOpenDate);
        editTextCloseDate = findViewById(R.id.editTextCloseDate);
        btnAddProduct = findViewById(R.id.btnAddProduct);

        //Date picker handle
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        editTextOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddProductActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        editTextOpenDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        editTextCloseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddProductActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        editTextCloseDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        //firebase
        //firebaseAuth = FirebaseAuth.getInstance();

        //set Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        //init permission arrays
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        productIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show dialog to pick image
                showImagePickDialog();
            }
        });

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
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

    private String productName;
    private double productPrice;
    private int productQuantity, productDiscount;
    private Date productOpenDate, productCloseDate;


    private void inputData() throws ParseException {
        //Input Data
        productName = editTextName.getText().toString().trim();
        productPrice = Double.parseDouble(editTextPrice.getText().toString().trim());
        productDiscount = Integer.parseInt(editTextDiscount.getText().toString().trim());
        productQuantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
        //productOpenDate = Calendar.getInstance().getTime();
        //productCloseDate = Calendar.getInstance().getTime();

        productOpenDate = new SimpleDateFormat("dd/MM/yyyy").parse(editTextOpenDate.getText().toString().trim());
        productCloseDate = new SimpleDateFormat("dd/MM/yyyy").parse(editTextCloseDate.getText().toString().trim());
        //productCloseDate = new SimpleDateFormat("hh:mm:ss a").parse(editTextCloseDate.getText().toString().trim());


        //Validate
        //Do it later WARNING

        //Add to DB
        addProduct();
    }


    private void addProduct() {
        progressDialog.setMessage("Adding Product");
        progressDialog.show();

        String timestamp = "" + System.currentTimeMillis();
        if (image_uri == null) {
            //upload without image

            //set up data to upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("productId", "" + timestamp);
            hashMap.put("productName", "" + productName);
            hashMap.put("productPrice", "" + productPrice);
            hashMap.put("productDiscount", "" + productDiscount);
            hashMap.put("productQuantity", "" + productQuantity);
            hashMap.put("productOpenDate", "" + productOpenDate);
            hashMap.put("productCloseDate", "" + productCloseDate);
            hashMap.put("timestamp", "" + timestamp);
            //hashMap.put("uid", "" + firebaseAuth.getUid());
            //need to make user id in future

            //add to db
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            //reference.child(firebaseAuth.getUid()).child("Products").child("timestamp").setValue(hashMap)
            reference.child("2").child("Products").child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //added to db
                            progressDialog.dismiss();
                            Intent intent = new Intent(AddProductActivity.this, PostActivity.class);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //failed to db
                            progressDialog.dismiss();
                        }
                    });

        } else {
            //upload with image

            //first upload image to storage

            //name and path of image to upload


            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(AddProductActivity.this, "Upload in progress", Toast.LENGTH_LONG).show();
            } else {
                fileUpload();
            }


        }
    }

    private String getExtension(Uri uri) {
        //url image handle
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void fileUpload() {

        StorageReference ref = mStorageReference.child(System.currentTimeMillis() + "." + getExtension(image_uri));
        uploadTask = ref.putFile(image_uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //do your stuff- uri.toString() will give you download URL\\
                                uri.toString();
                            }
                        });
                        Toast.makeText(AddProductActivity.this, "Image upload successful" + downloadUrl, Toast.LENGTH_LONG).show();

                        String timestamp = "" + System.currentTimeMillis();
                        //url of image received upload to db
                        //set up data to upload
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("productId", "" + timestamp);
                        hashMap.put("productName", "" + productName);
                        hashMap.put("productPrice", "" + productPrice);
                        hashMap.put("productDiscount", "" + productDiscount);
                        hashMap.put("productQuantity", "" + productQuantity);
                        hashMap.put("productOpenDate", "" + productOpenDate);
                        hashMap.put("productCloseDate", "" + productCloseDate);
                        hashMap.put("imageURL", "" + downloadUrl);

                        //add to db
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                        //reference.child(firebaseAuth.getUid()).child("Products").child("timestamp").setValue(hashMap)
                        //need to make user id in future
                        reference.child("2").child("Products").child(timestamp).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //added to db
                                progressDialog.dismiss();
                                Intent intent = new Intent(AddProductActivity.this, PostActivity.class);
                                startActivity(intent);
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed to db
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result; //return true/false
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean resultExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return resultCamera && resultExternalStorage;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }

    //handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        //both accept
                        pickFromCamera();
                    } else {
                        //both of one denied
                        Toast.makeText(this, "Camera&StoragePermission required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //accept
                        pickFromGallery();
                    } else {
                        //denied
                        Toast.makeText(this, "StoragePermission required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //handle image pick result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //image pick from gallery

                //save picked image uri
                image_uri = data.getData();

                //image picked from camera
                productIconIv.setImageURI(image_uri);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //image pick from camera
                productIconIv.setImageURI(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    }

