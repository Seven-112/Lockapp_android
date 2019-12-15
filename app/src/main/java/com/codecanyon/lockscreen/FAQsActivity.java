package com.codecanyon.lockscreen;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;
public class FAQsActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView BackgroundBlurLayer;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqs);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("FAQs");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        BackgroundBlurLayer = (ImageView) findViewById(R.id.BackgroundBlurLayer);
        setImageBaclgroundBlurLayer();
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

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

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Why my lockscreen is gone?");
        listDataHeader.add("Reguire Password");
        listDataHeader.add("Why my lockscreen not display?");
        listDataHeader.add("Why Parallax Wallpaper is not working?");

        List<String> lockscreenGone = new ArrayList<String>();
        lockscreenGone.add("Please do not use cleaning tools to clean up lockscreen, this will cause our lockscreen stop working.\n \n If you are using a cleaning app please add this lockscreen as ignore list.");

        List<String> requarePassword = new ArrayList<String>();
        requarePassword.add("The cell will not ask for the password in the set time. The cell will bw lock again later");

        List<String> notDisplay = new ArrayList<String>();
        notDisplay.add("Some device model not allow to draw overlay. You need to allow manually from system settings. \n System settings > Installed Apps > Permission manager > Display pop-up window.");

        List<String> parallaxNotWork = new ArrayList<String>();
        parallaxNotWork.add("Because some android device is not support Rotation Vector sensor.");


        listDataChild.put(listDataHeader.get(0), lockscreenGone);
        listDataChild.put(listDataHeader.get(1), requarePassword);
        listDataChild.put(listDataHeader.get(2), notDisplay);
        listDataChild.put(listDataHeader.get(3), parallaxNotWork);
    }


    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
    }

    @Override
    protected void onResume() {
        if (getpreferences("ChangeWallpaper").equalsIgnoreCase("true")) {
            setImageBaclgroundBlurLayer();
        }
        super.onResume();
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
}
