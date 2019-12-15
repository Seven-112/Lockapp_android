package com.codecanyon.lockscreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.codecanyon.lockscreen.Config.imageIdMain;

public class ViewWallpaperActivity extends AppCompatActivity {

    ImageView ImgBackgroundWallpaper;
    TextView UnlockTxtClock, UnlockTxtDay, UnlockTxtMsg;
    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    CustomDigitalClock UnlockDClock;
    RelativeLayout btn_done;
    Integer Position;
    String Path, PathBlur;
    InterstitialAd mInterstitialAd;
    AdRequest adRequestint;
    final String[] FontList = new String[]{"Roboto-Thin.ttf", "font1.ttf", "font2.ttf", "font3.otf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf"};
    AlertDialog.Builder acation_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        ImgBackgroundWallpaper = (ImageView) findViewById(R.id.ImgBackground);

        UnlockFontThin = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Light.ttf");
        UnlockTxtClock = (TextView) findViewById(R.id.UnlockTxtClock);
        UnlockTxtDay = (TextView) findViewById(R.id.UnlockTxtDay);
        UnlockDClock = (CustomDigitalClock) findViewById(R.id.UnlockDClock);
        UnlockTxtMsg = (TextView) findViewById(R.id.UnlockTxtMsg);
        btn_done = (RelativeLayout) findViewById(R.id.ic_done);
        UnlockTxtClock.setTypeface(UnlockFontThin);
        UnlockTxtDay.setTypeface(UnlockLight);
        UnlockTxtMsg.setTypeface(UnlockLight);

        if (this.getIntent().hasExtra("WallpaperPosition")) {
            Position = this.getIntent().getIntExtra("WallpaperPosition", 0);
            ImgBackgroundWallpaper.setImageResource(imageIdMain[Position]);
        } else if (this.getIntent().hasExtra("WallpaperGallary")) {
            Path = this.getIntent().getStringExtra("WallpaperGallary");
            PathBlur = this.getIntent().getStringExtra("WallpaperGallaryBlur");
            Bitmap selectedImageb = BitmapFactory.decodeFile(Path);
            ImgBackgroundWallpaper.setImageBitmap(selectedImageb);
        }

        if (!getpreferences("ClockSize").equalsIgnoreCase("0")) {
            UnlockTxtClock.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    Float.parseFloat(getpreferences("ClockSize")));
            UnlockTxtDay.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    Float.parseFloat(getpreferences("DaySize")));
        }

        if (!getpreferences("TextColor").equalsIgnoreCase("0")) {
            UnlockTxtClock.setTextColor(Integer
                    .parseInt(getpreferences("TextColor")));
            UnlockTxtDay.setTextColor(Integer
                    .parseInt(getpreferences("TextColor")));
        }

        if (!getpreferences("FontStyle").equalsIgnoreCase("0")) {
            Typeface type = Typeface.createFromAsset(getApplicationContext()
                    .getAssets(), FontList[Integer
                    .parseInt(getpreferences("FontStyle"))]);
            UnlockTxtClock.setTypeface(type, Typeface.NORMAL);
            UnlockTxtDay.setTypeface(type, Typeface.NORMAL);
        }

        if (!getpreferences("MessageText").equalsIgnoreCase("0")) {
            UnlockTxtMsg.setText(getpreferences("MessageText"));
        }

        loadInterstitialAd();

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

        btn_done.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (Path == null) {
                    SavePreferences("Wallpaper", "" + Position);
                    SavePreferences("WallpaperGallery", "false");
                    SavePreferences("ChangeWallpaper", "true");
                } else {
                    SavePreferences("WallpaperGallery", "" + Path);
                    SavePreferences("WallpaperGalleryBlur", "" + PathBlur);
                    SavePreferences("Wallpaper", "false");
                    SavePreferences("ChangeWallpaper", "true");
                }

                acation_done = new AlertDialog.Builder(ViewWallpaperActivity.this);
                acation_done.setTitle("Your wallpaper set successfully");
                acation_done.setCancelable(true);
                acation_done.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                finish();
                                showInterstitial();
                            }
                        });
                acation_done.show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        showInterstitial();
        finish();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        ImgBackgroundWallpaper.setImageDrawable(null);
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

    private void loadInterstitialAd() {
        adRequestint = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial));
        mInterstitialAd.loadAd(adRequestint);
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
