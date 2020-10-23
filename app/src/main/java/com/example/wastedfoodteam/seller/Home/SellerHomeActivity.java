package com.example.wastedfoodteam.seller.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.seller.AddProductActivity;
import com.example.wastedfoodteam.seller.PostActivity;
import com.example.wastedfoodteam.seller.editSeller.EditSellerActivity;
import com.example.wastedfoodteam.seller.sellerFragment.AddProductFragment;
import com.example.wastedfoodteam.seller.sellerFragment.ChangePasswordSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.EditSellerFragment;
import com.example.wastedfoodteam.seller.sellerFragment.SellerHomeFragment;
import com.example.wastedfoodteam.source.model.Seller;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class SellerHomeActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private FloatingActionButton btn_seller_home_fragment;

    private CircleImageView iv_nav_header_profile_image;

    private TextView tv_nav_header_user_name;

    Seller seller;


    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
    private ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        //TODO get seller from login
        seller = new Seller(1,"Tran Tuan Vu" , "12345678" , "gs://fir-demo-24196.appspot.com/1602521123529.jpg" , "123 Hoa Lac" , 21.0 , 13.0 , "123"  );

        //get the header view
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        //add to tab navigation header
        iv_nav_header_profile_image = (CircleImageView ) headerView.findViewById(R.id.iv_nav_header_profile_image);
        tv_nav_header_user_name = headerView.findViewById(R.id.tv_nav_header_user_name);


        //TODO đường dẫn ảnh firebase
        Glide.with(this).load(seller.getImage()).into(iv_nav_header_profile_image);
        tv_nav_header_user_name.setText(seller.getName());


        //button for seller add product
        btn_seller_home_fragment = (FloatingActionButton)  findViewById(R.id.fab);
        btn_seller_home_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SellerHomeActivity.this,"Make a Post",Toast.LENGTH_LONG).show();
                AddProductFragment addProductFragment = new AddProductFragment();
                FragmentManager manager = getSupportFragmentManager();
                manager.beginTransaction().replace(R.id.content_main, addProductFragment, addProductFragment.getTag()).commit();
            }
        });

        // Set a Toolbar to replace the ActionBar.
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-) can change in resource
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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
                    //pass data to edit seller fragment
                    Bundle editSellerBundler = new Bundle();
                    editSellerBundler.putString("name",seller.getName());
                    editSellerBundler.putString("address",seller.getAddress());
                    editSellerBundler.putString("description",seller.getDescription());
                    editSellerBundler.putInt("id" , seller.getAccount_id());
                    EditSellerFragment editSellerFragment = new EditSellerFragment();
                    editSellerFragment.setArguments(editSellerBundler);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,editSellerFragment,editSellerFragment.getTag()).commit();
                } else if(id == R.id.item_nav_drawer_menu_change_password){
                    //pass data to edit seller fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("password",seller.getPassword());
                    ChangePasswordSellerFragment changePasswordSellerFragment = new ChangePasswordSellerFragment();
                    changePasswordSellerFragment.setArguments(bundle);
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.content_main,changePasswordSellerFragment,changePasswordSellerFragment.getTag()).commit();
                }
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //set activity to home fragment
        SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();

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






}