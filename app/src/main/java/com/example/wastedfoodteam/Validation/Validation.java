package com.example.wastedfoodteam.Validation;

import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Validation {
    public final static boolean checkPhone(String string){
        return Pattern.compile("(84|0[3|5|7|8|9])+([0-9]{8})\\b").matcher(string).matches();
    }
    public final static boolean checkEmail(String string){
        return Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(string).matches();
    }
    public final static boolean checkPassword(String string){
        return Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$").matcher(string).matches();
    }
    public final static boolean checkName(String string){
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
