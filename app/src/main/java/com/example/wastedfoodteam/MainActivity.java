package com.example.wastedfoodteam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.wastedfoodteam.login.FragmentLoginBuyer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Vutt add fragment by code
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //fragment for login
        FragmentLoginBuyer fragment = new FragmentLoginBuyer();

        //fragment for buyer buy product

        fragmentTransaction.add(R.id.flFragmentLayoutAM,fragment);
        fragmentTransaction.commit();
    }
}