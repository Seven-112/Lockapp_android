package com.codecanyon.lockscreen;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Config {

    static int[] imageIdMain = {
            R.drawable.wp_0, R.drawable.wp_1, R.drawable.wp_2,
            R.drawable.wp_3, R.drawable.wp_4,
            R.drawable.wp_5, R.drawable.wp_6,
            R.drawable.wp_7, R.drawable.wp_8,};

    static int[] imageId = {
            R.drawable.wp_0_thumb, R.drawable.wp_1_thumb, R.drawable.wp_2_thumb,
            R.drawable.wp_3_thumb, R.drawable.wp_4_thumb,
            R.drawable.wp_5_thumb, R.drawable.wp_6_thumb,
            R.drawable.wp_7_thumb, R.drawable.wp_8_thumb,};

    public static int[] imageId_blur = {R.drawable.wp_0_blur, R.drawable.wp_1_blur, R.drawable.wp_2_blur,
            R.drawable.wp_3_blur, R.drawable.wp_4_blur,
            R.drawable.wp_5_blur, R.drawable.wp_6_blur,
            R.drawable.wp_7_blur, R.drawable.wp_8_blur,};


    static String[] gridColor = {
            "#008B8B",
            "#00FF00",
            "#48D1CC",
            "#556B2F",
            "#696969",
            "#6B8E23",
            "#8FBC8F",
            "#AFEEEE",
            "#B8860B",
            "#BDB76B",
            "#D8BFD8",
            "#DEB887",
            "#FFFF00",
            "#FFF0F5",
            "#EE82EE",
            "#DC143C",
            "#C0C0C0",
            "#FFFFFF"
    };

    static int DIALOG_TEXT_COLOR = 300;

}
