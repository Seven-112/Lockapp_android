package com.codecanyon.lockscreen.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class LockscreenServiceReceiver extends BroadcastReceiver {

	Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {

		mContext = context;
		if (intent.hasExtra("Service")) {
			if (intent.getStringExtra("Service").equalsIgnoreCase("ServiceStart")) {
				if (!isMyServiceRunning(LockscreenServiceStart.class)) {
					context.startService(new Intent(context, LockscreenServiceStart.class));
				}
			}
		}

	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
