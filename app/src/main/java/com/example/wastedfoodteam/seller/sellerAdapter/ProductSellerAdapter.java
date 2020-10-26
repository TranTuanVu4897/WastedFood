package com.example.wastedfoodteam.seller.sellerAdapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.Product1;
import com.example.wastedfoodteam.source.model.Product;
import com.example.wastedfoodteam.utils.DownloadImageTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductSellerAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Product> arrayProduct;
    Resources resources;

    private class ViewHolder {
        TextView tvName;
        ImageView ivImage;
    }

    public ProductSellerAdapter(Context context, int layout, List<Product> productList,Resources resources){
        myContext = context;
        myLayout = layout;
        arrayProduct = productList;
        this.resources = resources;
    }

    @Override
    public int getCount() {
        return arrayProduct.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(myLayout, null);
            holder.tvName = convertView.findViewById(R.id.tv_list_seller_name);
            holder.ivImage = convertView.findViewById(R.id.iv_list_seller_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = arrayProduct.get(position);
        holder.tvName.setText(product.getName());
        //remove piccaso
        new DownloadImageTask(holder.ivImage, resources).execute(product.getImage());

        return convertView;
    }
}
