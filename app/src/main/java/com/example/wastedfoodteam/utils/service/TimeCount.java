package com.example.wastedfoodteam.utils.service;

import android.widget.TextView;

import java.util.Calendar;

public class TimeCount {
    public String countTimeAgo(long time ){
        long timeDifferent =  time - Calendar.getInstance().getTime().getTime() ;
        if(timeDifferent > 0){
            long seconds = timeDifferent / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            //String time = days + ":" + hours % 24 + ":" + minutes % 60;
            return days + " NGÀY " + + hours%24 +" GIỜ"  + minutes%60  + " PHÚT TRƯỚC";
        }
        return "";
    }}

