package com.cuong.futurenav.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cuong.futurenav.R;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.service.MainIntentService;
import com.cuong.futurenav.util.Constants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Activity to demonstrate basic retrieval of the Google user's ID, email address, and basic
 * profile.
 */

public class LoginActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final int RC_GET_TOKEN = 9002;

    @Bind(R.id.status)
    TextView mStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        SignInButton signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setScopes(mGoogleSignInOptions.getScopeArray());
        // [END customize_button]
    }

    @Override
    public void onStart() {
        super.onStart(); //this will silently trying to connect, good or bad it will call handleSignInResult()
    }

    /**
     * For login activity, the signIn needs to be handled differently
     *
     */
    @Override
    protected void handleSignInResult(GoogleSignInResult result) {
        logd("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, set googleAccount
            setGoogleSignInAccount(result.getSignInAccount());

            logd("token: " + getGoogleSignInAccount().getIdToken());

            //call service to get user profile and do sign up if this is first time user
            MainIntentService.signIn(mReceiver, this
                    , getGoogleSignInAccount().getDisplayName()
                    , getGoogleSignInAccount().getEmail()
                    , getGoogleSignInAccount().getPhotoUrl().toString()
                    , "GOOGLE", getGoogleSignInAccount().getIdToken());

        } else {
            //show unauth screen so user can login
            updateUI(false);

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_GET_TOKEN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GET_TOKEN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText("Signed out");

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
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