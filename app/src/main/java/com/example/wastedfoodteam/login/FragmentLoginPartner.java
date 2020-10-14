package com.example.wastedfoodteam.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wastedfoodteam.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FragmentLoginPartner extends Fragment {
    Button btnSignIn, btnBuyerOption;
    EditText etSDT, etPass;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_partner, container, false);
        etSDT = view.findViewById(R.id.edit_sdt_partner);
        etPass = view.findViewById(R.id.edit_pass_partner);
        btnSignIn = view.findViewById(R.id.btn_signIn_partner);
        btnBuyerOption = view.findViewById(R.id.btn_buyer_option);


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
}
