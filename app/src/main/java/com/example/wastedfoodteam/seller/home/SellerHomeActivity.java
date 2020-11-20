package com.example.wastedfoodteam.seller.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.notification.NotificationFragment;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.seller.sellerFragment.AddProductFragment;
import com.example.wastedfoodteam.seller.sellerFragment.ChangePasswordSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.EditSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.ListOrderHistoryFragment;
import com.example.wastedfoodteam.seller.sellerFragment.ListProductSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.SellerHomeFragment;
import com.example.wastedfoodteam.seller.sellerFragment.SendFeedbackSellerFragment;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.SendNotificationPackage.APIService;
import com.example.wastedfoodteam.utils.SendNotificationPackage.Client;
import com.example.wastedfoodteam.utils.SendNotificationPackage.Data;
import com.example.wastedfoodteam.utils.SendNotificationPackage.MyFireBaseMessagingService;
import com.example.wastedfoodteam.utils.SendNotificationPackage.MyResponse;
import com.example.wastedfoodteam.utils.SendNotificationPackage.NotificationSender;
import com.example.wastedfoodteam.utils.SendNotificationPackage.SendNotif;
import com.example.wastedfoodteam.utils.SendNotificationPackage.Token;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SellerHomeActivity extends AppCompatActivity {

    private ImageButton information_tab_seller;

    private DrawerLayout drawerLayout;

    private NotificationUtil notificationUtil;

    private CircleImageView iv_nav_header_profile_image;

    private TextView tv_nav_header_user_name;

    private BottomNavigationView navigation;

    Seller seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        seller = new Seller();
        Intent intent = new Intent();
        notificationUtil = new NotificationUtil();

        final SendNotif sendNotif = new SendNotif();
        sendNotif.UpdateToken();

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        //add to tab navigation header
        iv_nav_header_profile_image = (CircleImageView ) headerView.findViewById(R.id.iv_nav_header_profile_image);
        tv_nav_header_user_name = headerView.findViewById(R.id.tv_nav_header_user_name);

        information_tab_seller = findViewById(R.id.information_tab_seller);

        //button for open drawer layout
        information_tab_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //TODO đường dẫn ảnh firebase
        Glide.with(this).load(Variable.SELLER.getImage()).into(iv_nav_header_profile_image);
        tv_nav_header_user_name.setText(Variable.SELLER.getName());



        /*//button for seller add product
        btn_seller_home_fragment = (FloatingActionButton)  findViewById(R.id.fab);
        btn_seller_home_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductFragment addProductFragment = new AddProductFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_main, addProductFragment, addProductFragment.getTag()).commit();
            }
        });*/

        // Find our drawer view
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //init navigation view
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.home){
                    SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();
                } else if(id == R.id.item_nav_drawer_menu_information){
                    EditSellerFragment editSellerFragment = new EditSellerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,editSellerFragment,editSellerFragment.getTag()).commit();
                } else if(id == R.id.item_nav_drawer_menu_change_password){
                    ChangePasswordSellerFragment changePasswordSellerFragment = new ChangePasswordSellerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,changePasswordSellerFragment,changePasswordSellerFragment.getTag()).commit();
                }else if (id == R.id.item_nav_drawer_menu_feedback){
                    SendFeedbackSellerFragment sendFeedbackSellerFragment = new SendFeedbackSellerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,sendFeedbackSellerFragment,sendFeedbackSellerFragment.getTag()).commit();
                }else if(id == R.id.item_nav_drawer_menu_manager){
                    ListProductSellerFragment listProductSellerFragment = new ListProductSellerFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,listProductSellerFragment,listProductSellerFragment.getTag()).commit();
                }else if(id == R.id.item_nav_drawer_menu_history){
                    ListOrderHistoryFragment listOrderHistoryFragment = new ListOrderHistoryFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,listOrderHistoryFragment,listOrderHistoryFragment.getTag()).commit();
                }else if(id == R.id.item_nav_drawer_menu_alert){
                    NotificationFragment notificationFragment = new NotificationFragment();
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,notificationFragment,notificationFragment.getTag()).commit();
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //bottom navigation
        navigation = (BottomNavigationView) findViewById(R.id.bottom_nav_seller);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //set activity to home fragment
        SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();

        notificationUtil.getTotalNotification(getApplicationContext(), Variable.SELLER.getId(),navigation);

        //notification badge
        if(Variable.TOTAL_NOTIFICATION > 0) {
            BadgeDrawable badge = navigation.getOrCreateBadge(R.id.item_bottom_nav_menu_notification);
            badge.setVisible(true);
            badge.setNumber(Variable.TOTAL_NOTIFICATION);
        }
        String type = getIntent().getStringExtra("From");
        if (type != null) {
            switch (type) {
                case "notifyFrag":
                    NotificationFragment notificationFragment = new NotificationFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_main,notificationFragment,notificationFragment.getTag()).commit();
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
                case R.id.item_bottom_nav_menu_home:
                    SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                    manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();
                    return true;
                case R.id.item_bottom_nav_menu_add:
                    AddProductFragment addProductFragment = new AddProductFragment();
                    manager.beginTransaction().replace(R.id.content_main, addProductFragment, addProductFragment.getTag()).commit();
                    return true;
                case R.id.item_bottom_nav_menu_notification:
                    NotificationFragment notificationFragment = new NotificationFragment();
                    manager.beginTransaction().replace(R.id.content_main,notificationFragment,notificationFragment.getTag()).commit();
                    notificationUtil.updateNotificationSeen(getApplicationContext(),Variable.SELLER.getId(),navigation);
                    return true;
            }
            return false;
        }
    };

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

}