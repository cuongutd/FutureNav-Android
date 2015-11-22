package com.cuong.futurenav.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cuong on 11/18/2015.
 */
public class MapLocation implements Parcelable {

    private Double mSearchLat;
    private Double mSearchLong;

    public MapLocation(Double lat, Double lon){
        mSearchLat = lat;
        mSearchLong = lon;
    }

    public Double getmSearchLong() {
        return mSearchLong;
    }

    public void setmSearchLong(Double mSearchLong) {
        this.mSearchLong = mSearchLong;
    }

    public Double getmSearchLat() {
        return mSearchLat;
    }

    public void setmSearchLat(Double mSearchLat) {
        this.mSearchLat = mSearchLat;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mSearchLat);
        dest.writeValue(this.mSearchLong);
    }

    protected MapLocation(Parcel in) {
        this.mSearchLat = (Double)in.readValue(Double.class.getClassLoader());
        this.mSearchLong = (Double)in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<MapLocation> CREATOR = new Parcelable.Creator<MapLocation>() {
        public MapLocation createFromParcel(Parcel source) {
            return new MapLocation(source);
        }

        public MapLocation[] newArray(int size) {
            return new MapLocation[size];
        }
    };
}
