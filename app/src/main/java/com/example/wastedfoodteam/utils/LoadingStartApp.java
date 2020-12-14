package com.example.wastedfoodteam.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.wastedfoodteam.R;

public class LoadingStartApp {
    final Activity activity;
    AlertDialog dialog;

    public LoadingStartApp(Activity myActivity){
        activity = myActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.DialogTheme);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.wasted_food_run_screen,null));
        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }
}
