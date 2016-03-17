package com.doctor.sun.bean;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rick on 5/1/2016.
 */
public class Province extends RealmObject {

    @PrimaryKey
    private String State;
    private double maxLon;
    private double maxLat;
    private double minLon;
    private double minLat;

    private RealmList<City> Cities;

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public RealmList<City> getCities() {
        return Cities;
    }

    public void setCities(RealmList<City> cities) {
        Cities = cities;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public void setMinLon(double minLon) {
        this.minLon = minLon;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }
}