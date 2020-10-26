package com.example.wastedfoodteam.buyer.buy;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wastedfoodteam.R;
import com.example.wastedfoodteam.model.Product;
import com.example.wastedfoodteam.utils.DownloadImageTask;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Product> productList;
    Resources resources;
    public ProductAdapter(Context context, int layout, List<Product> productList, Resources resources) {
        this.context = context;
        this.layout = layout;
        this.productList = productList;
        this.resources = resources;
    }

    private class ViewHolder {
        TextView tvTitle;
        ImageView ivProduct;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

    @Override
    public Object getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.tvTitle = convertView.findViewById(R.id.tvTitleLPI);
            holder.ivProduct = convertView.findViewById(R.id.ivProductLPI);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = productList.get(position);

        holder.tvTitle.setText(product.getName() + "");

        //get image from url
        new DownloadImageTask(holder.ivProduct,resources).execute(product.getImage());

        return convertView;

    }
}
