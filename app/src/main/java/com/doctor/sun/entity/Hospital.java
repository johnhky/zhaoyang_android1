package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lucas on 1/26/16.
 */
public class Hospital {

    /**
     * id : 1
     * name : 广州市脑科医院
     * detail : 广州市脑科医院（广州市精神病医院，俗称芳村精神病院）位于广州市，于1898年由美国人嘉约翰（John Kerr）在美国基督教长老会支持下以惠爱医癫院为名创办，是中国第一间精神病专科医院，曾用名广州市第十人民医院。设芳村、江村、荔湾三个门诊以及芳村、江村两个住院部。广州市脑科医院亦是三级甲等专科医院、广州市首批医保定点医疗机构。该医院为中国最早的一家西医精神病医院，对研究清代时期广州地区引入西医学术文化有较高的历史价值。
     * location : 广东省广州市芳村区明心路36号
     * image : http:\/\/a1.att.hudong.com\/86\/67\/01300542517414139856675747336_s.jpg
     * longitude : 113.24548
     * latitude : 23.104044
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("detail")
    private String detail;
    @JsonProperty("location")
    private String location;
    @JsonProperty("image")
    private String image;
    @JsonProperty("longitude")
    private double longitude;
    @JsonProperty("latitude")
    private double latitude;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

    public String getLocation() {
        return location;
    }

    public String getImage() {
        return image;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "Hospital{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", detail='" + detail + '\'' +
                ", location='" + location + '\'' +
                ", image='" + image + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }
}
