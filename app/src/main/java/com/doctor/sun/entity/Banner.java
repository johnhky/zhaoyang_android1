package com.doctor.sun.entity;

import android.content.Context;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.vm.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 20/7/2016.
 */

public class Banner implements LayoutId {

    /**
     * id : 12
     * activity_display_type : 3
     * activity_pic_small : http://xkt51.com2.z0.glb.qiniucdn.com/FknIy-p8ErYcJgpWb7Onzdo5g2uu
     */

    @JsonProperty("id")
    private String id;
    @JsonProperty("activity_display_type")
    private int activityDisplayType;
    @JsonProperty("activity_pic_small")
    private String activityPicSmall;
    @JsonProperty("activity_show_introduce")
    private int activityShowIntroduce;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivityPicSmall() {
        return activityPicSmall;
    }

    public void setActivityPicSmall(String activityPicSmall) {
        this.activityPicSmall = activityPicSmall;
    }

    public int getActivityShowIntroduce() {
        return activityShowIntroduce;
    }

    public void setActivityShowIntroduce(int activityShowIntroduce) {
        this.activityShowIntroduce = activityShowIntroduce;
    }

    public void viewDetail(final Context context) {
        ToolModule api = Api.of(ToolModule.class);

        api.bannerDetail(id).enqueue(new SimpleCallback<BannerDetail>() {
            @Override
            protected void handleResponse(BannerDetail response) {
                if (response != null) {
                    response.viewDetail(context);
                }
            }
        });

    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_bannner;
    }
}
