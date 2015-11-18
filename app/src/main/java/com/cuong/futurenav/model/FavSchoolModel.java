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

    public FavSchoolModel() {
    }

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
        sb.append(this.id);
        sb.append("]:");
        sb.append(this.affiliateType);
        sb.append("|");
        sb.append(this.note);
        sb.append("|");
        sb.append(this.auCreatedDt);
        sb.append("|");
        sb.append(this.auUpdatedDt);
        sb.append("|");
        sb.append(this.auCreatedBy);
        sb.append("|");
        sb.append(this.auUpdatedBy);
        return sb.toString();
    }

    protected FavSchoolModel(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        affiliateType = in.readString();
        note = in.readString();
        long tmpAuCreatedDt = in.readLong();
        auCreatedDt = tmpAuCreatedDt != -1 ? new Date(tmpAuCreatedDt) : null;
        long tmpAuUpdatedDt = in.readLong();
        auUpdatedDt = tmpAuUpdatedDt != -1 ? new Date(tmpAuUpdatedDt) : null;
        auCreatedBy = in.readByte() == 0x00 ? null : in.readInt();
        auUpdatedBy = in.readByte() == 0x00 ? null : in.readInt();
        school = (SchoolModel)in.readValue(SchoolModel.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte)(0x00));
        } else {
            dest.writeByte((byte)(0x01));
            dest.writeInt(id);
        }
        dest.writeString(affiliateType);
        dest.writeString(note);
        dest.writeLong(auCreatedDt != null ? auCreatedDt.getTime() : -1L);
        dest.writeLong(auUpdatedDt != null ? auUpdatedDt.getTime() : -1L);
        if (auCreatedBy == null) {
            dest.writeByte((byte)(0x00));
        } else {
            dest.writeByte((byte)(0x01));
            dest.writeInt(auCreatedBy);
        }
        if (auUpdatedBy == null) {
            dest.writeByte((byte)(0x00));
        } else {
            dest.writeByte((byte)(0x01));
            dest.writeInt(auUpdatedBy);
        }
        dest.writeValue(school);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<FavSchoolModel> CREATOR = new Parcelable.Creator<FavSchoolModel>() {
        @Override
        public FavSchoolModel createFromParcel(Parcel in) {
            return new FavSchoolModel(in);
        }

        @Override
        public FavSchoolModel[] newArray(int size) {
            return new FavSchoolModel[size];
        }
    };
}