package com.cuong.futurenav.model;

/**
 * Created by Cuong on 11/18/2015.
 */
public class StudentProfileRequest {
    private String displayName;
    private String email;
    private String photoUrl;
    private String networkGroup;
    private String authToken;

    public StudentProfileRequest() {
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getNetworkGroup() {
        return this.networkGroup;
    }

    public void setNetworkGroup(String networkGroup) {
        this.networkGroup = networkGroup;
    }
}
