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
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

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
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.sellerFragment.ListOrderHistoryFragment;
import com.example.wastedfoodteam.seller.sellerFragment.OrderDetailSellerFragment;
import com.example.wastedfoodteam.utils.service.updateStatusForOrder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPaymentAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Order> arrayOrder;
    Product product;
    Order order;
    Resources resources;
    FragmentActivity myFragmentActivity;

    private class ViewHolder {
        ImageView ivBuyer;
        Button btnConfirm,btnReject;
        TextView tvDescription,tvQuantity,tvTotalCost;
    }

    public OrderPaymentAdapter(Context context, int layout, List<Order> orderList , Resources resources , FragmentActivity fragmentActivity ){
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
            holder.tvDescription = convertView.findViewById(R.id.tv_list_seller_payment_description);
            holder.tvQuantity = convertView.findViewById(R.id.tv_list_seller_payment_quantity);
            holder.tvTotalCost = convertView.findViewById(R.id.tv_list_seller_payment_cost);
            holder.btnConfirm = convertView.findViewById(R.id.btn_list_seller_payment_confirm);
            holder.btnReject = convertView.findViewById(R.id.btn_list_seller_payment_reject);
            holder.ivBuyer = convertView.findViewById(R.id.iv_list_seller_payment_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        order = arrayOrder.get(position);
        product = Variable.PRODUCT;
        Glide.with(convertView.getContext()).load(order.getImage().isEmpty() ? Variable.noImageUrl : order.getImage()).into(holder.ivBuyer);
        holder.tvDescription.setText("Ghi chú: " + order.getBuyer_comment());
        holder.tvTotalCost.setText( "Thành tiền: " + String.valueOf(order.getTotal_cost()));
        holder.tvQuantity.setText("Số lượng: " + String.valueOf(order.getQuantity()));
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = done
                //reload fragment
                updateStatusForOrder.updateOrderStatus(Variable.ipAddress + "seller/updateStatusForOrderSeller.php","done", order.getId(),myContext);
                OrderDetailSellerFragment orderDetailSellerFragment = new OrderDetailSellerFragment();
                myFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, orderDetailSellerFragment, orderDetailSellerFragment.getTag()).commit();
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
