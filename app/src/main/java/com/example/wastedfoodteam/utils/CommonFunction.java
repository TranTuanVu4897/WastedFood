package com.example.wastedfoodteam.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CommonFunction {
    public static void setImageViewSrc(Context context, String src, ImageView imageView) {
        if (src == null || src.isEmpty())
            imageView.setImageResource(R.drawable.no_image);
        else
            Glide.with(context).load(src).into(imageView);
    }


    public static String getCurrency(Double money) {
        return String.format("%,.0f", money) + " VND";
    }

    public static String getOpenClose(Date start_time, Date end_time) {
        return start_time.getHours() + ":" + start_time.getMinutes() + " : "
                + end_time.getHours() + ":" + end_time.getMinutes();
    }

    public static String getDiscount(double sell_price, double original_price) {
        return "%" + String.format("%.0f", sell_price / original_price * 100);
    }

    public static String getQuantity(int remain_quantity, int original_quantity) {
        return "Còn: " + remain_quantity + "/" + original_quantity;
    }

    public static void setQuantityTextView(TextView tvQuantity, int remain_quantity, int original_quantity) {
        if (remain_quantity > 0)
            tvQuantity.setText(getQuantity(remain_quantity, original_quantity));
        else {
            tvQuantity.setText("Hết hàng");
            tvQuantity.setBackgroundColor(Color.RED);
        }
    }
}
