package com.codecanyon.lockscreen;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.codecanyon.lockscreen.Config.imageId_blur;

public class PatternActivity extends AppCompatActivity {

    private String CurrentPattern = "";
    private String EnterPattern = "";
    private String EnterPattern1 = "";
    private String Confirm = "";
    private String NewPasscode = "";
    private MaterialLockView materialLockView;
    private Button PatternButton1, PatternButton2;
    private TextView TxtDrawPattern;
    private ImageView ImagePatternBlurBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);

        PatternButton1 = (Button) findViewById(R.id.PatternButton1);
        PatternButton2 = (Button) findViewById(R.id.PatternButton2);
        TxtDrawPattern = (TextView) findViewById(R.id.TxtDrawPattern);
        ImagePatternBlurBg = (ImageView) findViewById(R.id.PatternImageBlur);

        setImageBaclgroundBlurLayer();

        if (this.getIntent().hasExtra("Confirm")) {
            Confirm = "yes";
        }
        if (this.getIntent().hasExtra("NewPasscode")) {
            NewPasscode = "yes";
        }

        if (NewPasscode.equalsIgnoreCase("")) {
            if (getpreferences("PasscodeType").equalsIgnoreCase("Pattern")) {
                PatternButton1.setText("CANCEL");
                PatternButton2.setText("CONFIRM");
                TxtDrawPattern.setText("Draw Current Pattern");
                CurrentPattern = getpreferences("PatternValue");
            } else {
                PatternButton1.setText("CANCEL");
                PatternButton2.setText("NEXT");
                TxtDrawPattern.setText("Draw Pattern");
                CurrentPattern = "";
            }
        } else {
            PatternButton1.setText("CANCEL");
            PatternButton2.setText("NEXT");
            TxtDrawPattern.setText("Draw Pattern");
            CurrentPattern = "";
        }


        materialLockView = (MaterialLockView) findViewById(R.id.pattern);
        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(
                    List<MaterialLockView.Cell> pattern,
                    String SimplePattern) {
                EnterPattern = SimplePattern;
                super.onPatternDetected(pattern, SimplePattern);
            }

        });

        PatternButton2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (PatternButton2.getText().toString()
                        .equalsIgnoreCase("CONFIRM")) {
                    if (EnterPattern.equalsIgnoreCase(CurrentPattern)) {
                        if (Confirm.equalsIgnoreCase("")) {
                            PatternButton1.setText("CANCEL");
                            PatternButton2.setText("NEXT");
                            TxtDrawPattern.setText("Draw Pattern");
                            CurrentPattern = "";
                            materialLockView.clearPattern();
                        } else {
                            Intent i = new Intent(getApplicationContext(), NewPasscodeSettingActivity.class);
                            startActivity(i);
                            finish();
                        }
                    } else {
                        materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                    }
                } else if (PatternButton2.getText().toString().equalsIgnoreCase("NEXT")) {
                    EnterPattern1 = EnterPattern;
                    materialLockView.clearPattern();
                    PatternButton2.setText("DONE");
                    TxtDrawPattern.setText("Draw Pattern Again");
                } else {
                    if (EnterPattern.equalsIgnoreCase(EnterPattern1)) {
                        SavePreferences("PatternValue", EnterPattern);
                        SavePreferences("PasscodeType", "Pattern");
                        finish();
                    } else {
                        materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                    }
                }
            }
        });

        PatternButton1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
    }

    private void setImageBaclgroundBlurLayer() {
        if (getpreferences("Wallpaper").equalsIgnoreCase("false")) {
            String filePath1 = getpreferences("WallpaperGalleryBlur");
            Glide.with(getApplicationContext())
                    .load(new File(filePath1))
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImagePatternBlurBg);

        } else {
            Glide.with(getApplicationContext())
                    .load(imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ImagePatternBlurBg);
        }
    }

}
