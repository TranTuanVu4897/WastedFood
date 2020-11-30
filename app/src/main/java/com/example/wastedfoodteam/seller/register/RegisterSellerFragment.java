package com.example.wastedfoodteam.seller.register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;
import com.google.android.material.textfield.TextInputLayout;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;

public class RegisterSellerFragment extends Fragment {

    EditText etName, etEmail, etPassword, etConfirmPassword,etDescription;
    Button btnNext;
    TextInputLayout tilName;

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
        btnNext = view.findViewById(R.id.btn_seller_register_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInformation()==true){
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
        return view;
    }

    private boolean validateInformation(){
        boolean flag = true;
        if(checkEmptyEditText(etName)){
            tilName.setErrorEnabled(false);
        }else {
            tilName.setError("Tên người dùng không được để trống");
            tilName.setErrorEnabled(true);
            flag = false;
        }
        return flag;
    }


}
