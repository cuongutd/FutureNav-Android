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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cuong.futurenav.R;
import com.cuong.futurenav.activity.adapter.SchoolAdapter;
import com.cuong.futurenav.model.FavSchoolModel;
import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.service.MainIntentService;
import com.cuong.futurenav.util.Constants;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends BaseAppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{



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

        bindMap(R.id.map);

        bindViews();

        bindRecyclerview();

        //view data (text, img) are populated
        if (getStudentProfile() != null)
            populateViewData();
        else {
            logd("profile is null! check saved instance");

            if (savedInstanceState != null && savedInstanceState.containsKey(Constants.EXTRA_STUDENT_PROFILE)) {
                StudentProfileModel profile = savedInstanceState.getParcelable(Constants.EXTRA_STUDENT_PROFILE);
                setStudentProfile(profile);
            }
        }
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

                        //remove from favorite list if user swipe right

                        int pos = viewHolder.getAdapterPosition();

                        Integer favId = mAdapter.getFavId(pos);

                        MainIntentService.unFav(mReceiver, MainActivity.this, favId, getStudentProfile().getId());

                        Toast.makeText(MainActivity.this,
                                R.string.school_unfaved_msg,
                                Toast.LENGTH_LONG).show();

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
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
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

        mUserName.setText(getStudentProfile().getNameFirst());
        mEmail.setText(getStudentProfile().getEmail());

        Glide.with(this).load(getStudentProfile().getPhotoUrl()).asBitmap().override(96, 96).centerCrop().into(new BitmapImageViewTarget(mStudentProfileImg) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(MainActivity.this.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                mStudentProfileImg.setImageDrawable(circularBitmapDrawable);
            }
        });

        mAdapter.swapCursor(getStudentProfile().getListOfFavSchool());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        if (getStudentProfile() == null)
            return;

        ArrayList<SchoolModel> s = new ArrayList<>();
        for (FavSchoolModel v: getStudentProfile().getListOfFavSchool()){
            s.add(v.getSchool());
        }

        showSchoolsOnMap(s);

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
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

//        } else if (id == R.id.nav_send) {
//
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void findSchool() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case Constants.RESULT_CODE_STUDENT_PROFILE:
                StudentProfileModel studentProfile = resultData.getParcelable(Constants.EXTRA_STUDENT_PROFILE);
                setStudentProfile(studentProfile);
                populateViewData();
                break;

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        int schoolId = getSchoolIdFromMarker(marker);
        int pos = mAdapter.getPosition(schoolId);
        if (pos >= 0) {
            logd("pos: " + pos);
            mLayoutManager.smoothScrollToPosition(mRecyclerView, null, pos);
        }

        return super.onMarkerClick(marker);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getStudentProfile() != null)
            mAdapter.swapCursor(getStudentProfile().getListOfFavSchool());
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //go to detail screen
        Intent i = new Intent(this, DetailActivity.class);
        int schoolId = getSchoolIdFromMarker(marker);
        SchoolModel schoolDetail = mAdapter.getSchoolFromSchoolId(schoolId);
        i.putExtra(Constants.EXTRA_SCHOOL_DETAIL, schoolDetail);
        startActivity(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.EXTRA_STUDENT_PROFILE, getStudentProfile());
    }
}
