package com.codecanyon.lockscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;

import android.preference.PreferenceFragment;
import android.util.Log;

import com.codecanyon.lockscreen.utils.LockscreenServiceStart;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class Settings extends PreferenceFragment {

    public static final String CONFIG_NAME = "PrefLockscreenSetting";
    Intent i;
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName(CONFIG_NAME);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.settings);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial));
        requestNewInterstitial();

        Preference myPref = findPreference("PrefLockscreenSelectNone");

        myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent("android.app.action.SET_NEW_PASSWORD");
                startActivity(intent);
                return true;
            }
        });

        Preference myPref_active = findPreference("PrefLockscreenActive");

        myPref_active.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference preference,
                                              Object newValue) {
                // TODO Auto-generated method stub
                if (newValue.toString().equalsIgnoreCase("true")) {
                    getActivity().startService(new Intent(getActivity(), LockscreenServiceStart.class));
                    Log.i("Settings_test", "true");
                } else {
                    Log.i("Settings_test", "false");
                    getActivity().stopService(new Intent(getActivity(), LockscreenServiceStart.class));
                }


                return true;
            }
        });

        Preference PrefAboutUs = findPreference("PrefAboutUs");

        PrefAboutUs.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                i = new Intent(getActivity(), AboutActivity.class);
                startActivity(i);
                showInstrititial();
                return false;
            }
        });


        Preference PrefLicence = findPreference("PrefLicence");

        PrefLicence.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getActivity(), LicenseActivity.class);
                startActivity(intent);
                showInstrititial();
                return false;
            }
        });

        Preference PrefFAQ = findPreference("PrefFAQ");

        PrefFAQ.setOnPreferenceClickListener(new OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {

                Intent intent = new Intent(getActivity(), FAQsActivity.class);
                startActivity(intent);
                showInstrititial();
                return false;
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
                requestNewInterstitial();
            }
        }
    }
}
