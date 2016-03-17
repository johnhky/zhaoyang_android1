package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 11/18/15.
 */
public class Photo {

    /**
     * url : upload\/2015-07-11\/xutjcCT3es5ACQY.jpg
     */

    @JsonProperty("url")
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    @Override
    public String toString() {
        return "Photo{" +
                "url='" + url + '\'' +
                '}';
    }
}
