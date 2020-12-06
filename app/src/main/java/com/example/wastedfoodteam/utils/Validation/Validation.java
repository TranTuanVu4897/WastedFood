package com.example.wastedfoodteam.utils.Validation;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.global.Variable;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Validation {

    public final static void checkPhoneExist(Context context, final ApiCallback apiCallback, String phone) {

        String urlGetData = Variable.IP_ADDRESS + "register/checkPhoneExist.php?phone=" + phone;
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == "notExist")
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

    public final static boolean checkPhone(String string) {
        try {
            return Pattern.compile("^(84|0)([3|5|7|8|9])([0-9]{8})$").matcher(string).matches();
        } catch (Exception e) {
            return false;
        }
    }

    public final static boolean checkEmail(String string) {
        try {
            return Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(string).matches();
        }catch (Exception e){
            return false;
        }
    }

    public final static boolean checkPassword(String string) {
        try {
            return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,16}$").matcher(string).matches();
        }catch ( Exception e){
            return false;
        }
    }

    public final static boolean checkName(String string) {
        try{
        return Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$").matcher(string).matches();}
        catch (Exception e){
            return false;
        }
    }

    @NotNull
    public final static Boolean validateDate(String date) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date strDate = sdf.parse(date);
            if (new Date().after(strDate)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.d("Lỗi Date : ", date);
            return false;
        }

    }
}
