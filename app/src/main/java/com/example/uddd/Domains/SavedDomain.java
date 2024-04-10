package com.example.uddd.Domains;

import java.io.Serializable;

public class SavedDomain implements Serializable {
    private String locationName;
    private String address;

    public SavedDomain(String locationName, String address) {
        this.locationName = locationName;
        this.address = address;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
