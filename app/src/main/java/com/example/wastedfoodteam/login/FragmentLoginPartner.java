package com.example.wastedfoodteam.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.BuyHomeActivity;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.Home.SellerHomeActivity;
import com.example.wastedfoodteam.source.model.Buyer;
import com.example.wastedfoodteam.source.model.Seller;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FragmentLoginPartner extends Fragment {
    Button btnSignIn, btnBuyerOption;
    EditText etSDT, etPass;
    String urlGetData = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_partner, container, false);
        etSDT = view.findViewById(R.id.etSdtPartnerFLP);
        etPass = view.findViewById(R.id.etPassPartnerFLP);
        btnSignIn = view.findViewById(R.id.btnSignInPartnerFLP);
        btnBuyerOption = view.findViewById(R.id.btnBuyerOptionFLP);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlGetData = Variable.ipAddress + "login/sellerLogin.php?username=" + etSDT.getText().toString() + "&password=" + md5(etPass.getText().toString());
                getData(urlGetData);
            }
        });

        btnBuyerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentLoginPartner();
            }
        });
        return view;
    }

    /**
     * encode md5
     * @param str
     * @return
     */
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

    /**
     * get data from mySql
     * @param url
     */
    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "not exist account":
                        Toast.makeText(getActivity(), "lỗi " + urlGetData, Toast.LENGTH_LONG).show();//TODO fix for suitable error
                        break;
                    case "not match role":
                        Toast.makeText(getActivity(), "lỗi " + urlGetData, Toast.LENGTH_LONG).show();//TODO fix for suitable error
                        break;
                    default:
                        Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();//TODO get data
                        try {
                            JSONArray object = new JSONArray(response);

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                            Seller seller = gson.fromJson(object.getString(0), Seller.class);

                            Intent intent = new Intent(getActivity(), SellerHomeActivity.class);//TODO change to seller activity
                            intent.putExtra("SELLER",seller);
                            //TODO pass data through intent
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi " + urlGetData, Toast.LENGTH_LONG).show();//TODO get data
                Log.d("MK ", md5(etPass.getText().toString()));
            }
        });
        requestQueue.add(stringRequest);
    }

    /**
     * move to fragment buyer
     */
    public void addFragmentLoginPartner(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentLoginBuyer fragmentLoginBuyer = new FragmentLoginBuyer();
        fragmentTransaction.replace(R.id.flFragmentLayoutAM,fragmentLoginBuyer);
        fragmentTransaction.commit();
    }
}
