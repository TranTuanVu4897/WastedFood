package com.example.wastedfoodteam.seller.sellerAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;

import java.util.List;

public class OrderConfirmAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Order> arrayOrder;
    Order order;
    Resources resources;

    private class ViewHolder {
        ImageView ivBuyer;
        Button btnConfirm,btnReject;
        TextView tvDescription,tvQuantity,tvTotalCost;
    }

    public OrderConfirmAdapter(Context context, int layout, List<Order> orderList , Resources resources){
        myContext = context;
        myLayout = layout;
        arrayOrder = orderList;
        this.resources = resources;
    }



    @Override
    public int getCount() {
        return arrayOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayOrder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayOrder.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(myLayout, null);
            holder.tvDescription = convertView.findViewById(R.id.tv_list_seller_confirm_description);
            holder.tvQuantity = convertView.findViewById(R.id.tv_list_seller_confirm_quantity);
            holder.tvTotalCost = convertView.findViewById(R.id.tv_list_seller_confirm_cost);
            holder.btnConfirm = convertView.findViewById(R.id.btn_list_seller_confirm_confirm);
            holder.btnReject = convertView.findViewById(R.id.btn_list_seller_confirm_reject);
            holder.ivBuyer = convertView.findViewById(R.id.iv_list_seller_confirm_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        order = arrayOrder.get(position);
        Glide.with(convertView.getContext()).load(order.getImage().isEmpty() ? Variable.noImageUrl : order.getImage()).into(holder.ivBuyer);
        holder.tvDescription.setText("Ghi chú: " + order.getBuyer_comment());
        holder.tvTotalCost.setText(String.valueOf(order.getTotal_cost()));
        holder.tvQuantity.setText(String.valueOf(order.getQuantity()));
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = wait for payment
                //reload fragment
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = rejected
                //set btnReject text to Đã từ chối set clickable = false
                //ẩn btnAdd
            }
        });
        return convertView;
    }
}
