package com.example.wastedfoodteam.seller.forgetPassword;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.login.FragmentLoginPartner;
import com.example.wastedfoodteam.utils.validation.Validation;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.home.SellerHomeFragment;
import com.facebook.login.LoginFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import static com.example.wastedfoodteam.utils.Encode.md5;


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
        btnConfirm = view.findViewById(R.id.btnChangePassword);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
             phone = bundle.getString("phone", ""); // Key, default value
        }
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePassword()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(md5(strPassword)).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("firebase", "User password failure.");
                        }
                    })
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("firebase", "User password updated.");
                                    }
                                }

                            });
                    updateSellerPasswordByPhone(phone);
                }
            }
        });
        return view;
    }

    //update seller account password
    private void updateSellerPasswordByPhone(final String phone){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String urlGetData = Variable.IP_ADDRESS + "seller/updatePasswordByUsername.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            Toast.makeText(getActivity(),"Đổi mật khẩu thành công",Toast.LENGTH_SHORT).show();
                            FragmentLoginPartner fragmentLoginPartner = new FragmentLoginPartner();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flFragmentLayoutAM, fragmentLoginPartner);
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
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                params.put("phone", phone );
                params.put("password",md5(etPassword.getText().toString().trim()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public boolean validatePassword(){
        boolean flag = true;
        strPassword = etPassword.getText().toString().trim();
        strConfirmPassword = etConfirmPassword.getText().toString().trim();
        if(!strPassword.equals(strConfirmPassword)){
            tilConfirmPass.setError("Mật khẩu xác nhận với mật khẩu không giống nhau");
            flag = false;
        }else {
            tilPassword.setError(null);
        }
        if(!Validation.checkPassword(strPassword)){
            tilPassword.setError("Mật khẩu phải gồm 1 chữ cái và có từ 8 đến 16 kí tự");
            flag = false;
        }else{
            tilConfirmPass.setError(null);
        }
        return flag;
    }
}