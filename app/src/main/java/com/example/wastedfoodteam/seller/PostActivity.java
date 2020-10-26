package com.example.wastedfoodteam.seller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wastedfoodteam.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    private  DatabaseReference databaseReference;
    ListView lvProduct;
    ArrayList<Product1> arrayList = new ArrayList<>();
    ArrayAdapter<Product1> arrayAdapter;
    Product1 product;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        //init
        btnAdd = findViewById(R.id.btnAdd);
        lvProduct = findViewById(R.id.lvProduct);

        product = new Product1();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child("2").child("Products");

        arrayAdapter = new ArrayAdapter<Product1>(this, android.R.layout.simple_list_item_1,arrayList);
        lvProduct.setAdapter(arrayAdapter);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product1 value = snapshot.getValue(Product1.class);
                //String value = snapshot.getValue(String.class);
                arrayList.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //btnAdd handle
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, AddProductActivity.class);
                startActivity(intent);
            }
        });

    }
}
