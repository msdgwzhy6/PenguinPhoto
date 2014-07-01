package com.jamesdev.penguinphoto.app;

import android.content.Context;
import com.jamesdev.penguinphoto.util.FileUtils;
import com.jamesdev.penguinphoto.util.EnvironmentUtils;

import java.io.File;

/**
 * Created by Administrator on 14-5-25.
 */
public class AppConfig {
    public static String SERVER_URL = "";
    private static String sPhotoFolderPath;
    private static String sCacheFolderPath;
    private static String sPenguinFolderPath;

    private static final String PENGUIN_FOLDER_NAME = "penguin_photo";
    private static final String CACHE_FOLDER_NAME = "cache";
    private static final String PHOTO_FOLDER_NAME = "photo";


    public static void init(Context context, boolean createFolder) {
        sPenguinFolderPath = EnvironmentUtils.Storage.getSDCardPath() + File.separator + PENGUIN_FOLDER_NAME;
        sPhotoFolderPath = sPenguinFolderPath + File.separator + PHOTO_FOLDER_NAME;
        sCacheFolderPath = sPenguinFolderPath + File.separator + CACHE_FOLDER_NAME;

        if (createFolder) {
            FileUtils.createFolder(sPhotoFolderPath);
            FileUtils.createFolder(sCacheFolderPath);
        }
    }

    public static String getPhotoFolderPath() {
        return sPhotoFolderPath;
    }

    public static String getCacheFolderPath() {
        return sCacheFolderPath;
    }


}
