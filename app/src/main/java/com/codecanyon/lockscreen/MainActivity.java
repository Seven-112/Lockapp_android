package com.codecanyon.lockscreen;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;

import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codecanyon.lockscreen.databinding.ActivityMainBinding;
import com.codecanyon.lockscreen.utils.LockscreenServiceStart;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    AlertDialog.Builder alert_overlay, alert_permission;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
    List<String> permissionsNeeded = new ArrayList<>();
    List<String> permissionsList = new ArrayList<>();
    ImageView BackgroundBlurLayer;
    GridView gridView;
    AdepterForMainScreenItem adepter;
    TextView text;
    InterstitialAd mInterstitialAd;

    Intent i;

    private String[] image_name = {
            "Setting",
            "Wallpaper",
            "Passcode",
            "Lock Style",
            "Rate Now",
            "Share app"
    };

    private Integer[] image_orignal = {
            R.drawable.ic_setting,
            R.drawable.ic_wallpaper,
            R.drawable.ic_passcode,
            R.drawable.ic_lockstyle,
            R.drawable.ic_rateus,
            R.drawable.ic_shareapp,
    };


    ActivityMainBinding binding;

    public static final String PrefLockScreenActive= "PrefLockscreenActive";
    public static final String PrefLockScreenVibration =  "PrefLockscreenVibration";
    public static final String PrefLockScreenSound =  "PrefLockscreenSound";

    String TAG = "MainActivity";
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.ad_banner));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        binding.adContrainer.removeAllViews();
        binding.adContrainer.addView(adView);

        Log.i(TAG, getBoolActive(this)+"");

        PreferenceManager.setDefaultValues(getApplicationContext(), Settings.CONFIG_NAME, Context.MODE_PRIVATE, R.xml.settings, true);
        pref = getApplicationContext().getSharedPreferences(Settings.CONFIG_NAME, MODE_PRIVATE);
        editor = pref.edit();

        if (getBoolActive(getApplicationContext())) {
            Log.i("testing", "in if");
            binding.switchLockScreenEnable.setChecked(true);
            startService(new Intent(this, LockscreenServiceStart.class));
        }


        if (getVibration(this)){
            binding.switchVibration.setChecked(true);
        }

        if (getSound(this))
            binding.switchSound.setChecked(true);


        //FirebaseApp.initializeApp(getApplicationContext());
        //FirebaseMessaging.getInstance().subscribeToTopic("Lockscreen");


        someMethod();
        setuppermission();

        File rootPath = new File(Environment.getExternalStorageDirectory(), "." + "templockimg");
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        String path1 = Environment.getExternalStorageDirectory() + File.separator + "." + "lockscreentemp";
        File outDir = new File(path1);
        if (!outDir.exists())
            outDir.mkdir();


        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.ad_interstitial));
        requestNewInterstitial();


        binding.enableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchLockScreenEnable.isChecked()){
                    binding.switchLockScreenEnable.setChecked(false);
                    stopService(new Intent(MainActivity.this, LockscreenServiceStart.class));
                    editor.putBoolean(PrefLockScreenActive, false);
                    editor.apply();

                } else{
                    binding.switchLockScreenEnable.setChecked(true);
                    startService(new Intent(MainActivity.this, LockscreenServiceStart.class));
                    editor.putBoolean(PrefLockScreenActive, true);
                    editor.apply();
                }

            }
        });


        binding.btnLockScreenVibration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchVibration.isChecked()){
                    binding.switchVibration.setChecked(false);
                    editor.putBoolean(PrefLockScreenVibration, false);
                    editor.apply();
                }else {
                    binding.switchVibration.setChecked(true);
                    editor.putBoolean(PrefLockScreenVibration, true);
                    editor.apply();
                }

            }
        });

        binding.btnLockScreenSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchSound.isChecked()){
                    binding.switchSound.setChecked(false);

                    editor.putBoolean(PrefLockScreenSound , false);
                    editor.apply();
                }else {
                    binding.switchSound.setChecked(true);
                    editor.putBoolean(PrefLockScreenSound , true);
                    editor.apply();
                }
            }
        });



        binding.moreSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                showInstrititial();
            }
        });


        binding.btnLockStyles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), SetStyleActivity.class);
                startActivity(i);
                showInstrititial();
            }
        });

        binding.passcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getpreferences("PasscodeType").equalsIgnoreCase("Pin")) {
                    i = new Intent(getApplicationContext(), SetPinPasscodeActivity.class);
                    i.putExtra("Confirm", "Yes");
                    startActivity(i);
                } else if (getpreferences("PasscodeType").equalsIgnoreCase("Pattern")) {
                    i = new Intent(getApplicationContext(), PatternActivity.class);
                    i.putExtra("Confirm", "Yes");
                    startActivity(i);
                } else {
                    i = new Intent(getApplicationContext(), NewPasscodeSettingActivity.class);
                    startActivity(i);

                }
                showInstrititial();
            }
        });

        binding.btnWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent(getApplicationContext(), WallpaperActivity.class);
                startActivity(i);
                showInstrititial();
            }
        });


        binding.btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        binding.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");
                final String text = "Check out "
                        + getResources().getString(R.string.app_name)
                        + ", the free app " + getResources().getString(R.string.app_name) + ". https://play.google.com/store/apps/details?id="
                        + getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, text);
                Intent sender = Intent.createChooser(i, "Share " + getString(R.string.app_name));
                startActivity(sender);
            }
        });


        binding.btnAboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                showInstrititial();
            }
        });
    }

    private String getpreferences(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("pref", 0);
        return sharedPreferences.getString(key, "0");
    }


    public boolean getBoolActive(Context context) {
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean("PrefLockscreenActive", true);
    }

    public boolean getVibration(Context context){
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean(PrefLockScreenVibration, false);
    }

    public boolean getSound(Context context){
        return context.getSharedPreferences(Settings.CONFIG_NAME, Context.MODE_PRIVATE).getBoolean(PrefLockScreenSound, false);
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




    @Override
    public void onPause() {
        super.onPause();
    }


    public void someMethod() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!android.provider.Settings.canDrawOverlays(this)) {

                alert_overlay = new AlertDialog.Builder(MainActivity.this);
                alert_overlay.setMessage("You need to grant access to system overlay");
                alert_overlay.setCancelable(false);
                alert_overlay.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intent;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                                }

                            }
                        });

                alert_overlay.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                alert_overlay.show();
            }
        }
    }

    private void setuppermission() {
        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Call Phone");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                alert_permission = new AlertDialog.Builder(MainActivity.this);
                alert_permission.setMessage(message);
                alert_permission.setCancelable(false);
                alert_permission.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });

                alert_permission.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @SuppressLint("NewApi")
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                alert_permission.show();
                return;
            }
            ActivityCompat.requestPermissions(MainActivity.this, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

        }
    }





    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_CONTACTS, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(), "Some Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    @Override
    protected void onResume() {
        if (getpreferences("ChangeWallpaper").equalsIgnoreCase("true")) {
        }


        if (getBoolActive(getApplicationContext())) {
            Log.i("testing", "in if");
            binding.switchLockScreenEnable.setChecked(true);
            startService(new Intent(this, LockscreenServiceStart.class));
        }


        if (getVibration(this)){
            binding.switchVibration.setChecked(true);
        }

        if (getSound(this))
            binding.switchSound.setChecked(true);

        super.onResume();
    }



}
