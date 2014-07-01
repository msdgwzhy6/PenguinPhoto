package com.jamesdev.penguinphoto.util;

import java.io.File;

/**
 * Created by Administrator on 14-5-25.
 */
public class FileUtils {
    public static synchronized File createFolder(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
