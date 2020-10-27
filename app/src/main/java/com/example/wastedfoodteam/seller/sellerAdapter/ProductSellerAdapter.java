package com.example.wastedfoodteam.seller.sellerAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.Product1;
import com.example.wastedfoodteam.utils.DownloadImageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductSellerAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Product> arrayProduct;
    Product product;
    Resources resources;

    private class ViewHolder {
        TextView tvName;
        ImageView ivImage;
        Switch swOnOff;
    }

    public ProductSellerAdapter(Context context, int layout, List<Product> productList , Resources resources){
        myContext = context;
        myLayout = layout;
        arrayProduct = productList;
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return arrayProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(myLayout, null);
            holder.tvName = convertView.findViewById(R.id.tv_list_seller_name);
            holder.ivImage = convertView.findViewById(R.id.iv_list_seller_image);
            holder.swOnOff = convertView.findViewById(R.id.sw_list_seller_onOff);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String urlGetData = Variable.ipAddress + "seller/setActiveForProduct.php";
        product = arrayProduct.get(position);
        if(product.getStatus().equals("selling")){
            holder.swOnOff.setChecked(true);
        }else{
            holder.swOnOff.setChecked(false);
        }
        holder.swOnOff.setTag(position);
        holder.tvName.setText(product.getName());
        //get image from url
        new DownloadImageTask(holder.ivImage,resources).execute(product.getImage());
        //Picasso.get().load(product.getImage().isEmpty() ? Variable.noImageUrl : product.getImage()).into(holder.ivImage);
        // TODO replace with other type
        holder.swOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.swOnOff.isChecked())
                {
                    int position=(Integer)v.getTag();
                    product = arrayProduct.get(position);
                    Toast.makeText( v.getContext() , "Switch is on", Toast.LENGTH_LONG).show();
                    updateProductStatus(urlGetData,"selling",product.getId());
                }
                else {
                    int position=(Integer)v.getTag();
                    product = arrayProduct.get(position);
                    Toast.makeText( v.getContext(), "Switch is Off", Toast.LENGTH_LONG).show();
                    updateProductStatus(urlGetData, "stop",product.getId());
                }
            }
        });
        return convertView;
    }

    //update product status
    private void updateProductStatus(String url, final String status , final int id){
        RequestQueue requestQueue = Volley.newRequestQueue(myContext);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.trim().equals("Succesfully update")){
                            Toast.makeText(myContext,"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                            //TODO move back to home
                        }else{
                            Toast.makeText( myContext,"Lỗi cập nhật",Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(myContext,"Xảy ra lỗi, vui lòng thử lại",Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("seller_id", String.valueOf(product.getSeller_id()));
                params.put("status",  status );
                params.put("id" ,  String.valueOf(id) );
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
