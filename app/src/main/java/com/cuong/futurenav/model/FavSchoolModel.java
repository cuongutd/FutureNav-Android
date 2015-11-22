package com.cuong.futurenav.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Cuong on 11/17/2015.
 */
public class FavSchoolModel implements Parcelable {
    private Integer id;
    private String affiliateType;
    private String note;
    private Date auCreatedDt;
    private Date auUpdatedDt;
    private Integer auCreatedBy;
    private Integer auUpdatedBy;
    private SchoolModel school;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setAffiliateType(String affiliateType) {
        this.affiliateType = affiliateType;
    }

    public String getAffiliateType() {
        return this.affiliateType;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return this.note;
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

    public void setSchool(SchoolModel school) {
        this.school = school;
    }

    public SchoolModel getSchool() {
        return this.school;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(id);
        sb.append("]:");
        sb.append(affiliateType);
        sb.append("|");
        sb.append(note);
        sb.append("|");
        sb.append(auCreatedDt);
        sb.append("|");
        sb.append(auUpdatedDt);
        sb.append("|");
        sb.append(auCreatedBy);
        sb.append("|");
        sb.append(auUpdatedBy);
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.affiliateType);
        dest.writeString(this.note);
        dest.writeLong(auCreatedDt != null ? auCreatedDt.getTime() : -1);
        dest.writeLong(auUpdatedDt != null ? auUpdatedDt.getTime() : -1);
        dest.writeValue(this.auCreatedBy);
        dest.writeValue(this.auUpdatedBy);
        dest.writeParcelable(this.school, 0);
    }

    public FavSchoolModel() {
    }

    protected FavSchoolModel(Parcel in) {
        this.id = (Integer)in.readValue(Integer.class.getClassLoader());
        this.affiliateType = in.readString();
        this.note = in.readString();
        long tmpAuCreatedDt = in.readLong();
        this.auCreatedDt = tmpAuCreatedDt == -1 ? null : new Date(tmpAuCreatedDt);
        long tmpAuUpdatedDt = in.readLong();
        this.auUpdatedDt = tmpAuUpdatedDt == -1 ? null : new Date(tmpAuUpdatedDt);
        this.auCreatedBy = (Integer)in.readValue(Integer.class.getClassLoader());
        this.auUpdatedBy = (Integer)in.readValue(Integer.class.getClassLoader());
        this.school = in.readParcelable(SchoolModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<FavSchoolModel> CREATOR = new Parcelable.Creator<FavSchoolModel>() {
        public FavSchoolModel createFromParcel(Parcel source) {
            return new FavSchoolModel(source);
        }

        public FavSchoolModel[] newArray(int size) {
            return new FavSchoolModel[size];
        }
    };
}