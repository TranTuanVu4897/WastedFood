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
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wastedfoodteam.LoginActivity;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;
import com.example.wastedfoodteam.buyer.followseller.FragmentListSellerFollow;
import com.example.wastedfoodteam.buyer.infomation.FragmentEditInformationBuyer;
import com.example.wastedfoodteam.buyer.order.FragmentOrderHistory;
import com.example.wastedfoodteam.global.Variable;

import com.example.wastedfoodteam.seller.notification.NotificationFragment;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.seller.sellerFragment.SendFeedbackSellerFragment;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.GPSTracker;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class BuyHomeActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 2;
    ImageButton ibUserInfo;
    FragmentListProduct fragmentListProduct;
    NotificationUtil notificationUtil;
    GPSTracker gps;
    private BottomNavigationView navigation;
    TextView tv_nav_header_buyer_user_name;
    ImageView iv_nav_header_buyer_profile_image;
    private DrawerLayout drawerLayout;

    @SuppressLint("SetTextI18n")
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


        Variable.CURRENT_USER = "BUYER";
        notificationUtil = new NotificationUtil();

        //get GPS
        gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            Variable.gps = gps.getLocation();
        } else {
            gps.showSettingAlert();
        }

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view_buyer);
        View headerView = navigationView.getHeaderView(0);

        tv_nav_header_buyer_user_name = headerView.findViewById(R.id.tv_nav_header_buyer_user_name);
        iv_nav_header_buyer_profile_image = headerView.findViewById(R.id.iv_nav_header_buyer_profile_image);

        tv_nav_header_buyer_user_name.setText(Variable.BUYER.getName() + "");


        CommonFunction.setImageViewSrc(this, Variable.BUYER.getImage(), iv_nav_header_buyer_profile_image);

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
                final int id = item.getItemId();
                switch (id) {
                    case R.id.itemNavMenuBuyerInfor:
                        FragmentEditInformationBuyer fragment = new FragmentEditInformationBuyer();
                        FragmentManager manager = getSupportFragmentManager();
                        manager.beginTransaction().replace(R.id.flSearchResultAH, fragment, fragment.getTag()).commit();
                        break;
                    case R.id.itemNavLogout:
                        Variable.BUYER = null;
                        switch (Variable.CHECK_LOGIN) {
                            case 2:
                                finish();
                                signOutFacebook();
                                break;
                            case 0:
                                finish();
                                startActivity(new Intent(BuyHomeActivity.this, LoginActivity.class));
                                break;
                            case 1:
                                finish();
                                signOutGoogle();
                                break;
                        }
                        break;
                    case R.id.itemNavFeedBack:
                        SendFeedbackSellerFragment sendFeedbackSellerFragment = new SendFeedbackSellerFragment(new SendFeedbackSellerFragment.HandleSendFeedBack() {
                            @Override
                            public void onSuccess() {


                            }
                        }, Variable.BUYER.getId());
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.flSearchResultAH, sendFeedbackSellerFragment, sendFeedbackSellerFragment.getTag()).commit();
                        break;
                    case R.id.itemNavFollowSeller:
                        addFragmentSellerFollow();
                        break;
                    case R.id.itemNavOrderHistory:
                        FragmentOrderHistory fragmentOrderHistory = new FragmentOrderHistory();
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.flSearchResultAH, fragmentOrderHistory, "")
                                .addToBackStack(null)
                                .commit();
                        break;
                    case R.id.itemNavNotification:
                        NotificationFragment notificationFragment = new NotificationFragment(Variable.BUYER.getId() + "");
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.flSearchResultAH, notificationFragment, "")
                                .addToBackStack(null)
                                .commit();
                        notificationUtil.updateNotificationSeen(getApplicationContext(), Variable.BUYER.getId(), navigation);
                        break;
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout_buyer);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        addFragmentListProduct();

        //bottom navigation
        navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_buyer);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        notificationUtil.getTotalNotification(getApplicationContext(), Variable.BUYER.getId(), navigation);
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
                    NotificationFragment notificationFragment = new NotificationFragment(Variable.BUYER.getId() + "");
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
                    NotificationFragment notificationFragment = new NotificationFragment(Variable.BUYER.getId() + "");
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flSearchResultAH, notificationFragment, "")
                            .addToBackStack(null)
                            .commit();
                    notificationUtil.updateNotificationSeen(getApplicationContext(), Variable.BUYER.getId(), navigation);
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

        SharedPreferences sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().apply();

        finishAndRemoveTask();
        Toast.makeText(BuyHomeActivity.this, "Sign out Success", Toast.LENGTH_LONG).show();
        startActivity(new Intent(BuyHomeActivity.this, LoginActivity.class));
    }

    private void signOutGoogle() {
        try {
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(
                    getApplicationContext(),
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
            ).signOut();

            SharedPreferences sharedpreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
            sharedpreferences.edit().clear().apply();

            finishAndRemoveTask();
            startActivity(new Intent(BuyHomeActivity.this, LoginActivity.class));

        } catch (Exception e) {
            Log.d("e: ", e.getMessage());
        }

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