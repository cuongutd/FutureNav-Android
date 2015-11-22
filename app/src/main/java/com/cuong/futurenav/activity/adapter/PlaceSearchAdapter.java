package com.cuong.futurenav.activity.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.FilterQueryProvider;
import android.widget.TextView;

import com.cuong.futurenav.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Created by Cuong on 11/20/2015.
 */
public class PlaceSearchAdapter extends CursorAdapter {

    private GoogleApiClient mGoogleApiClient;
    private LatLngBounds mBounds;
    private static final String TAG = PlaceAutocompleteAdapter.class.getSimpleName();
    private ArrayList<PlaceAutocomplete> mResultList;


    public PlaceSearchAdapter(Context context, Cursor c, GoogleApiClient client, LatLngBounds bounds) {
        super(context, c, false);
        mGoogleApiClient = client;
        mBounds = bounds;

        setFilterQueryProvider(filterQueryProvider);
    }

    private FilterQueryProvider filterQueryProvider = new FilterQueryProvider() {
        @Override
        public Cursor runQuery(CharSequence constraint) {

            mResultList = getAutocomplete(constraint);
            Cursor c = convertToCursor();
            return c;
        }
    };




    private Cursor convertToCursor(){
        String[] columns = new String[]{"_id", "placeid", "description"};
        MatrixCursor cursor = new MatrixCursor(columns);
        if (mResultList != null) {
            // The API successfully returned results.
            int i = 0;
            for (PlaceAutocomplete place : mResultList) {
                String[] temp = new String[3];
                temp[0] = Integer.toString(i);
                temp[1] = place.placeId;
                temp[2] = place.description;
                Log.d(TAG, place.description);
                cursor.addRow(temp);
                i = i + 1;
            }
        }
        return cursor;
    }


//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();
//                // Skip the autocomplete query if no constraints are given.
//                if (constraint != null) {
//                    // Query the autocomplete API for the (constraint) search string.
//                    mResultList = getAutocomplete(constraint);
//
//                    if (mResultList != null) {
//                        // The API successfully returned results.
//                        String[] columns = new String[]{"_id", "placeid", "description"};
//                        MatrixCursor cursor = new MatrixCursor(columns);
//                        int i = 0;
//                        for (PlaceAutocomplete place : mResultList) {
//                            String[] temp = new String[3];
//                            i = i + 1;
//                            temp[0] = Integer.toString(i);
//
//                            temp[1] = place.placeId;
//                            temp[2] = place.description;
//                            cursor.addRow(temp);
//                        }
//                        PlaceSearchAdapter.this.changeCursor(cursor);
//
//                        results.values = mResultList;
//                        results.count = mResultList.size();
//                    }
//                }
//                return results;
//            }
//
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results != null && results.count > 0) {
//                    // The API returned at least one result, update the data.
//                    notifyDataSetChanged();
//                } else {
//                    // The API did not return any results, invalidate the data set.
//                    notifyDataSetInvalidated();
//                }
//            }
//        };
//        return filter;
//    }

    //should be called when search text changed
    private ArrayList<PlaceAutocomplete> getAutocomplete(CharSequence constraint) {
        Log.d(TAG, "getAutocomplete");
        if (mGoogleApiClient.isConnected()) {
            Log.i(TAG, "Starting autocomplete query for: " + constraint);

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(mGoogleApiClient, constraint.toString(),
                                    mBounds, null);

            AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {

                Log.e(TAG, "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i(TAG, "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Copy the results into our own data structure, because we can't hold onto the buffer.
            // AutocompletePrediction objects encapsulate the API response (place ID and description).

            Iterator<AutocompletePrediction> iterator = autocompletePredictions.iterator();
            ArrayList resultList = new ArrayList<>(autocompletePredictions.getCount());
            int i = 0;
            while (iterator.hasNext()) {
                AutocompletePrediction prediction = iterator.next();
                // Get the details of this prediction and copy it into a new PlaceAutocomplete object.
                resultList.add(i, new PlaceAutocomplete(prediction.getPlaceId(),
                        prediction.getDescription()));
                i+=1;
            }

            // Release the buffer now that all data has been copied.
            autocompletePredictions.release();

            return resultList;
        }
        Log.d(TAG, "Google API client is not connected for autocomplete query.");
        return null;
    }


    public TextView mDescription;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_suggestion, parent, false);

        mDescription = (TextView)view.findViewById(R.id.placeDescription);

        return view;
    }

    public String getPlaceId(int position){
        return mResultList.get(position).placeId;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mDescription.setText(cursor.getString(2));
    }

    public class PlaceAutocomplete {

        public String placeId;
        public String description;

        PlaceAutocomplete(String placeId, String description) {
            this.placeId = placeId;
            this.description = description;
        }

        @Override
        public String toString() {
            return description.toString();
        }
    }


}
