package com.cuong.futurenav.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.model.StudentProfileRequest;

import java.util.ArrayList;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class MainIntentService extends IntentService {

    private static final String ACTION_FIND_SCHOOL = "com.cuong.futurenav.service.action.FIND_SCHOOL";
    private static final String ACTION_SCHOOL_DETAIL = "com.cuong.futurenav.service.action.SCHOOL_DETAIL";
    private static final String ACTION_ADD_TO_FAV = "com.cuong.futurenav.service.action.ADD_TO_FAV";
    private static final String ACTION_UNFAV = "com.cuong.futurenav.service.action.UNFAV";
    private static final String ACTION_SIGN_IN = "com.cuong.futurenav.service.action.SIGN_IN";
    private static final String ACTION_FAV_NOTE = "com.cuong.futurenav.service.action.FAV_NOTE";

    private static final String EXTRA_LAT = "com.cuong.futurenav.service.extra.LAT";
    private static final String EXTRA_LON = "com.cuong.futurenav.service.extra.LON";
    private static final String EXTRA_SCHOOL_ID = "com.cuong.futurenav.service.extra.SCHOOL_ID";
    private static final String EXTRA_STUDENT_ID = "com.cuong.futurenav.service.extra.STUDENT_ID";
    private static final String EXTRA_NOTE = "com.cuong.futurenav.service.extra.NOTE";
    private static final String EXTRA_FAV_ID = "com.cuong.futurenav.service.extra.FAV_ID";
    private static final String EXTRA_NAME_FIRST = "com.cuong.futurenav.service.extra.NAME_FIRST";
    private static final String EXTRA_EMAIL = "com.cuong.futurenav.service.extra.EMAIL";
    private static final String EXTRA_PHOTO_URL = "com.cuong.futurenav.service.extra.PHOTO_URL";
    private static final String EXTRA_NETWORK_GROUP = "com.cuong.futurenav.service.extra.NETWORK_GROUP";
    private static final String EXTRA_AUTH_TOKEN = "com.cuong.futurenav.service.extra.AUTH_TOKEN";
    private static final String EXTRA_RESULT_RECEIVER = "com.cuong.futurenav.service.extra.RESULT_RECEIVER";

    public static final String EXTRA_RESULT_SCHOOL_LIST = "com.cuong.futurenav.service.extra.RESULT_SCHOOL_LIST";
    public static final String EXTRA_RESULT_SCHOOL_DETAIL = "com.cuong.futurenav.service.extra.RESULT_SCHOOL_DETAIL";
    public static final String EXTRA_RESULT_STUDENT_PROFILE = "com.cuong.futurenav.service.extra.RESULT_STUDENT_PROFILE";

    public static final int RESULT_CODE_START = 0;
    public static final int RESULT_CODE_RUNNING = 1;
    public static final int RESULT_CODE_COMPLETED = 2;
    public static final int RESULT_CODE_FAILED = 3;


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
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(ACTION_FIND_SCHOOL);
        intent.putExtra(EXTRA_LAT, lat);
        intent.putExtra(EXTRA_LON, lon);
        context.startService(intent);
    }

    /**
     * Starts this service to perform get school detail with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void getSchoolInfo(ResultReceiver receiver, Context context, String schoolId) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(ACTION_SCHOOL_DETAIL);
        intent.putExtra(EXTRA_SCHOOL_ID, schoolId);
        context.startService(intent);
    }

    /**
     * Starts this service to add school to fav list with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void addToFav(ResultReceiver receiver, Context context, String schoolId, String studentId, String note) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(ACTION_ADD_TO_FAV);
        intent.putExtra(EXTRA_SCHOOL_ID, schoolId);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
        intent.putExtra(EXTRA_NOTE, note);
        context.startService(intent);
    }

    /**
     * Starts this service to remove school from favorite list with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void unFav(ResultReceiver receiver, Context context, String favId, String studentId) {
        Intent intent = new Intent(context, MainIntentService.class);
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(ACTION_UNFAV);
        intent.putExtra(EXTRA_FAV_ID, favId);
        intent.putExtra(EXTRA_STUDENT_ID, studentId);
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
        intent.putExtra(EXTRA_RESULT_RECEIVER, receiver);
        intent.setAction(ACTION_SIGN_IN);
        intent.putExtra(EXTRA_NAME_FIRST, displayName);
        intent.putExtra(EXTRA_EMAIL, email);
        intent.putExtra(EXTRA_PHOTO_URL, photoUrl);
        intent.putExtra(EXTRA_NETWORK_GROUP, networkGroup);
        intent.putExtra(EXTRA_AUTH_TOKEN, authToken);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            ResultReceiver rec = intent.getParcelableExtra(EXTRA_RESULT_RECEIVER);

            if (ACTION_FIND_SCHOOL.equals(action)) {
                final String lat = intent.getStringExtra(EXTRA_LAT);
                final String lon = intent.getStringExtra(EXTRA_LON);
                handleActionFindSchool(rec, lat, lon);
            } else if (ACTION_SCHOOL_DETAIL.equals(action)) {
                final String schoolId = intent.getStringExtra(EXTRA_SCHOOL_ID);
                handleActionSchoolDetail(rec, schoolId);
            } else if (ACTION_ADD_TO_FAV.equals(action)) {
                final String schoolId = intent.getStringExtra(EXTRA_SCHOOL_ID);
                final String studentId = intent.getStringExtra(EXTRA_STUDENT_ID);
                handleActionAddToFav(rec, schoolId, studentId);
            } else if (ACTION_UNFAV.equals(action)) {
                final String studentId = intent.getStringExtra(EXTRA_STUDENT_ID);
                final String favId = intent.getStringExtra(EXTRA_FAV_ID);
                handleActionUnfav(rec, studentId, favId);
            } else if (ACTION_SIGN_IN.equals(action)) {
                StudentProfileRequest userReq = new StudentProfileRequest();
                userReq.setAuthToken(intent.getStringExtra(EXTRA_AUTH_TOKEN));
                userReq.setDisplayName(intent.getStringExtra(EXTRA_NAME_FIRST));
                userReq.setEmail(intent.getStringExtra(EXTRA_EMAIL));
                userReq.setNetworkGroup(intent.getStringExtra(EXTRA_NETWORK_GROUP));
                userReq.setPhotoUrl(intent.getStringExtra(EXTRA_PHOTO_URL));
                handleActionSignIn(rec, userReq);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFindSchool(ResultReceiver receiver, String lat, String lon) {

        ArrayList<SchoolModel> schools = RestClient.getApiService().findSchoolByLocation(lat, lon);

        Bundle b = new Bundle();
        b.putParcelableArrayList(EXTRA_RESULT_SCHOOL_LIST, schools);
        receiver.send(RESULT_CODE_COMPLETED, b);
    }

    private void handleActionSchoolDetail(ResultReceiver receiver, String schoolId){



    }

    private void handleActionAddToFav(ResultReceiver receiver, String schoolId, String studentId){
    }

    private void handleActionUnfav(ResultReceiver receiver, String studentId, String favId){
    }

    private void handleActionSignIn(ResultReceiver receiver, StudentProfileRequest userReq){

        StudentProfileModel p = RestClient.getApiService().tokenSignIn(userReq);
        Bundle b = new Bundle();
        b.putParcelable(EXTRA_RESULT_STUDENT_PROFILE, p);
        receiver.send(RESULT_CODE_COMPLETED, b);

    }
}