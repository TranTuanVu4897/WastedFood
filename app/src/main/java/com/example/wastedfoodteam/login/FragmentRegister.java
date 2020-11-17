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

import com.example.wastedfoodteam.R;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FragmentRegister extends Fragment {
    EditText etPassword, etConfirmPassword;
    Button btnContinue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        etPassword = view.findViewById((R.id.et_password));
        etConfirmPassword = view.findViewById(R.id.et_password_confirm);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                regex(etPassword.toString(),etConfirmPassword.toString());
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regex(etPassword.toString(),etConfirmPassword.toString());
            }
        });

        return view;
    }
    //Check legal
    public void regex( String password, String cfPassword ){
        Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[a-zA-Z]).{8,16}$");
        if(pattern.matcher(password).matches() == false){
            Toast.makeText(getActivity(), "Mật khẩu từ 8-16 ký tự và có ít nhất một chữ cái và một số",Toast.LENGTH_LONG);
            return;
        }
        if(password.equals(cfPassword)){
            Toast.makeText(getActivity(), "Xác nhận mật khẩu sai",Toast.LENGTH_LONG);
            return;
        }

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
