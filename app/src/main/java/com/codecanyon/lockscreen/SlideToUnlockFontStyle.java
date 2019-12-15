package com.codecanyon.lockscreen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class SlideToUnlockFontStyle extends Fragment {

    Typeface UnlockFontThin, UnlockMedium, UnlockLight;
    String FontName;
    OnFragmentInteractionListener mListener;
    GridView GridForFontStyle;

    final String[] FontList = new String[]{"Roboto-Medium.ttf", "font1.ttf", "font2.ttf", "font3.otf", "font4.ttf", "font5.ttf", "font6.ttf", "font7.ttf"};

    public SlideToUnlockFontStyle() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slidetounlock_style, container, false);

        UnlockFontThin = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Thin.ttf");
        UnlockMedium = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Medium.ttf");
        UnlockLight = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");

        if (!getpreferences("SlideFontStyle").equalsIgnoreCase("0")) {
            onStyleChange(Integer.parseInt(getpreferences("SlideFontStyle")));

        }

        GridForFontStyle = (GridView) view.findViewById(R.id.GridForFontStyle);
        GridForFontStyle.setAdapter(new TextFontAdepter(getActivity(), FontList));
        GridForFontStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FontName = FontList[position];
                SavePreferences("SlideFontStyle", "" + position);
                onStyleChange(position);
            }
        });

        return view;
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

    public void onStyleChange(int style) {
        if (mListener != null) {
            mListener.onFragmentInteraction(style, 2, "null");
        }
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
        void onFragmentInteraction(int style, int type, String temp);

    }

}
