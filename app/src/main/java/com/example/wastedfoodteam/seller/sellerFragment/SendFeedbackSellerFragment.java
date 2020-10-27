package com.example.wastedfoodteam.seller.sellerFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wastedfoodteam.R;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class SendFeedbackSellerFragment extends Fragment {

    //ui view
    EditText editText_sendFeedback_title;
    EditText editText_sendFeedback_description;
    Button btn_sendFeedback_send;

    String title,description;

    int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_feedback_seller, container, false);

        //init ui view
        editText_sendFeedback_title = view.findViewById(R.id.editText_sendFeedback_title);
        editText_sendFeedback_description = view.findViewById(R.id.editText_sendFeedback_description);
        btn_sendFeedback_send = view.findViewById(R.id.btn_sendFeedback_send);

        //for multiline EditText
        //scroll for EditText
        editText_sendFeedback_description.setScroller(new Scroller( getActivity().getApplicationContext()));
        editText_sendFeedback_description.setVerticalScrollBarEnabled(true);

        //Edit Text Line
        editText_sendFeedback_description.setMinLines(5);
        editText_sendFeedback_description.setMaxLines(5);

        //get data from home activity
        Bundle bundle = getArguments();
        if(bundle!= null){
            id = bundle.getInt("id");
        }

        //click send button handle
        btn_sendFeedback_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputData();
            }
        });

        return view;
    }

    private void ClearText(){
        editText_sendFeedback_description.setText("");
        editText_sendFeedback_title.setText("");
    }

    private void inputData(){
        title = editText_sendFeedback_title.getText().toString();
        description = editText_sendFeedback_description.getText().toString();

        //validate data
        //TODO

        //add to db
        addFeedback("http://192.168.1.10/wastedfoodphp/seller/sellerFeedback.php");

    }



    //add feedback data
    private void addFeedback(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            Toast.makeText(getActivity(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            ClearText();
                            //TODO move back to home
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
                params.put("account_id", String.valueOf(id));
                params.put("title",title);
                params.put("description", description);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}