package com.example.wastedfoodteam.buyer.order;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.buyer.buy.ProductAdapter;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.utils.CommonFunction;

import java.util.List;

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Order> orderList;
    Resources resources;

    //TODO fix all sources
    public OrderAdapter(Context context, int layout, List<Order> orderList, Resources resources) {
        this.context = context;
        this.layout = layout;
        this.orderList = orderList;
        this.resources = resources;
    }

    private class ViewHolder {
        TextView tvName,tvDiscount,tvQuantity,tvOriginalPrice,tvSellPrice,tvOpenTime,tvDirect,tvRating;
        ImageView ivProduct;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return orderList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.tvName = convertView.findViewById(R.id.tvTitleLPI);
            holder.ivProduct = convertView.findViewById(R.id.ivProductLPI);
            holder.tvDirect = convertView.findViewById(R.id.tvDirection);
            holder.tvDiscount = convertView.findViewById(R.id.tvDiscount);
            holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.tvOpenTime = convertView.findViewById(R.id.tvOpenTime);
            holder.tvOriginalPrice = convertView.findViewById(R.id.tvOriginalPrice);
            holder.tvSellPrice = convertView.findViewById(R.id.tvSellPrice);
            holder.tvRating = convertView.findViewById(R.id.tvRating);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Order order = orderList.get(position);

        holder.tvName.setText(order.getProduct_id() + "");
        holder.tvSellPrice.setText(CommonFunction.getCurrency(order.getTotal_cost()));
        holder.tvOpenTime.setText(order.getBuyer_rating());
        //get image from url
        CommonFunction.setImageViewSrc(context,"",holder.ivProduct);

        return convertView;

    }
}
