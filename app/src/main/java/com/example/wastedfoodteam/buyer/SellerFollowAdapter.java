package com.example.wastedfoodteam.buyer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.example.wastedfoodteam.model.Seller;

import java.util.List;

public class SellerFollowAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Seller> sellerList;
    Resource resource;

    public SellerFollowAdapter(Context context, int layout, List<Seller> sellerList, Resource resource){
        this.context = context;
        this.layout = layout;
        this.sellerList = sellerList;
        this.resource = resource;
    }
    @Override
    public int getCount() {
        return sellerList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView ivseller;
        TextView tvNameSeller;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
