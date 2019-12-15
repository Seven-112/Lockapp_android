package com.codecanyon.lockscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PinPasswordLayout extends RelativeLayout {

    @SuppressLint("StaticFieldLeak")
    private static ViewGroup mContainer;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    TextView TxtPasscode1, TxtPasscode2, TxtPasscode3, TxtPasscode4, TxtPasscode5, TxtPasscode6, TxtPasscode7, TxtPasscode8, TxtPasscode9, TxtPasscode0, TxtEnterPasscode;
    RelativeLayout BtnPasscode1, BtnPasscode2, BtnPasscode3, BtnPasscode4, BtnPasscode5, BtnPasscode6, BtnPasscode7, BtnPasscode8, BtnPasscode9, BtnPasscode0;
    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    ImageView PassDot1, PassDot2, PassDot3, PassDot4, PassDot5, PassDot6, PassDot7, PassDot8;
    String Passcode, EnterPasscode;
    Boolean vibrate = false;
    private MediaPlayer mMediaPlayer;
    Integer Lenth;
    ArrayList<ImageView> mPassDot = new ArrayList<>();

    public PinPasswordLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public PinPasswordLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public PinPasswordLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public static PinPasswordLayout fromXml(Context paramContext, ViewGroup paramViewGroup) {
        mContext = paramContext;
        mContainer = paramViewGroup;
        return (PinPasswordLayout) LayoutInflater.from(paramContext).inflate(R.layout.new_fragment1, paramViewGroup, false);
    }

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(Context c, int rid) {
        stop();
        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });
        mMediaPlayer.start();
    }

    @Override
    protected void onFinishInflate() {
        // TODO Auto-generated method stub
        super.onFinishInflate();

        Lenth = getpreferences("PasscodeValue").length();
        Passcode = getpreferences("PasscodeValue");
        EnterPasscode = "";

        if (getBoolVibration(mContext)) {
            vibrate = true;
        }

        UnlockFontThin = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(mContext.getAssets(), "Roboto-Light.ttf");
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

        for (int i = 0; i < Lenth; i++) {
            mPassDot.get(i).setVisibility(View.VISIBLE);
        }


        BtnPasscode1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "1";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);

            }

        });

        BtnPasscode2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "2";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "3";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode4.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "4";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode5.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "5";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode6.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "6";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode7.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "7";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode8.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "8";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode9.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "9";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });

        BtnPasscode0.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                EnterPasscode = EnterPasscode + "0";
                if (vibrate) {
                    Vibrator v1 = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v1.vibrate(20);
                }
                PasscodeEntered(EnterPasscode);
            }
        });
    }

    public void PasscodeEntered(final String EPass) {
        // TODO Auto-generated method stub

        for (int i = 0; i < EPass.length(); i++) {
            if (EPass.length() <= Lenth) {
                mPassDot.get(i).setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pass_fill));
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
                    if (EPass.equalsIgnoreCase(Passcode)) {
                        if (getBoolSound(mContext)) {
                            play(mContext, R.raw.unlock);
                        }
                        mContext.stopService(new Intent(mContext, LockscreenService.class));
                        SavePreferences("LockState", "OPEN");
                    } else {

                        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
                        findViewById(R.id.LayoutPasscode).startAnimation(shake);
                        if (getBoolVibration(mContext)) {
                            Vibrator v = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(500);
                        }
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
                                    mPassDot.get(i).setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.pass_border));
                                }

                            }
                        });

                    }
                }
            }, 300);

        }

    }

    public boolean getBoolSound(Context context) {
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean("PrefLockscreenSound", false);
    }

    public boolean getBoolVibration(Context context) {
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean("PrefLockscreenVibration", false);
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
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
}
