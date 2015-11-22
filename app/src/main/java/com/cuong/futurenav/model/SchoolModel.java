package com.cuong.futurenav.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Cuong on 11/17/2015.
 */
public class SchoolModel implements Parcelable {
    private Integer id;
    private String name;
    private String type;
    private String relegious;
    private Date established;
    private boolean active;
    private Byte gradeFrom;
    private Byte gradeTo;
    private String website;
    private String gender;
    private String crestUrl;
    private String slogan;

    private String street;
    private String city;
    private String state;
    private String countrycode;
    private String zip;

    private Double longitude;
    private Double latitude;
    private Date auCreatedDt;
    private Date auUpdatedDt;
    private List<SchoolDetailModel> listOfSchoolDetail;
    private String contactNumber;
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRelegious() {
        return relegious;
    }

    public void setRelegious(String relegious) {
        this.relegious = relegious;
    }

    public Date getEstablished() {
        return established;
    }

    public void setEstablished(Date established) {
        this.established = established;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Byte getGradeFrom() {
        return gradeFrom;
    }

    public void setGradeFrom(Byte gradeFrom) {
        this.gradeFrom = gradeFrom;
    }

    public Byte getGradeTo() {
        return gradeTo;
    }

    public void setGradeTo(Byte gradeTo) {
        this.gradeTo = gradeTo;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCrestUrl() {
        return crestUrl;
    }

    public void setCrestUrl(String crestUrl) {
        this.crestUrl = crestUrl;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Date getAuCreatedDt() {
        return auCreatedDt;
    }

    public void setAuCreatedDt(Date auCreatedDt) {
        this.auCreatedDt = auCreatedDt;
    }

    public Date getAuUpdatedDt() {
        return auUpdatedDt;
    }

    public void setAuUpdatedDt(Date auUpdatedDt) {
        this.auUpdatedDt = auUpdatedDt;
    }

    public List<SchoolDetailModel> getListOfSchoolDetail() {
        return listOfSchoolDetail;
    }

    public void setListOfSchoolDetail(List<SchoolDetailModel> listOfSchoolDetail) {
        this.listOfSchoolDetail = listOfSchoolDetail;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.relegious);
        dest.writeLong(established != null ? established.getTime() : -1);
        dest.writeByte(active ? (byte)1 : (byte)0);
        dest.writeValue(this.gradeFrom);
        dest.writeValue(this.gradeTo);
        dest.writeString(this.website);
        dest.writeString(this.gender);
        dest.writeString(this.crestUrl);
        dest.writeString(this.slogan);
        dest.writeString(this.street);
        dest.writeString(this.city);
        dest.writeString(this.state);
        dest.writeString(this.countrycode);
        dest.writeString(this.zip);
        dest.writeValue(this.longitude);
        dest.writeValue(this.latitude);
        dest.writeLong(auCreatedDt != null ? auCreatedDt.getTime() : -1);
        dest.writeLong(auUpdatedDt != null ? auUpdatedDt.getTime() : -1);
        dest.writeList(this.listOfSchoolDetail);
        dest.writeString(this.contactNumber);
        dest.writeString(this.email);
    }

    public SchoolModel() {
    }

    protected SchoolModel(Parcel in) {
        this.id = (Integer)in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.type = in.readString();
        this.relegious = in.readString();
        long tmpEstablished = in.readLong();
        this.established = tmpEstablished == -1 ? null : new Date(tmpEstablished);
        this.active = in.readByte() != 0;
        this.gradeFrom = (Byte)in.readValue(Byte.class.getClassLoader());
        this.gradeTo = (Byte)in.readValue(Byte.class.getClassLoader());
        this.website = in.readString();
        this.gender = in.readString();
        this.crestUrl = in.readString();
        this.slogan = in.readString();
        this.street = in.readString();
        this.city = in.readString();
        this.state = in.readString();
        this.countrycode = in.readString();
        this.zip = in.readString();
        this.longitude = (Double)in.readValue(Double.class.getClassLoader());
        this.latitude = (Double)in.readValue(Double.class.getClassLoader());
        long tmpAuCreatedDt = in.readLong();
        this.auCreatedDt = tmpAuCreatedDt == -1 ? null : new Date(tmpAuCreatedDt);
        long tmpAuUpdatedDt = in.readLong();
        this.auUpdatedDt = tmpAuUpdatedDt == -1 ? null : new Date(tmpAuUpdatedDt);
        this.listOfSchoolDetail = new ArrayList<SchoolDetailModel>();
        in.readList(this.listOfSchoolDetail, List.class.getClassLoader());
        this.contactNumber = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<SchoolModel> CREATOR = new Parcelable.Creator<SchoolModel>() {
        public SchoolModel createFromParcel(Parcel source) {
            return new SchoolModel(source);
        }

        public SchoolModel[] newArray(int size) {
            return new SchoolModel[size];
        }
    };
}