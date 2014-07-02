package com.jamesdev.penguinphoto.activities;

import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.jamesdev.penguinphoto.R;
import com.jamesdev.penguinphoto.data.UserPrefs;
import com.jamesdev.penguinphoto.loader.LoginLoader;
import com.jamesdev.penguinphoto.model.Result;

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

    public enum LoginResult {
        EMPTY, SUCCESS, FAILURE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViews();

        UserPrefs userData = new UserPrefs(this);
        if (userData.isLoggedIn()) {
            Toast.makeText(this, "Already logged int", Toast.LENGTH_SHORT).show();
            finish();
        }

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
        return new LoginLoader(this, mUsername, mPassword);
    }

    @Override
    public void onLoadFinished(Loader<Result> loader, Result result) {
        if (result == Result.EMPTY) {
            return;// this means the request was from initLoader()
        }

        if (result == Result.SUCCESS) {
            Toast.makeText(this, getString(R.string.logging_in_), Toast.LENGTH_SHORT).show();
            //TODO here
            finish();
        } else {
            showError();
            Toast.makeText(this, getString(R.string.login_failure), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<Result> loader) {
        // No implementation necessary
    }

    private void showError() {
        if (mEditUsername != null) mEditUsername.setEnabled(true);
        if (mEditPassword != null) mEditPassword.setEnabled(true);
        mLoginButton.setVisibility(View.VISIBLE);
        mLoginIndicator.setVisibility(View.GONE);
    }
}