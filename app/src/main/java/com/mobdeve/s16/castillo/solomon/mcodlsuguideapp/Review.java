package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


import java.util.Date;

public class Review implements Parcelable {
    private String reviewID;
    private String reviewContent;
    private Integer imageUri;
    private float rate;
    private String userID;
    private Date date;

    public Review(String reviewID, String reviewContent, Integer imageUri, float rate, String user,
                  Date date){
        this.reviewID = reviewID;
        this.reviewContent = reviewContent;
        this.imageUri = imageUri;
        this.rate = rate;
        this.userID = user;
        this.date = date;
    }

    public Review(String reviewContent, Integer imageUri, float rate, String user, Date date){
        this("0", reviewContent, imageUri, rate, user, date);
    }

    protected Review(Parcel in) {
        reviewID = in.readString();
        reviewContent = in.readString();
        if (in.readByte() == 0) {
            imageUri = null;
        } else {
            imageUri = in.readInt();
        }
        rate = in.readFloat();
        userID = in.readString();
        long tmpDate = in.readLong();
        date = tmpDate != -1 ? new Date(tmpDate) : null;
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getReviewID() {
        return this.reviewID;
    }
    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }
    public String getReviewContent() {
        return this.reviewContent;
    }
    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
    public Integer getImageUri() {
        return this.imageUri;
    }
    public void setImageUri(Integer imageUri) {
        this.imageUri = imageUri;
    }
    public float getRate() {
        return this.rate;
    }
    public void setRate(float rate) {
        this.rate = rate;
    }
    public String getUserID() {
        return this.userID;
    }
    public void setUser(String user) {
        this.userID = user;
    }
    public Date getDate(){
        return this.date;
    }
    public void setDate(Date date){
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(reviewID);
        dest.writeString(reviewContent);
        if (imageUri == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(imageUri);
        }
        dest.writeFloat(rate);
        dest.writeString(userID);
        dest.writeLong(date != null ? date.getTime() : -1L);
    }
    @Override
    public String toString() {
        return "Review content: " + reviewContent + ", User: " + userID;
    }

}
