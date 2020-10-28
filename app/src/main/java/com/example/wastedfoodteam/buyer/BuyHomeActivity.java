package com.example.wastedfoodteam.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;
import com.example.wastedfoodteam.seller.sellerFragment.ChangePasswordSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.EditSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.ListProductSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.SellerHomeFragment;
import com.example.wastedfoodteam.seller.sellerFragment.SendFeedbackSellerFragment;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class BuyHomeActivity extends AppCompatActivity {
    Button btnLogout;
    ImageView imageView,imageButton;
    String checkOption = "";
    Bundle bundle;
    EditText etSearch;


    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_home);
        btnLogout = findViewById(R.id.btnLogout);
        imageView = findViewById(R.id.imageView);
        etSearch = findViewById(R.id.etSearchBHA);
        imageButton = findViewById(R.id.imageButton);
        SharedPreferences pre = getSharedPreferences("my_data",MODE_PRIVATE);
        String name = pre.getString("name","khong thay");

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);
        View headerView = navigationView.getHeaderView(0);



        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar_buyer);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-) can change in resource
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Find our drawer view
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_buyer);

        //or maybe u can use button instead
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //init navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view_buyer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                DrawerLayout drawer = findViewById(R.id.drawer_layout_buyer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        bundle = getIntent().getExtras();
        if(bundle != null){
            checkOption = bundle.getString("Check_option");
        }
        if(checkOption.equals("2")){
                resultFacebook();
        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (checkOption){
                    case "2":
                        signOutFacebook();
                        break;
                    case "1":
                        startActivity(new Intent(BuyHomeActivity.this,MainActivity.class));
                        break;
                }
            }
        });

        FragmentListProduct fragmentListProduct = new FragmentListProduct();

        //add fragment search result
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flSearchResultAH, fragmentListProduct, "")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
        Toast.makeText(BuyHomeActivity.this, "Sign out Success", Toast.LENGTH_LONG).show();
        startActivity(new Intent(BuyHomeActivity.this, MainActivity.class));
    }

    private void resultFacebook() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("Json", response.getJSONObject().toString());
                try {

//                    txtEmail.setText("Email:" + object.getString("email"));
//                    txtName.setText("Name:" + object.getString("name"));
//                    txtId.setText("id:" + object.getString("id"));
                    String idF = object.getString("id");
                    etSearch.setText("id:" + object.getString("id"));
                    String imageF = "https://graph.facebook.com/" + idF + "/picture?type=large";
                    Glide.with(BuyHomeActivity.this).load(imageF).into(imageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameter = new Bundle();
        parameter.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameter);
        graphRequest.executeAsync();
        Log.d("Tag: ", "failed");
    }

}