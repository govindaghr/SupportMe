package com.lhayuel.supportme;

public class Posts {
    public String uid, time, date, title, postimage, description, profileimage, fullname;

    public Posts()
    {

    }

    public Posts(String uid, String time, String date, String title, String postimage, String description, String fullname)
    {
        this.uid = uid;
        this.title = title;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.description = description;
        this.fullname = fullname;
    }

    public String getTitle(){ return title; }

    public void setTitle(String description) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
