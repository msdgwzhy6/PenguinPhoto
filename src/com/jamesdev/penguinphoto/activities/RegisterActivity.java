package com.jamesdev.penguinphoto.activities;

import android.app.Activity;
import android.app.LoaderManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.View;
import com.jamesdev.penguinphoto.R;
import com.jamesdev.penguinphoto.loader.RegisterLoader;
import com.jamesdev.penguinphoto.model.Result;

/**
 * Created by Administrator on 14-6-28.
 */
public class RegisterActivity extends FragmentActivity implements LoaderCallbacks<Result> {
    private String mUsername;
    private String mEmail;
    private String mPassword;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public Loader<Result> onCreateLoader(int id, Bundle args) {
        return new RegisterLoader(this, mUsername, mEmail, mPassword);
    }


    private void findViews() {

    }
}