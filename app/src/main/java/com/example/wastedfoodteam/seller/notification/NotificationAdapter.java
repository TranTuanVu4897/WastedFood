package com.example.wastedfoodteam.seller.notification;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Notification;
import com.example.wastedfoodteam.utils.CommonFunction;
import com.example.wastedfoodteam.utils.service.TimeCount;

import java.util.List;

public class NotificationAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    FragmentActivity myActivity;
    List<Notification> arrayNotification;
    Notification notification;
    Resources resources;
    TimeCount timeCount;

    private class ViewHolder {
        ImageView ivBuyer;
        TextView tvContent, tvTime;
    }

    public NotificationAdapter(Context context, int layout, List<Notification> notificationList, Resources resources, FragmentActivity fragmentActivity) {
        myContext = context;
        myLayout = layout;
        arrayNotification = notificationList;
        this.resources = resources;
        myActivity = fragmentActivity;
    }

    @Override
    public int getCount() {
        return arrayNotification.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayNotification.get(position);
    }

    @Override
    public long getItemId(int position) {
        return arrayNotification.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(myLayout, null);
            holder.tvContent = convertView.findViewById(R.id.tv_notification_seller_content);
            holder.tvTime = convertView.findViewById(R.id.tv_notification_seller_time);
            holder.ivBuyer = convertView.findViewById(R.id.iv_notification_seller_buyer_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        notification = arrayNotification.get(position);
        timeCount = new TimeCount();
        CommonFunction.setImageViewSrc(myContext, notification.getBuyer_image(), holder.ivBuyer);
        //Glide.with(convertView.getContext()).load(order.getImage().isEmpty() ? Variable.noImageUrl : order.getImage()).into(holder.ivBuyer);
        String time = timeCount.countTimeAgo(notification.getModified_date().getTime());
        holder.tvTime.setText(time);
        holder.tvContent.setText(notification.getContent());
        return convertView;
    }

}