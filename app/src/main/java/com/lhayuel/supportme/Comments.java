package com.lhayuel.supportme;

public class Comments {
    public String comment, date, time, username, uid;

    public Comments()
    {

    }

    public Comments(String comment, String date, String time, String username, String uid)
    {
        this.comment = comment;
        this.date = date;
        this.time = time;
        this.username = username;
        this.uid = uid;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
