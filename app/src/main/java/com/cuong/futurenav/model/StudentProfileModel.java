package com.cuong.futurenav.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Cuong on 11/17/2015.
 */
public class StudentProfileModel implements Parcelable {

    private Integer id;
    private String nameFirst;
    private String nameLast;
    private String nameMiddle;
    private String email;
    private String city;
    private String country;
    private Date bornYear;
    private Date birthDate;
    private Date auCreatedDt;
    private Date auUpdatedDt;
    private Integer auCreatedBy;
    private Integer auUpdatedBy;
    private String networkUserId;
    private String networkGroup;
    private String photoUrl;
    private List<FavSchoolModel> listOfFavSchool;

    public StudentProfileModel() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setNameFirst(String nameFirst) {
        this.nameFirst = nameFirst;
    }

    public String getNameFirst() {
        return this.nameFirst;
    }

    public void setNameLast(String nameLast) {
        this.nameLast = nameLast;
    }

    public String getNameLast() {
        return this.nameLast;
    }

    public void setNameMiddle(String nameMiddle) {
        this.nameMiddle = nameMiddle;
    }

    public String getNameMiddle() {
        return this.nameMiddle;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return this.city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return this.country;
    }

    public void setBornYear(Date bornYear) {
        this.bornYear = bornYear;
    }

    public Date getBornYear() {
        return this.bornYear;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() {
        return this.birthDate;
    }

    public void setAuCreatedDt(Date auCreatedDt) {
        this.auCreatedDt = auCreatedDt;
    }

    public Date getAuCreatedDt() {
        return this.auCreatedDt;
    }

    public void setAuUpdatedDt(Date auUpdatedDt) {
        this.auUpdatedDt = auUpdatedDt;
    }

    public Date getAuUpdatedDt() {
        return this.auUpdatedDt;
    }

    public void setAuCreatedBy(Integer auCreatedBy) {
        this.auCreatedBy = auCreatedBy;
    }

    public Integer getAuCreatedBy() {
        return this.auCreatedBy;
    }

    public void setAuUpdatedBy(Integer auUpdatedBy) {
        this.auUpdatedBy = auUpdatedBy;
    }

    public Integer getAuUpdatedBy() {
        return this.auUpdatedBy;
    }

    public void setListOfFavSchool(List<FavSchoolModel> listOfFavSchool) {
        this.listOfFavSchool = listOfFavSchool;
    }

    public List<FavSchoolModel> getListOfFavSchool() {
        return this.listOfFavSchool;
    }

    public String getNetworkUserId() {
        return this.networkUserId;
    }

    public void setNetworkUserId(String networkuserid) {
        this.networkUserId = networkuserid;
    }

    public String getNetworkGroup() {
        return this.networkGroup;
    }

    public void setNetworkGroup(String networkgroup) {
        this.networkGroup = networkgroup;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(this.id);
        sb.append("]:");
        sb.append(this.nameFirst);
        sb.append("|");
        sb.append(this.nameLast);
        sb.append("|");
        sb.append(this.nameMiddle);
        sb.append("|");
        sb.append(this.email);
        sb.append("|");
        sb.append(this.city);
        sb.append("|");
        sb.append(this.country);
        sb.append("|");
        sb.append(this.bornYear);
        sb.append("|");
        sb.append(this.birthDate);
        sb.append("|");
        sb.append(this.auCreatedDt);
        sb.append("|");
        sb.append(this.auUpdatedDt);
        sb.append("|");
        sb.append(this.auCreatedBy);
        sb.append("|");
        sb.append(this.auUpdatedBy);
        sb.append("|");
        sb.append(this.photoUrl);
        return sb.toString();
    }

    protected StudentProfileModel(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        nameFirst = in.readString();
        nameLast = in.readString();
        nameMiddle = in.readString();
        email = in.readString();
        city = in.readString();
        country = in.readString();
        long tmpBornYear = in.readLong();
        bornYear = tmpBornYear != -1 ? new Date(tmpBornYear) : null;
        long tmpBirthDate = in.readLong();
        birthDate = tmpBirthDate != -1 ? new Date(tmpBirthDate) : null;
        long tmpAuCreatedDt = in.readLong();
        auCreatedDt = tmpAuCreatedDt != -1 ? new Date(tmpAuCreatedDt) : null;
        long tmpAuUpdatedDt = in.readLong();
        auUpdatedDt = tmpAuUpdatedDt != -1 ? new Date(tmpAuUpdatedDt) : null;
        auCreatedBy = in.readByte() == 0x00 ? null : in.readInt();
        auUpdatedBy = in.readByte() == 0x00 ? null : in.readInt();
        networkUserId = in.readString();
        networkGroup = in.readString();
        photoUrl = in.readString();
        if (in.readByte() == 0x01) {
            listOfFavSchool = new ArrayList<FavSchoolModel>();
            in.readList(listOfFavSchool, FavSchoolModel.class.getClassLoader());
        } else {
            listOfFavSchool = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        dest.writeString(nameFirst);
        dest.writeString(nameLast);
        dest.writeString(nameMiddle);
        dest.writeString(email);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeLong(bornYear != null ? bornYear.getTime() : -1L);
        dest.writeLong(birthDate != null ? birthDate.getTime() : -1L);
        dest.writeLong(auCreatedDt != null ? auCreatedDt.getTime() : -1L);
        dest.writeLong(auUpdatedDt != null ? auUpdatedDt.getTime() : -1L);
        if (auCreatedBy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(auCreatedBy);
        }
        if (auUpdatedBy == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(auUpdatedBy);
        }
        dest.writeString(networkUserId);
        dest.writeString(networkGroup);
        dest.writeString(photoUrl);
        if (listOfFavSchool == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(listOfFavSchool);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StudentProfileModel> CREATOR = new Parcelable.Creator<StudentProfileModel>() {
        @Override
        public StudentProfileModel createFromParcel(Parcel in) {
            return new StudentProfileModel(in);
        }

        @Override
        public StudentProfileModel[] newArray(int size) {
            return new StudentProfileModel[size];
        }
    };
}