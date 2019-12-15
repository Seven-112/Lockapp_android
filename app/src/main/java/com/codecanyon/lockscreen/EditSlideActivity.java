package com.codecanyon.lockscreen;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class EditSlideActivity extends AppCompatActivity
        implements SlideToUnlockFontSize.OnFragmentInteractionListener,
        SlideToUnlockFontStyle.OnFragmentInteractionListener,
        SlideToUnlockText.OnFragmentInteractionListener {

    Toolbar toolbar;
    ImageView ImgEditSlideBg;
    public ShinnyTextView TxtSlide;
    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    ImageView BackgroundBlurLayer;
    SlideToUnlockText mSlideToUnlockText;
    SlideToUnlockFontStyle mSlideToUnlockFontStyle;
    SlideToUnlockFontSize mSlideToUnlockFontSize;
    final String[] FontList = new String[]{"Roboto-Medium.ttf", "font1.ttf", "font2.ttf", "font3.otf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf"};
    TabLayout tabs;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_slide);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Slide Text Style");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mSlideToUnlockText = new SlideToUnlockText();
        mSlideToUnlockFontStyle = new SlideToUnlockFontStyle();
        mSlideToUnlockFontSize = new SlideToUnlockFontSize();

        viewPager = (ViewPager) findViewById(R.id.pager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);

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

        UnlockFontThin = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Light.ttf");

        ImgEditSlideBg = (ImageView) findViewById(R.id.ImgEditSlideBg);

        TxtSlide = (ShinnyTextView) findViewById(R.id.UnlockTxtSlide);
        BackgroundBlurLayer = (ImageView) findViewById(R.id.BackgroundBlurLayer);

        TxtSlide.setTypeface(UnlockMedium);

        setImageBaclgroundBlurLayer();

        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {
            String filePath1 = getpreferences("WallpaperGalleryBlur");
            Glide.with(getApplicationContext())
                    .load(new File(filePath1))
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImgEditSlideBg);

        } else {
            Glide.with(getApplicationContext())
                    .load(Config.imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImgEditSlideBg);
        }

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
            case android.R.id.home:
                finish();
                return true;

            case R.id.reset:
                if (mSlideToUnlockFontSize.SeekbarTextSize != null) {
                    mSlideToUnlockFontSize.SeekbarTextSize.setProgress(0);
                }
                Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Medium.ttf");
                TxtSlide.setTypeface(typeface, Typeface.NORMAL);
                TxtSlide.setTypeface(typeface, Typeface.NORMAL);
                TxtSlide.setText(("Slide to unlock"));

                if (mSlideToUnlockText.etSlideText != null) {
                    mSlideToUnlockText.etSlideText.setText("Slide to unlock");
                }

                SavePreferences("SlideText", "0");
                SavePreferences("SlideTextSize", "0");
                SavePreferences("SlideFontStyle", "0");

                break;
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(mSlideToUnlockText, "Text");
        adapter.addFragment(mSlideToUnlockFontStyle, "Style");
        adapter.addFragment(mSlideToUnlockFontSize, "Size");
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
    public void onFragmentInteraction(int size, int type, String txt) {
        if (type == 3) {
            TxtSlide.setTextSize((float) size);
        }
        if (type == 2) {
            Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), FontList[size]);
            TxtSlide.setTypeface(typeface, Typeface.NORMAL);
        }
        if (type == 1) {
            TxtSlide.setText(txt);
        }

    }

    @Override
    protected void onResume() {
        if (getpreferences("ChangeWallpaper").equalsIgnoreCase("true")) {
            setImageBaclgroundBlurLayer();
        }
        super.onResume();
    }

}
