package com.example.wastedfoodteam.buyer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;
import com.example.wastedfoodteam.buyer.followseller.FragmentListSellerFollow;
import com.example.wastedfoodteam.buyer.infomation.FragmentEditInformationBuyer;
import com.example.wastedfoodteam.buyer.order.FragmentOrderHistory;
import com.example.wastedfoodteam.global.Variable;

import com.example.wastedfoodteam.seller.notification.NotificationFragment;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.seller.sellerFragment.AddProductFragment;
import com.example.wastedfoodteam.seller.sellerFragment.SellerHomeFragment;
import com.example.wastedfoodteam.utils.FilterDialog;
import com.example.wastedfoodteam.utils.GPSTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.Time;
import java.util.List;

public class BuyHomeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    ImageView ivAppIcon;
    ImageButton ibUserInfo, ibFilter;
    FragmentListProduct fragmentListProduct;
    NotificationUtil notificationUtil;
    GPSTracker gps;
    private BottomNavigationView navigation;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInApi mGoogleSignInApi;

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

                finishAndRemoveTask();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //mapping
        ibUserInfo = findViewById(R.id.ibUserInfo);
        ibFilter = findViewById(R.id.ibFilter);
        Variable.CURRENT_USER = "BUYER";
        notificationUtil = new NotificationUtil();

        //get GPS
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            Variable.gps = gps.getLocation();
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                    + gps.getLatitude() + "\nLong: " + gps.getLongitude(), Toast.LENGTH_LONG).show();
        } else {
            gps.showSettingAlert();
        }

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);
        View headerView = navigationView.getHeaderView(0);

        // Find our drawer view
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_buyer);

        //or maybe u can use button instead
        ibUserInfo.setOnClickListener(new View.OnClickListener() {
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
                switch (id) {
                    case R.id.itemNavMenuBuyerInfor:
                        //nhớ này muốn sửa đoạn header của drawer navigation thì vào nav_header_buyer và sửa và xem menu thì vào nav_header_buyer
                        FragmentEditInformationBuyer fragment = new FragmentEditInformationBuyer();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.flSearchResultAH, fragment, fragment.getTag()).commit();
                        break;
                    case R.id.itemNavLogout:
                        Variable.ACCOUNT_ID = 0;
                        switch (Variable.CHECK_LOGIN) {
                            case 2:
                                finish();
                                signOutFacebook();
                                break;
                            case 0:
                                finish();
                                startActivity(new Intent(BuyHomeActivity.this, MainActivity.class));
                                break;
                            case 1:
                                finish();
                                signOutGoogle();
                                break;
                        }
                        break;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout_buyer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        ibFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterDialog filterDialog = new FilterDialog(getLayoutInflater(), BuyHomeActivity.this);
                filterDialog.showFilterDialog(new FilterDialog.ModifyFilter() {
                    @Override
                    public void onClear() {
                        Variable.startTime = null;
                        Variable.endTime = null;
                        Variable.distance = "20";
                        Variable.discount = null;

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

        //bottom navigation
        navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_buyer);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        notificationUtil.getTotalNotification(getApplicationContext(), Variable.ACCOUNT_ID, navigation);
        //notification badge
        if (Variable.TOTAL_NOTIFICATION > 0) {
            BadgeDrawable badge = navigation.getOrCreateBadge(R.id.item_bottom_nav_menu_notification);
            badge.setVisible(true);
            badge.setNumber(Variable.TOTAL_NOTIFICATION);
        }
        String type = getIntent().getStringExtra("From");
        if (type != null) {
            switch (type) {
                case "notifyFrag":
                    NotificationFragment notificationFragment = new NotificationFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flSearchResultAH, notificationFragment, "")
                            .addToBackStack(null)
                            .commit();
            }
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.item_bottom_nav_menu_buyer_home:
                    addFragmentListProduct();
                    return true;
                case R.id.item_bottom_nav_menu_buyer_notification:
                    NotificationFragment notificationFragment = new NotificationFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flSearchResultAH, notificationFragment, "")
                            .addToBackStack(null)
                            .commit();
                    notificationUtil.updateNotificationSeen(getApplicationContext(), Variable.ACCOUNT_ID, navigation);
                    return true;
                case R.id.item_bottom_nav_menu_buyer_history:
                    FragmentOrderHistory fragmentOrderHistory = new FragmentOrderHistory();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flSearchResultAH, fragmentOrderHistory, "")
                            .addToBackStack(null)
                            .commit();
                    return true;
                case R.id.item_bottom_nav_menu_buyer_follow:
                    addFragmentSellerFollow();
                    return true;
            }
            return false;
        }
    };

    private void addFragmentListProduct() {
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
        try {
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(
                    getApplicationContext(),
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut();
            startActivity(new Intent(BuyHomeActivity.this, MainActivity.class));

        } catch (Exception e) {
            Log.d("e: ", e.getMessage());
        }

    }

    private void changeListProductItem() {
        fragmentListProduct.createNewArrayProduct();
        fragmentListProduct.getData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}