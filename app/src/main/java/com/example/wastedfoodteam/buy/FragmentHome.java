package com.example.wastedfoodteam.buy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentHome extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        FragmentListProduct fragmentListProduct = new FragmentListProduct();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.flSearchResultAH,fragmentListProduct, "")
                .addToBackStack(null)
                .commit();
        return view;
    }


}
