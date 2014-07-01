package com.jamesdev.penguinphoto.activities;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.jamesdev.penguinphoto.R;

/**
 * Created by Administrator on 14-6-28.
 */
public class LoginActivity extends FragmentActivity implements View.OnClickListener, LoaderCallbacks<Result> {
    private EditText mEditUsername;
    private EditText mEditPassword;
    private Button mLoginButton;
    private Button mRegisterButton;
    private View mLoginIndicator;

    private String mUsername;
    private String mPassword;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();

        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void findViews() {
        mEditUsername = (EditText) findViewById(R.id.editText_email);
        mEditPassword = (EditText) findViewById(R.id.editText_password);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mLoginButton.setOnClickListener(this);
        mLoginIndicator = findViewById(R.id.login_indicator);
        mRegisterButton = (Button) findViewById(R.id.button_register);
        mRegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_register:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                performSubmit();
                break;
            default:
                break;
        }
    }

    private void performSubmit() {
        if (mEditUsername.getText() != null) mUsername = mEditUsername.getText().toString();
        if (mEditPassword.getText() != null) mPassword = mEditPassword.getText().toString();
        mLoginButton.setVisibility(View.GONE);
        mLoginIndicator.setVisibility(View.VISIBLE);

        showLoading();

        getSupportLoaderManager().restartLoader(0, null, LoginActivity.this);
    }

    private void showLoading() {
        if (mEditUsername != null) mEditUsername.setEnabled(false);
        if (mEditPassword != null) mEditPassword.setEnabled(false);
        mLoginButton.setVisibility(View.GONE);
        mLoginIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Result> onCreateLoader(int id, Bundle args) {

    }

    @Override
    public void onLoadFinished(Loader<Result> loader, Result result) {

    }

    @Override
    public void onLoaderReset(Loader<Result> loader) {
        // No implementation necessary
    }
}