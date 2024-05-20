package com.example.uddd.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
public class PlacesInfo {
    public class Address {
        @SerializedName("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;

        @SerializedName("address_number")
        public String getAddress_number() {
            return this.address_number;
        }

        public void setAddress_number(String address_number) {
            this.address_number = address_number;
        }

        String address_number;

        @SerializedName("street_name")
        public String getStreet_name() {
            return this.street_name;
        }

        public void setStreet_name(String street_name) {
            this.street_name = street_name;
        }

        String street_name;
    }

    public class Context {
        @SerializedName("postcode")
        public Postcode getPostcode() {
            return this.postcode;
        }

        public void setPostcode(Postcode postcode) {
            this.postcode = postcode;
        }

        Postcode postcode;

        @SerializedName("place")
        public Place getPlace() {
            return this.place;
        }

        public void setPlace(Place place) {
            this.place = place;
        }

        Place place;

        @SerializedName("address")
        public Address getAddress() {
            return this.address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        Address address;

        @SerializedName("street")
        public Street getStreet() {
            return this.street;
        }

        public void setStreet(Street street) {
            this.street = street;
        }

        Street street;
    }

    public class Coordinates {
        @SerializedName("latitude")
        public double getLatitude() {
            return this.latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        double latitude;

        @SerializedName("longitude")
        public double getLongitude() {
            return this.longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        double longitude;
    }

    public class ExternalIds {
        @SerializedName("id_osm")
        public String getId_osm() {
            return this.id_osm;
        }

        public void setId_osm(String id_osm) {
            this.id_osm = id_osm;
        }

        String id_osm;
    }

    public class Feature {
        @SerializedName("type")
        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        String type;

        @SerializedName("geometry")
        public Geometry getGeometry() {
            return this.geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        Geometry geometry;

        @SerializedName("properties")
        public Properties getProperties() {
            return this.properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        Properties properties;
    }

    public class Geometry {
        @SerializedName("coordinates")
        public ArrayList<Double> getCoordinates() {
            return this.coordinates;
        }

        public void setCoordinates(ArrayList<Double> coordinates) {
            this.coordinates = coordinates;
        }

        ArrayList<Double> coordinates;

        @SerializedName("type")
        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        String type;
    }

    public class Metadata {
        @SerializedName("phone")
        public String getPhone() {
            return this.phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        String phone;

        @SerializedName("website")
        public String getWebsite() {
            return this.website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        String website;
    }

    public class Place {
        @SerializedName("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;
    }

    public class Postcode {
        @SerializedName("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;
    }

    public class Properties {
        @SerializedName("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;

        @SerializedName("mapbox_id")
        public String getMapbox_id() {
            return this.mapbox_id;
        }

        public void setMapbox_id(String mapbox_id) {
            this.mapbox_id = mapbox_id;
        }

        String mapbox_id;

        @SerializedName("feature_type")
        public String getFeature_type() {
            return this.feature_type;
        }

        public void setFeature_type(String feature_type) {
            this.feature_type = feature_type;
        }

        String feature_type;

        @SerializedName("address")
        public String getAddress() {
            return this.address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        String address;

        @SerializedName("full_address")
        public String getFull_address() {
            return this.full_address;
        }

        public void setFull_address(String full_address) {
            this.full_address = full_address;
        }

        String full_address;

        @SerializedName("place_formatted")
        public String getPlace_formatted() {
            return this.place_formatted;
        }

        public void setPlace_formatted(String place_formatted) {
            this.place_formatted = place_formatted;
        }

        String place_formatted;

        @SerializedName("context")
        public Context getContext() {
            return this.context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        Context context;

        @SerializedName("coordinates")
        public Coordinates getCoordinates() {
            return this.coordinates;
        }

        public void setCoordinates(Coordinates coordinates) {
            this.coordinates = coordinates;
        }

        Coordinates coordinates;

        @SerializedName("language")
        public String getLanguage() {
            return this.language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        String language;

        @SerializedName("maki")
        public String getMaki() {
            return this.maki;
        }

        public void setMaki(String maki) {
            this.maki = maki;
        }

        String maki;

        @SerializedName("poi_category")
        public ArrayList<String> getPoi_category() {
            return this.poi_category;
        }

        public void setPoi_category(ArrayList<String> poi_category) {
            this.poi_category = poi_category;
        }

        ArrayList<String> poi_category;

        @SerializedName("poi_category_ids")
        public ArrayList<String> getPoi_category_ids() {
            return this.poi_category_ids;
        }

        public void setPoi_category_ids(ArrayList<String> poi_category_ids) {
            this.poi_category_ids = poi_category_ids;
        }

        ArrayList<String> poi_category_ids;

        @SerializedName("external_ids")
        public ExternalIds getExternal_ids() {
            return this.external_ids;
        }

        public void setExternal_ids(ExternalIds external_ids) {
            this.external_ids = external_ids;
        }

        ExternalIds external_ids;

        @SerializedName("metadata")
        public Metadata getMetadata() {
            return this.metadata;
        }

        public void setMetadata(Metadata metadata) {
            this.metadata = metadata;
        }

        Metadata metadata;
    }


        @SerializedName("type")
        public String getType() {
            return this.type;
        }

        public void setType(String type) {
            this.type = type;
        }

        String type;

        @SerializedName("features")
        public ArrayList<Feature> getFeatures() {
            return this.features;
        }

        public void setFeatures(ArrayList<Feature> features) {
            this.features = features;
        }

        ArrayList<Feature> features;

        @SerializedName("attribution")
        public String getAttribution() {
            return this.attribution;
        }

        public void setAttribution(String attribution) {
            this.attribution = attribution;
        }

        String attribution;

    public class Street {
        @SerializedName("name")
        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String name;
    }
}
