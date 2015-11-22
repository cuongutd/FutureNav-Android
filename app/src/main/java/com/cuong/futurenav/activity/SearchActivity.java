package com.cuong.futurenav.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.AutoCompleteTextView;

import com.cuong.futurenav.R;
import com.cuong.futurenav.activity.adapter.PlaceAutocompleteAdapter;
import com.cuong.futurenav.activity.adapter.PlaceSearchAdapter;
import com.cuong.futurenav.model.MapLocation;
import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.service.GooglePlacesHelper;
import com.cuong.futurenav.service.MainIntentService;
import com.cuong.futurenav.service.SuggestionListener;
import com.cuong.futurenav.util.Constants;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

public class SearchActivity extends BaseAppCompatActivity{

    private PlaceAutocompleteAdapter mPlaceAdapter;
    private AutoCompleteTextView mAutocompleteView;

    PlaceSearchAdapter newAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindMap(R.id.searchmap);

        initSearchTextview();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.search, menu);
//
//        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//
//        searchView.setSuggestionsAdapter(newAdapter);
//
//        searchView.setOnSuggestionListener(new SuggestionListener(mGoogleApiClient, mReceiver, newAdapter));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//
//            @Override
//            public boolean onQueryTextChange(String query) {
//
//                logd("onQueryTextChange");
//                if (query != null && query.length() > 0)
//                    newAdapter.getFilter().filter(query);
//
//                return true;
//
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                logd("onQueryTextSubmit");
//
//                return true;
//
//            }
//
//        });

        return true;
    }

    private void initSearchTextview() {

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);
        // Register a listener that receives callbacks when a suggestion has been selected

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mPlaceAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_US, null);

        mAutocompleteView.setAdapter(mPlaceAdapter);

        GooglePlacesHelper mAutocompleteClickListener = new GooglePlacesHelper(mGoogleApiClient, mReceiver, mPlaceAdapter);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);



        //new experiment

        //newAdapter = new PlaceSearchAdapter(this, null, mGoogleApiClient, BOUNDS_US);



    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return true;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //go to detail screen
        Intent i = new Intent(this, DetailActivity.class);
        int schoolId = getSchoolIdFromMarker(marker);

        MainIntentService.getSchoolInfo(mReceiver, this, schoolId);

    }


    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

        switch(resultCode) {
            case Constants.RESULT_CODE_GOOGLE_PLACE:
                //Google places  api return new search location, start calling ws to get new school list
                MapLocation loc = resultData.getParcelable(Constants.EXTRA_GOOGLE_PLACE);

                MainIntentService.findSchool(mReceiver, this, loc.getmSearchLat().toString(), loc.getmSearchLong().toString());
                break;
            case Constants.RESULT_CODE_SCHOOL_LIST:
                //show on the map
                ArrayList<SchoolModel> schools = resultData.getParcelableArrayList(Constants.EXTRA_SCHOOL_LIST);
                showSchoolsOnMap(schools);
                break;
            case Constants.RESULT_CODE_SCHOOL_DETAIL:
                //show on the map
                SchoolModel schoolDetail = resultData.getParcelable(Constants.EXTRA_SCHOOL_DETAIL);
                Intent i = new Intent(this, DetailActivity.class);
                i.putExtra(Constants.EXTRA_SCHOOL_DETAIL, schoolDetail);
                startActivity(i);
                break;
        }
    }






}
