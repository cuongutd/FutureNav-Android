package com.cuong.futurenav.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cuong.futurenav.R;
import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.service.BaseResultReceiver;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Cuong on 9/10/2015.
 * To track activity's lifecycle. Other activities in the project extend this base class
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity implements BaseResultReceiver.Receiver
        , OnMapReadyCallback
        , GoogleMap.OnMarkerClickListener
        , GoogleMap.OnInfoWindowClickListener
        , GoogleApiClient.OnConnectionFailedListener{

    private String LOG_TAG = this.getClass().getSimpleName();

    protected BaseResultReceiver mReceiver;

    //map related
    protected GoogleMap mMap; // Might be null if Google Play services APK is not available.
    protected Map<Integer, Marker> mMapMarkers = new HashMap<Integer, Marker>();
    protected static final LatLngBounds BOUNDS_US = new LatLngBounds(
            new LatLng(28.410307, -123.922802), new LatLng(49.454750, -66.900840));
    protected static final int mMapPadding = 100;
    protected static final LatLng centerUSLatLong = new LatLng(37.09024, -95.712891);
    protected int mapWidth;
    protected int mapHeight;

    protected GoogleApiClient mGoogleApiClient;

    GoogleSignInOptions mGoogleSignInOptions;

    protected ProgressDialog mProgressDialog;

    protected void bindMap(int id) {
        if (mMap == null) {

            SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(id);
            supportMapFragment.getMapAsync(this);
            mapHeight = supportMapFragment.getView().getLayoutParams().height;
            mapWidth = supportMapFragment.getView().getLayoutParams().width;

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    protected int getSchoolIdFromMarker(Marker marker) {

        int id = -1;
        for (Map.Entry<Integer, Marker> entry : mMapMarkers.entrySet())
            //assuming title is unique within a list
            if (marker.getTitle().equals(entry.getValue().getTitle())) {
                id = entry.getKey().intValue();
                break;
            }
        return id;
    }

    protected StudentProfileModel getStudentProfile() {
        return ((BaseApplication)getApplication()).getmStudentProfile();
    }

    protected void setStudentProfile(StudentProfileModel profile) {
        ((BaseApplication)getApplication()).setmStudentProfile(profile);
    }

    protected GoogleSignInAccount getGoogleSignInAccount(){
        return ((BaseApplication)getApplication()).getmGoogleSignAccount();
    }

    protected void setGoogleSignInAccount(GoogleSignInAccount acct){
        ((BaseApplication)getApplication()).setmGoogleSignAccount(acct);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(centerUSLatLong));
    }

    protected void showSchoolsOnMap(ArrayList<SchoolModel> data) {

        mMapMarkers = new HashMap<Integer, Marker>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (mMap != null) {
            mMap.clear();
            for (SchoolModel school : data) {

                LatLng l = new LatLng( school.getLatitude(), school.getLongitude());

                Marker marker = mMap.addMarker(new MarkerOptions().position(l).title(school.getName()).snippet(school.getType()));
                mMapMarkers.put(school.getId(), marker);

                builder.include(l);

            }
            CameraUpdate cu = null;
            if (mMapMarkers.size() == 0) { //no fav, move map to US

                cu = CameraUpdateFactory.newLatLngBounds(BOUNDS_US, mMapPadding);

            } else if (mMapMarkers.size() == 1)
                cu = CameraUpdateFactory.newLatLngZoom(mMapMarkers.values().iterator().next().getPosition(), 12F);
            else {
                //re focus map
                LatLngBounds bounds = builder.build();
                if (mapWidth < 0)
                    mapWidth = getResources().getDisplayMetrics().widthPixels;
                if (mapHeight < 0)
                    mapHeight = getResources().getDisplayMetrics().heightPixels;
                int padding = (int)(mapWidth * 0.12); // offset from edges of the map 12% of screen

                logd("mapWidth: " + mapWidth);
                logd("mapHeight: " + mapHeight);

                cu = CameraUpdateFactory.newLatLngBounds(bounds, mapWidth, mapHeight, padding);

                //cu = CameraUpdateFactory.newLatLngBounds(bounds, mMapPadding);
            }

            if (cu != null) {
                mMap.animateCamera(cu);
            }
        }

    }

    /**
     * This can be called from any activity to sign out
     *
     *
     */
    protected void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // make sure all data is wiped out
                        setGoogleSignInAccount(null);
                        setStudentProfile(null);
                        startActivity(new Intent(BaseAppCompatActivity.this, LoginActivity.class));
                    }
                });
    }

    // [START revokeAccess]
    protected void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                    }
                });
    }
    // [END revokeAccess]



    // [START handleSignInResult]
    protected void handleSignInResult(GoogleSignInResult result) {
        Log.d(LOG_TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            setGoogleSignInAccount(result.getSignInAccount());

        } else {
            // make sure all data is wiped out
            setGoogleSignInAccount(null);
            setStudentProfile(null);
            //Signed out, show unauthenticated UI. redirect user to login screen
            startActivity(new Intent(this, LoginActivity.class));

        }
    }
    // [END handleSignInResult]


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BaseResultReceiver(new Handler());
        mReceiver.setReceiver(this);
        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .addApi(Places.GEO_DATA_API)
                .build();

        logd("onCreate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        logd("onConfigurationChanged");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        logd("onPostCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logd("onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        logd("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logd("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mReceiver.isNull()) {
            mReceiver = new BaseResultReceiver(new Handler());
            mReceiver.setReceiver(this);
        }
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            logd("Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
        logd("onStart");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logd("onSaveInstanceState");
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        logd("onResumeFragments");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mReceiver.setReceiver(null); // clear receiver so no leaks.
        logd("onStop");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        logd( "onConnectionFailed:" + connectionResult);
    }

    protected void logd(String msg) {
        Log.d(LOG_TAG, msg);
    }

}
