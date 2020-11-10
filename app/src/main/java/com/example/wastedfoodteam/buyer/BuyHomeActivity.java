package com.example.wastedfoodteam.buyer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;
import com.example.wastedfoodteam.buyer.order.FragmentOrderHistory;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.utils.FilterDialog;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;

import java.sql.Time;

public class BuyHomeActivity extends AppCompatActivity {
    Button btnLogout, btnFollow, btnHistory, btnHome;
    ImageView ivAppIcon;
    ImageButton ibUserInformation, ibFilter;
    Bundle bundle;
    EditText etSearch;
    FragmentListProduct fragmentListProduct;


    private Toolbar toolbar;

    private DrawerLayout drawerUserInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_home);

        //mapping
        btnLogout = findViewById(R.id.btnLogout);
        btnFollow = findViewById(R.id.btnFollow);
        btnHistory = findViewById(R.id.btnHistory);
        btnHome = findViewById(R.id.btnHome);
        ivAppIcon = findViewById(R.id.ivAppIcon);
        etSearch = findViewById(R.id.etSearchBHA);
        ibFilter = findViewById(R.id.ibFilter);
        ibUserInformation = findViewById(R.id.ibUserInfo);


        SharedPreferences pre = getSharedPreferences("my_data", MODE_PRIVATE);
        String name = pre.getString("name", "khong thay");

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);
        View headerView = navigationView.getHeaderView(0);


        // Find our drawer view
        drawerUserInformation = (DrawerLayout) findViewById(R.id.drawer_layout_buyer);

        //or maybe u can use button instead
        ibUserInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerUserInformation.openDrawer(GravityCompat.START);
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


//        if(Variable.CHECK_LOGIN == 2){
//                //resultFacebook();
//        }


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
                        .replace(R.id.flSearchResultAH, fragmentOrderHistory, "")
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentHome();
            }
        });

        openFragmentHome();
    }

    private void openFragmentHome() {
        fragmentListProduct = new FragmentListProduct();

        //add fragment search result
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flSearchResultAH, fragmentListProduct, "")
                .addToBackStack(null)
                .commit();


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
                drawerUserInformation.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void signOutFacebook() {
        LoginManager.getInstance().logOut();
        Toast.makeText(BuyHomeActivity.this, "Sign out Success", Toast.LENGTH_LONG).show();
        startActivity(new Intent(BuyHomeActivity.this, MainActivity.class));
    }

    private void changeListProductItem(){
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