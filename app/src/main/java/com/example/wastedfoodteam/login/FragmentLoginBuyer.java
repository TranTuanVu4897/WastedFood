package com.example.wastedfoodteam.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buy.BuyHomeActivity;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.source.model.Buyer;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static android.content.Context.MODE_PRIVATE;


public class FragmentLoginBuyer extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN;
    String checkOption = "";
    EditText etSDT, etPass;
    TextView tvWarning;
    Button btnSignIn, btnSignInGoogle, btnPartnerOption;
    LoginButton btnSignInFacebook;
    CallbackManager callbackManager;
    String urlGetData = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_buyer, container, false);
        etSDT = view.findViewById(R.id.etSdtBuyerFLB);
        etPass = view.findViewById(R.id.etPassBuyerFLB);
        tvWarning = view.findViewById(R.id.tvWarningFLB);
        btnSignIn = view.findViewById(R.id.btnSignInBuyerFLB);
        btnSignInGoogle = view.findViewById(R.id.btnGoogleSignInFLB);
        btnSignInFacebook = view.findViewById(R.id.btnFacebookSignInFLB);
        btnPartnerOption = view.findViewById(R.id.btnPartnerOptionFLB);
        handleSignInFacebook();

        //facebook option

        callbackManager = CallbackManager.Factory.create();
        btnSignInFacebook.setPermissions(Arrays.asList("public_profile", "email"));
        btnSignInFacebook.setFragment(this);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                urlGetData = Variable.ipAddress + "login/buyerLogin.php?third_party_id=" + etSDT.getText().toString();
//                getData(urlGetData);
                resultFacebook();
                Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                checkOption = "2";
                startActivity(intent);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(), "Kiểm tra lại kết nối Internet", Toast.LENGTH_LONG).show();
            }
        });

        //google option
        AddGoogleSignInOption();
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvWarning.setText("");
                //To Do Check Phone
                signInGoogle();

            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etSDT.getText().toString().length() != 10) {
                    tvWarning.setText("SDT không hợp lệ");

                } else {
                    urlGetData = Variable.ipAddress + "login/buyerLogin.php?phone=" + etSDT.getText().toString() + "&password=" + md5(etPass.getText().toString());
                    getData(urlGetData);
                }
            }
        });
        btnPartnerOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragmentLoginPartner();
            }
        });

        return view;
    }

    /**
     * google sign in option
     */
    private void AddGoogleSignInOption() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    /**
     * Start Sign in Google flow
     */
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * keep Sign In Google
     *
     * @param completedTask
     */
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(getActivity(), BuyHomeActivity.class));
        } catch (ApiException e) {
            e.printStackTrace();
            Log.w("TAG", "Failed code" + e.getStatusCode());
            Log.d("Tag", e.getMessage());
            Toast.makeText(getActivity(), "Failed Connect " + e.getStatusCode(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Check account
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        } catch (Exception e) {
            Log.w("SignIn", "Code" + e.getStackTrace());
        }
    }

    @Override
    public void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            startActivity(new Intent(getActivity(), BuyHomeActivity.class));
        }
        super.onStart();
    }

    /**
     * Keep Sign In Facebook
     */
    private void handleSignInFacebook() {
        //check loginFB
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {


            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
            checkOption = "2";
            intent.putExtra("Check_option", checkOption);
            startActivity(intent);

        }
    }

    /**
     * add fragment login for seller
     *
     */
    public void addFragmentLoginPartner() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentLoginPartner fragmentLoginPartner = new FragmentLoginPartner();
        fragmentTransaction.replace(R.id.flFragmentLayoutAM, fragmentLoginPartner);
        fragmentTransaction.commit();
    }

    /**
     * encode md5
     *
     * @param str
     * @return
     */
    private String md5(String str) {
        String result = "";
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            BigInteger bigInteger = new BigInteger(1, digest.digest());
            result = bigInteger.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get data from mySql
     *
     * @param url
     */
    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                switch (response) {
                    case "not exist account":
                    case "account is locked":
                        Toast.makeText(getActivity(), "Mật khẩu sai", Toast.LENGTH_LONG).show();//TODO fix for suitable error
                        break;
                    case "not match role":
                        Toast.makeText(getActivity(), "lỗi " + urlGetData, Toast.LENGTH_LONG).show();//TODO fix for suitable error
                        break;
                    case "PHONE_IS_NULL":

                        //startActivity(new Intent(getActivity(),BuyHomeActivity.class));
                    default:
                        Toast.makeText(getActivity(), "OK", Toast.LENGTH_LONG).show();//TODO get data
                        try {
                            JSONArray object = new JSONArray(response);

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

                            Buyer buyer = gson.fromJson(object.getString(0), Buyer.class);

                            sharePreferences();
                            Intent intent = new Intent(getActivity(), BuyHomeActivity.class);
                            checkOption = "1";
                            intent.putExtra("Check_option", checkOption);
                            //TODO pass data through intent
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
                Toast.makeText(getActivity(), "lỗi kết nỗi" + urlGetData, Toast.LENGTH_LONG).show();//TODO get data
                Log.d("MK ", md5(etPass.getText().toString()));
            }
        }
        );
        requestQueue.add(stringRequest);
    }

    /**
     * handle status login
     */
    private void sharePreferences() {
        SharedPreferences pre = getActivity().getSharedPreferences("my_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("name", "Tung");
        editor.putBoolean("check", true);
        editor.commit();
        //TODO
    }
    private void resultFacebook() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.d("Json", response.getJSONObject().toString());
                try {
                    String email = object.getString("email");
                    String name = object.getString("name");
                    String id = object.getString("id");
                    String dob = object.getString("birthday");
                    String imageF = "https://graph.facebook.com/" + id + "/picture?type=large";
                    RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
//TODO
//                    StringRequest stringRequest = new StringRequest();

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
