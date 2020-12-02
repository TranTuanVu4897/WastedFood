package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.seller.sellerAdapter.SellerOrder;
import com.example.wastedfoodteam.utils.CommonFunction;

public class SellerOrderDetailFragment extends Fragment {
    EditText etProductName,etQuantity,etTotalCost,etBuyerName,etDescription;
    ImageView ivBuyer;
    Button btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        SellerOrder order= (SellerOrder) bundle.getSerializable("order");
        View view = inflater.inflate(R.layout.fragment_seller_order_detail, container, false);
        etProductName = view.findViewById(R.id.et_order_productName);
        etQuantity = view.findViewById(R.id.et_order_quantity);
        etTotalCost = view.findViewById(R.id.et_order_total_cost);
        etBuyerName = view.findViewById(R.id.et_order_buyer_name);
        etDescription = view.findViewById(R.id.et_order_description);
        btnBack = view.findViewById(R.id.btn_order_back);
        etProductName.setText(order.getProduct_name());
        etQuantity.setText(String.valueOf(order.getQuantity()));
        etTotalCost.setText(CommonFunction.getCurrency(order.getTotal_cost()));
        etDescription.setText(order.getBuyer_comment());
        etBuyerName.setText(order.getBuyer_name());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetailSellerFragment productDetailSellerFragment = new ProductDetailSellerFragment();
                //open seller detail product fragment
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_main, productDetailSellerFragment, "")//TODO check if this work
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }
}