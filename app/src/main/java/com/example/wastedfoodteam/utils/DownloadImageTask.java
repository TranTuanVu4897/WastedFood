package com.example.wastedfoodteam.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.Resource;
import com.example.wastedfoodteam.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * class for load image from url to image view
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView imageView;
    Resources resource;
    public DownloadImageTask(ImageView imageView, Resources resource) {
        this.imageView = imageView;
        this.resource = resource;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String imageUrl = urls[0];
        Bitmap bmIcon = null;

        try {
            InputStream in = new URL(imageUrl).openStream();
            bmIcon = BitmapFactory.decodeStream(in);
        } catch (IOException e) {
            bmIcon = BitmapFactory.decodeResource(resource,R.drawable.no_image);

        }
        return bmIcon;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
