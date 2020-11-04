package com.example.wastedfoodteam.seller.sellerAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Order> arrayOrder;
    Order order;
    Resources resources;

    private class ViewHolder {
        TextView tvNameBuyer;
        TextView tvNameProduct;
        TextView tvQuantity;
        TextView tvTotalCost;
    }

    public OrderAdapter(Context context, int layout, List<Order> orderList , Resources resources){
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
            holder.tvNameBuyer = convertView.findViewById(R.id.tv_list_order_buyerName);
            holder.tvNameProduct = convertView.findViewById(R.id.tv_list_order_productName);
            holder.tvQuantity = convertView.findViewById(R.id.tv_list_order_quantity);
            holder.tvTotalCost = convertView.findViewById(R.id.tv_list_order_price);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        order = arrayOrder.get(position);
        holder.tvNameBuyer.setText(order.getBuyerName());
        holder.tvTotalCost.setText(String.valueOf(order.getTotal_cost()));
        holder.tvNameProduct.setText(String.valueOf(order.getNameProduct()));
        holder.tvQuantity.setText(String.valueOf(order.getQuantity()));
        return convertView;
    }
}
