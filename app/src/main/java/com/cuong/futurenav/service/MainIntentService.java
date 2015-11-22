package com.cuong.futurenav.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.model.StudentProfileRequest;
import com.cuong.futurenav.util.Constants;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class MainIntentService extends IntentService {

    public MainIntentService() {
        super("MainIntentService");
    }

    /**
     * Starts this service to perform action find school with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void findSchool(ResultReceiver receiver, Context context, String lat, String lon) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_FIND_SCHOOL);
        intent.putExtra(Constants.EXTRA_LAT, lat);
        intent.putExtra(Constants.EXTRA_LON, lon);
        context.startService(intent);
    }

    /**
     * Starts this service to perform get school detail with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void getSchoolInfo(ResultReceiver receiver, Context context, int schoolId) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_SCHOOL_DETAIL);
        intent.putExtra(Constants.EXTRA_SCHOOL_ID, schoolId);
        context.startService(intent);
    }

    /**
     * Starts this service to add school to fav list with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void addToFav(ResultReceiver receiver, Context context, Integer schoolId, Integer studentId, String note) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_ADD_TO_FAV);
        intent.putExtra(Constants.EXTRA_SCHOOL_ID, schoolId.intValue());
        intent.putExtra(Constants.EXTRA_STUDENT_ID, studentId.intValue());
        intent.putExtra(Constants.EXTRA_NOTE, note);
        context.startService(intent);
    }

    /**
     * Starts this service to remove school from favorite list with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void unFav(ResultReceiver receiver, Context context, Integer favId, Integer studentId) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_UNFAV);
        intent.putExtra(Constants.EXTRA_FAV_ID, favId);
        intent.putExtra(Constants.EXTRA_STUDENT_ID, studentId);
        context.startService(intent);
    }

    /**
     * Starts this service to sign in with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void signIn(ResultReceiver receiver, Context context, String displayName,
                              String email,
                              String photoUrl,
                              String networkGroup,
                              String authToken) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(Constants.EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(Constants.ACTION_SIGN_IN);
        intent.putExtra(Constants.EXTRA_NAME_FIRST, displayName);
        intent.putExtra(Constants.EXTRA_EMAIL, email);
        intent.putExtra(Constants.EXTRA_PHOTO_URL, photoUrl);
        intent.putExtra(Constants.EXTRA_NETWORK_GROUP, networkGroup);
        intent.putExtra(Constants.EXTRA_AUTH_TOKEN, authToken);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            ResultReceiver rec = intent.getParcelableExtra(Constants.EXTRA_RESULT_RECEIVER);

            if (Constants.ACTION_FIND_SCHOOL.equals(action)) {
                final String lat = intent.getStringExtra(Constants.EXTRA_LAT);
                final String lon = intent.getStringExtra(Constants.EXTRA_LON);
                handleActionFindSchool(rec, lat, lon);
            } else if (Constants.ACTION_SCHOOL_DETAIL.equals(action)) {
                final int schoolId = intent.getIntExtra(Constants.EXTRA_SCHOOL_ID, -1);
                handleActionSchoolDetail(rec, schoolId);
            } else if (Constants.ACTION_ADD_TO_FAV.equals(action)) {
                final int schoolId = intent.getIntExtra(Constants.EXTRA_SCHOOL_ID, -1);
                final int studentId = intent.getIntExtra(Constants.EXTRA_STUDENT_ID, -1);
                final String note = intent.getStringExtra(Constants.EXTRA_NOTE);
                handleActionAddToFav(rec, schoolId, studentId, note);
            } else if (Constants.ACTION_UNFAV.equals(action)) {
                final int studentId = intent.getIntExtra(Constants.EXTRA_STUDENT_ID, -1);
                final int favId = intent.getIntExtra(Constants.EXTRA_FAV_ID, -1);
                handleActionUnfav(rec, studentId, favId);
            } else if (Constants.ACTION_SIGN_IN.equals(action)) {
                StudentProfileRequest userReq = new StudentProfileRequest();
                userReq.setAuthToken(intent.getStringExtra(Constants.EXTRA_AUTH_TOKEN));
                userReq.setDisplayName(intent.getStringExtra(Constants.EXTRA_NAME_FIRST));
                userReq.setEmail(intent.getStringExtra(Constants.EXTRA_EMAIL));
                userReq.setNetworkGroup(intent.getStringExtra(Constants.EXTRA_NETWORK_GROUP));
                userReq.setPhotoUrl(intent.getStringExtra(Constants.EXTRA_PHOTO_URL));
                handleActionSignIn(rec, userReq);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFindSchool(ResultReceiver receiver, String lat, String lon) {

        ArrayList<SchoolModel> schools = RestClient.getInstance(this).getApiService().findSchoolByLocation(lat, lon);

        Bundle b = new Bundle();
        b.putParcelableArrayList(Constants.EXTRA_SCHOOL_LIST, schools);
        receiver.send(Constants.RESULT_CODE_SCHOOL_LIST, b);
    }

    private void handleActionSchoolDetail(ResultReceiver receiver, int schoolId){

        SchoolModel school = RestClient.getInstance(this).getApiService().getSchoolDetail(schoolId);
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_SCHOOL_DETAIL, school);
        receiver.send(Constants.RESULT_CODE_SCHOOL_DETAIL, b);

    }

    private void handleActionAddToFav(ResultReceiver receiver, int schoolId, int studentId, String note){

        StudentProfileModel p = RestClient.getInstance(this).getApiService().addSchoolToFav(studentId, schoolId, note);
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_STUDENT_PROFILE, p);
        receiver.send(Constants.RESULT_CODE_STUDENT_PROFILE, b);
    }

    private void handleActionUnfav(ResultReceiver receiver, int studentId, int favId){
        StudentProfileModel p = RestClient.getInstance(this).getApiService().removeFromFav(studentId, favId);
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_STUDENT_PROFILE, p);
        receiver.send(Constants.RESULT_CODE_STUDENT_PROFILE, b);
    }

    private void handleActionSignIn(ResultReceiver receiver, StudentProfileRequest userReq){

        StudentProfileModel p = RestClient.getInstance(this).getApiService().tokenSignIn(userReq);
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_STUDENT_PROFILE, p);
        receiver.send(Constants.RESULT_CODE_STUDENT_PROFILE, b);

    }
}