package com.doctor.sun.bean;

import io.realm.RealmObject;

/**
 * Created by rick on 5/1/2016.
 */

public class City extends RealmObject {
    private String city;
    private double lon;
    private double lat;

    public void setCity(String city) {
        this.city = city;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCity() {
        return city;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }
}
