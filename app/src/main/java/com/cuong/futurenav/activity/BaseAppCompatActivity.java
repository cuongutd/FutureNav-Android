package com.cuong.futurenav.activity;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cuong.futurenav.R;
import com.cuong.futurenav.service.BaseResultReceiver;

/**
 * Created by Cuong on 9/10/2015.
 * To track activity's lifecycle. Other activities in the project extend this base class
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseResultReceiver.Receiver{

    protected BaseResultReceiver mReceiver;

    protected ProgressDialog mProgressDialog;

    protected final String TAG = this.getClass().getSimpleName();

    protected void logd(String msg){
        Log.d(TAG, msg);
    }


    protected void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private String LOG_TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BaseResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(LOG_TAG, "onConfigurationChanged");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d(LOG_TAG, "onPostCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mReceiver.setReceiver(null); // clear receiver so no leaks.
        Log.d(LOG_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "onStart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.d(LOG_TAG, "onResumeFragments");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "onStop");
    }
}
