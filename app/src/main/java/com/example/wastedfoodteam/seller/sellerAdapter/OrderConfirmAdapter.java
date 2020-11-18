package com.example.wastedfoodteam.seller.sellerAdapter;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;

import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.sellerFragment.AddProductFragment;
import com.example.wastedfoodteam.seller.sellerFragment.OrderDetailSellerFragment;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.service.updateStatusForOrder;

import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Random;

public class OrderConfirmAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    FragmentActivity myActivity;
    List<SellerOrder> arrayOrder;
    SellerOrder order;
    Seller seller;
    Resources resources;
    FirebaseDatabase database;
    DatabaseReference reference;

    private class ViewHolder {
        ImageView ivBuyer;
        Button btnConfirm,btnReject;
        TextView tvDescription,tvQuantity,tvTotalCost;
    }

    public OrderConfirmAdapter(Context context, int layout, List<SellerOrder> orderList , Resources resources ,FragmentActivity fragmentActivity ){
        myContext = context;
        myLayout = layout;
        arrayOrder = orderList;
        this.resources = resources;
        myActivity = fragmentActivity;
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
        final ViewHolder holder;
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

        CommonFunction.setImageViewSrc(myContext,order.getBuyer_avatar(),holder.ivBuyer);
        //Glide.with(convertView.getContext()).load(order.getImage().isEmpty() ? Variable.noImageUrl : order.getImage()).into(holder.ivBuyer);
        holder.tvDescription.setText("Ghi chú: " + order.getBuyer_comment());
        holder.tvTotalCost.setText( "Thành tiền: " + String.valueOf(order.getTotal_cost()));
        holder.tvQuantity.setText("Số lượng: " + String.valueOf(order.getQuantity()));
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = wait for payment
                //reload fragment
                updateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php","wait for payment", order.getId(),myContext);
                OrderDetailSellerFragment orderDetailSellerFragment = new OrderDetailSellerFragment();
                myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, orderDetailSellerFragment, orderDetailSellerFragment.getTag()).commit();
                //showNotification("Dữ liệu","Đã được thay đổi");
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
