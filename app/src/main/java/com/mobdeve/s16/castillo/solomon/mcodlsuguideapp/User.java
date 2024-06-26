package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;



import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;


public class User {
    @DocumentId
    private DocumentReference userRef;
    private String username;
    private String email;
    private String password;
    private int idNo;

    public User(){

    }

    public User(String username, String email, String password, int idNo){
        this.username = username;
        this.email = email;
        this.password = password;
        this.idNo = idNo;
    }


    public DocumentReference getUserRef(){
        return this.userRef;
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
    public void setUserID(DocumentReference userID) {
        this.userRef = userID;
    }

}
