package com.example.wastedfoodteam.Validation;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Validation {

    public final static void checkPhoneExist(Context context, final ApiCallback apiCallback , String phone) {

        String urlGetData = Variable.IP_ADDRESS + "register/checkPhoneExist.php?phone=" + phone;
        RequestQueue requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        StringRequest getSellerRequestString = new StringRequest(Request.Method.GET, urlGetData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == "notExist")
                            apiCallback.onResponse(false);
                        else if (response.equals("exist")==true){
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
        return Pattern.compile("(84|0[3|5|7|8|9])+([0-9]{8})\\b").matcher(string).matches();
    }

    public final static boolean checkEmail(String string) {
        return Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(string).matches();
    }

    public final static boolean checkPassword(String string) {
        return Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,16}$").matcher(string).matches();
    }

    public final static boolean checkName(String string) {
        return Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$").matcher(string).matches();
    }

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
