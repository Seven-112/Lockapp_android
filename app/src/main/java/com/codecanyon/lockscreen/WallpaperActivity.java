package com.codecanyon.lockscreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.codecanyon.lockscreen.gellery_action.GlideImageLoader;
import com.codecanyon.lockscreen.gellery_action.GlidePauseOnScrollListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import jp.wasabeef.glide.transformations.BlurTransformation;

import static com.codecanyon.lockscreen.Config.imageId;
import static com.codecanyon.lockscreen.Config.imageId_blur;


public class WallpaperActivity extends AppCompatActivity {

    Toolbar toolbar;
    GridView grid;
    private static final String TEMP_PHOTO_FILE = "/.templockimg/temp.jpg";
    private static final String TEMP_BIG_PHOTO_FILE = "/.templockimg";
    ImageView BackgroundBlurLayer;
    FunctionConfig functionConfig;
    ImageLoader imageloader;
    GlidePauseOnScrollListener pauseOnScrollListener;
    ThemeConfig theme;
    CoreConfig coreConfig;
    Integer RequestCodeForImage;
    private List<PhotoInfo> mPhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Wallpaper");
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        BackgroundBlurLayer = (ImageView) findViewById(R.id.BackgroundBlurLayer);
        RequestCodeForImage = 1000;
        mPhotoList = new ArrayList<>();

