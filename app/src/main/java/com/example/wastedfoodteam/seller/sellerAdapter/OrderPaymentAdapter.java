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

import androidx.fragment.app.FragmentActivity;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.model.Order;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.seller.notification.NotificationUtil;
import com.example.wastedfoodteam.seller.sellerFragment.ProductDetailSellerFragment;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.SendNotificationPackage.SendNotif;
import com.example.wastedfoodteam.utils.service.updateStatusForOrder;

import java.util.List;

public class OrderPaymentAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<SellerOrder> arrayOrder;
    SendNotif sendNotif = new SendNotif();
    NotificationUtil util;
    Product product;
    SellerOrder order;
    Resources resources;
    FragmentActivity myFragmentActivity;

    private class ViewHolder {
        ImageView ivBuyer;
        Button btnConfirm,btnReject;
        TextView tvDescription,tvQuantity,tvTotalCost;
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

        util = new NotificationUtil();
        order = arrayOrder.get(position);
        product = Variable.PRODUCT;
        CommonFunction.setImageViewSrc(myContext,order.getBuyer_avatar(),holder.ivBuyer);
        holder.tvDescription.setText("Ghi chú: " + order.getBuyer_comment());
        holder.tvTotalCost.setText( "Thành tiền: " + String.valueOf(order.getTotal_cost()));
        holder.tvQuantity.setText("Số lượng: " + String.valueOf(order.getQuantity()));
        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = done
                //reload fragment
                //sendNotif.notificationHandle(order.getBuyer_name());
                updateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php",Order.Status.SUCCESS, order.getId(),myContext);
                String message = Variable.SELLER.getName() + " đã xác nhận thanh toán của bạn\r Cảm ơn bạn vì đã sử dụng dịch vụ của chúng tôi";
                util.addNotification(myContext,Variable.SELLER.getId() , order.getBuyer_id(), message , order.getId() );
                sendNotif.notificationHandle(order.getFirebase_UID(), "Wasted food app" , message);
                ProductDetailSellerFragment productDetailSellerFragment = new ProductDetailSellerFragment();
                myFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, productDetailSellerFragment, productDetailSellerFragment.getTag()).commit();
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set status = rejected
                //set btnReject text to Đã từ chối set clickable = false
                updateStatusForOrder.updateOrderStatus(Variable.IP_ADDRESS + "seller/updateStatusForOrderSeller.php",Order.Status.CANCEL, order.getId(),myContext);
                ProductDetailSellerFragment productDetailSellerFragment = new ProductDetailSellerFragment();
                myFragmentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.content_main, productDetailSellerFragment, productDetailSellerFragment.getTag()).commit();
            }
        });
        return convertView;
    }

}
