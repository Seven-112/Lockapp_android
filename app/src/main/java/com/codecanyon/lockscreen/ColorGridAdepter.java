package com.codecanyon.lockscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

class ColorGridAdepter extends ArrayAdapter<String> {

    private Context mContext;
    private final String[] gridcolor;
    LayoutInflater inflater;
    private int mColor;


    ColorGridAdepter(Context context, String[] gridcolor, int SelectedColor) {
        super(context, R.layout.grid_layout);
        mContext = context;
        this.mColor = SelectedColor;
        this.gridcolor = gridcolor;
    }

    @Override
    public int getCount() {
        return gridcolor.length;
    }

    @Override
    public String getItem(int position) {
        return String.valueOf(gridcolor[position]);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @SuppressLint({"ViewHolder", "InflateParams"})
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.grid_layout, null);

        ImageView item = (ImageView) convertView.findViewById(R.id.grid_img);
        ImageView color_picker_image = (ImageView) convertView.findViewById(R.id.color_picker_image);

        if (position == 0) {
            color_picker_image.setVisibility(View.VISIBLE);
            item.setBackgroundColor(mColor);

        } else {
            item.setBackgroundColor(Color.parseColor(gridcolor[position]));
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        convertView.setLayoutParams(new GridView.LayoutParams(params));

        return convertView;
    }
}
