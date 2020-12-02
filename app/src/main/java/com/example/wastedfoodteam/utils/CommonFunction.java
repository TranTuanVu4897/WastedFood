package com.example.wastedfoodteam.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Seller;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CommonFunction {

    /**
     * set image view src from image url
     * @param context
     * @param src
     * @param imageView
     */
    public static void setImageViewSrc(Context context, String src, ImageView imageView) {
        if (src == null || src.isEmpty())
            imageView.setImageResource(R.drawable.no_image);
        else
            Glide.with(context).load(src).into(imageView);
    }

    /**
     * set currency by double money input
     * @param money
     * @return
     */
    public static String getCurrency(Double money) {
        return String.format("%,.0f", money) + " VND";
    }

    /**
     * set open close time string
     * @param start_time
     * @param end_time
     * @return
     */
    public static String getOpenClose(Date start_time, Date end_time) {
        return start_time.getHours() + ":" + start_time.getMinutes() + " : "
                + end_time.getHours() + ":" + end_time.getMinutes();
    }

    /**
     * set discount string
     * @param sell_price
     * @param original_price
     * @return
     */
    public static String getDiscount(double sell_price, double original_price) {
        return "%" + String.format("%.0f", 100 - sell_price / original_price * 100);
    }

    /**
     * set quantity string
     * @param remain_quantity
     * @param original_quantity
     * @return
     */
    public static String getQuantity(int remain_quantity, int original_quantity) {
        return "Còn: " + remain_quantity + "/" + original_quantity;
    }

    /**
     * set textview for out of stock
     * @param tvQuantity
     * @param remain_quantity
     * @param original_quantity
     */
    public static void setQuantityTextView(TextView tvQuantity, int remain_quantity, int original_quantity) {
        if (remain_quantity > 0)
            tvQuantity.setText(getQuantity(remain_quantity, original_quantity));
        else {
            tvQuantity.setText("Hết hàng");
            tvQuantity.setBackgroundColor(Color.RED);
        }
    }

    public static String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        return String.format("%d-%02d-%02d", currentTime.getYear(), currentTime.getMonth() + 1, currentTime.getDay());
    }

    public static boolean checkEmptyEditText(EditText editText){
        if (editText.getText().toString().trim().length() > 0)
            return true;

        return false;
    }

    public static String getStringDistance(Seller seller) {
        String distance = "km";
        try{
            distance = seller.getDistance()+distance;
        }catch (Exception e){
            distance = "Không rõ";
        }
        return distance;
    }
}
