package com.codecanyon.lockscreen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class SlideToUnlockText extends Fragment {

    public EditText etSlideText;
    OnFragmentInteractionListener mListener;

    public SlideToUnlockText() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slidetounlock_text, container, false);

        etSlideText = (EditText) view.findViewById(R.id.etSlideText);

        if (getpreferences("SlideText").equalsIgnoreCase("0")) {
            etSlideText.setText("Slide to unlock");
            onTextChange("Slide to unlock");
        } else {
            etSlideText.setText(getpreferences("SlideText"));
            onTextChange(getpreferences("SlideText"));
        }

        etSlideText.addTextChangedListener(tw);

        return view;
    }

    private TextWatcher tw = new TextWatcher() {

        public void afterTextChanged(Editable s) {

        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String UpdateString = etSlideText.getText().toString();
            onTextChange(UpdateString);
            SavePreferences("SlideText", "" + UpdateString);
        }
    };

    public void onTextChange(String text) {
        if (mListener != null) {
            mListener.onFragmentInteraction(0, 1, text);
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
        void onFragmentInteraction(int temp, int type, String text);

    }
}