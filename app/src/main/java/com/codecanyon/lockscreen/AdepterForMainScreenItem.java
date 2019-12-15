package com.codecanyon.lockscreen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

class AdepterForMainScreenItem extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    private String[] image_name;
    private Integer[] image_orignal;

    AdepterForMainScreenItem(Context context, String[] image_name, Integer[] image_orignal) {
        this.context = context;
        this.image_name = image_name;
        this.image_orignal = image_orignal;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            row = inflater.inflate(R.layout.items, parent, false);
            holder = new ViewHolder();
            holder.imageForOrignal = (ImageView) row.findViewById(R.id.image_orignal);
            holder.textView = (TextView) row.findViewById(R.id.text);
            holder.textView.setText(image_name[position]);
            holder.imageForOrignal.setImageResource(image_orignal[position]);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        return row;
    }

    private static class ViewHolder {
        ImageView imageForOrignal;
        TextView textView;
    }

}
