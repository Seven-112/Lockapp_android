package com.codecanyon.lockscreen;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PatternLayout extends RelativeLayout {

    @SuppressLint("StaticFieldLeak")
    private static ViewGroup mContainer;
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private String CurrentPattern = "";
    private MaterialLockView materialLockView;
    private MediaPlayer mMediaPlayer;
    private TextView txtDrawPattern;

    public PatternLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public PatternLayout(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public PatternLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public static PatternLayout fromXml(Context paramContext, ViewGroup paramViewGroup) {
        mContext = paramContext;
        mContainer = paramViewGroup;
        return (PatternLayout) LayoutInflater.from(paramContext).inflate(R.layout.fragment_pattern, paramViewGroup, false);
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

        CurrentPattern = getpreferences("PatternValue");

        materialLockView = (MaterialLockView) findViewById(R.id.pattern);
        txtDrawPattern = (TextView) findViewById(R.id.TxtDrawPattern);
        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(
                    List<MaterialLockView.Cell> pattern,
                    String SimplePattern) {
                if (CurrentPattern.equalsIgnoreCase(SimplePattern)) {
                    if (getBoolSound(mContext)) {
                        play(mContext, R.raw.unlock);
                    }
                    mContext.stopService(new Intent(mContext, LockscreenService.class));
                    SavePreferences("LockState", "OPEN");
                } else {
                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                    txtDrawPattern.setText("Wrong pattern");
                    if (getBoolVibration(mContext)) {
                        Vibrator v = (Vibrator) mContext.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                    }
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            txtDrawPattern.setText("Draw Pattern");
                            materialLockView.clearPattern();
                        }
                    }, 500);

                }
                super.onPatternDetected(pattern, SimplePattern);
            }

        });

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
