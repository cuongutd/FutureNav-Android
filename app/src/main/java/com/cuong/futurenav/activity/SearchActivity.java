package com.cuong.futurenav.activity;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.cuong.futurenav.R;
import com.cuong.futurenav.model.MapLocation;
import com.cuong.futurenav.service.GooglePlacesHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SearchActivity extends BaseAppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private MapLocation mSeachLoc;
    private PlaceAutocompleteAdapter mPlaceAdapter;
    protected GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView mAutocompleteView;
    private ImageView mGoogleLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager()
                .findFragmentById(R.id.searchmap);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.animateCamera(CameraUpdateFactory.newLatLng(centerUSLatLong));
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {

    }

    private void initSearchTextview(View root) {

        //from google place api sample PlaceComplete
        mGoogleLogo = (ImageView) root.findViewById(R.id.powerbygoogle);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this) //so that it will connect when activity start and dis when stop
                .addApi(Places.GEO_DATA_API)
                .build();

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView) root.findViewById(R.id.autocomplete_places);
        // Register a listener that receives callbacks when a suggestion has been selected

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mPlaceAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_US, null);

        mAutocompleteView.setAdapter(mPlaceAdapter);

        GooglePlacesHelper mAutocompleteClickListener = new GooglePlacesHelper(mGoogleApiClient, mSeachLoc, mPlaceAdapter);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);


    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        logd("onConnectionFailed:" + connectionResult);
    }
}
