package com.codecanyon.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


class CustomGrid extends ArrayAdapter {

    private Context context;
    private int layoutResourceId;
    private int[] image_orignal;

    CustomGrid(Context context, int layoutResourceId, int[] item_image) {
        super(context, layoutResourceId);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.image_orignal = item_image;
    }


    @Override
    public int getCount() {
        return image_orignal.length;
    }

    @Override
    public Object getItem(int position) {
        return image_orignal[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageForOrignal = (ImageView) row.findViewById(R.id.ImgWallpaper);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        Glide.with(context)
                .load(image_orignal[position])
                .into(holder.imageForOrignal);
        return row;
    }

    private static class ViewHolder {
        ImageView imageForOrignal;
    }
}
