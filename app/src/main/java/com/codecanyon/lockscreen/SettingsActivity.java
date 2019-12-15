package com.codecanyon.lockscreen;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SettingsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView BackgroundBlurLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Settings");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        BackgroundBlurLayer = (ImageView) findViewById(R.id.BackgroundBlurLayer);

        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        Settings mPrefsFragment = new Settings();
        mFragmentTransaction.replace(R.id.content_frame, mPrefsFragment);
        mFragmentTransaction.commit();

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

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
    }

    private void setImageBaclgroundBlurLayer() {
        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {

            String filePath1 = getpreferences("WallpaperGalleryBlur");

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

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getpreferences("ChangeWallpaper").equalsIgnoreCase("true")) {
            setImageBaclgroundBlurLayer();
        }
    }
}
