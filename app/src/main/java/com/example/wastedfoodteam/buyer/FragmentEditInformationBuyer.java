package com.example.wastedfoodteam.buyer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentEditInformationBuyer extends Fragment {
    EditText etName, etPhone, etDob;
    RadioButton rbBoy, rbGirl;
    String urlGetData = "";
    Buyer buyer;
    TextView tvMail;
    ImageView ivAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_buyer, container, false);
        mapping(view);
        resultFacebook();
        urlGetData = Variable.ipAddress + "information/information.php?id=305";
        buyer = getData(urlGetData);
        etDob.setText(buyer.getDate_of_birth().toString());
        if(buyer.isGender()){
            rbBoy.setChecked(true);
        } else {
            rbGirl.setChecked(true);
        }

        return view;
    }

    private void mapping(View view) {
        etName = view.findViewById(R.id.etBuyerNameFEB);
        etDob = view.findViewById(R.id.etEditBuyerDateofBirth);
        etPhone = view.findViewById(R.id.etEditPhoneFEB);
        rbBoy = view.findViewById(R.id.rbBoy);
        rbGirl = view.findViewById(R.id.rbGirl);
        tvMail = view.findViewById(R.id.tvMailFEB);
        ivAvatar = view.findViewById(R.id.ivBuyerAvatarFEB);
    }

    private Buyer getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();//TODO get data
                try {
                    JSONArray object = new JSONArray(response);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    buyer = gson.fromJson(object.getString(0), Buyer.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi kết nỗi" + urlGetData, Toast.LENGTH_LONG).show();//TODO get data

            }
        }
        );
        requestQueue.add(stringRequest);
        return buyer;
    }
    private void resultFacebook() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("Json", response.getJSONObject().toString());
                try {

                    tvMail.setText("Email: " + object.getString("email"));
                    etDob.setText("Birthday: " + object.getString("birthday"));
                    String idF = object.getString("id");

                    String imageF = "https://graph.facebook.com/" + idF + "/picture?type=large";
                    Glide.with(getActivity()).load(imageF).into(ivAvatar);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameter = new Bundle();
        parameter.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameter);
        graphRequest.executeAsync();
        Log.d("Tag: ", "failed");
    }
}
