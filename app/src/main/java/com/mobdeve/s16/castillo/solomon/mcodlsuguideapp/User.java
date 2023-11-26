package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/*
* Temporary User Class
* can be modified
* */
public class User implements Parcelable {
    private String userID;
    private String username;
    private String email;
    private String password;
    private int idNo;

    public User(String username, String email, String password, int idNo){
        this.userID = String.valueOf(idNo);
        this.username = username;
        this.email = email;
        this.password = password;
        this.idNo = idNo;
    }

    protected User(Parcel in) {
        userID = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        idNo = in.readInt();
    }
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getUserID(){
        return this.userID;
    }
    public void setIdNo(int idNo) {
        this.idNo = idNo;
    }
    public String getUsername(){
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return this.email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getIdNo() {
        return this.idNo;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(userID);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(idNo);
    }
}
