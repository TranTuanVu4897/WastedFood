package com.example.wastedfoodteam.seller.forgetPassword;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.Validation.Validation;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.sellerFragment.SellerHomeFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class SellerChangePassword extends Fragment {

    EditText etPassword,etConfirmPassword;
    String strPassword,strConfirmPassword;
    TextInputLayout tilPassword,tilConfirmPass;
    String phone;
    Button btnConfirm;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_change_password, container, false);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        tilPassword = view.findViewById(R.id.textInputPasswordNew);
        tilConfirmPass = view.findViewById(R.id.textInputConfirmPassword);
        strPassword = etPassword.getText().toString().trim();
        strConfirmPassword = etConfirmPassword.getText().toString().trim();
        btnConfirm = view.findViewById(R.id.btnChangePassword);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             phone = bundle.getString("phone", ""); // Key, default value
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePassword()){
                    updateSellerPasswordByPhone(phone);
                }
            }
        });
        return view;
    }

    //update seller account password
    private void updateSellerPasswordByPhone(final String phone){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String urlGetData = Variable.IP_ADDRESS + "seller/updatePasswordAccountByUsername.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            Toast.makeText(getActivity(),"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                            SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flFragmentLayoutAM,sellerHomeFragment);
                            fragmentTransaction.commit();
                        }else{
                            Toast.makeText(getActivity(),"Lỗi cập nhật, vui lòng thử lại sau",Toast.LENGTH_SHORT).show();
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
                params.put("phone", phone );
                params.put("password",md5(etPassword.getText().toString().trim()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public String md5(String str) {
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

    public boolean validatePassword(){
        boolean flag = true;
        if(strPassword!=strConfirmPassword){
            tilConfirmPass.setError("Mật khẩu xác nhận với mật khẩu không giống nhau");
            tilConfirmPass.setErrorEnabled(true);
            flag = false;
        }else {
            tilConfirmPass.setErrorEnabled(false);
        }
        if(Validation.checkPassword(strPassword)){
            tilPassword.setError("Mật khẩu phải có từ 8 đến 16 kí tự");
            tilPassword.setErrorEnabled(true);
            flag = false;
        }
        return flag;
    }
}