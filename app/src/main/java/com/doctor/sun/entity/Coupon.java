package com.doctor.sun.entity;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.doctor.sun.R;
import com.doctor.sun.vm.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by rick on 20/5/2016.
 */
public class Coupon implements LayoutId {


    /**
     * threshold : 1
     * scope : all
     * platform : all
     * info : 测试
     * id : 7
     * coupon_money : 100
     * valid_begin_time : 2016-06-01
     * valid_end_time : 2016-06-30
     * coupon_status : 0
     * created_at : 2016-05-19 11:25:32
     */

    @JsonProperty("id")
    public String id;
    @JsonProperty("coupon_money")
    public String couponMoney;
    @JsonProperty("valid_begin_time")
    public String validBeginTime;
    @JsonProperty("valid_end_time")
    public String validEndTime;
    @JsonProperty("coupon_status")
    public String couponStatus;
    @JsonProperty("created_at")
    public String createdAt;
    @JsonProperty("threshold")
    public String threshold;
    @JsonProperty("scope")
    public String scope;
    @JsonProperty("platform")
    public String platform;
    @JsonProperty("info")
    public String info;
    @JsonProperty("threshold_available")
    public boolean threshold_available;

    // extra method
    public String getStatus() {
        switch (couponStatus) {
            case "used":
                return "已使用";
            case "available":
                return "有效期: " + validBeginTime + " 至 " + validEndTime;
            case "expire":
                return "已过期";
            default:
                return "已过期";
        }
    }

    public String getValidEndTime() {
        return validEndTime;
    }

    public void setValidEndTime(String validEndTime) {
        this.validEndTime = validEndTime;
    }

    public String getCouponMoney() {
        return couponMoney;
    }

    public void setCouponMoney(String couponMoney) {
        this.couponMoney = couponMoney;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isThreshold_available() {
        return threshold_available;
    }

    public void setThreshold_available(boolean threshold_available) {
        this.threshold_available = threshold_available;
    }

    public boolean isValid() {
        return couponStatus.equals("available");
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_coupon;
    }

    @Override
    public String toString() {
        return detail() + availability() + "\n" + validBeginTime + "至" + validEndTime;
    }

    public String detail() {
        String prefix;
        String suffix = "优惠券";
        int IntThreshold = 0;
        if (this.threshold != null) {
            IntThreshold = Integer.parseInt(this.threshold);
        }
        if (IntThreshold <= 0) {
            prefix = "立减";
        } else {
            prefix = "满" + threshold + "减";
        }
        return prefix + couponMoney + suffix;
    }

    public String couponDetail() {
        String prefix = "";
        int threshId = 0;
        if (this.threshold != null) {
            threshId = Integer.parseInt(this.threshold);
        }
        if (threshId <= 0) {
            prefix = "立减￥"+couponMoney;
        } else {
            prefix = "满" + threshold + "减" + couponMoney;
        }
        return prefix;
    }

    public String couponScope() {
        if (!TextUtils.isEmpty(scope)) {
            if (scope.equals(Scope.DRUG_ORDER)) {
                return "（仅寄药可用）";
            }
            if (scope.equals(Scope.PREMIUM_APPOINTMENT)) {
                return "（仅网诊可用）";
            }
            if (scope.equals(Scope.STANDARD_APPOINTMENT)) {
                return "（仅复诊可用）";
            }
            if (scope.equals(Scope.SURFACE_APPOINTMENT)) {
                return "（仅面诊可用）";
            }
            if (scope.equals(Scope.ALL)) {
                return "（全部可用）";
            }
        }

        return "";
    }

    public String availability() {
        if (null != platform) {
            if (platform.equals(Scope.ALL)) {
                return "(全部可用)";
            }
            if (platform.equals(Scope.DRUG_ORDER)) {
                return "(寄药可用)";
            }
            if (platform.equals(Scope.STANDARD_APPOINTMENT)) {
                return "(简易复诊可用)";
            }
            if (platform.equals(Scope.PREMIUM_APPOINTMENT)) {
                return "(VIP网诊可用)";
            }
        }
        return "";
    }

    public interface Scope {
        String ALL = "all";
        String DRUG_ORDER = "drug_order";
        String STANDARD_APPOINTMENT = "simple_consult";
        String PREMIUM_APPOINTMENT = "detail_consult";
        String SURFACE_APPOINTMENT = "surface_consult";
    }
}
