package com.cuong.futurenav.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.cuong.futurenav.R;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.service.MainIntentService;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageView mStudentProfileImg;
    TextView mUserName;
    TextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();

        StudentProfileModel studentProfile = i.getParcelableExtra(MainIntentService.EXTRA_RESULT_STUDENT_PROFILE);



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

//        Glide.with(this)
//                .load(studentProfile.getPhotoUrl())
//                .placeholder(android.R.drawable.sym_def_app_icon)
//                .centerCrop()
//                .into(mStudentProfileImg);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
}
