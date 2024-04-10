package com.example.uddd.Domains;

import java.io.Serializable;

public class CommentDomain implements Serializable {
    private String username;
    private String comment;
    private float score;
    private int agree;
    private int disagree;
    private String date;
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getAgree() {
        return agree;
    }

    public void setAgree(int agree) {
        this.agree = agree;
    }

    public int getDisagree() {
        return disagree;
    }

    public void setDisagree(int disagree) {
        this.disagree = disagree;
    }

    public CommentDomain(String username, String comment, float score, int agree, int disagree, String date, String pic) {
        this.username = username;
        this.comment = comment;
        this.score = score;
        this.agree = agree;
        this.disagree = disagree;
        this.date = date;
        this.pic = pic;
    }
}
