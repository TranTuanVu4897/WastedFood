package com.example.wastedfoodteam.buyer;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Buyer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FragmentEditInformationBuyer extends Fragment {
    EditText etName, etPhone, etDob, etMail;
    RadioButton rbBoy, rbGirl;
    String url = "";
    Buyer buyer;
    ImageView ivAvatar;
    Button btUpdate, btCancel;
    String accountId;
    int lastSelectedYear;
    int lastSelectedMonth;
    int lastSelectedDayOfMonth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_buyer, container, false);
        mapping(view);
//        btUpdate.setVisibility(View.VISIBLE);
//        btCancel.setVisibility(View.VISIBLE);
//        if (Variable.CHECK_LOGIN == 2) {
//            btUpdate.setVisibility(View.INVISIBLE);
//            btCancel.setVisibility(View.INVISIBLE);
//            resultFacebook();
//        } else if (Variable.CHECK_LOGIN == 0) {
            accountId = Variable.ACCOUNT_ID + "";
            url = Variable.ipAddress + "information/informationBuyer.php?account_id=" + accountId;
            getData(url);
//        etDob.setText(buyer.getDate_of_birth().toString());
//        if (buyer.isGender()) {
//            rbBoy.setChecked(true);
//        } else {
//            rbGirl.setChecked(true);
//        }
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });
        final Calendar c = Calendar.getInstance();
        this.lastSelectedYear = c.get(Calendar.YEAR);
        this.lastSelectedMonth = c.get(Calendar.MONTH);
        this.lastSelectedDayOfMonth = c.get(Calendar.DAY_OF_MONTH);

            btUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    url = Variable.ipAddress + "information/changeInfoBuyer.php";
                    String name = etName.getText().toString();
                    if (name.trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Vui lòng điền tên", Toast.LENGTH_LONG).show();
                        return;
                    }

                    String phone = etPhone.getText().toString();
                    String urlImage = "";
                    String dob = buyer.getDate_of_birth().toString();
                    //check information change
                    if (!buyer.getDate_of_birth().toString().equals(etDob.getText().toString()))
                        dob = etDob.getText().toString();
                    String gender = "";
                    if (rbBoy.isChecked()) {
                        gender = "0";
                    } else {
                        gender = "1";
                    }
                    updateData(url, accountId, name, phone, urlImage, dob, gender);
                }
            });
//        }


        //get account it


        return view;
    }

    private void mapping(View view) {
        etName = view.findViewById(R.id.etBuyerNameFEB);
        etDob = view.findViewById(R.id.etEditBuyerDateofBirth);
        etPhone = view.findViewById(R.id.etEditPhoneFEB);
        rbBoy = view.findViewById(R.id.rbBoy);
        rbGirl = view.findViewById(R.id.rbGirl);
        etMail = view.findViewById(R.id.etMailFEB);
        ivAvatar = view.findViewById(R.id.ivBuyerAvatarFEB);
        btUpdate = view.findViewById(R.id.btUpdateBuyerFEB);
        btCancel = view.findViewById(R.id.btCancelFEB);
    }

    private void getData(final String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();//TODO get data
                try {
                    JSONArray object = new JSONArray(response);
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    buyer = gson.fromJson(object.getString(0), Buyer.class);

                    //set edit text here
                    etName.setText(buyer.getName());
                    etDob.setText(buyer.getDate_of_birth() + "");
                    etMail.setText(buyer.getEmail());
                    etPhone.setText(buyer.getPhone());
                    if (buyer.isGender()) {
                        rbGirl.setChecked(true);
                    } else {
                        rbBoy.setChecked(true);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi kết nỗi" + url, Toast.LENGTH_LONG).show();//TODO get data

            }
        }
        );
        requestQueue.add(stringRequest);
    }

    private void updateData(String url, final String accountId, final String name, final String phone, final String urlImage, final String dob, final String gender) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                switch (response) {
                    case "failed":
                        Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(getActivity(), "OK Insert data", Toast.LENGTH_LONG).show();
                        try {


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "lỗi kết nỗi" + FragmentEditInformationBuyer.this.url, Toast.LENGTH_LONG).show();//TODO get data

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("account_id", accountId);
                params.put("name", name);
                params.put("phone", phone);
                params.put("urlImage", urlImage);
                params.put("gender", gender);
                params.put("dob", dob);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    private void selectDate(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                etDob.setText(year + "-" + month + "-" + dayOfMonth);
                lastSelectedYear = year;
                lastSelectedMonth = month;
                lastSelectedDayOfMonth = dayOfMonth;
            }
        };
        DatePickerDialog datePickerDialog = null;
        datePickerDialog = new DatePickerDialog(getActivity(),
                dateSetListener, lastSelectedYear, lastSelectedMonth, lastSelectedDayOfMonth);
        datePickerDialog.show();
    }
//    private void resultFacebook() {
//        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                Log.d("Json", response.getJSONObject().toString());
//                try {
//                    etName.setText("Tên: " + object.getString("name"));
//                    tvMail.setText("Email: " + object.getString("email"));
//                    etDob.setText("Birthday: " + object.getString("birthday"));
//                    String idF = object.getString("id");
//
//                    String imageF = "https://graph.facebook.com/" + idF + "/picture?type=large";
//                    Glide.with(getActivity()).load(imageF).into(ivAvatar);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        Bundle parameter = new Bundle();
//        parameter.putString("fields", "id,name,email,gender,birthday");
//        graphRequest.setParameters(parameter);
//        graphRequest.executeAsync();
//        Log.d("Tag: ", "failed");
//    }
}
