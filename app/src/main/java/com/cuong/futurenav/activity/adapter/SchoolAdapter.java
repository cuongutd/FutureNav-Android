package com.cuong.futurenav.activity.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.cuong.futurenav.R;
import com.cuong.futurenav.activity.BaseAppCompatActivity;
import com.cuong.futurenav.activity.DetailActivity;
import com.cuong.futurenav.model.FavSchoolModel;
import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.util.Constants;

import java.util.ArrayList;

/**
 * Created by Cuong on 8/26/2015.
 */

public class SchoolAdapter extends RecyclerView.Adapter<SchoolAdapter.FavoriteViewHolder> {

    private ArrayList<FavSchoolModel> mSchools;
    private TextView mEmptyView;
    private Context mContext;


    public SchoolAdapter(TextView emptyView, Context context) {
        mEmptyView = emptyView;
        mContext = context;
    }


    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mSchoolName;
        public TextView mAddress;
        public TextView mInfo;

        public FavoriteViewHolder(View view) {
            super(view);
            mSchoolName = (TextView) view.findViewById(R.id.schoolname);
            mAddress = (TextView) view.findViewById(R.id.address);
            mInfo = (TextView) view.findViewById(R.id.info);

        }

        public void print(){
            Log.d("mSchoolName: ", mSchoolName.getText().toString());
            Log.d("mAddress: ", mAddress.getText().toString());
            Log.d("mInfo: ", mInfo.getText().toString());
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewGroup instanceof RecyclerView) {

            int layoutId = R.layout.schoolcard;

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);

            final FavoriteViewHolder holder = new FavoriteViewHolder(view);

            view.setFocusable(true);

            view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            int position = holder.getAdapterPosition();
                                            SchoolModel school = mSchools.get(position).getSchool();

                                            Intent intent = new Intent(mContext, DetailActivity.class);
                                            intent.putExtra(Constants.EXTRA_SCHOOL_DETAIL, school);

                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ActivityOptionsCompat options = ActivityOptionsCompat.
                                                        makeSceneTransitionAnimation((BaseAppCompatActivity)mContext, view, mContext.getString(R.string.activity_trans));
                                                mContext.startActivity(intent, options.toBundle());
                                            }
                                            else {
                                                mContext.startActivity(intent);
                                            }

                                        }
                                    }
            );


            return holder;
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder viewHolder, int i){

        FavSchoolModel school = mSchools.get(i);

        viewHolder.mSchoolName.setText(school.getSchool().getName());
        viewHolder.mAddress.setText(school.getSchool().getCity() + ", " + school.getSchool().getState());

        StringBuffer schoolInfo = new StringBuffer();
        schoolInfo.append(school.getSchool().getType());
        schoolInfo.append(", ");

        if (!"BOTH".equals(school.getSchool().getGender())) {
            schoolInfo.append(school.getSchool().getGender());
            schoolInfo.append(" ONLY, ");
        }

        schoolInfo.append("Grade ");
        schoolInfo.append(school.getSchool().getGradeFrom() + "-" + school.getSchool().getGradeTo());

        viewHolder.mInfo.setText(schoolInfo.toString());


    }

    public void swapCursor(ArrayList<FavSchoolModel> schools) {
        mSchools = schools;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        if (null == mSchools) return 0;
        return mSchools.size();
    }

    @Override
    public long getItemId(int position) {
        return mSchools.get(position).getId().longValue();
    }

    public Integer getFavId(int position) {
        return mSchools.get(position).getId();
    }


    public int getPosition(int schoolId){
        int p = -1;
        for (FavSchoolModel m : mSchools){
            if (schoolId == m.getSchool().getId().intValue()) {
                p = mSchools.indexOf(m);
                break;
            }

        }
        return p;
    }

    public SchoolModel getSchoolFromSchoolId(int schoolId){
        SchoolModel s = null;
        for (FavSchoolModel m : mSchools){
            if (schoolId == m.getSchool().getId().intValue()) {
                s = m.getSchool();
                break;
            }

        }
        return s;
    }


}
