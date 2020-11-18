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

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.utils.CommonFunction;

import java.util.List;

public class OrderDoneAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Order> arrayOrder;
    Order order;
    Resources resources;

    private class ViewHolder {
        ImageView ivBuyer;
        Button btnDetail;
        TextView tvDescription,tvQuantity,tvTotalCost;
    }

    public OrderDoneAdapter(Context context, int layout, List<Order> orderList , Resources resources){
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
            holder.tvDescription = convertView.findViewById(R.id.tv_list_seller_done_description);
            holder.tvQuantity = convertView.findViewById(R.id.tv_list_seller_done_quantity);
            holder.tvTotalCost = convertView.findViewById(R.id.tv_list_seller_done_cost);
            holder.btnDetail = convertView.findViewById(R.id.btn_list_seller_done_detail);
            holder.ivBuyer = convertView.findViewById(R.id.iv_list_seller_done_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        order = arrayOrder.get(position);
        CommonFunction.setImageViewSrc(myContext,order.getBuyer_avatar(),holder.ivBuyer);
        holder.tvDescription.setText("Ghi chú: " + order.getBuyer_comment());
        holder.tvTotalCost.setText( "Thành tiền: " + String.valueOf(order.getTotal_cost()));
        holder.tvQuantity.setText("Số lượng: " + String.valueOf(order.getQuantity()));
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = wait for payment
                //reload fragment
            }
        });

        return convertView;
    }
}
