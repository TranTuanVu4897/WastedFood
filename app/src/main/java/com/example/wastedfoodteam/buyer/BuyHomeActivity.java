package com.example.wastedfoodteam.buyer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;
import com.example.wastedfoodteam.global.Variable;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;

public class BuyHomeActivity extends AppCompatActivity {
    Button btnLogout, btnHome;
    ImageView imageView, imageButton;
    Bundle bundle;
    EditText etSearch;


    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_home);

        //mapping
        btnLogout = findViewById(R.id.btnLogout);
        imageView = findViewById(R.id.imageView);
        etSearch = findViewById(R.id.etSearchBHA);
        imageButton = findViewById(R.id.imageButton);
        btnHome = findViewById(R.id.btnHome);


        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        String name = pre.getString("name", "khong thay");

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);
        View headerView = navigationView.getHeaderView(0);


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
                if (id == R.id.item_nav_drawer_menu_buyer_information) {
                    //nhớ này muốn sửa đoạn header của drawer navigation thì vào nav_header_buyer và sửa và xem menu thì vào nav_header_buyer
                    FragmentEditInformationBuyer fragment = new FragmentEditInformationBuyer();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.flSearchResultAH, fragment, fragment.getTag()).commit();
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout_buyer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        if (Variable.CHECK_LOGIN == 2) {
            //resultFacebook();
        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Variable.CHECK_LOGIN) {
                    case 2:
                        signOutFacebook();
                        break;
                    case 0:
                        startActivity(new Intent(BuyHomeActivity.this, MainActivity.class));
                        break;
                }
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeFragment();
            }
        });

        homeFragment();


    }


    public void homeFragment() {
        //add fragment search result
        FragmentListProduct fragmentListProduct = new FragmentListProduct();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flSearchResultAH, fragmentListProduct, "SEARCH_HOME")
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

//    private void resultFacebook() {
//        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                Log.d("Json", response.getJSONObject().toString());
//                try {
//
////                    txtEmail.setText("Email:" + object.getString("email"));
////                    txtName.setText("Name:" + object.getString("name"));
////                    txtId.setText("id:" + object.getString("id"));
//                    String idF = object.getString("id");
//                    etSearch.setText("id:" + object.getString("id"));
//                    String imageF = "https://graph.facebook.com/" + idF + "/picture?type=large";
//                    Glide.with(BuyHomeActivity.this).load(imageF).into(imageView);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Bundle parameter = new Bundle();
//        parameter.putString("fields", "id,name,email,gender,birthday");
//        graphRequest.setParameters(parameter);
//        graphRequest.executeAsync();
//        Log.d("Tag: ", "failed");
//    }

}