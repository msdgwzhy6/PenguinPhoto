package com.jamesdev.penguinphoto.app.framework;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.jamesdev.penguinphoto.app.AppConfig;
import com.jamesdev.penguinphoto.util.DisplayUtils;

import java.util.List;

/**
 * Created by Administrator on 14-5-25.
 */
public class BaseApplication extends Application {
    private final static String TAG = "BaseApplication";
    private static BaseApplication sApp = null;
    private static String sProcessName;

    private static final String PROFESS_PREFIX = "com.jamesdev.penguinphoto" + ".";
    private static final String MAIN_PROCESS_NAME = PROFESS_PREFIX + "main";

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public final void onCreate() {
        super.onCreate();
        sApp = this;
        sProcessName = getProcessName();

        if (isMainProcess()) {
            onMainProcessCreated();
        } else {
            Log.e(TAG, "unknow process");
        }
    }

    protected void onMainProcessCreated() {
        DisplayUtils.init(this);
        AppConfig.init(this, true);
    }

    public boolean isMainProcess() {
        return MAIN_PROCESS_NAME.equals(sProcessName);
    }

    private String getProcessName() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList != null) {
            for (RunningAppProcessInfo appProcessInfo : runningAppProcessInfoList) {
                if (android.os.Process.myPid() == appProcessInfo.pid) {
                    return appProcessInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     * 获取Application实例
     *
     * @return Application 实例
     */
    public static BaseApplication getApp() {
        return sApp;
    }
}