        theme = new ThemeConfig.Builder()
                .setEditPhotoBgTexture(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wp_0_blur))
                .setPreviewBg(ContextCompat.getDrawable(getApplicationContext(), R.drawable.wp_0_blur))
                .setTitleBarTextColor(R.color.transpent)
                .setCropControlColor(R.color.transpent)
                .setTitleBarBgColor(R.color.transpent)
                .setTitleBarTextColor(R.color.white)
                .setIconFolderArrow(R.color.transpent)
                .setCheckSelectedColor(Color.BLACK)
                .build();

        imageloader = new GlideImageLoader();
        pauseOnScrollListener = new GlidePauseOnScrollListener(false, true);

        functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(false)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setEnablePreview(true)
                .setCropReplaceSource(false)
                .setRotateReplaceSource(false)
                .setForceCrop(false)
                .build();


        grid = (GridView) findViewById(R.id.myGrid);
        CustomGrid adapter = new CustomGrid(WallpaperActivity.this, R.layout.wallpaper_grid_item, imageId);
        grid.setAdapter(adapter);

        final AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(getResources().getString(R.string.test_device_id)).build();
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        setImageBaclgroundBlurLayer();

        grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                Intent i = new Intent(WallpaperActivity.this, ViewWallpaperActivity.class);
                i.putExtra("WallpaperPosition", position);
                startActivity(i);

            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == 0) {
            if (data != null) {
                beginCrop(data.getData());
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }

    }

    private void beginCrop(Uri source) {
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(source).output(outputUri).withAspect(2, 3).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Crop.getOutput(result));

                File f = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);
                FileOutputStream fOut = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();

                ImageCompressionBlur imgsmall = new ImageCompressionBlur(getApplicationContext());
                imgsmall.execute(Environment.getExternalStorageDirectory() + TEMP_PHOTO_FILE);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wallpaper, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_gallery:

                coreConfig = new CoreConfig.Builder(getApplicationContext(), imageloader, theme)
                        .setFunctionConfig(functionConfig)
                        .setPauseOnScrollListener(new GlidePauseOnScrollListener(false, true))
                        .build();
                GalleryFinal.init(coreConfig);
                GalleryFinal.openGallerySingle(RequestCodeForImage, mOnHanlderResultCallback);

                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
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

    private class ImageCompressionBlur extends AsyncTask<String, Void, String> {

        private Context context;
        private static final float maxHeight = 200.0f;
        private static final float maxWidth = 200.0f;
        ProgressDialog dialog;

        ImageCompressionBlur(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return compressImage(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = new ProgressDialog(WallpaperActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        protected void onPostExecute(String imagePath) {
            dialog.dismiss();

            ImageCompressionBig imgbig = new ImageCompressionBig(getApplicationContext());
            imgbig.execute(Environment.getExternalStorageDirectory() + TEMP_PHOTO_FILE);
        }

        String compressImage(String imagePath) {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = null;
            if (scaledBitmap != null) {
                canvas = new Canvas(scaledBitmap);
            }
            if (canvas != null) {
                canvas.setMatrix(scaleMatrix);
            }
            if (canvas != null) {
                canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            }
            if (bmp != null) {
                bmp.recycle();
            }

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                if (scaledBitmap != null) {
                    scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out;
            String filepath = getFilename();
            try {
                out = new FileOutputStream(filepath);
                if (scaledBitmap != null) {
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filepath;
        }

        int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }

        String getFilename() {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), TEMP_BIG_PHOTO_FILE);
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            return (mediaStorageDir.getAbsolutePath() + "/temp_blur.jpg");

        }

    }

    private class ImageCompressionBig extends AsyncTask<String, Void, String> {

        private Context context;
        private static final float maxHeight = 1280.0f;
        private static final float maxWidth = 1280.0f;
        ProgressDialog dialog;

        ImageCompressionBig(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0 || strings[0] == null)
                return null;

            return compressImage(strings[0]);
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = new ProgressDialog(WallpaperActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();
        }

        protected void onPostExecute(String imagePath) {
            dialog.dismiss();
            String filePath = Environment.getExternalStorageDirectory() + "/.templockimg/temp_blur.jpg";
            Bitmap selectedImageb = BitmapFactory.decodeFile(filePath);
            new ImageBlurBitmap().execute(selectedImageb);

        }

        String compressImage(String imagePath) {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

            float imgRatio = (float) actualWidth / (float) actualHeight;
            float maxRatio = maxWidth / maxHeight;

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);
            options.inJustDecodeBounds = false;
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
                bmp = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            Canvas canvas = null;
            if (scaledBitmap != null) {
                canvas = new Canvas(scaledBitmap);
            }
            if (canvas != null) {
                canvas.setMatrix(scaleMatrix);
            }
            if (canvas != null) {
                canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
            }
            if (bmp != null) {
                bmp.recycle();
            }

            ExifInterface exif;
            try {
                exif = new ExifInterface(imagePath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                if (scaledBitmap != null) {
                    scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileOutputStream out;
            String filepath = getFilename();
            try {
                out = new FileOutputStream(filepath);
                if (scaledBitmap != null) {
                    scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return filepath;
        }

        int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }

        String getFilename() {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), TEMP_BIG_PHOTO_FILE);
            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            return (mediaStorageDir.getAbsolutePath() + "/temp_big.jpg");

        }

    }

    private class ImageBlurBitmap extends AsyncTask<Bitmap, Void, Bitmap> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            dialog = new ProgressDialog(WallpaperActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setIndeterminate(true);
            dialog.show();

            Intent i = new Intent(WallpaperActivity.this, ViewWallpaperActivity.class);
            i.putExtra("WallpaperGallaryBlur", Environment.getExternalStorageDirectory() + TEMP_BIG_PHOTO_FILE + "/temp_blur.jpg");
            i.putExtra("WallpaperGallary", Environment.getExternalStorageDirectory() + TEMP_BIG_PHOTO_FILE + "/temp_big.jpg");
            startActivity(i);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            // TODO Auto-generated method stub
            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            if (sdkVersion <= 17) {
                return Blur.fastblur(params[0], 20);
            } else {
                return Blur.CreateBlurredImage(getApplicationContext(), params[0]);
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            // TODO Auto-generated method stub

            FileOutputStream out;
            String filepath = getFilename();
            try {
                out = new FileOutputStream(filepath);
                result.compress(Bitmap.CompressFormat.JPEG, 80, out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            SavePreferences("WallpaperGalleryBlur", "" + filepath);
            dialog.dismiss();
        }

        String getFilename() {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), TEMP_BIG_PHOTO_FILE);

            if (!mediaStorageDir.exists()) {
                mediaStorageDir.mkdirs();
            }
            return (mediaStorageDir.getAbsolutePath() + "/temp_blur.jpg");

        }

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
                    .into(BackgroundBlurLayer);
        } else {
            Glide.with(getApplicationContext())
                    .load(imageId_blur[Integer.parseInt(getpreferences("Wallpaper"))])
                    .bitmapTransform(new BlurTransformation(getApplicationContext(), 25))
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(BackgroundBlurLayer);
        }
    }

    @Override
    protected void onResume() {
        if (getpreferences("ChangeWallpaper").equalsIgnoreCase("true")) {
            setImageBaclgroundBlurLayer();
        }
        super.onResume();
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {

                mPhotoList.clear();
                mPhotoList.addAll(resultList);
                String Path = mPhotoList.get(0).getPhotoPath();
                Bitmap bitmap = BitmapFactory.decodeFile(Path);
                setPhoto(bitmap);
            }

        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private void setPhoto(Bitmap bitmap) {


        File f = new File(Environment.getExternalStorageDirectory(), TEMP_PHOTO_FILE);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

        try {
            if (fOut != null) {
                fOut.flush();
                fOut.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageCompressionBlur imgsmall = new ImageCompressionBlur(getApplicationContext());
        imgsmall.execute(Environment.getExternalStorageDirectory() + TEMP_PHOTO_FILE);
    }

}
