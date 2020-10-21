package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.seller.Product;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class PostSellerFragment extends Fragment {

    private DatabaseReference databaseReference;
    ListView lvProduct;
    ArrayList<Product> arrayList = new ArrayList<>();
    ArrayAdapter<Product> arrayAdapter;
    Product product;
    private Button btn_post_add_product;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_seller, container, false);

        //init ui
        btn_post_add_product = view.findViewById(R.id.btn_post_add_product);
        lvProduct = view.findViewById(R.id.lvProduct);

        return view;
    }
}