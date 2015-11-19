package com.cuong.futurenav.model;

/**
 * Created by Cuong on 11/18/2015.
 */
public class MapLocation {

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
}
