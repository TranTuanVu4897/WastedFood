package com.example.wastedfoodteam.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;

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
        btnBuyerOption = view.findViewById(R.id.btnBuyerOptionFLB);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlGetData = "http://192.168.1.46/wastedfoodphp/login/sellerLogin.php?username="+etSDT.getText().toString()+"&password="+etPass.getText().toString();
                getData(urlGetData);
            }
        });

        return view;
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
    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(getActivity(),"OK",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"lỗi",Toast.LENGTH_LONG).show();
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }
}
