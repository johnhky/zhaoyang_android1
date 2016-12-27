package com.doctor.sun.entity;

import com.doctor.sun.vm.BaseItem;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by kb on 16-11-1.
 */

public class CallConfig extends BaseItem {

    @JsonProperty("phone_call_enable")
    private boolean phoneCallEnable;
    @JsonProperty("phone_call_text")
    private String phoneCallText;
    @JsonProperty("banner_icon")
    private String bannerIcon;

    public boolean isPhoneCallEnable() {
        return phoneCallEnable;
    }

    public void setPhoneCallEnable(boolean phoneCallEnable) {
        this.phoneCallEnable = phoneCallEnable;
    }

    public String getPhoneCallText() {
        return phoneCallText;
    }

    public void setPhoneCallText(String phoneCallText) {
        this.phoneCallText = phoneCallText;
    }

    public String getBannerIcon() {
        return bannerIcon;
    }

    public void setBannerIcon(String bannerIcon) {
        this.bannerIcon = bannerIcon;
    }
}
