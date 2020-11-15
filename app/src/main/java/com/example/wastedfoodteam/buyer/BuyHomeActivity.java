package com.example.wastedfoodteam.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;
import com.example.wastedfoodteam.buyer.order.FragmentOrderHistory;
import com.example.wastedfoodteam.global.Variable;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BuyHomeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    Button btnHome, btnLogout, btnFollow, btnHistory;
    ImageView ivAppIcon;
    ImageButton ibUserInfo, ibFilter;
    EditText etSearch;
    FragmentListProduct fragmentListProduct;
    GPSTracker gps;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInApi mGoogleSignInApi;



    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_CODE_PERMISSION);

                //TODO dialog confirm
                finishAndRemoveTask();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //mapping
        btnLogout = findViewById(R.id.btnLogout);
        btnFollow = findViewById(R.id.btnFollow);
        btnHistory = findViewById(R.id.btnHistory);
        imageView = findViewById(R.id.ivAppIcon);
        etSearch = findViewById(R.id.etSearchBHA);
        imageButton = findViewById(R.id.ibUserInfo);

        //get GPS
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            Variable.gps = gps.getLocation();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + gps.getLatitude() + "\nLong: " + gps.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingAlert();
        }

        SharedPreferences pre = getSharedPreferences("my_data",MODE_PRIVATE);
        String name = pre.getString("name","khong thay");

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
                if(id == R.id.item_nav_drawer_menu_buyer_information){
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



//        if(Variable.CHECK_LOGIN == 2){
//                //resultFacebook();
//        }


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (Variable.CHECK_LOGIN){
                    case 2:
                        signOutFacebook();
                        break;
                    case 0:
                        startActivity(new Intent(BuyHomeActivity.this,MainActivity.class));
                        break;
                    case 1:
                        signOutGoogle();
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
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentSellerFollow();
            }
        });


        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentOrderHistory fragmentOrderHistory = new FragmentOrderHistory();
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.flSearchResultAH,fragmentOrderHistory,"")
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentListProduct();
            }
        });

        ibFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filterDialog = new FilterDialog(getLayoutInflater(), BuyHomeActivity.this);
                filterDialog.showFilterDialog(new FilterDialog.ModifyFilter() {
                    @Override
                    public void onClear() {
                        Variable.startTime = Time.valueOf("11:00:00");
                        Variable.endTime = Time.valueOf("22:00:00");
                        Variable.distance = "20";
                        Variable.discount = "90";

                        if (fragmentListProduct != null) {
                            changeListProductItem();
                        }
                    }

                    @Override
                    public void onChange() {
                        if (fragmentListProduct != null) {
                            changeListProductItem();
                        }
                    }


                });
            }
        });

        addFragmentListProduct();

    }
    public void addFragmentSellerFollow() {

        FragmentListSellerFollow fragment = new FragmentListSellerFollow();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.flSearchResultAH, fragment, fragment.getTag()).commit();
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

    private void signOutGoogle() {
try{
    FirebaseAuth.getInstance().signOut();
    GoogleSignIn.getClient(
            getApplicationContext(),
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
    ).signOut();
    startActivity(new Intent(BuyHomeActivity.this, MainActivity.class));
//    mGoogleSignInClient.signOut()
////            .addOnCompleteListener(this, new OnCompleteListener<Void>() {
////                @Override
////                public void onComplete(@NonNull Task<Void> task) {
////                    startActivity(new Intent(BuyHomeActivity.this, MainActivity.class));
////                }
////            });

} catch (Exception e){
    Log.d("e: ", e.getMessage());
}

    }


    private void changeListProductItem() {
        fragmentListProduct.createNewArrayProduct();
        fragmentListProduct.getData();
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