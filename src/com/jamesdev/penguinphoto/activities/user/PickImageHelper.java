package com.jamesdev.penguinphoto.activities.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import com.jamesdev.penguinphoto.app.AppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 14-5-25.
 */
public class PickImageHelper {
    private Activity mAttachedActivity;

    private int mCachedWidth, mCachedHeight;
    public static final String TEMP_CAMERA_PATH = AppConfig.getCacheFolderPath() + File.separator + ".tmp.jpg";

    public PickImageHelper(Activity activity) {
        mAttachedActivity = activity;
    }

    public boolean pickImageFromCamera(CharSequence title, int requestCode, int width, int height, String outputUri) {
        try {
            pickImage(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(outputUri)))
                    , title, requestCode, width, height);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void pickImage(Intent actionIntent, CharSequence title, int requestCode, int width, int height) {
        mAttachedActivity.startActivityForResult(Intent.createChooser(
                actionIntent.putExtra("return-data", false)
                , title)
                , requestCode);
        mCachedWidth = width;
        mCachedHeight = height;
    }

    public void savePhoto(String outputPath, Intent input) {
        Uri uri = input == null ? null : input.getData();
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
