package com.soundcloud.android.crop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

/**
 * Builder for crop Intents and utils for handling result
 */
public class Crop {

    public static final int REQUEST_CROP = 6709;
	private static final int PICK_FROM_CAMERA = 1;
    public static final int REQUEST_PICK = 9162;
    public static final int RESULT_ERROR = 404;
    private static Uri uri;
    static interface Extra {
        String ASPECT_X = "aspect_x";
        String ASPECT_Y = "aspect_y";
        String MAX_X = "max_x";
        String MAX_Y = "max_y";
        String ERROR = "error";
    }

    private Intent cropIntent;

    /**
     * Create a crop Intent builder with source image
     *
     * @param source Source image URI
     */
    public Crop(Uri source) {
        cropIntent = new Intent();
        cropIntent.setData(source);
    }

    /**
     * Set output URI where the cropped image will be saved
     *
     * @param output Output image URI
     */
    public Crop output(Uri output) {
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, output);
        return this;
    }

    /**
     * Set fixed aspect ratio for crop area
     *
     * @param x Aspect X
     * @param y Aspect Y
     */
    public Crop withAspect(int x, int y) {
        cropIntent.putExtra(Extra.ASPECT_X, x);
        cropIntent.putExtra(Extra.ASPECT_Y, y);
        return this;
    }

    /**
     * Crop area with fixed 1:1 aspect ratio
     */
    public Crop asSquare() {
        cropIntent.putExtra(Extra.ASPECT_X, 1);
        cropIntent.putExtra(Extra.ASPECT_Y, 1);
        return this;
    }

    /**
     * Set maximum crop size
     *
     * @param width Max width
     * @param height Max height
     */
    public Crop withMaxSize(int width, int height) {
        cropIntent.putExtra(Extra.MAX_X, width);
        cropIntent.putExtra(Extra.MAX_Y, height);
        return this;
    }

    /**
     * Send the crop Intent!
     *
     * @param activity Activity to receive result
     */
    public void start(Activity activity) {
        start(activity, REQUEST_CROP);
    }

    /**
     * Send the crop Intent with a custom requestCode
     *
     * @param activity Activity to receive result
     * @param requestCode requestCode for result
     */
    public void start(Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    /**
     * Send the crop Intent!
     *
     * @param context Context
     * @param fragment Fragment to receive result
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start(Context context, Fragment fragment) {
        start(context, fragment, REQUEST_CROP);
    }

    /**
     * Send the crop Intent with a custom requestCode
     *
     * @param context Context
     * @param fragment Fragment to receive result
     * @param requestCode requestCode for result
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void start(Context context, Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(context), requestCode);
    }

    /**
     * Get Intent to start crop Activity
     *
     * @param context Context
     * @return Intent for CropImageActivity
     */
    public Intent getIntent(Context context) {
        cropIntent.setClass(context, CropImageActivity.class);
        return cropIntent;
    }

    /**
     * Retrieve URI for cropped image, as set in the Intent builder
     *
     * @param result Output Image URI
     */
    public static Uri getOutput(Intent result) {
        return result.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
    }

    /**
     * Retrieve error that caused crop to fail
     *
     * @param result Result Intent
     * @return Throwable handled in CropImageActivity
     */
    public static Throwable getError(Intent result) {
        return (Throwable) result.getSerializableExtra(Extra.ERROR);
    }

    /**
     * Utility to start an image picker
     *
     * @param activity Activity that will receive result
     */
    public static void pickImage(Activity activity) {
        pickImage(activity, REQUEST_PICK);
    }
    
    public static void captureImage(Activity activity){
    	 capture(activity, PICK_FROM_CAMERA);
    	
    }

    @SuppressLint("NewApi")
	private static void capture(Activity activity, int pickFromCamera) {
		// TODO Auto-generated method stub
    	if (Build.VERSION.SDK_INT < 19) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			uri = Uri.fromFile(new File(Environment
					.getExternalStorageDirectory(), "tmp_avatar_"
					+ String.valueOf(System.currentTimeMillis())
					+ ".jpg"));

			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					uri);

			try {
				intent.putExtra("return-data", true);

				 activity.startActivityForResult(intent, pickFromCamera);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		} else {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
					.format(new Date());

			ContentValues values = new ContentValues();
			values.put(MediaStore.Images.Media.TITLE, "IMG_"
					+ timeStamp + ".jpg");

			Intent pictureActionIntent = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			uri = activity.getContentResolver().insert(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					values); // store content values
			// pictureActionIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			// uri);
			// uri = Uri.fromFile(new File(Environment
			// .getExternalStorageDirectory(), "tmp_avatar_"
			// + String.valueOf(System.currentTimeMillis()) + ".jpg"));

			pictureActionIntent.putExtra(
					android.provider.MediaStore.EXTRA_OUTPUT, uri);

			try {
				pictureActionIntent.putExtra("return-data", true);

				activity.startActivityForResult(pictureActionIntent, pickFromCamera);
			} catch (ActivityNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
     * Utility to start an image picker with request code
     *
     * @param activity Activity that will receive result
     * @param requestCode requestCode for result
     */
    public static void pickImage(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        try {
            activity.startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity, R.string.crop__pick_error, Toast.LENGTH_SHORT).show();
        }
    }

}
