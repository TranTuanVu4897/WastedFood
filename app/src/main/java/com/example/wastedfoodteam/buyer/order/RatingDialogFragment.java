package com.example.wastedfoodteam.buyer.order;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;

import java.util.HashMap;
import java.util.Map;

public class RatingDialogFragment extends DialogFragment {
    private RatingBar rbRating;
    private EditText etRating;
    private String urlUpdateRating;
    private int rating;
    Activity activity;
    Order order;

    public RatingDialogFragment(Activity activity, Order order) {
        this.activity = activity;
        this.order = order;
        rating = 5;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        urlUpdateRating = Variable.ipAddress + Variable.UPDATE_RATING;
        rbRating = activity.findViewById(R.id.rbRating);
        etRating = activity.findViewById(R.id.etRating);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());
        builder.setView(activity.getLayoutInflater().inflate(R.layout.dialog_buyer_rating, null))
                .setPositiveButton("Gửi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendRating();
                    }
                })
                .setNegativeButton("Bỏ qua", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RatingDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    private void sendRating() {
        RequestQueue requestInsertOrder = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequestInsert = new StringRequest(Request.Method.POST, urlUpdateRating, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("SUCCESS")) {
                    Toast.makeText(getActivity().getApplicationContext(), "Thành công", Toast.LENGTH_LONG);
                    RatingDialogFragment.this.getDialog().cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Lỗi hệ thống: " + error.getMessage(), Toast.LENGTH_LONG);
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("buyer_id", order.getBuyer_id() + "");//TODO change to other ways
                params.put("product_id", order.getProduct_id() + "");
                params.put("rating", rbRating.getRating() + "");
                params.put("buyer_comment", etRating.getText().toString());
                return params;
            }
        };
        requestInsertOrder.add(stringRequestInsert);
    }


}