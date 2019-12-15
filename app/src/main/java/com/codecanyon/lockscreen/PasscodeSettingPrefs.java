package com.codecanyon.lockscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class PasscodeSettingPrefs extends PreferenceFragment {

    public static final String CONFIG_NAME = "PrefPasscodeSetting";
    Preference PrefPasscodeChange;
    ListPreference PrefPasscodeStyle;
    InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(CONFIG_NAME);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.passcode_setting);

        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial));
        requestNewInterstitial();

        PrefPasscodeChange = findPreference("PrefPasscodeChange");
        PrefPasscodeChange.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {

                if (getpreferences("PasscodeType").equalsIgnoreCase("Pattern")) {
                    Intent i = new Intent(getActivity(), PatternActivity.class);
                    i.putExtra("NewPasscode", "Yes");
                    startActivity(i);
                } else if (getpreferences("PasscodeType").equalsIgnoreCase("Pin")) {
                    Intent i = new Intent(getActivity(), SetPinPasscodeActivity.class);
                    i.putExtra("NewPasscode", "Yes");
                    startActivity(i);
                }

                showInstrititial();
                return true;
            }
        });


        PrefPasscodeStyle = (ListPreference) findPreference("PrefPasscodeStyle");

        PrefPasscodeStyle.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if (newValue.equals("Passcode")) {

                    if (!getpreferences("PasscodeType").equalsIgnoreCase("Pin")) {
                        Intent i = new Intent(getActivity(), SetPinPasscodeActivity.class);
                        i.putExtra("NewPasscode", "Yes");
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "You already select passcode", Toast.LENGTH_SHORT).show();
                    }
                } else if (newValue.equals("Pattern")) {
                    if (!getpreferences("PasscodeType").equalsIgnoreCase("Pattern")) {
                        Intent i = new Intent(getActivity(), PatternActivity.class);
                        i.putExtra("NewPasscode", "Yes");
                        startActivity(i);
                    } else {
                        Toast.makeText(getActivity(), "You already select pattern", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (!getpreferences("PasscodeType").equalsIgnoreCase("None")) {
                        SavePreferences("PasscodeType", "None");
                        SavePreferences("PasscodeValue", "0");

                    }
                    PrefPasscodeStyle.setSummary("None");
                    PrefPasscodeStyle.setValueIndex(0);
                    PrefPasscodeChange.setEnabled(false);
                }
                return false;
            }
        });


        checkOnChange();

    }

    private void checkOnChange() {

        if (getpreferences("PasscodeType").equalsIgnoreCase("None") || getpreferences("PasscodeType").equalsIgnoreCase("0")) {
            PrefPasscodeChange.setEnabled(false);
        } else {
            PrefPasscodeChange.setEnabled(true);
        }

        if (getpreferences("PasscodeType").equalsIgnoreCase("Pattern")) {
            PrefPasscodeStyle.setSummary("Pattern");
            PrefPasscodeStyle.setValueIndex(2);
        } else if (getpreferences("PasscodeType").equalsIgnoreCase("Pin")) {
            PrefPasscodeStyle.setSummary("Passcode");
            PrefPasscodeStyle.setValueIndex(1);
        } else {
            PrefPasscodeStyle.setSummary("None");
            PrefPasscodeStyle.setValueIndex(0);
            PrefPasscodeChange.setEnabled(false);
        }
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
    }

    @Override
    public void onResume() {
        super.onResume();
        checkOnChange();
        requestNewInterstitial();

    }

    private void requestNewInterstitial() {
        if (mInterstitialAd != null) {
            AdRequest adRequestint = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
            mInterstitialAd.loadAd(adRequestint);
        }
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
