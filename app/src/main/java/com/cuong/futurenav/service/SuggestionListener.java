package com.cuong.futurenav.service;

import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.cuong.futurenav.activity.adapter.PlaceSearchAdapter;
import com.cuong.futurenav.model.MapLocation;
import com.cuong.futurenav.util.Constants;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

/**
 * Created by Cuong on 11/20/2015.
 */
public class SuggestionListener implements SearchView.OnSuggestionListener, ResultCallback<PlaceBuffer> {

    private GoogleApiClient mGoogleApiClient;
    private PlaceSearchAdapter mPlaceAdapter;
    private ResultReceiver mReceiver;
    protected final String TAG = this.getClass().getSimpleName();


    public SuggestionListener(GoogleApiClient apiClient, ResultReceiver receiver, PlaceSearchAdapter adapter) {
        mGoogleApiClient = apiClient;
        mReceiver = receiver;
        mPlaceAdapter = adapter;
    }


    @Override
    public boolean onSuggestionSelect(int position) {

        final String placeId = mPlaceAdapter.getPlaceId(position);

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(this);

        Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);

        return false;
    }

    @Override
    public boolean onSuggestionClick(int position) {

        final String placeId = mPlaceAdapter.getPlaceId(position);

        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(this);

        Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        return true;
    }


    @Override
    public void onResult(PlaceBuffer places) {
        if (!places.getStatus().isSuccess()) {
            // Request did not complete successfully
            Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
            places.release();
            return;
        }
        // Get the Place object from the buffer.
        final Place place = places.get(0);

        MapLocation searchLoc = new MapLocation(place.getLatLng().latitude, place.getLatLng().longitude);
        Bundle b = new Bundle();
        b.putParcelable(Constants.EXTRA_GOOGLE_PLACE, searchLoc);
        mReceiver.send(Constants.RESULT_CODE_GOOGLE_PLACE, b);

        Log.i(TAG, "Place details received: " + place.getName());

        places.release();
    }
}
