package com.codecanyon.lockscreen;

import android.animation.Animator;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

public class LockscreenService extends Service implements OnClickListener, View.OnKeyListener {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mLayoutParams;
    private View root;
    private ImageView MainLockImage;
    ImageView MainLockImageBlur;
    TextView MainLockBtnCancel;
    CustomViewPager mViewPager;
    String LockState = "";

    StateListener phoneStateListener;
    TelephonyManager telephonyManager;

    private MediaPlayer mMediaPlayer;

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
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        phoneStateListener = new StateListener();
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                mLayoutParams = new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                        WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                        PixelFormat.TRANSLUCENT);

        } else {
            mLayoutParams = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                            | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                    PixelFormat.TRANSLUCENT);
        }
        mLayoutParams.screenOrientation = 1;

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        LayoutInflater li = LayoutInflater.from(this);
        root = (View) li.inflate(R.layout.service_lockscreen, null);
        root.setOnKeyListener(this);
        root.setFocusable(true);
        root.setFocusableInTouchMode(true);

        mViewPager = (CustomViewPager) root.findViewById(R.id.pager);
        MainLockBtnCancel = (TextView) root.findViewById(R.id.MainLockBtnCancel);

        MainLockBtnCancel.setVisibility(View.GONE);
        MainLockImage = (ImageView) root.findViewById(R.id.MainLockImage);
        MainLockImageBlur = (ImageView) root.findViewById(R.id.MainLockImageBlur);

        Thread background = new Thread(new Runnable() {
            public void run() {
                try {
                    mViewPager.setAdapter(new CustomPagerAdapter(getApplicationContext()));
                    mViewPager.setCurrentItem(1);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        });
        background.start();

        try {
            if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {
                String filePath = getpreferences("WallpaperGallery");
                Glide.with(getApplicationContext()).load(Uri.fromFile(new File(filePath)))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(MainLockImage);

                String filePath1 = getpreferences("WallpaperGalleryBlur");
                Glide.with(getApplicationContext()).load(Uri.fromFile(new File(filePath1)))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(MainLockImageBlur);
            } else {
                Glide.with(getApplicationContext()).load(Config.imageIdMain[Integer
                        .parseInt(getpreferences("Wallpaper"))])
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(MainLockImage);
                Glide.with(getApplicationContext()).load(Config.imageId_blur[Integer
                        .parseInt(getpreferences("Wallpaper"))])
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(MainLockImageBlur);
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        MainLockBtnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mViewPager.setPagingEnabled(true);
                mViewPager.setCurrentItem(1);
            }
        });

        mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                if (arg0 == 0) {

                    if (getpreferences("PasscodeType").equalsIgnoreCase("0") || getpreferences("PasscodeType").equalsIgnoreCase("None")) {
                        SavePreferences("LockState", "OPEN");
                        if (getBoolSound(getApplicationContext())) {
                            play(getApplicationContext(), R.raw.unlock);
                        }
                        finish();
                    } else {
                        if (getpreferences("PasscodeType").equalsIgnoreCase("Pattern")) {
                            MainLockBtnCancel.setVisibility(View.VISIBLE);
                            mViewPager.setPagingEnabled(false);
                        }

                    }
                    MainLockImage.clearAnimation();
                    MainLockImage.animate().alpha(0).setDuration(500).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            MainLockImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });


                } else if (arg0 == 1) {
                    if (MainLockImage.getVisibility() == View.GONE) {
                        MainLockImage.clearAnimation();
                        MainLockImage.setVisibility(View.VISIBLE);
                        MainLockImage.animate().alpha(1).setDuration(500).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                    MainLockBtnCancel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        mLayoutParams.windowAnimations = android.R.style.Animation_Toast;
    }

    private class StateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    if (LockState.equalsIgnoreCase("LOCKED")) {
                        if (root != null) {
                            mWindowManager.removeView(root);
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:

                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    if (root != null) {
                        LockState = "LOCKED";
                        SavePreferences("LockState", "LOCKED");
                        if (root.getParent() == null)
                            mWindowManager.addView(root, mLayoutParams);
                    }
                    break;
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (root != null) {
            if (mWindowManager != null) {
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
               mViewPager.setAdapter(null);
                mViewPager.removeAllViews();
                MainLockImage.setImageDrawable(null);
                MainLockImageBlur.setImageDrawable(null);
                mWindowManager.removeView(root);
                mWindowManager = null;
                LockState = "OPEN";
                root = null;
            }
        }
    }

    public void finish() {

        if (root != null) {
            if (mWindowManager != null) {
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
                mViewPager.setAdapter(null);
                mViewPager.removeAllViews();
                MainLockImage.setImageDrawable(null);
                MainLockImageBlur.setImageDrawable(null);
                mWindowManager.removeView(root);
                mWindowManager = null;
                root = null;
                LockState = "OPEN";
                stopSelf();
            }
        }
    }

    public boolean getBoolSound(Context context) {
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean("PrefLockscreenSound", false);
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
