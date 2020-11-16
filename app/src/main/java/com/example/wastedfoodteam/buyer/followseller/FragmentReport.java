package com.example.wastedfoodteam.buyer.followseller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.BuyHomeActivity;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Seller;

import java.util.HashMap;
import java.util.Map;

public class FragmentReport extends Fragment {
    TextView tvAccused;
    EditText etContent;
    Button btnCommit;
    Seller seller;
    String accusedName;
    String content ="";
    String url;
    String accusedId;
    String reporterId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report, container, false);
        seller = (Seller) getArguments().get("SELLER");
        //mapping
        mapping(view);
        //get param
        url = Variable.ipAddress + "FeedbackReport/report.php";
        accusedName = seller.getName();
        reporterId = Variable.ACCOUNT_ID+"";
        tvAccused.setText(seller.getName());

        accusedId = seller.getId()+"";
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = etContent.getText().toString();
                //TODO don't know get url image
                insertData(url, reporterId, accusedId, content,"");

            }
        });
        return view;
    }

    private void insertData(final String url, final String reporter_id, final String accused_id, final String report_text, final String report_image) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                switch (response) {
                    case "ERROR":
                        Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "OK Insert data", Toast.LENGTH_LONG).show();
                        try {

                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "ERROR " + url, Toast.LENGTH_LONG).show();//TODO get data
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("reporter_id", reporter_id);
                params.put("accused_id", accused_id);
                params.put("report_text", report_text);
                params.put("report_image", report_image);
                return params;
            }
        };
        requestQueue.add(stringRequest);

    }

    private void mapping(View view) {
        tvAccused = view.findViewById(R.id.tvAccusedFR);
        etContent = view.findViewById(R.id.etContentFR);
        btnCommit = view.findViewById(R.id.btnCommitFR);
    }
}
