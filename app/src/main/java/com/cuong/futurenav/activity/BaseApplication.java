package com.cuong.futurenav.activity;

import android.app.Application;

import com.cuong.futurenav.model.StudentProfileModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Cuong on 8/28/2015.
 * Contains list of favorite schools
 */
public class BaseApplication extends Application{

    private StudentProfileModel mStudentProfile;
    private GoogleSignInAccount mGoogleSignAccount;

    public GoogleSignInAccount getmGoogleSignAccount() {
        return mGoogleSignAccount;
    }

    public void setmGoogleSignAccount(GoogleSignInAccount mGoogleSignAccount) {
        this.mGoogleSignAccount = mGoogleSignAccount;
    }


    public StudentProfileModel getmStudentProfile() {
        return mStudentProfile;
    }

    public void setmStudentProfile(StudentProfileModel mStudentProfile) {
        this.mStudentProfile = mStudentProfile;
    }

}
