package com.example.uddd.Domains;
import java.io.Serializable;
public class PopularDomain implements Serializable {
    private int locationID;
    private String photo;
    private String type;
    private String name;
    private String address;
    private float avgStar;
    private int totalComment;
    private String description;
    public PopularDomain(int locationID, String photo, String type, String name, String address, float avgStar, int totalComment, String description) {
        this.locationID = locationID;
        this.photo = photo;
        this.type = type;
        this.name = name;
        this.address = address;
        this.avgStar = avgStar;
        this.totalComment = totalComment;
        this.description = description;
    }
    public int getLocationID() {
        return locationID;
    }

    public void setLocationID(int locationID) {
        this.locationID = locationID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getAvgStar() {
        return avgStar;
    }

    public void setAvgStar(float avgStar) {
        this.avgStar = avgStar;
    }
}
