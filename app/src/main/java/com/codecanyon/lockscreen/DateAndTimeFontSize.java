package com.codecanyon.lockscreen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;


public class DateAndTimeFontSize extends Fragment {

    public SeekBar SeekbarTextSize;
    OnFragmentInteractionListener mListener;

    public DateAndTimeFontSize() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dateandtimefont_size, container, false);

        SeekbarTextSize = (SeekBar) view.findViewById(R.id.seekBar_fontsize);


        if (getpreferences("ClockSize").equalsIgnoreCase("0")) {
            SeekbarTextSize.setProgress(20);
        } else {
            SeekbarTextSize.setProgress(Integer.parseInt(getpreferences("ClockSize")) - 60);
            onSeekChange(Integer.parseInt(getpreferences("ClockSize")));
        }
        SeekbarTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SavePreferences("ClockSize", "" + (progress + 60));
                SavePreferences("DaySize", "" + ((progress + 60) / 4));
                onSeekChange((progress + 60));
            }
        });

        return view;
    }

    public void onSeekChange(int size) {
        if (mListener != null) {
            mListener.onFragmentInteraction(size, 3);
        }
    }


    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pref", 0);

        return sharedPreferences.getString(key, "0");

    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    interface OnFragmentInteractionListener {
        void onFragmentInteraction(int size, int type);

    }

}