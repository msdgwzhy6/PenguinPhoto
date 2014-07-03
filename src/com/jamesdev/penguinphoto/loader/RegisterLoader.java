package com.jamesdev.penguinphoto.loader;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import com.jamesdev.penguinphoto.data.ConnectionManager;
import com.jamesdev.penguinphoto.data.LoginManager;
import com.jamesdev.penguinphoto.data.RegisterManager;
import com.jamesdev.penguinphoto.model.Result;

/**
 * Created by Administrator on 2014/7/3.
 */
public class RegisterLoader extends AsyncTaskLoader<Result> {
    private String mUsername;
    private String mEmail;
    private String mPassword;

    public RegisterLoader(Context context, String username, String email, String password) {
        super(context);
        mUsername = username;
        mEmail = email;
        mPassword = password;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Result loadInBackground() {
        if (mUsername == null || mEmail == null || mPassword == null) return Result.EMPTY;
        String newCookie = RegisterManager.register(mUsername,mEmail, mPassword);
        boolean isSuccess = newCookie != null;

        return isSuccess ? Result.SUCCESS : Result.FAILURE;
    }
}
