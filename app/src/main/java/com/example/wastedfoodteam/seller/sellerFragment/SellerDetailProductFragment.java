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
import com.example.wastedfoodteam.utils.CameraStorageFunction;

public class SellerDetailProductFragment extends Fragment {
    //ui view
    private ImageView iv_detail_product_icon;
    private EditText name;
    private EditText originalPrice;
    private EditText sellPrice;
    private EditText openTime;
    private EditText closeTime;
    private EditText saleDate;
    private Button btn_detail_product_add;

    CameraStorageFunction cameraStorageFunction;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_seller_detail_product, container, false);
        //unit ui
        iv_detail_product_icon = view.findViewById(R.id.iv_detail_product_icon);

        cameraStorageFunction = new CameraStorageFunction(getActivity(),getContext());

        //
        iv_detail_product_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraStorageFunction.pickFromGallery();
            }
        });



        return view;
    }


}