package com.jamesdev.penguinphoto.util;

import android.content.Context;
import android.os.Environment;
import com.jamesdev.penguinphoto.R;
import com.jamesdev.penguinphoto.app.framework.BaseApplication;

/**
 * Created by Administrator on 14-5-25.
 */
public class EnvironmentUtils {
    public static class Storage {
        public static String getSDCardPath() {
            Context context = BaseApplication.getApp();
            String sdPath = null;
            try {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    sdPath = Environment.getExternalStorageDirectory().toString();
                } else {
                    ToastUtil.show(context, R.string.sd_not_mount);
                }
            } catch (Exception e) {
                ToastUtil.show(context, R.string.sd_error);
            }
            return sdPath;
        }
    }
}
