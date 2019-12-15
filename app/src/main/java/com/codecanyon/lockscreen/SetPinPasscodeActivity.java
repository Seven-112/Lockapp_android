package com.codecanyon.lockscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SetPinPasscodeActivity extends AppCompatActivity {

    TextView TxtPasscode1, TxtPasscode2, TxtPasscode3, TxtPasscode4, TxtPasscode5, TxtPasscode6, TxtPasscode7, TxtPasscode8, TxtPasscode9, TxtPasscode0, TxtEnterPasscode;
    RelativeLayout BtnPasscode1, BtnPasscode2, BtnPasscode3, BtnPasscode4, BtnPasscode5, BtnPasscode6, BtnPasscode7, BtnPasscode8, BtnPasscode9, BtnPasscode0;
    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    ImageView PassDot1, PassDot2, PassDot3, PassDot4, PassDot5, PassDot6, PassDot7, PassDot8;
    String Passcode, EnterPasscode, CurrentPasscode;
    private String Confirm = "";
    private String NewPasscode = "";
    Integer Lenth;
    ArrayList<ImageView> mPassDot = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pin_passcode);

        if (this.getIntent().hasExtra("ChangePasscodeLenth")) {
            Lenth = this.getIntent().getIntExtra("ChangePasscodeLenth", 4);
        } else {
            if (getpreferences("PasscodeValue").length() == 1) {
                Lenth = 4;
            } else {
                Lenth = getpreferences("PasscodeValue").length();
            }

        }

        if (this.getIntent().hasExtra("Confirm")) {
            Confirm = "yes";
        }

        if (this.getIntent().hasExtra("NewPasscode")) {
            NewPasscode = "yes";
        }

        Passcode = "";
        EnterPasscode = "";

        ImageView imagePasscodeBlurBg = (ImageView) findViewById(R.id.PasscodeImageBlur);

        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {

            String filePath1 = getpreferences("WallpaperGalleryBlur");

            Glide.with(getApplicationContext())
                    .load(new File(filePath1))
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imagePasscodeBlurBg);

        } else {
            Glide.with(getApplicationContext())
                    .load(Config.imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(imagePasscodeBlurBg);

        }

        UnlockFontThin = Typeface.createFromAsset(this.getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(this.getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(this.getAssets(), "Roboto-Light.ttf");
        BtnPasscode1 = (RelativeLayout) findViewById(R.id.BtnPasscode1);
        BtnPasscode2 = (RelativeLayout) findViewById(R.id.BtnPasscode2);
        BtnPasscode3 = (RelativeLayout) findViewById(R.id.BtnPasscode3);
        BtnPasscode4 = (RelativeLayout) findViewById(R.id.BtnPasscode4);
        BtnPasscode5 = (RelativeLayout) findViewById(R.id.BtnPasscode5);
        BtnPasscode6 = (RelativeLayout) findViewById(R.id.BtnPasscode6);
        BtnPasscode7 = (RelativeLayout) findViewById(R.id.BtnPasscode7);
        BtnPasscode8 = (RelativeLayout) findViewById(R.id.BtnPasscode8);
        BtnPasscode9 = (RelativeLayout) findViewById(R.id.BtnPasscode9);
        BtnPasscode0 = (RelativeLayout) findViewById(R.id.BtnPasscode0);

        TxtPasscode1 = (TextView) findViewById(R.id.TxtPasscode1);
        TxtPasscode2 = (TextView) findViewById(R.id.TxtPasscode2);
        TxtPasscode3 = (TextView) findViewById(R.id.TxtPasscode3);
        TxtPasscode4 = (TextView) findViewById(R.id.TxtPasscode4);
        TxtPasscode5 = (TextView) findViewById(R.id.TxtPasscode5);
        TxtPasscode6 = (TextView) findViewById(R.id.TxtPasscode6);
        TxtPasscode7 = (TextView) findViewById(R.id.TxtPasscode7);
        TxtPasscode8 = (TextView) findViewById(R.id.TxtPasscode8);
        TxtPasscode9 = (TextView) findViewById(R.id.TxtPasscode9);
        TxtPasscode0 = (TextView) findViewById(R.id.TxtPasscode0);

        TxtEnterPasscode = (TextView) findViewById(R.id.TxtEnterPasscode);

        PassDot1 = (ImageView) findViewById(R.id.PassDot1);
        PassDot2 = (ImageView) findViewById(R.id.PassDot2);
        PassDot3 = (ImageView) findViewById(R.id.PassDot3);
        PassDot4 = (ImageView) findViewById(R.id.PassDot4);
        PassDot5 = (ImageView) findViewById(R.id.PassDot5);
        PassDot6 = (ImageView) findViewById(R.id.PassDot6);
        PassDot7 = (ImageView) findViewById(R.id.PassDot7);
        PassDot8 = (ImageView) findViewById(R.id.PassDot8);

        mPassDot.add(PassDot1);
        mPassDot.add(PassDot2);
        mPassDot.add(PassDot3);
        mPassDot.add(PassDot4);
        mPassDot.add(PassDot5);
        mPassDot.add(PassDot6);
        mPassDot.add(PassDot7);
        mPassDot.add(PassDot8);

        TxtPasscode1.setTypeface(UnlockFontThin);
        TxtPasscode2.setTypeface(UnlockFontThin);
        TxtPasscode3.setTypeface(UnlockFontThin);
        TxtPasscode4.setTypeface(UnlockFontThin);
        TxtPasscode5.setTypeface(UnlockFontThin);
        TxtPasscode6.setTypeface(UnlockFontThin);
        TxtPasscode7.setTypeface(UnlockFontThin);
        TxtPasscode8.setTypeface(UnlockFontThin);
        TxtPasscode9.setTypeface(UnlockFontThin);
        TxtPasscode0.setTypeface(UnlockFontThin);
        TxtEnterPasscode.setTypeface(UnlockLight);


        if (NewPasscode.equalsIgnoreCase("")) {
            if (getpreferences("PasscodeValue").equalsIgnoreCase("0")) {
                TxtEnterPasscode.setText("Enter Passcode");
                CurrentPasscode = "";
            } else {
                TxtEnterPasscode.setText("Enter Current Passcode");
                CurrentPasscode = getpreferences("PasscodeValue");
            }
        } else {
            TxtEnterPasscode.setText("Enter Passcode");
            CurrentPasscode = "";
        }


        for (int i = 0; i < Lenth; i++) {
            mPassDot.get(i).setVisibility(View.VISIBLE);
        }

        BtnPasscode1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "1";
                PasscodeEntered(EnterPasscode);

            }

        });

        BtnPasscode2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "2";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "3";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "4";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "5";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "6";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "7";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "8";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "9";
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "0";
                PasscodeEntered(EnterPasscode);
            }
        });
    }

    public void PasscodeEntered(final String EPass) {
        // TODO Auto-generated method stub


        for (int i = 0; i < EPass.length(); i++) {
            if (EPass.length() <= Lenth) {
                mPassDot.get(i).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_fill));
            } else {
                BtnPasscode0.setClickable(true);
                BtnPasscode1.setClickable(true);
                BtnPasscode2.setClickable(true);
                BtnPasscode3.setClickable(true);
                BtnPasscode4.setClickable(true);
                BtnPasscode5.setClickable(true);
                BtnPasscode6.setClickable(true);
                BtnPasscode7.setClickable(true);
                BtnPasscode8.setClickable(true);
                BtnPasscode9.setClickable(true);
            }
        }

        if (EPass.length() >= Lenth) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (CurrentPasscode.equalsIgnoreCase("")) {
                        if (Passcode.equalsIgnoreCase("")) {
                            Passcode = EPass;
                            TxtEnterPasscode.setText("Confirm Passcode");
                            EnterPasscode = "";
                            for (int i = 0; i < Lenth; i++) {
                                mPassDot.get(i).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_border));
                            }
                        } else {
                            if (EPass.equalsIgnoreCase(Passcode)) {
                                SavePreferences("PasscodeType", "Pin");
                                SavePreferences("PasscodeValue", Passcode);
                                finish();
                            } else {
                                EnterPasscode = "";
                                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                                findViewById(R.id.LayoutPasscode).startAnimation(shake);
                                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(500);

                                shake.setAnimationListener(new AnimationListener() {

                                    @Override
                                    public void onAnimationStart(Animation animation) {
                                        // TODO Auto-generated method stub
                                        EnterPasscode = "";
                                    }

                                    @Override
                                    public void onAnimationRepeat(
                                            Animation animation) {
                                        // TODO Auto-generated method stub

                                    }

                                    @Override
                                    public void onAnimationEnd(Animation animation) {
                                        // TODO Auto-generated method stub
                                        for (int i = 0; i < Lenth; i++) {
                                            mPassDot.get(i).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_border));
                                        }
                                    }
                                });
                            }
                        }
                    } else {
                        if (CurrentPasscode.equalsIgnoreCase(EPass)) {
                            if (Confirm.equalsIgnoreCase("")) {
                                TxtEnterPasscode.setText("Enter New Passcode");
                                EnterPasscode = "";
                                CurrentPasscode = "";

                                for (int i = 0; i < Lenth; i++) {
                                    mPassDot.get(i).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_border));
                                }
                            } else {
                                Intent i = new Intent(getApplicationContext(), NewPasscodeSettingActivity.class);
                                startActivity(i);
                                finish();

                            }
                        } else {
                            EnterPasscode = "";
                            Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                            findViewById(R.id.LayoutPasscode).startAnimation(shake);
                            Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(500);

                            shake.setAnimationListener(new AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {
                                    // TODO Auto-generated method stub
                                    EnterPasscode = "";
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                    // TODO Auto-generated method stub

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    // TODO Auto-generated method stub
                                    for (int i = 0; i < Lenth; i++) {
                                        mPassDot.get(i).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.pass_border));
                                    }
                                }
                            });
                        }
                    }
                }
            }, 300);

        }
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
}
