package com.example.wastedfoodteam.seller.sellerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.global.Variable;
import com.example.wastedfoodteam.seller.Product1;
import com.example.wastedfoodteam.source.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductSellerAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Product> arrayProduct;

    private class ViewHolder {
        TextView tvName;
        ImageView ivImage;
    }

    public ProductSellerAdapter(Context context, int layout, List<Product> productList){
        myContext = context;
        myLayout = layout;
        arrayProduct = productList;
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
        Picasso.get().load(product.getImage().isEmpty() ? Variable.noImageUrl : product.getImage()).into(holder.ivImage);//TODO replace with other type
        return convertView;
    }
}
