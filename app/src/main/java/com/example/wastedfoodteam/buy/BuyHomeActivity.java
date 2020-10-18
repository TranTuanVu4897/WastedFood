package com.example.wastedfoodteam.buy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wastedfoodteam.R;

public class BuyHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_home);
        FragmentListProduct fragmentListProduct = new FragmentListProduct();

        //add fragment search result
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flSearchResultAH,fragmentListProduct, "")
                .addToBackStack(null)
                .commit();
    }
}