package com.example.wastedfoodteam.buyer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.FragmentListProduct;

public class FragmentHome extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_buy_home,container,false);
        FragmentListProduct fragmentListProduct = new FragmentListProduct();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flSearchResultAH,fragmentListProduct, "")
                .addToBackStack(null)
                .commit();
        return view;
    }


}
