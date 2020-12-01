package com.example.wastedfoodteam.seller.register;

import android.content.Context;
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
import com.example.wastedfoodteam.utils.OTPFirebase.VerifyPhoneFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;


public class RegisterSellerPhoneFragment extends Fragment {
    EditText etPhone;
    TextInputLayout tilPhone;
    Button btnNext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_seller_phone, container, false);
        etPhone = view.findViewById(R.id.et_seller_register_phone);
        tilPhone = view.findViewById(R.id.textInputPhone);
        btnNext = view.findViewById(R.id.btn_seller_register_phone_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePhone()==true){
                    String phoneNumber = "+" + 84 + etPhone.getText().toString().trim();
                    //TODO
                    Variable.RESISTER_SELLER.setImage("123");
                    Bundle bundle=new Bundle();
                    bundle.putString("phone", phoneNumber);
                    VerifyPhoneFragment verifyPhoneFragment=new VerifyPhoneFragment();
                    verifyPhoneFragment.setArguments(bundle);
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.flFragmentLayoutAM,verifyPhoneFragment);
                    fragmentTransaction.commit();
                }
            }
        });
        return view;
    }

    private boolean validatePhone(){
        boolean flag = true;
        if(checkEmptyEditText(etPhone) && etPhone.getText().toString().trim().length() == 10 && etPhone.getText().toString().trim().charAt(0) == '0'){
            tilPhone.setErrorEnabled(false);
        }else {
            tilPhone.setError("Số điện thoại không hợp lệ");
            tilPhone.setErrorEnabled(true);
            flag = false;
        }
        return flag;
    }
}