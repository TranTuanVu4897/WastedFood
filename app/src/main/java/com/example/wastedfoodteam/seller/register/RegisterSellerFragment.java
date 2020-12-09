package com.example.wastedfoodteam.seller.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
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
import com.example.wastedfoodteam.utils.validation.Validation;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;

public class RegisterSellerFragment extends Fragment {

    EditText etName, etEmail, etPassword, etConfirmPassword,etDescription;
    Button btnNext;
    TextInputLayout tilName,tilEmail,tilPassword,tilConfirmPassword,tilDescription;
    String errName,errEmail,errPassword,errConfirmPassword,errDescription,pass;
    Boolean bolName,bolEmail,bolPassword,bolConfirmPassword,bolDescription;

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
        stringSetUp();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                if(bolName == true && bolEmail == true && bolPassword == true && bolConfirmPassword==true  && bolDescription==true){
                    Variable.RESISTER_SELLER = new Seller();
                    Variable.RESISTER_SELLER.setName(etName.getText().toString().trim());
                    Variable.RESISTER_SELLER.setEmail(etEmail.getText().toString().trim());
                    Variable.RESISTER_SELLER.setPassword(etPassword.getText().toString().trim());
                    Variable.RESISTER_SELLER.setDescription(etDescription.getText().toString().trim());
                    RegisterSellerPhoneFragment registerSellerPhoneFragment = new RegisterSellerPhoneFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragmentLayoutAM,registerSellerPhoneFragment);
                    fragmentTransaction.commit();
                }else{
                    //Yêu cầu fill thông tin
                }
            }catch (Exception e){
                    Log.e("Error","RegisterSellerFragment");
                }
        }});
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(checkEmptyEditText(etName)){
                        bolName = true;
                        tilName.setError(null);
                    }else {
                        tilName.setError("Tên người dùng không được để trống");
                        bolName = false;
                    }
                }
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(Validation.checkPassword(etPassword.getText().toString().trim())){
                        bolPassword = true;
                        tilPassword.setError(null);
                    }else {
                        tilPassword.setError("Mật khẩu không hợp lệ");
                        bolPassword = false;
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
                        bolEmail = false;
                    }
                }
            }
        });
        etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(etConfirmPassword.getText().toString().trim().equals(etPassword.getText().toString())){
                        bolConfirmPassword = true;
                        tilConfirmPassword.setError(null);
                    }else {
                        tilConfirmPassword.setError("Mật khẩu xác nhận và mật khẩu phải giống nhau");
                        bolConfirmPassword = false;
                    }
                }
            }
        });
        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(etDescription.getText().toString().trim().length() <= 100){
                        bolDescription = true;
                        tilDescription.setError(null);
                    }else {
                        tilDescription.setError("Ghi chú không được vượt quá 100 ký tự");
                        bolDescription = false;
                    }
                }
            }
        });
        return view;
    }

    private void stringSetUp(){
        errName = "Tên tài khoản không được để trống";
        errPassword = "Mật khẩu không được để trống";
        errEmail = "Địa chỉ email không được để trống";
        errConfirmPassword = "Mật khẩu xác nhận không được để trống";
        pass = "pass";
        bolConfirmPassword = false;
        bolDescription = true;
        bolEmail = false;
        bolName = false;
        bolPassword = false;
    }

    private  void checkEmailExist(final String email) {

        String urlGetData = Variable.IP_ADDRESS + "register/checkEmailExist.php?emailUser=" + email ;
        final RequestQueue requestQueue = Volley.newRequestQueue( getActivity().getApplicationContext());
        final StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean emailExist;
                        emailExist = response.equals("exist");
                        if(emailExist){
                            tilEmail.setError("Địa chỉ email đã tồn tại");
                            bolEmail = false;
                        }else{
                            tilEmail.setError(null);
                            bolEmail = true;
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
