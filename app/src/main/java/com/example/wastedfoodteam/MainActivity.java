package com.example.wastedfoodteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.example.wastedfoodteam.buy.FragmentHome;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.login.FragmentLoginBuyer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Variable.gps = new LatLng(21.0110168,105.5182143);

        //Vutt add fragment by code
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //fragment for login
        FragmentLoginBuyer fragment = new FragmentLoginBuyer();

        //fragment for buyer buy product
//        FragmentHome fragment = new FragmentHome();

        fragmentTransaction.add(R.id.flFragmentLayoutAM, fragment);
        fragmentTransaction.commit();


    }


}