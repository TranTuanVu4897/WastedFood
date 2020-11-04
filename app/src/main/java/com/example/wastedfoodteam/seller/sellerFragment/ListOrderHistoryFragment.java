package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;

import java.util.ArrayList;

public class ListOrderHistoryFragment extends Fragment {

    ListView lvOrder;
    ArrayList<Order> arrOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_order_history, container, false);
        lvOrder = view.findViewById(android.R.id.list);
        arrOrder = new ArrayList<Order>();

        return view;
    }
}