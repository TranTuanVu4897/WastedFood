package com.example.wastedfoodteam.buyer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentBuyerInformation extends Fragment {
    TextView tvName, tvPhone, tvGender, tvDob,tvEmail;
    ImageView avatarBuyer;
    Bundle bundle;
    String checkOption = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information_buyer, container, false);
        mapping(view);
//        bundle = getIntent().getExtras();
        //TODO
        if(bundle != null){
            checkOption = bundle.getString("Check_option");
        }
        if(checkOption.equals("2")){
            resultFacebook();
        }
        return view;
    }
    private void resultFacebook() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("Json", response.getJSONObject().toString());
                try {

                    tvEmail.setText("Email:" + object.getString("email"));
                    tvName.setText("Name:" + object.getString("name"));
//                    txtId.setText("id:" + object.getString("id"));
                    String idF = object.getString("id");
                    String imageF = "https://graph.facebook.com/" + idF + "/picture?type=large";
                    Glide.with(getActivity()).load(imageF).into(avatarBuyer);
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
    private void mapping(View view){
        tvName = view.findViewById(R.id.tvBuyerNameFIB);
        tvPhone = view.findViewById(R.id.tvPhoneFIB);
        tvGender = view.findViewById(R.id.tvGenBuyerFIB);
        tvDob = view.findViewById(R.id.tvDobFIB);
        avatarBuyer = view.findViewById(R.id.ivBuyerAvatarFIB);
        tvEmail = view.findViewById(R.id.tvEmailFIB);
    }
}
