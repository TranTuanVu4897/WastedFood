package com.example.wastedfoodteam.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Seller;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonFunction {

    /**
     * set image view src from image url
     *
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
     *
     * @param money
     * @return
     */
    @SuppressLint("DefaultLocale")
    @NotNull
    public static String getCurrency(Double money) {
        return String.format("%,.0f", money) + " VND";
    }

    /**
     * set open close time string
     *
     * @param start_time
     * @param end_time
     * @return
     */
    @NotNull
    public static String getOpenClose(Date start_time, Date end_time) {
        try {
             SimpleDateFormat getHourAndMinute = new SimpleDateFormat("hh.mm");
            return getHourAndMinute.format(start_time) + " - "
                    + getHourAndMinute.format(end_time);
        } catch (Exception e) {
            return "00.00 - 23.59";
        }
    }

    /**
     * set discount string
     *
     * @param sell_price
     * @param original_price
     * @return
     */
    @SuppressLint("DefaultLocale")
    @NotNull
    public static String getDiscount(double sell_price, double original_price) {
        try {
            return "%" + String.format("%.0f", 100 - sell_price / original_price * 100);
        } catch (Exception e) {
            return "%0";
        }
    }

    /**
     * set quantity string
     *
     * @param remain_quantity
     * @param original_quantity
     * @return
     */
    @NotNull
    @Contract(pure = true)
    public static String getQuantity(int remain_quantity, int original_quantity) {
        if (!isOutOfStock(remain_quantity))
            return "Còn: " + remain_quantity + "/" + original_quantity;
        return "Hết hàng";
    }

    /**
     * set text view for out of stock
     *
     * @param tvQuantity
     * @param remain_quantity
     * @param original_quantity
     */
    public static void setQuantityTextView(TextView tvQuantity, int remain_quantity, int original_quantity) {
        try {
            tvQuantity.setText(getQuantity(remain_quantity, original_quantity));
            if (isOutOfStock(remain_quantity)) {
                tvQuantity.setBackgroundColor(Color.RED);
            }
        } catch (Exception e) {
            Log.e("CommonFunction", e.getMessage());
        }
    }

    /**
     * get current date by format "yyyy-MM-dd"
     * @return
     */
    @NotNull
    public static String getCurrentDate() {
        Date currentTime = Calendar.getInstance().getTime();
        return new SimpleDateFormat("yyyy-MM-dd").format(currentTime);
    }

    /**
     *
     * @param editText
     * @return
     */
    public static boolean checkEmptyEditText(EditText editText) {
        try {
            if (editText.getText().toString().trim().length() > 0)
                return true;
        } catch (Exception e) {
            Log.e("CommonFunction", e.getMessage());
        }
        return false;
    }

    /**
     *
     * @param seller
     * @param currentGPS
     * @return
     */
    public static String getStringDistance(Seller seller, Location currentGPS) {
        String distance = "km";
        try {
            if (seller.getDistance() != 0)
                distance = roundToTwoDecimal(seller.getDistance()) + distance;
            else distance = roundToTwoDecimal(getDistanceBetweenTwoPlace(currentGPS.getLatitude(),
                    currentGPS.getLongitude(),
                    seller.getLatitude(),
                    seller.getLongitude())) + distance;
        } catch (Exception e) {
            distance = "Không rõ";
        }
        return distance;
    }

    private static double getDistanceBetweenTwoPlace(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (Math.round(dist));
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @NotNull
    private static String roundToTwoDecimal(double input) {
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(input);
    }

    private static boolean isOutOfStock(int remain_quantity) {
        return remain_quantity == 0;
    }

}
