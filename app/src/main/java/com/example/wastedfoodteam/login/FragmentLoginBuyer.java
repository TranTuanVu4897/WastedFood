package com.example.wastedfoodteam.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class FragmentLoginBuyer extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN;
    EditText etSDT, etPass;
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
        btnSignIn = view.findViewById(R.id.btnSignInBuyerFLB);
        btnSignInGoogle = view.findViewById(R.id.btnGoogleSignInFLB);
        btnSignInFacebook = view.findViewById(R.id.btnFacebookSignInFLB);


        //facebook option

        callbackManager = CallbackManager.Factory.create();
        btnSignInFacebook.setPermissions(Arrays.asList("public_profile", "email"));
        ;
        btnSignInFacebook.setFragment(this);
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //google option
        AddGoogleSignInOption();
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //urlGetData ="http://localhost/wastedfoodphp/login/buyerLogin.php?username=tungpt36&password=tung1998";
                urlGetData = "http://192.168.1.46/wastedfoodphp/login/buyerLogin.php?username="+etSDT.getText().toString()+"&password="+md5(etPass.getText().toString());
                getData(urlGetData);
            }
        });

        return view;


    }

    private void AddGoogleSignInOption() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }

    //Start Sign in Google flow
    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //keep Sign In Google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(getActivity(), MainActivity.class));
        } catch (ApiException e) {
            e.printStackTrace();
            Log.w("TAG", "Failed code" + e.getStatusCode());
            Log.d("Tag", e.getMessage());
            Toast.makeText(getActivity(), "Failed Connect " + e.getStatusCode(), Toast.LENGTH_LONG).show();
        }
    }

    //Check account
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
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
        super.onStart();
    }

    //Keep Sign In Facebook
    private void handleSignInFacebook() {
        //check loginFB
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            //startActivity(new Intent(MainActivity.this,MainActivity2.class));
        }
    }
    public void addFragmentLoginPartner(View view){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentLoginPartner fragmentLoginPartner = new FragmentLoginPartner();
        fragmentTransaction.add(R.id.fragmentPartner,fragmentLoginPartner);
        fragmentTransaction.commit();
    }

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
    private void getData(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Toast.makeText(getActivity(),"OK",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"lỗi " + urlGetData,Toast.LENGTH_LONG).show();
                Log.d("MK ", md5(etPass.getText().toString()));
            }
        }
        );
        requestQueue.add(jsonArrayRequest);
    }

}
