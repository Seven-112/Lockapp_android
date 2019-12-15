package com.codecanyon.lockscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;

    CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Object localObject = null;

        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (position) {
            case 0:

                if (getpreferences("PasscodeType").equalsIgnoreCase("Pin")) {
                    localObject = PinPasswordLayout.fromXml(this.mContext, collection);
                    ((PinPasswordLayout) localObject).openLayout();
                } else if (getpreferences("PasscodeType").equalsIgnoreCase("Pattern")) {
                    localObject = PatternLayout.fromXml(this.mContext, collection);
                    ((PatternLayout) localObject).openLayout();
                }


                break;
            case 1:
                localObject = LockscrenLayout.fromXml(this.mContext, collection);
                ((LockscrenLayout) localObject).openLayout();
                break;
        }

        return localObject;

    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");

    }

}
