package com.example.wastedfoodteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.login.FragmentLoginBuyer;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //fragment for login
        FragmentLoginBuyer fragment = new FragmentLoginBuyer();


        fragmentTransaction.add(R.id.flFragmentLayoutAM, fragment);
        fragmentTransaction.commit();


    }


}