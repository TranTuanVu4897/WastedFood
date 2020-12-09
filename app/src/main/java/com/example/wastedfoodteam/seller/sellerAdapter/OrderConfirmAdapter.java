package com.example.wastedfoodteam.seller.sellerAdapter;

import android.content.Context;
import android.content.res.Resources;

import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.seller.order.ProductOrderSellerFragment;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.service.updateStatusForOrder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderConfirmAdapter extends BaseAdapter {
    final Context myContext;
    final int myLayout;
    final FragmentActivity myActivity;
    final List<SellerOrder> arrayOrder;
    SellerOrder order;
    Seller seller;
    final Resources resources;
    FirebaseDatabase database;
    DatabaseReference reference;

    private static class ViewHolder {
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
        holder.tvDescription.setText("Ghi chú: " + order.getBuyer_comment());
        holder.tvTotalCost.setText( "Thành tiền: " + CommonFunction.getCurrency(order.getTotal_cost()));
        holder.tvQuantity.setText("Số lượng: " + order.getQuantity());
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = wait for payment
                //reload fragment
                updateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php",Order.Status.SUCCESS, order.getId(),myContext);
                ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
                myActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, productOrderSellerFragment, productOrderSellerFragment.getTag()).commit();
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
