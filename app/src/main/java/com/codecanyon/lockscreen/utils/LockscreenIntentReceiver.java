package com.codecanyon.lockscreen.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import com.codecanyon.lockscreen.LockscreenService;
import com.codecanyon.lockscreen.Settings;

public class LockscreenIntentReceiver extends BroadcastReceiver {

    Context mContext;
    Handler handler = new Handler();

    @Override
    public void onReceive(Context context, final Intent intent) {
        mContext = context;

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            start_lockscreen(context);
            if (!getBoolPower(mContext)) {
                handler.removeCallbacks(StopServiceRun);
            }
        }
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            if (getBoolPower(mContext)) {
                stop_lockscreen(mContext);
            } else {
                start_lockscreen(context);
                handler.postDelayed(StopServiceRun, 60000);
            }

        }
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            if (getBoolActive(context)) {
                context.startService(new Intent(context, LockscreenServiceStart.class));
                start_lockscreen(context);
            }
        }
    }

    public Runnable StopServiceRun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub

            stop_lockscreen(mContext);

        }

    };

    private void start_lockscreen(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (android.provider.Settings.canDrawOverlays(context)) {
                if (getBoolActive(context)) {
                    context.startService(new Intent(context, LockscreenService.class));

                }
            }
        } else {
            if (getBoolActive(context)) {
                context.startService(new Intent(context, LockscreenService.class));
            }
        }
    }

    private void stop_lockscreen(Context context) {
        if (getBoolActive(context)) {
            mContext.stopService(new Intent(mContext, LockscreenService.class));
        }
    }

    public boolean getBoolActive(Context context) {
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean("PrefLockscreenActive", true);
    }

    public boolean getBoolPower(Context context) {
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean("PrefLockscreenPowerSaving", true);
    }


}
