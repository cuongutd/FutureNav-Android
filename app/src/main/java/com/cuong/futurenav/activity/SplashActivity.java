package com.cuong.futurenav.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.cuong.futurenav.R;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.service.MainIntentService;
import com.cuong.futurenav.util.Constants;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void handleSignInResult(GoogleSignInResult result) {
        super.handleSignInResult(result);
        if (getGoogleSignInAccount() != null)//call service to get profile
            MainIntentService.signIn(mReceiver, this
                    , getGoogleSignInAccount().getDisplayName()
                    , getGoogleSignInAccount().getEmail()
                    , getGoogleSignInAccount().getPhotoUrl().toString()
                    , "GOOGLE", getGoogleSignInAccount().getIdToken());
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Constants.RESULT_CODE_STUDENT_PROFILE:
                StudentProfileModel studentProfile = resultData.getParcelable(Constants.EXTRA_STUDENT_PROFILE);
                // do something interesting
                //send to mainActivity to show on the screen

                setStudentProfile(studentProfile);

                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;

        }
    }
}
