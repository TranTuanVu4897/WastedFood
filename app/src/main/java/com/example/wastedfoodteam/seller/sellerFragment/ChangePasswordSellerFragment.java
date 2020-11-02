package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class ChangePasswordSellerFragment extends Fragment {


    int id;
    String oldPassword, currentPassword , confirmPassword , newPassword ;

    //ui
    EditText editText_change_password_current,editText_change_password_new,editText_change_password_confirm;
    Button btn_change_password_seller;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password_seller, container, false);

        //get from home seller
        id = Variable.ACCOUNT_ID;
        oldPassword = Variable.SELLER.getPassword();

        //ui view
        editText_change_password_current = view.findViewById(R.id.editText_change_password_current);
        editText_change_password_confirm = view.findViewById(R.id.editText_change_password_confirm);
        editText_change_password_new = view.findViewById(R.id.editText_change_password_new);
        btn_change_password_seller = view.findViewById(R.id.btn_change_password_seller);



        btn_change_password_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = editText_change_password_current.getText().toString();
                confirmPassword = editText_change_password_confirm.getText().toString();
                newPassword = editText_change_password_new.getText().toString();
                //validate current password
                if(!md5(currentPassword).equals(oldPassword)){
                    Toast.makeText(getActivity(), "Mật khẩu sai thử lại lần nữa", Toast.LENGTH_LONG).show();
                }else if(!newPassword.equals(confirmPassword)){
                    Toast.makeText(getActivity(), "Mật khẩu và xác nhận mật khẩu phải giống nhau", Toast.LENGTH_LONG).show();
                }else {
                    //validate new password
                    //update password to db
                    String urlGetData = Variable.ipAddress + "seller/updatePasswordAccount.php";
                    updateSellerPassword(urlGetData);
                }
            }
        });

        return view;
    }


    //update seller account password
    private void updateSellerPassword(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            Toast.makeText(getActivity(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            oldPassword = md5(confirmPassword);
                            //TODO move back to home
                            SellerHomeFragment sellerHomeFragment = new SellerHomeFragment();
                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            manager.beginTransaction().replace(R.id.content_main, sellerHomeFragment, sellerHomeFragment.getTag()).commit();
                        }else{
                            Toast.makeText(getActivity(),"Lỗi cập nhật",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("password",md5(editText_change_password_new.getText().toString()));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }



    //hash the password for user security
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
}