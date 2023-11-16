package com.mobdeve.s16.castillo.solomon.mcodlsuguideapp;


public class Directory {
    private String directoryName;
    private String location;
    private Integer image;
    private String tag;

    public Directory(String directoryName, String location, Integer image, String tag){
        this.directoryName = directoryName;
        this.location = location;
        this.image = image;
        this.tag = new String(tag);
    }

    public String getDirectoryName(){
        return this.directoryName;
    }
    public String getLocation(){
        return this.location;
    }

    public Integer getImage() {
        return this.image;
    }

    public String getTags() {
        return this.tag;
    }
}
