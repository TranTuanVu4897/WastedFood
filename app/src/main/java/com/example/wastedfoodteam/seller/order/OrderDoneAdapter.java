package com.example.wastedfoodteam.seller.order;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.seller.order.SellerOrderDetailFragment;
import com.example.wastedfoodteam.seller.sellerAdapter.SellerOrder;
import com.example.wastedfoodteam.utils.CommonFunction;

import java.util.List;

public class OrderDoneAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<SellerOrder> arrayOrder;
    SellerOrder order;
    FragmentActivity myFragmentActivity;
    Resources resources;

    private class ViewHolder {
        ImageView ivBuyer;
        Button btnDetail;
        TextView tvDescription,tvQuantity,tvTotalCost;
    }

    public OrderDoneAdapter(Context context, int layout, List<SellerOrder> orderList , Resources resources , FragmentActivity fragmentActivity){
        myContext = context;
        myLayout = layout;
        arrayOrder = orderList;
        this.resources = resources;
        myFragmentActivity = fragmentActivity;
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
        holder.tvTotalCost.setText("Thành tiền: " + String.valueOf(CommonFunction.getCurrency(order.getTotal_cost())));
        holder.tvQuantity.setText("Số lượng: " + String.valueOf(order.getQuantity()));
        holder.btnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = wait for payment
                //reload fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", order);
                SellerOrderDetailFragment sellerOrderDetailFragment = new SellerOrderDetailFragment();
                sellerOrderDetailFragment.setArguments(bundle);
                myFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, sellerOrderDetailFragment, sellerOrderDetailFragment.getTag()).commit();
            }
        });
        return convertView;
    }
}
