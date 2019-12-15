package com.codecanyon.lockscreen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class LockscrenLayout extends RelativeLayout {

    @SuppressLint("StaticFieldLeak")
    private static ViewGroup mContainer;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    TextView UnlockTxtClock, UnlockTxtDay, UnlockTxtMsg;
    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    CustomDigitalClock UnlockDClock;
    ShinnyTextView TxtSlide;
    final String[] FontList = new String[]{"Roboto-Thin.ttf", "font1.ttf", "font2.ttf", "font3.otf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf"};
    final String[] SFontList = new String[]{"Roboto-Medium.ttf", "font1.ttf", "font2.ttf", "font3.otf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf"};
    public LockscrenLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public LockscrenLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public LockscrenLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public static LockscrenLayout fromXml(Context paramContext, ViewGroup paramViewGroup) {

        mContext = paramContext;
        mContainer = paramViewGroup;
        return (LockscrenLayout) LayoutInflater.from(paramContext).inflate(R.layout.fragment2, paramViewGroup, false);
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();

        UnlockFontThin = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Light.ttf");

        UnlockTxtClock = (TextView) findViewById(R.id.UnlockTxtClock);
        UnlockTxtDay = (TextView) findViewById(R.id.UnlockTxtDay);
        TxtSlide = (ShinnyTextView) findViewById(R.id.UnlockTxtSlide);
        UnlockDClock = (CustomDigitalClock) findViewById(R.id.UnlockDClock);
        UnlockTxtMsg = (TextView) findViewById(R.id.UnlockTxtMsg);

        TxtSlide.setTypeface(UnlockMedium);
        UnlockTxtClock.setTypeface(UnlockFontThin);
        UnlockTxtDay.setTypeface(UnlockLight);
        UnlockTxtMsg.setTypeface(UnlockLight);

        if (!getpreferences("ClockSize").equalsIgnoreCase("0")) {
            UnlockTxtClock.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(getpreferences("ClockSize")));
            UnlockTxtDay.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(getpreferences("DaySize")));
        }

        if (!getpreferences("SlideTextSize").equalsIgnoreCase("0")) {
            TxtSlide.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(getpreferences("SlideTextSize")));
        }

        if (!getpreferences("TextColor").equalsIgnoreCase("0")) {
            UnlockTxtClock.setTextColor(Integer.parseInt(getpreferences("TextColor")));
            UnlockTxtDay.setTextColor(Integer.parseInt(getpreferences("TextColor")));
        }

        if (!getpreferences("FontStyle").equalsIgnoreCase("0")) {
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), FontList[Integer.parseInt(getpreferences("FontStyle"))]);
            UnlockTxtClock.setTypeface(type, Typeface.NORMAL);
            UnlockTxtDay.setTypeface(type, Typeface.NORMAL);
        }

        if (!getpreferences("SlideFontStyle").equalsIgnoreCase("0")) {
            Typeface type = Typeface.createFromAsset(mContext.getAssets(), SFontList[Integer.parseInt(getpreferences("SlideFontStyle"))]);
            TxtSlide.setTypeface(type, Typeface.NORMAL);
        }

        if (!getpreferences("SlideText").equalsIgnoreCase("0")) {
            TxtSlide.setText(getpreferences("SlideText"));
        }

        if (!getpreferences("MessageText").equalsIgnoreCase("0")) {
            UnlockTxtMsg.setText(getpreferences("MessageText"));
        }

        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_MONTH);
        String dt = String.valueOf(date);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(c.getTime());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);

        UnlockTxtDay.setText(dayOfTheWeek + ", " + month_name + " " + dt);

        UnlockDClock.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                UnlockTxtClock.setText(s.toString());
            }
        });

    }

    public void openLayout() {
        mContainer.addView(this);
        try {
            requestFocus();
            requestLayout();
            return;
        } catch (Exception localException) {
            for (; ; ) {
                localException.printStackTrace();
            }
        }
    }

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
    }
}
