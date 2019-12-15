package com.codecanyon.lockscreen;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import afzkl.development.colorpickerview.dialog.ColorPickerDialogFragment;
import jp.wasabeef.glide.transformations.BlurTransformation;


public class EditClockActivity extends AppCompatActivity
        implements ColorPickerDialogFragment.ColorPickerDialogListener,
        DateAndTimeFontSize.OnFragmentInteractionListener,
        DateAndTimeFontStyle.OnFragmentInteractionListener,
        DateAndTimeFontColor.OnFragmentInteractionListener {

    Toolbar toolbar;
    ImageView ImgEditClockBg;
    TextView UnlockTxtClock, UnlockTxtDay;
    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    CustomDigitalClock UnlockDClock;
    DateAndTimeFontSize mDateAndTimeFontSize;
    DateAndTimeFontColor mDateAndTimeFontColor;
    DateAndTimeFontStyle mDateAndTimeFontStyle;
    ViewPagerAdapter adapter;
    ImageView BackgroundBlurLayer;
    TabLayout tabs;
    Integer TextColor;
    final String[] FontList = new String[]{"Roboto-Thin.ttf", "font1.ttf", "font2.ttf", "font3.otf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_clock);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Clock Style");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDateAndTimeFontSize = new DateAndTimeFontSize();
        mDateAndTimeFontColor = new DateAndTimeFontColor();
        mDateAndTimeFontStyle = new DateAndTimeFontStyle();

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);


        UnlockFontThin = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Light.ttf");

        ImgEditClockBg = (ImageView) findViewById(R.id.ImgEditClockBg);

        UnlockTxtClock = (TextView) findViewById(R.id.UnlockTxtClock);
        UnlockTxtDay = (TextView) findViewById(R.id.UnlockTxtDay);
        UnlockDClock = (CustomDigitalClock) findViewById(R.id.UnlockDClock);
        BackgroundBlurLayer = (ImageView) findViewById(R.id.BackgroundBlurLayer);
        UnlockTxtClock.setTypeface(UnlockFontThin);
        UnlockTxtDay.setTypeface(UnlockLight);
        setImageBaclgroundBlurLayer();

        final AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });


        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {

            String filePath1 = getpreferences("WallpaperGalleryBlur");

            Glide.with(getApplicationContext())
                    .load(new File(filePath1))
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImgEditClockBg);

        } else {
            Glide.with(getApplicationContext())
                    .load(Config.imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImgEditClockBg);
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


    private void setImageBaclgroundBlurLayer() {
        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {

            String filePath1 = getpreferences("WallpaperGallery");

            Glide.with(getApplicationContext())
                    .load(new File(filePath1))
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(BackgroundBlurLayer);
        } else {

            Glide.with(getApplicationContext())
                    .load(Config.imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(BackgroundBlurLayer);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mDateAndTimeFontColor, "Color");
        adapter.addFragment(mDateAndTimeFontStyle, "Style");
        adapter.addFragment(mDateAndTimeFontSize, "Size");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public void onFragmentInteraction(int size, int type) {
        if (type == 3) {
            UnlockTxtClock.setTextSize((float) size);
            UnlockTxtDay.setTextSize((float) size / 4);
        }
        if (type == 2) {
            Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), FontList[size]);
            UnlockTxtClock.setTypeface(typeface, Typeface.NORMAL);
            UnlockTxtDay.setTypeface(typeface, Typeface.NORMAL);
        }
        if (type == 1) {

            UnlockTxtClock.setTextColor(size);
            UnlockTxtDay.setTextColor(size);
            SavePreferences("TextColor", "" + size);
        }

    }

    @Override
    protected void onResume() {
        if (getpreferences("ChangeWallpaper").equalsIgnoreCase("true")) {
            setImageBaclgroundBlurLayer();
        }
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;

            case R.id.reset:

                if (mDateAndTimeFontSize.SeekbarTextSize != null) {
                    mDateAndTimeFontSize.SeekbarTextSize.setProgress(20);
                }
                Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Thin.ttf");
                UnlockTxtClock.setTypeface(typeface, Typeface.NORMAL);
                UnlockTxtDay.setTypeface(typeface, Typeface.NORMAL);
                UnlockTxtClock.setTextColor(Color.parseColor("#FFFFFF"));
                UnlockTxtDay.setTextColor(Color.parseColor("#FFFFFF"));
                SavePreferences("TextColor", "0");
                SavePreferences("ClockSize", "0");
                SavePreferences("FontStyle", "0");

                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onColorSelected(int dialogId, int color) {

        if (dialogId == Config.DIALOG_TEXT_COLOR) {
            TextColor = color;
            UnlockTxtDay.setTextColor(TextColor);
            UnlockTxtClock.setTextColor(TextColor);
            mDateAndTimeFontColor.ChangeTextColor(TextColor);
        }

    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }

}
