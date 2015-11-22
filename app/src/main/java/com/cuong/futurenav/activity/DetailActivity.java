package com.cuong.futurenav.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cuong.futurenav.R;
import com.cuong.futurenav.model.FavSchoolModel;
import com.cuong.futurenav.model.SchoolDetailModel;
import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.service.MainIntentService;
import com.cuong.futurenav.util.Constants;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.schoolname)
    TextView schoolName;
    @Bind(R.id.website)
    TextView website;
    @Bind(R.id.levels)
    TextView level;
    @Bind(R.id.type)
    TextView type;
    @Bind(R.id.gender)
    TextView gender;
    @Bind(R.id.classsize)
    TextView classSize;
    @Bind(R.id.ratio)
    TextView ratio;
    @Bind(R.id.nostudent)
    TextView nostudent;
    @Bind(R.id.sat)
    TextView satScore;
    @Bind(R.id.tuition)
    TextView tuition;
    @Bind(R.id.contactnumber)
    TextView contactnumber;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.address)
    TextView address;

    private ShareActionProvider mShareActionProvider;
    SchoolModel mSchool;
    Integer favSchoolId;
    boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        mSchool = i.getParcelableExtra(Constants.EXTRA_SCHOOL_DETAIL);
        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.EXTRA_SCHOOL_DETAIL))
            mSchool = savedInstanceState.getParcelable(Constants.EXTRA_SCHOOL_DETAIL);


        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        bindMap(R.id.detailmap);

        populateViewData();

        isSaved();

        if (isSaved)
            fab.setImageResource(android.R.drawable.btn_star_big_off);
        else
            fab.setImageResource(android.R.drawable.btn_star_big_on);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isSaved) {
                    fab.setImageResource(android.R.drawable.btn_star_big_off);
                    isSaved = true;
                    MainIntentService.addToFav(mReceiver, DetailActivity.this, mSchool.getId(), getStudentProfile().getId(), "This is a good school!");
                } else {
                    fab.setImageResource(android.R.drawable.btn_star_big_on);
                    isSaved = false;
                    MainIntentService.unFav(mReceiver, DetailActivity.this, favSchoolId, getStudentProfile().getId());
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void isSaved(){
        if (getStudentProfile() == null)
            return;
        for (FavSchoolModel fav : getStudentProfile().getListOfFavSchool()){

            if (mSchool.getId().equals(fav.getSchool().getId())) {
                favSchoolId = fav.getId();
                isSaved = true;
                return;
            }
        }

        isSaved = false;
    }


    private void populateViewData() {

        if (mSchool == null)
            return;
        SchoolDetailModel detail = new SchoolDetailModel();
        ArrayList<SchoolDetailModel> details = (ArrayList<SchoolDetailModel>)mSchool.getListOfSchoolDetail();

        //get the most recent year data
        if (details != null && details.size() > 0) {
            if (details.size() > 1)
                Collections.sort(details, new Comparator<SchoolDetailModel>() {
                    @Override
                    public int compare(SchoolDetailModel rec1, SchoolDetailModel rec2) {
                        return rec1.getYear().compareTo(rec2.getYear());
                    }
                });

            detail = details.get(details.size() - 1);
        }

        schoolName.setText(mSchool.getName());

        website.setText(mSchool.getWebsite());

        level.setText(String.valueOf(mSchool.getGradeFrom())); //TODO

        type.setText(mSchool.getType());

        gender.setText(mSchool.getGender());

        classSize.setText(String.valueOf(detail.getAvgClassSize()));

        ratio.setText(String.valueOf(detail.getRatio()));

        nostudent.setText(String.valueOf(detail.getStudentCount()));

        tuition.setText(String.valueOf(detail.getTuition()));

        satScore.setText(String.valueOf(detail.getAvgSatScore()));

        contactnumber.setText(mSchool.getContactNumber());

        email.setText(mSchool.getEmail());

        address.setText(mSchool.getStreet() + ", " + mSchool.getCity() + ", " + mSchool.getState() + " " + mSchool.getZip());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        super.onMapReady(googleMap);

        ArrayList<SchoolModel> s = new ArrayList<>();
        s.add(mSchool);
        showSchoolsOnMap(s);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mSchool.getName());
        shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("<a href=\"" + mSchool.getWebsite() + "\">" + mSchool.getWebsite() + "</a>") + "\n" + mSchool.getName());
        mShareActionProvider.setShareIntent(shareIntent);

        return true;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == Constants.RESULT_CODE_STUDENT_PROFILE) {
            if (!isSaved)
                Toast.makeText(this, R.string.school_faved_msg, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, R.string.school_unfaved_msg, Toast.LENGTH_LONG).show();

            StudentProfileModel studentProfile = resultData.getParcelable(Constants.EXTRA_STUDENT_PROFILE);
            // do something interesting
            //send to mainActivity to show on the screen

            setStudentProfile(studentProfile);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(Constants.EXTRA_SCHOOL_DETAIL, mSchool);

    }
}
