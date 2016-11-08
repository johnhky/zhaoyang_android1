package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.vo.LayoutId;
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
        int IntThreshold = Integer.parseInt(this.threshold);
        if (IntThreshold <= 0) {
            prefix = "立减";
        } else {
            prefix = "满" + threshold + "减";
        }
        return prefix + couponMoney + suffix;
    }

    public String availability() {
        if (platform.equals(Scope.ALL)) {
            return "(全部订单可用)";
        }
        if (platform.equals(Scope.DRUG_ORDER)) {
            return "(寄药订单可用)";
        }
        if (platform.equals(Scope.STANDARD_APPOINTMENT)) {
            return "(闲时咨询订单可用)";
        }
        if (platform.equals(Scope.PREMIUM_APPOINTMENT)) {
            return "(专属咨询订单可用)";
        }
        return "";
    }

    public interface Scope {
        String ALL = "all";
        String DRUG_ORDER = "drug_order";
        String STANDARD_APPOINTMENT = "simple_consult";
        String PREMIUM_APPOINTMENT = "detail_consult";
    }
}
