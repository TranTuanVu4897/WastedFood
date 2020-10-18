package com.example.wastedfoodteam.buy;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastedfoodteam.R;

public class BuyHomeActivityTest extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
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
