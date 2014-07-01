package com.jamesdev.penguinphoto.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import com.jamesdev.penguinphoto.model.Result;

/**
 * Created by Administrator on 14-6-29.
 */
public class LoginLoader extends AsyncTaskLoader<Result> {
    String mUsername;
    String mPassword;

    public LoginLoader(Context context, String username, String password) {
        super(context);
        mUsername = username;
        mPassword = password;
    }

    @Override
    public Result loadInBackground() {
        if (mUsername == null || mPassword == null) return Result.EMPTY;


    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
