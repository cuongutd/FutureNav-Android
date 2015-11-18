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

    @GET("/findSchoolByLocation")
    public ArrayList<SchoolModel> findSchoolByLocation(@Query("lat") String lat, @Query("lon") String lon);

    @GET("/getSchoolDetail")
    public SchoolModel getSchoolDetail(@Query("schoolId") int schoolId);

    @GET("/addSchoolToFav")
    public StudentProfileModel addSchoolToFav(@Query("studentId") int studentId, @Query("schoolId") int schoolId, @Query("note") String note);

    @GET("/removeFromFav")
    public StudentProfileModel removeFromFav(@Query("studentId") int studentId, @Query("favId") int favId);

    @GET("/getStudentProfile")
    public StudentProfileModel getStudentProfile(@Query("email") String email);

    @POST("/tokenSignIn")
    public StudentProfileModel tokenSignIn(@Body StudentProfileRequest userProfile);

    @POST("/takeNoteOnFav")
    public void takeNoteOnFav(@Query("studentId") int studentId, @Query("favId") int favId, @Query("note") String note);

}
