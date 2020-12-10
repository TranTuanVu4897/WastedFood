package com.example.wastedfoodteam.seller.order;

import android.content.Context;
import android.content.res.Resources;
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
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.notification.SendNotif;
import com.example.wastedfoodteam.utils.service.updateStatusForOrder;

import java.util.List;

public class OrderPaymentAdapter extends BaseAdapter {
    final Context myContext;
    final int myLayout;
    final List<SellerOrder> arrayOrder;
    final SendNotif sendNotif = new SendNotif();
    NotificationUtil util;
    Product product;
    SellerOrder order;
    final Resources resources;
    final FragmentActivity myFragmentActivity;

    private static class ViewHolder {
        ImageView ivBuyer;
        Button btnConfirm,btnReject;
        TextView tvQuantity,tvTotalCost;
    }

    public OrderPaymentAdapter(Context context, int layout, List<SellerOrder> orderList , Resources resources , FragmentActivity fragmentActivity ){
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
            holder.tvQuantity = convertView.findViewById(R.id.tv_list_seller_payment_quantity);
            holder.tvTotalCost = convertView.findViewById(R.id.tv_list_seller_payment_cost);
            holder.btnConfirm = convertView.findViewById(R.id.btn_list_seller_payment_confirm);
            holder.btnReject = convertView.findViewById(R.id.btn_list_seller_payment_reject);
            holder.ivBuyer = convertView.findViewById(R.id.iv_list_seller_payment_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        util = new NotificationUtil();
        order = arrayOrder.get(position);
        product = Variable.PRODUCT;
        CommonFunction.setImageViewSrc(myContext,order.getBuyer_avatar(),holder.ivBuyer);
        holder.tvTotalCost.setText( "Thành tiền: " + CommonFunction.getCurrency(order.getTotal_cost()));
        holder.tvQuantity.setText("Số lượng: " + order.getQuantity());
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = done
                //reload fragment
                //sendNotif.notificationHandle(order.getBuyer_name());
                updateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php",Order.Status.SUCCESS, order.getId(),myContext);
                String message = Variable.SELLER.getName() + " đã xác nhận thanh toán của bạn\r Cảm ơn bạn vì đã sử dụng dịch vụ của chúng tôi";
                util.addNotification(myContext,Variable.SELLER.getId() , order.getBuyer_id(), message , order.getId());
                sendNotif.notificationHandle(order.getFirebase_UID(), "Wasted food app" , message);
                ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
                myFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, productOrderSellerFragment, productOrderSellerFragment.getTag()).commit();
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = rejected
                //set btnReject text to Đã từ chối set clickable = false
                updateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php",Order.Status.CANCEL, order.getId(),myContext);
                ProductOrderSellerFragment productOrderSellerFragment = new ProductOrderSellerFragment();
                myFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, productOrderSellerFragment, productOrderSellerFragment.getTag()).commit();
            }
        });
        return convertView;
    }

}