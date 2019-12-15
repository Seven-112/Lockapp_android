package com.codecanyon.lockscreen;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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

public class LicenseActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView BackgroundBlurLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_license);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("License");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
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

        BackgroundBlurLayer = (ImageView) findViewById(R.id.BackgroundBlurLayer);
        setImageBaclgroundBlurLayer();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setImageBaclgroundBlurLayer() {
        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {
            String filePath1 = getpreferences("WallpaperGalleryBlur");
            Glide.with(getApplicationContext())
                    .load(new File(filePath1))
                    .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(BackgroundBlurLayer);
        } else {
            Glide.with(getApplicationContext())
                    .load(Config.imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new jp.wasabeef.glide.transformations.BlurTransformation(getApplicationContext(), 25))
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
}
