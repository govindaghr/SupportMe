package com.lhayuel.supportme;

public class Posts {
    public String uid, time, date, title, postimage, description, username;

    public Posts()
    {

    }

    public Posts(String uid, String time, String date, String title, String postimage, String description, String username)
    {
        this.uid = uid;
        this.title = title;
        this.time = time;
        this.date = date;
        this.postimage = postimage;
        this.description = description;
        this.username = username;
    }

    public String getTitle(){ return title; }
    public String getTime() {
        return time;
    }
    public String getDate() {
        return date;
    }
    public String getPostimage() {
        return postimage;
    }
    public String getUsername() {
        return username;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setDate(String date) {
        this.date = date;
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
    public void setUsername(String username) {
        this.username = username;
    }

}
