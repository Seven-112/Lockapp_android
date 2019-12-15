package com.codecanyon.lockscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class StyleSettings extends PreferenceFragment {

    public static final String CONFIG_NAME = "PrefLockscreenSetting";
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(CONFIG_NAME);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.style_settings);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial));
        requestNewInterstitial();

        Preference myPref = findPreference("PrefStyleClock");
        myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), EditClockActivity.class);
                startActivity(intent);
                showInstrititial();
                return true;
            }
        });

        Preference myPrefSlide = findPreference("PrefStyleSlide");
        myPrefSlide.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), EditSlideActivity.class);
                startActivity(intent);
                showInstrititial();
                return true;
            }
        });

        Preference myPrefMessage = findPreference("PrefStyleMessage");
        myPrefMessage.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), EditMessageActivity.class);
                startActivity(intent);
                showInstrititial();
                return true;
            }
        });
    }

    private void requestNewInterstitial() {
        if (mInterstitialAd != null) {
            AdRequest adRequestint = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
            mInterstitialAd.loadAd(adRequestint);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requestNewInterstitial();
    }

    public void showInstrititial() {
        if (mInterstitialAd != null) {
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        }
        requestNewInterstitial();
    }
}
