package com.example.wastedfoodteam.utils.Validation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.global.Variable;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Validation {

    public static void checkPhoneExist(Context context, final ApiCallback apiCallback, String phone) {

        String urlGetData = Variable.IP_ADDRESS + "register/checkPhoneExist.php?phone=" + phone;
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("notExist"))
                            apiCallback.onResponse(false);
                        else if (response.equals("exist")) {
                            int a = 1;
                            apiCallback.onResponse(true);
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

    public static boolean checkPhone(String string) {
        try {
            return Pattern.compile("^(84|0)([3|5|7|8|9])([0-9]{8})$").matcher(string).matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkEmail(String string) {
        try {
            return Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(string).matches();
        }catch (Exception e){
            return false;
        }
    }

    public static boolean checkPassword(String string) {
        try {
            return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,16}$").matcher(string).matches();
        }catch ( Exception e){
            return false;
        }
    }

    public static boolean checkName(String string) {
        try{
        return Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$").matcher(string).matches();}
        catch (Exception e){
            return false;
        }
    }

    @NotNull
    public static Boolean validateDate(String date) {
        try {
            Calendar cal = Calendar.getInstance();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date strDate = sdf.parse(date);
            return new Date().after(strDate);
        } catch (Exception e) {
            Log.d("Lỗi Date : ", date);
            return false;
        }

    }

    public static void checkPhoneExist(final String phone , final TextInputLayout tilPhone , Context context) {

        String urlGetData = Variable.IP_ADDRESS + "register/checkPhoneExist.php?phone=" + phone ;
        final RequestQueue requestQueue = Volley.newRequestQueue( context.getApplicationContext()  );
        final StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean emailExist;
                        emailExist = response.equals("exist");
                        if(emailExist){
                            tilPhone.setError("Số điện thoại đã tồn tại");
                            tilPhone.setErrorEnabled(true);
                        }else{
                            tilPhone.setErrorEnabled(false);
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
