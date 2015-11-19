package com.cuong.futurenav.service;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.cuong.futurenav.activity.PlaceAutocompleteAdapter;
import com.cuong.futurenav.model.MapLocation;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;

import java.util.Map;

/**
 * Created by Cuong on 11/18/2015.
 */
public class GooglePlacesHelper implements AdapterView.OnItemClickListener, ResultCallback<PlaceBuffer> {

    private GoogleApiClient mGoogleApiClient;
    MapLocation mSearchLoc;
    private PlaceAutocompleteAdapter mPlaceAdapter;

    protected final String TAG = this.getClass().getSimpleName();


    public GooglePlacesHelper(GoogleApiClient apiClient, MapLocation loc, PlaceAutocompleteAdapter adapter){
        mGoogleApiClient = apiClient;
        mSearchLoc = loc;
        mPlaceAdapter = adapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
/*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
        final PlaceAutocompleteAdapter.PlaceAutocomplete item = mPlaceAdapter.getItem(i);
        final String placeId = String.valueOf(item.placeId);
        Log.i(TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(this);

        Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);
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

        mSearchLoc.setmSearchLat(place.getLatLng().latitude);
        mSearchLoc.setmSearchLong(place.getLatLng().longitude);

        Log.i(TAG, "Place details received: " + place.getName());

        places.release();
    }
}
