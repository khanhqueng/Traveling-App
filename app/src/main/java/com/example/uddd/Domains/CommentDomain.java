package com.example.uddd.Domains;

import java.io.Serializable;

public class CommentDomain implements Serializable {
    public int id;
    public int userID;
    public int locationID;
    public String date;
    public float numStar;

    public String content;
    public int likes;
    public int dislikes;

    public CommentDomain(int id, int userID, int locationID, String date, float numStar, String content, int likes, int dislikes) {
        this.id = id;
        this.userID = userID;
        this.locationID = locationID;
        this.date = date;
        this.numStar = numStar;
        this.content = content;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getNumStar() {
        return numStar;
    }

    public void setNumStar(int numStar) {
        this.numStar = numStar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }
}
