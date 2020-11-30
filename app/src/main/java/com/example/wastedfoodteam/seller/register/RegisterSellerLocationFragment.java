package com.example.wastedfoodteam.seller.register;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.BuyHomeActivity;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.login.FragmentLoginBuyer;
import com.example.wastedfoodteam.model.Buyer;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.home.SellerHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;

public class RegisterSellerLocationFragment extends Fragment {
    EditText etLat,etLng,etAddress;
    Button btnComplete;
    private FirebaseAuth mAuth;
    Seller seller ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_seller_location, container, false);
        etLat = view.findViewById(R.id.et_seller_register_lat);
        etLng = view.findViewById(R.id.et_seller_register_lng);
        etAddress = view.findViewById(R.id.et_seller_register_address);
        btnComplete = view.findViewById(R.id.btn_seller_register_phone_location_next);
        mAuth = FirebaseAuth.getInstance();
        seller = Variable.RESISTER_SELLER;
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateLocation()==true){
                    seller.setLatitude( Double.parseDouble(etLat.getText().toString()));
                    seller.setLongitude( Double.parseDouble(etLng.getText().toString()));
                    seller.setAddress( etAddress.getText().toString());
                    String url = Variable.IP_ADDRESS + "seller/registerSeller.php";
                    createFirebaseAccount(seller.getEmail(),md5(seller.getPassword()));
                }
            }
        });
        return view;
    }


    private void createFirebaseAccount(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(getActivity(), new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("signUp", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        seller.setFirebase_UID(user.getUid());
                        String url = Variable.IP_ADDRESS + "seller/registerSeller.php";
                        registerSellerData(url,seller.getName(),seller.getPassword(),seller.getPhone() , seller.getEmail(),seller.getLatitude() +"",seller.getLongitude() +"",seller.getAddress(),seller.getImage(),seller.getFirebase_UID(),seller.getDescription());
                        final Intent intent = new Intent(getActivity(), MainActivity.class);//TODO change to seller activity
                        startActivity(intent);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        SuccessRegisFragment successRegisFragment = new SuccessRegisFragment();
                        fragmentTransaction.replace(R.id.flFragmentLayoutAM,successRegisFragment);
                        fragmentTransaction.commit();
                    }
                });

    }

    //register for seller
    private void registerSellerData(final String url, final String name, final String password, final String phone, final String email, final String latitude, final String longitude , final String address , final String imageURL , final String firebase_UID , final  String description) {
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
            protected Map<String, String> getParams() throws AuthFailureError {
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
                params.put("description",description);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }





    private boolean validateLocation(){
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
}