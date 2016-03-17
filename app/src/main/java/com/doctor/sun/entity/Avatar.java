package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 1/2/2016.
 */
public class Avatar  {
    /**
     * avatar : https://trello-avatars.s3.amazonaws.com/eb8345770e0fd6183d370fc3e2b1f1d3/30.png
     * name : 大明
     */

    @JsonProperty("avatar")
    private String avatar;
    @JsonProperty("name")
    private String name;

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }
}
