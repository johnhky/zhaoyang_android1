package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rick on 20/7/2016.
 */

public class Banner {

    /**
     * id : 12
     * activity_display_type : 3
     * activity_pic_small : http://xkt51.com2.z0.glb.qiniucdn.com/FknIy-p8ErYcJgpWb7Onzdo5g2uu
     */

    @JsonProperty("id")
    private String id;
    @JsonProperty("activity_display_type")
    private String activityDisplayType;
    @JsonProperty("activity_pic_small")
    private String activityPicSmall;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityDisplayType() {
        return activityDisplayType;
    }

    public void setActivityDisplayType(String activityDisplayType) {
        this.activityDisplayType = activityDisplayType;
    }

    public String getActivityPicSmall() {
        return activityPicSmall;
    }

    public void setActivityPicSmall(String activityPicSmall) {
        this.activityPicSmall = activityPicSmall;
    }
}
