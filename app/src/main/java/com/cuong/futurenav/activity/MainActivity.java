package com.cuong.futurenav.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cuong.futurenav.R;
import com.cuong.futurenav.model.FavSchoolModel;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.service.MainIntentService;
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

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        , GoogleMap.OnMarkerClickListener
        , OnMapReadyCallback{

    //map related
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Map<Integer, Marker> mMapMarkers = new HashMap<Integer, Marker>();

    //show list of fav schools
    private RecyclerView mRecyclerView;
    private android.support.v7.widget.LinearLayoutManager mLayoutManager;
    private SchoolAdapter mAdapter;
    private TextView mEmptyView;

    //show user info
    private ImageView mStudentProfileImg;
    private TextView mUserName;
    private TextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //wiring all view components

        bindMap();

        bindViews();

        bindRecyclerview();

        //view data (text, img) are populated
        populateViewData();

    }

    private void bindRecyclerview() {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SchoolAdapter(mEmptyView, this);
        mRecyclerView.setAdapter(mAdapter);

        //on favorite list, swiping right item will delete school off the list
        ItemTouchHelper mIth = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return true;// true if moved, false otherwise
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                        Log.d(LOG_TAG, "direction: " + direction);
//                        Log.d(LOG_TAG, "ItemTouchHelper.RIGHT: " + ItemTouchHelper.RIGHT);
//                        if (direction == ItemTouchHelper.RIGHT && !mSearchMode) {
//                            // remove from adapter
//                            int pos = viewHolder.getAdapterPosition();
//                            long id = mAdapter.getItemId(pos);
//                            String[] args = {String.valueOf(id)};
//                            getActivity().getContentResolver().delete(DBContract.FavoriteEntry.CONTENT_URI, DBContract.FavoriteEntry._ID + " = ? ", args);
//                            SchoolActivityFragment.this.getLoaderManager().restartLoader(SEARCH_LOADER, null, SchoolActivityFragment.this);
//                            String website = mAdapter.getWebSite(pos);
//                            ((MyApplication) getActivity().getApplication()).getmFavoriteSchoolList().remove(website);
//                            mMapMarkers.get(Integer.valueOf(pos)).remove();
//                            Toast.makeText(getActivity(),
//                                    R.string.school_msg_remove,
//                                    Toast.LENGTH_SHORT).show();
//                        }
                    }

                    @Override
                    public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                        //only enable swipe mode on favorite list, not search result
                        return super.getSwipeDirs(recyclerView, viewHolder);
                    }

                });
        mIth.attachToRecyclerView(mRecyclerView);
    }

    private void bindViews() {

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //http://stackoverflow.com/questions/33199764/android-api-23-change-navigation-view-headerlayout-textview
        View headerView = navigationView.getHeaderView(0);

        mStudentProfileImg = (ImageView)headerView.findViewById(R.id.imageView);
        mUserName = (TextView)headerView.findViewById(R.id.username);
        mEmail = (TextView)headerView.findViewById(R.id.email);


        mEmptyView = (TextView)findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView)findViewById(R.id.schoollist);


    }

    private void populateViewData() {
        Intent i = getIntent();
        StudentProfileModel studentProfile = i.getParcelableExtra(MainIntentService.EXTRA_RESULT_STUDENT_PROFILE);

        mUserName.setText(studentProfile.getNameFirst());
        mEmail.setText(studentProfile.getEmail());

        Glide.with(this).load(studentProfile.getPhotoUrl()).asBitmap().override(96, 96).centerCrop().into(new BitmapImageViewTarget(mStudentProfileImg) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mStudentProfileImg.setImageDrawable(circularBitmapDrawable);
            }
        });

        mAdapter.swapCursor(studentProfile.getListOfFavSchool());

        showSchoolsOnMap(studentProfile.getListOfFavSchool());

    }


    private void bindMap() {
        if (mMap == null) {

            SupportMapFragment supportMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            supportMapFragment.getMapAsync(this);
        }

    }

    private void showSchoolsOnMap(ArrayList<FavSchoolModel> data) {

        mMapMarkers = new HashMap<Integer, Marker>();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (mMap != null) {
            mMap.clear();
            for (FavSchoolModel school : data) {
                String schoolName = school.getSchool().getName();
                double lon = school.getSchool().getLatitude();
                double lat = school.getSchool().getLongitude();

                LatLng l = new LatLng(lat, lon);

                Marker marker = mMap.addMarker(new MarkerOptions().position(l).title(schoolName));

                mMapMarkers.put(school.getId(), marker);

                builder.include(l);

            }
            //re focus map
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = null;
            if (mMapMarkers.size() == 0) { //no fav, move map to US

                cu = CameraUpdateFactory.newLatLngBounds(BOUNDS_US, mMapPadding);

            } else if (mMapMarkers.size() == 1)
                cu = CameraUpdateFactory.newLatLngZoom(mMapMarkers.values().iterator().next().getPosition(), 12F);
            else
                cu = CameraUpdateFactory.newLatLngBounds(bounds, mMapPadding);


            if (cu != null)
                mMap.animateCamera(cu);
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            signOut();
        } else if (id == R.id.nav_search) {
            findSchool();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void findSchool() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    private void signOut() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case MainIntentService.RESULT_CODE_START:
                showProgressDialog();
                break;
            case MainIntentService.RESULT_CODE_RUNNING:
                //show progress
                break;
            case MainIntentService.RESULT_CODE_COMPLETED:
                StudentProfileModel studentProfile = resultData.getParcelable(MainIntentService.EXTRA_RESULT_STUDENT_PROFILE);
                // do something interesting
                // hide progress
                hideProgressDialog();
                break;
            case MainIntentService.RESULT_CODE_FAILED:
                // handle the error;
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        int pos = -1;
        for (Map.Entry<Integer, Marker> entry : mMapMarkers.entrySet())
            //assuming title is unique within a list
            if (marker.getTitle().equals(entry.getValue().getTitle())) {
                pos = entry.getKey().intValue();
                break;
            }
        if (pos >= 0) {
            logd("pos: " + pos);
            mLayoutManager.smoothScrollToPosition(mRecyclerView, null, pos);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(MainActivity.this);
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        builder.include(new LatLng(28.410307, -123.922802));
//        builder.include(new LatLng(49.454750, -66.900840));
//        LatLngBounds bounds = builder.build();
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, mMapPadding));

        mMap.animateCamera(CameraUpdateFactory.newLatLng(centerUSLatLong));
    }
}
