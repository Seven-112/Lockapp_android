package com.codecanyon.lockscreen;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import afzkl.development.colorpickerview.dialog.ColorPickerDialogFragment;

public class DateAndTimeFontColor extends Fragment {


    OnFragmentInteractionListener mListener;

    GridView gridViewColor;
    int selectedColor = Color.BLACK;
    ImageView item;
    ColorGridAdepter adapter;

    public DateAndTimeFontColor() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dateandtimefont_color, container, false);

        adapter = new ColorGridAdepter(getActivity(), Config.gridColor, selectedColor);
        gridViewColor = (GridView) view.findViewById(R.id.grid_color);

        gridViewColor.setAdapter(adapter);


        gridViewColor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                item = (ImageView) view.findViewById(R.id.grid_img);

                if (position == 0) {
                    // TODO Auto-generated method stub
                    ColorPickerDialogFragment f = ColorPickerDialogFragment.newInstance(Config.DIALOG_TEXT_COLOR, null, null, selectedColor, true);
                    f.setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog);
                    f.show(getActivity().getFragmentManager(), "d");

                } else {
                    onColorChange(Color.parseColor(Config.gridColor[position]));
                    SavePreferences("TextColor", "" + Color.parseColor(Config.gridColor[position]));

                }
            }
        });


        return view;
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public void onColorChange(int color) {
        if (mListener != null) {
            mListener.onFragmentInteraction(color, 1);
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
        void onFragmentInteraction(int color, int type);

    }

    public void ChangeTextColor(int color) {
        selectedColor = color;
        SavePreferences("TextColor", "" + selectedColor);
        adapter = null;
        adapter = new ColorGridAdepter(getActivity(), Config.gridColor, selectedColor);
        gridViewColor.setAdapter(adapter);
    }

}