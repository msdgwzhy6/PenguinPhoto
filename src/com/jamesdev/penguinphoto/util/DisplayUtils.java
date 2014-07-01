package com.jamesdev.penguinphoto.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 14-5-25.
 */
public class DisplayUtils {
    private static DisplayMetrics sDisplayMetrics;
    private static Configuration sConfiguration;

    private static final float ROUND_DIFFERENCE = 0.5f;

    public static void init(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        sConfiguration = context.getResources().getConfiguration();
    }

    public static void onConfigurationChanged(Context context, Configuration newConfiguration) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
        sConfiguration = newConfiguration;
    }

    public static int getWidthPixels() {
        return sDisplayMetrics.widthPixels;
    }

    public static int getHeightPixels() {
        return sDisplayMetrics.heightPixels;
    }

    public static float getDensity() {
        return sDisplayMetrics.density;
    }

    public static int dp2px(int dp) {
        return (int)(dp * sDisplayMetrics.density + ROUND_DIFFERENCE);
    }

    public static int px2dp(int px) {
        return (int)(px / sDisplayMetrics.density + ROUND_DIFFERENCE);
    }
}
