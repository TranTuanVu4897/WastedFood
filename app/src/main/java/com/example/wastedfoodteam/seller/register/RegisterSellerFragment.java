package com.example.wastedfoodteam.seller.register;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.forgetPassword.VerifyPhoneForgotPasswordFragment;
import com.example.wastedfoodteam.utils.Validation.Validation;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;

public class RegisterSellerFragment extends Fragment {

    EditText etName, etEmail, etPassword, etConfirmPassword,etDescription;
    Button btnNext;
    TextInputLayout tilName,tilEmail,tilPassword,tilConfirmPassword,tilDescription;
    private String pass = "pass";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_seller, container, false);
        etName = view.findViewById(R.id.et_seller_register_name);
        etEmail = view.findViewById(R.id.et_seller_register_email);
        etPassword = view.findViewById(R.id.et_seller_register_password);
        etConfirmPassword = view.findViewById(R.id.et_seller_register_confirm_password);
        etDescription = view.findViewById(R.id.et_seller_register_Description);
        etDescription.setMaxLines(3);
        etDescription.setMinLines(3);
        tilName = view.findViewById(R.id.textInputName);
        tilEmail = view.findViewById(R.id.textInputEmail);
        tilPassword = view.findViewById(R.id.textInputPassword);
        tilConfirmPassword = view.findViewById(R.id.textInputConfirmPassword);
        tilDescription = view.findViewById(R.id.textInputDescription);
        btnNext = view.findViewById(R.id.btn_seller_register_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tilName.getError().equals(pass)&&tilEmail.getError().equals(pass)&&tilPassword.getError().equals(pass) && tilConfirmPassword.getError().equals(pass)  && tilDescription.getError().equals(pass)){
                    Variable.RESISTER_SELLER = new Seller();
                    Variable.RESISTER_SELLER.setName(etName.getText().toString().trim());
                    Variable.RESISTER_SELLER.setEmail(etEmail.getText().toString().trim());
                    Variable.RESISTER_SELLER.setPassword(etPassword.getText().toString().trim());
                    Variable.RESISTER_SELLER.setDescription(etDescription.getText().toString().trim());
                    RegisterSellerPhoneFragment registerSellerPhoneFragment = new RegisterSellerPhoneFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragmentLayoutAM,registerSellerPhoneFragment);
                    fragmentTransaction.commit();
                }
            }
        });
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(checkEmptyEditText(etName)){
                        tilName.setErrorEnabled(false);
                        tilName.setError(pass);
                    }else {
                        tilName.setError("Tên người dùng không được để trống");
                        tilName.setErrorEnabled(true);
                    }
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(Validation.checkPassword(etPassword.getText().toString().trim())){
                        tilPassword.setErrorEnabled(false);
                        tilName.setError(pass);
                    }else {
                        tilPassword.setError("Địa chỉ email không hợp lệ");
                        tilPassword.setErrorEnabled(true);
                    }
                }
            }
        });
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(checkEmptyEditText(etEmail)&& Validation.checkEmail(etEmail.getText().toString().trim())){
                       checkEmailExist(etEmail.getText().toString().trim());
                    }else {
                        tilEmail.setError("Địa chỉ email không hợp lệ");
                        tilEmail.setErrorEnabled(true);
                    }
                }
            }
        });
        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(etConfirmPassword.getText().toString().trim() != etPassword.getText().toString()){
                        tilConfirmPassword.setErrorEnabled(false);
                        tilConfirmPassword.setError(pass);
                    }else {
                        tilConfirmPassword.setError("Mật khẩu xác nhận và mật khẩu phải giống nhau");
                        tilConfirmPassword.setErrorEnabled(true);

                    }
                }
            }
        });
        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(etDescription.getText().toString().trim().length() > 100){
                        tilDescription.setErrorEnabled(false);
                        tilDescription.setError(pass);
                    }else {
                        tilDescription.setError("Ghi chú không được vượt quá 100 ký tự");
                        tilDescription.setErrorEnabled(true);

                    }
                }
            }
        });
        return view;
    }

    private  void checkEmailExist(final String email) {

        String urlGetData = Variable.IP_ADDRESS + "register/checkEmailExist.php?email=" + email ;
        final RequestQueue requestQueue = Volley.newRequestQueue( getActivity().getApplicationContext());
        final StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Boolean emailExist;
                        if(response.equals("exist")){
                            emailExist = true;
                        }else{
                            emailExist = false;
                        }
                        if(emailExist){
                            tilEmail.setError("Địa chỉ email đã tồn tại");
                            tilEmail.setErrorEnabled(true);
                        }else{
                            tilEmail.setErrorEnabled(false);
                            tilEmail.setError(pass);
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
}
