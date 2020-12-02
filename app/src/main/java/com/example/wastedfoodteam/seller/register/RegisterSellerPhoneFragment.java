package com.example.wastedfoodteam.seller.register;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.CameraStorageFunction;
import com.example.wastedfoodteam.utils.OTPFirebase.VerifyPhoneFragment;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.wastedfoodteam.utils.CommonFunction.checkEmptyEditText;


public class RegisterSellerPhoneFragment extends Fragment {
    EditText etPhone;
    TextInputLayout tilPhone;
    Button btnNext;
    ImageView ivSeller;
    FirebaseStorage storage;
    StorageReference storageReference;
    private String storage_location;
    CameraStorageFunction cameraStorageFunction;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_seller_phone, container, false);
        etPhone = view.findViewById(R.id.et_seller_register_phone);
        tilPhone = view.findViewById(R.id.textInputPhone);
        ivSeller = view.findViewById(R.id.iv_seller_register_phone);
        btnNext = view.findViewById(R.id.btn_seller_register_phone_next);
        cameraStorageFunction = new CameraStorageFunction(getActivity(), getContext(),ivSeller);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validatePhone()==true){
                    cameraStorageFunction.uploadImage(new CameraStorageFunction.HandleUploadImage() {
                        @Override
                        public void onSuccess(String url) {
                            storage_location = url;
                            String phoneNumber = "+" + 84 + etPhone.getText().toString().trim();
                            Variable.RESISTER_SELLER.setImage(storage_location);
                            Bundle bundle=new Bundle();
                            bundle.putString("phone", phoneNumber);
                            VerifyPhoneFragment verifyPhoneFragment=new VerifyPhoneFragment();
                            verifyPhoneFragment.setArguments(bundle);
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.flFragmentLayoutAM,verifyPhoneFragment);
                            fragmentTransaction.commit();
                        }

                        @Override
                        public void onError() {

                        }
                    });



                }
            }
        });
        ivSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.showImagePickDialog();
            }
        });
        return view;
    }

    //handle image pick result
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cameraStorageFunction.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

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