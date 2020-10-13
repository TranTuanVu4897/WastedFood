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

import com.example.wastedfoodteam.MainActivity;
import com.example.wastedfoodteam.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class FragmentLoginBuyer extends Fragment {
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN;
    EditText etSDT, etPass;
    Button btnSignIn,btnSignInGoogle,btnSignInFacebook;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_buyer, container, false);
        etSDT = view.findViewById(R.id.edit_sdt);
        etPass = view.findViewById(R.id.edit_pass);
        btnSignIn = view.findViewById(R.id.btn_signIn);
        btnSignInGoogle = view.findViewById(R.id.btn_google_signin);
        btnSignInGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }
        });

        AddGoogleSignInOption();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etSDT.getText().equals("0385818813") && etPass.getText().equals("11011998")){
                    Toast.makeText(getActivity(),"Dung",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(),"Your SDT or Password wrong",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;


    }
    private void AddGoogleSignInOption(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
    }
    //Start Sign in Google flow
    private void signInGoogle(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            startActivity(new Intent(getActivity(),MainActivity.class));
        } catch (ApiException e) {
            e.printStackTrace();
            Log.w("TAG","Failed code" + e.getStatusCode());
            Log.d("Tag",e.getMessage());
            Toast.makeText(getActivity(),"Failed Connect " + e.getStatusCode(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if(requestCode == RC_SIGN_IN){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }catch (Exception e){
            Log.w("SignIn","Code" + e.getStackTrace());
        }
    }

    @Override
    public void onStart() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null){
            startActivity(new Intent(getActivity(),MainActivity.class));
        }
        super.onStart();
    }
}
