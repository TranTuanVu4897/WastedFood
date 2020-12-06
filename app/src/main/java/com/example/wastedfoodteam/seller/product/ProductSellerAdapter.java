package com.example.wastedfoodteam.seller.product;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
import com.example.wastedfoodteam.utils.CommonFunction;

import java.util.Calendar;
import java.util.Date;
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
        //Switch swOnOff;
        TextView tvRemainProduct;
        TextView tvSoldProduct;
        TextView tvTimeProduct;
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
        return arrayProduct.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayProduct.get(position).getId();
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
            /*holder.swOnOff = convertView.findViewById(R.id.sw_list_seller_onOff);*/
            holder.tvRemainProduct = convertView.findViewById(R.id.tv_list_seller_remainTotal);
            holder.tvSoldProduct = convertView.findViewById(R.id.tv_list_seller_product_sold);
            holder.tvTimeProduct = convertView.findViewById(R.id.tv_list_seller_product_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String urlGetData = Variable.IP_ADDRESS + "seller/setActiveForProduct.php";
        product = arrayProduct.get(position);
        //for remain textView
        if(product.getRemain_quantity()!=0){
            holder.tvRemainProduct.setText("CÒN " + product.getRemain_quantity() + " SẢN PHẨM");
            holder.tvRemainProduct.setBackgroundResource(R.color.colorLightGreen);
        }else {
            holder.tvRemainProduct.setText("HẾT HÀNG");
            holder.tvRemainProduct.setBackgroundResource(R.color.colorHeavyGrey);
        }

        holder.tvName.setText(product.getName());
        holder.tvSoldProduct.setText(product.getOriginal_quantity() - product.getRemain_quantity() + " ĐÃ BÁN");
        CommonFunction.setImageViewSrc(convertView.getContext(),product.getImage(),holder.ivImage);
        long timeDifferent =  product.getSell_date().getTime() - Calendar.getInstance().getTime().getTime();
        if(timeDifferent > 0){
            long seconds = timeDifferent / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            //String time = days + ":" + hours % 24 + ":" + minutes % 60;
            holder.tvTimeProduct.setText("KẾT THÚC TRONG : " + days + " NGÀY " + + hours%24 +" GIỜ"  + minutes%60  + " PHÚT ");
        }else {
            holder.tvTimeProduct.setText("ĐÃ KẾT THÚC");
        }

        return convertView;
    }

    //date to calender
    public static Calendar toCalendar(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
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
