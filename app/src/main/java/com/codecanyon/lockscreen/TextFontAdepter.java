package com.codecanyon.lockscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;


class TextFontAdepter extends BaseAdapter {

    private Context context;
    private final String[] mFonts;

    TextFontAdepter(Context context, String[] fonts) {
        this.context = context;
        this.mFonts = fonts;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        ViewHolderItem viewHolder = new ViewHolderItem();
        gridView = inflater.inflate(R.layout.item_font_style, null);
        viewHolder.txt_font_item = (TextView) gridView.findViewById(R.id.txt_font_item);
        RetrieveTimeWS async = new RetrieveTimeWS(viewHolder.txt_font_item, mFonts[position]);
        async.execute();

        gridView.setTag(viewHolder);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        gridView.setLayoutParams(new GridView.LayoutParams(params));

        return gridView;
    }

    @Override
    public int getCount() {
        return mFonts.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private static class ViewHolderItem {
        TextView txt_font_item;
    }


    private class RetrieveTimeWS extends AsyncTask<String, Void, Typeface> {

        TextView txt;
        String mfont;

        RetrieveTimeWS(TextView txt, String font) {
            super();
            this.txt = txt;
            this.mfont = font;
        }

        @Override
        protected Typeface doInBackground(String... params) {
            return Typeface.createFromAsset(context.getAssets(), mfont);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txt.setText("Loading..");
        }

        @Override
        protected void onPostExecute(Typeface s) {
            super.onPostExecute(s);
            txt.setText("AaBb");
            txt.setTypeface(s);
        }
    }


}
