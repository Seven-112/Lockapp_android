package com.codecanyon.lockscreen;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class EditMessageActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView ImgEditMessageBg;
    TextView UnlockTxtClock, UnlockTxtDay, UnlockTxtMsg, Txt_msg;
    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    CustomDigitalClock UnlockDClock;
    final String[] FontList = new String[]{"Roboto-Thin.ttf", "font1.ttf", "font2.ttf", "font3.otf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf"};
    ImageView BackgroundBlurLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_message);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Message Text");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        UnlockFontThin = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "Roboto-Light.ttf");

        ImgEditMessageBg = (ImageView) findViewById(R.id.ImgEditMessageBg);

        UnlockTxtClock = (TextView) findViewById(R.id.UnlockTxtClock);
        UnlockTxtDay = (TextView) findViewById(R.id.UnlockTxtDay);
        UnlockTxtMsg = (TextView) findViewById(R.id.UnlockTxtMsg);
        Txt_msg = (TextView) findViewById(R.id.txt_msg_text);
        UnlockDClock = (CustomDigitalClock) findViewById(R.id.UnlockDClock);
        BackgroundBlurLayer = (ImageView) findViewById(R.id.BackgroundBlurLayer);

        UnlockTxtClock.setTypeface(UnlockFontThin);
        UnlockTxtDay.setTypeface(UnlockLight);
        UnlockTxtMsg.setTypeface(UnlockLight);

        setImageBaclgroundBlurLayer();

        if (!getpreferences("ClockSize").equalsIgnoreCase("0")) {
            UnlockTxtClock.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(getpreferences("ClockSize")));
            UnlockTxtDay.setTextSize(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(getpreferences("DaySize")));
        }

        if (!getpreferences("TextColor").equalsIgnoreCase("0")) {
            UnlockTxtClock.setTextColor(Integer.parseInt(getpreferences("TextColor")));
            UnlockTxtDay.setTextColor(Integer.parseInt(getpreferences("TextColor")));

        }

        if (!getpreferences("MessageText").equalsIgnoreCase("0")) {

            Txt_msg.setText(getpreferences("MessageText"));
            UnlockTxtMsg.setText(getpreferences("MessageText"));
        }

        if (!getpreferences("FontStyle").equalsIgnoreCase("0")) {
            Typeface type = Typeface.createFromAsset(getApplicationContext().getAssets(), FontList[Integer.parseInt(getpreferences("FontStyle"))]);
            UnlockTxtClock.setTypeface(type, Typeface.NORMAL);
            UnlockTxtDay.setTypeface(type, Typeface.NORMAL);
        }

        Calendar c = Calendar.getInstance();
        int date = c.get(Calendar.DAY_OF_MONTH);
        String dt = String.valueOf(date);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
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


        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {

            String filePath1 = getpreferences("WallpaperGalleryBlur");

            Glide.with(getApplicationContext())
                    .load(new File(filePath1))
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImgEditMessageBg);

        } else {
            Glide.with(getApplicationContext())
                    .load(Config.imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImgEditMessageBg);
        }

        Txt_msg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditMessageActivity.this);
                alertDialog.setTitle("Message text");
                alertDialog.setMessage("Enter text");

                final EditText input = new EditText(EditMessageActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                if (!getpreferences("MessageText").equalsIgnoreCase("0")) {
                    input.setText(getpreferences("MessageText"));
                }
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("SET",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                UnlockTxtMsg.setText(input.getText().toString());
                                SavePreferences("MessageText", input.getText().toString());
                                Txt_msg.setText(input.getText().toString());

                            }
                        });

                alertDialog.setNegativeButton("CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }
        });
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
            case android.R.id.home:
                finish();
                return true;

            case R.id.reset:
                SavePreferences("MessageText", "0");
                Txt_msg.setText("");
                UnlockTxtMsg.setText("");

                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
