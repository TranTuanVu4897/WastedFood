package com.example.wastedfoodteam.seller.register;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.LoginActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class RegisterSellerLocationFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private static final int REQUEST_CODE_PERMISSION = 2;
    EditText etLat, etLng, etAddress;
    Button btnComplete;
    private FirebaseAuth mAuth;
    Seller seller;
    GPSTracker gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_seller_location, container, false);
        etLat = view.findViewById(R.id.etLatitude);
        etLng = view.findViewById(R.id.etLongitude);
        etAddress = view.findViewById(R.id.etAddress);
        btnComplete = view.findViewById(R.id.btnNext);
        mAuth = FirebaseAuth.getInstance();
        seller = Variable.RESISTER_SELLER;

        getGPSPermission();

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateLocation()) {
                    seller.setLatitude(Double.parseDouble(etLat.getText().toString()));
                    seller.setLongitude(Double.parseDouble(etLng.getText().toString()));
                    seller.setAddress(etAddress.getText().toString());
                    createFirebaseAccount(seller.getEmail(), md5(seller.getPassword()));
                }
            }
        });

        etLng.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mMap != null) {
                    if (isNullOrEmpty(etLng.getText().toString()))
                        setMarker(Double.parseDouble(etLat.getText().toString()), Double.parseDouble(etLng.getText().toString()));
                }
            }
        });
        etLat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mMap != null) {
                    if (isNullOrEmpty(etLat.getText().toString()))
                        setMarker(Double.parseDouble(etLat.getText().toString()), Double.parseDouble(etLng.getText().toString()));
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    private boolean isNullOrEmpty(String toString) {
        return toString != null && !toString.isEmpty();
    }

    private void getGPSPermission() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);


            } else getGPS();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getGPS() {
        gps = new GPSTracker(getActivity());
        if (gps.canGetLocation()) {
            refreshTextEditText(gps.getLatitude(), gps.getLongitude());
        } else {
            gps.showSettingAlert();
        }
    }

    private void refreshTextEditText(double latitude, double longitude) {
        etLat.setText(latitude + "");
        etLng.setText(longitude + "");
    }


    private void createFirebaseAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("signUp", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        seller.setFirebase_UID(user.getUid());
                        String url = Variable.IP_ADDRESS + "seller/registerSeller.php";
                        registerSellerData(url, seller.getName(), seller.getPassword(), seller.getPhone(), seller.getEmail(), seller.getLatitude() + "", seller.getLongitude() + "", seller.getAddress(), seller.getImage(), seller.getFirebase_UID(), seller.getDescription());
                        final Intent intent = new Intent(getActivity(), LoginActivity.class);//TODO change to seller activity
                        startActivity(intent);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        SuccessRegisFragment successRegisFragment = new SuccessRegisFragment();
                        fragmentTransaction.replace(R.id.flFragmentLayoutAM, successRegisFragment);
                        fragmentTransaction.commit();
                    }
                });

    }

    //register for seller
    private void registerSellerData(final String url, final String name, final String password, final String phone, final String email, final String latitude, final String longitude, final String address, final String imageURL, final String firebase_UID, final String description) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi kết nỗi" + url, Toast.LENGTH_LONG).show();//TODO get data
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("password", md5(password));
                params.put("phone", phone);
                params.put("email", email);
                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("address", address);
                params.put("imageURL", imageURL);
                params.put("firebase_UID", firebase_UID);
                params.put("description", description);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private boolean validateLocation() {
        boolean flag = true;
        return flag;
    }

    private String md5(String str) {
        String result = "";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            BigInteger bigInteger = new BigInteger(1, digest.digest());
            result = bigInteger.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setMarker(latLng.latitude, latLng.longitude);
            }
        });
        setMarkerByGPS();
    }

    private void setMarkerByGPS() {
        if (gps != null)
            if (gps.canGetLocation()) {
                setMarker(gps.getLatitude(), gps.getLongitude());
            }
    }

    private void setMarker(double latitude, double longitude) {
        LatLng current = new LatLng(latitude, longitude);
        MarkerOptions marker = new MarkerOptions().position(current).title("Vị trí nhà hàng bạn?");
        mMap.clear();
        mMap.addMarker(marker);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 16f));
        refreshTextEditText(latitude, longitude);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSION) {
            getGPS();
            setMarkerByGPS();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}