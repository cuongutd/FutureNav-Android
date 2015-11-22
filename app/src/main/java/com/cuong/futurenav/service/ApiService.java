package com.cuong.futurenav.service;

import com.cuong.futurenav.model.SchoolModel;
import com.cuong.futurenav.model.StudentProfileModel;
import com.cuong.futurenav.model.StudentProfileRequest;

import java.util.ArrayList;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by Cuong on 8/25/2015.
 */
public interface ApiService {

    @GET("/api/findSchoolByLocation")
    public ArrayList<SchoolModel> findSchoolByLocation(@Query("lat") String lat, @Query("lon") String lon);

    @GET("/api/getSchoolDetail")
    public SchoolModel getSchoolDetail(@Query("schoolId") int schoolId);

    @GET("/api/addSchoolToFav")
    public StudentProfileModel addSchoolToFav(@Query("studentId") int studentId, @Query("schoolId") int schoolId, @Query("note") String note);

    @GET("/api/removeFromFav")
    public StudentProfileModel removeFromFav(@Query("studentId") int studentId, @Query("favId") int favId);

    @GET("/api/getStudentProfile")
    public StudentProfileModel getStudentProfile(@Query("email") String email);

    @POST("/api/tokenSignIn")
    public StudentProfileModel tokenSignIn(@Body StudentProfileRequest userProfile);

    @POST("/api/takeNoteOnFav")
    public void takeNoteOnFav(@Query("studentId") int studentId, @Query("favId") int favId, @Query("note") String note);

}
