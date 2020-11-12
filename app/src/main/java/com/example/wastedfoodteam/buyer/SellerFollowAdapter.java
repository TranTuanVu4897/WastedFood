package com.example.wastedfoodteam.buyer;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Seller;
import com.example.wastedfoodteam.utils.CommonFunction;

import java.util.List;

public class SellerFollowAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Seller> sellerList;
    Resources resources;

    public SellerFollowAdapter(Context context, int layout, List<Seller> sellerList, Resources resources) {
        this.context = context;
        this.layout = layout;
        this.sellerList = sellerList;
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return sellerList.size();
    }

    @Override
    public Object getItem(int position) {
        return sellerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return sellerList.get(position).getId();
    }

    private class ViewHolder {
        ImageView ivSeller;
        TextView tvNameSeller, tvDirection, tvAddress;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout,null);
            holder.tvNameSeller = convertView.findViewById(R.id.tvNameSellerFSFI);
            holder.tvAddress = convertView.findViewById(R.id.tvAddress);
            holder.ivSeller = convertView.findViewById(R.id.ivSellerLSFI);
            holder.tvDirection = convertView.findViewById(R.id.tvDirection);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        Seller seller = sellerList.get(position);

        holder.tvNameSeller.setText(seller.getName());
        holder.tvAddress.setText(seller.getAddress());
        holder.tvDirection.setText("2km");
        CommonFunction.setImageViewSrc(context,seller.getImage(),holder.ivSeller);
        return convertView;
    }
}
