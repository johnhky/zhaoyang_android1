package com.doctor.sun.immutables;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.Bindable;
import android.os.Bundle;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.DrugExtraFee;
import com.doctor.sun.entity.constans.PayMethod;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.PayPrescriptionsFragment;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.util.HashMap;
import java.util.List;

/**
 * Created by rick on 28/11/2016.
 */
@Value.Immutable
@Value.Style(jdkOnly = true)
@JsonSerialize(as = ImmutableDrug.class)
@JsonDeserialize(as = ImmutableDrug.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Drug extends BaseItem {

    public abstract String getId();

    public abstract String getUser_coupon_id();

    public abstract String getDoctor_id();

    public abstract String getTo();

    public abstract String getPhone();

    public abstract String getAddress();

    public abstract String getRemark();

    public abstract double getMoney();

    public abstract double getNeed_pay();

    public abstract int getHas_pay();

    public abstract String getRefund_status();

    public abstract String getCreated_at();

    public abstract String getRecord_name();

    public abstract String getType();

    @Value.Default
    public double getDrug_money() {
        return 0D;
    }

    @JsonProperty("charge")
    @Value.Default
    public DrugExtraFee getExtra_fee() {
        return new DrugExtraFee();
    }

    public abstract Doctor getDoctor();

    public abstract List<String> getDrug();

    public abstract List<DrugEntity> getDrug_detail();


    @Override
    public int getItemLayoutId() {
        return R.layout.p_item_drug2;
    }


    @Bindable
    public String getStatus() {
        if (getHas_pay() == 1) {
            return "已支付";
        } else {
            return "未支付";
        }
    }

    public String getDetail() {
        return getDrug() + "，邮寄地址:" + getAddress() + "，收件人:" + getTo() + "，" + getPhone();
    }


    public void showPayMethod(Context context, Drug drug) {
        showDetail(context, drug);
    }

    public void cancelOrder(Context context, String id) {
        DrugModule api = Api.of(DrugModule.class);
        api.cancelOrder(id).enqueue(new ApiCallback<String>() {
            @Override
            protected void handleResponse(String response) {

            }

            @Override
            protected void handleApi(ApiDTO<String> body) {

            }
        });
    }

    public String statusColor(String status) {
        if (status.equals("已支付")) {
            return "#88cb5a";
        }
        if (status.equals("未支付")) {
            return "#f76d02";
        }
        if (status.equals("已关闭")) {
            return "#898989";
        }
        return "#898989";
    }


    public String styledStatus() {
        return "<font color='" + statusColor(getStatus()) + "'>" + getStatus() + "</font>";
    }

    public void showDetail(Context context, Drug drug) {

        Bundle args = PayPrescriptionsFragment.getArgs(String.valueOf(drug.getId()));
        Intent payPrescriptionIntent = SingleFragmentActivity.intentFor(context, "寄药支付", args);
        context.startActivity(payPrescriptionIntent);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DrugEntity {
        public String drug;
        public String price;

        @Override
        public String toString() {
            return drug + ": " + price;
        }
    }

    public void confirmPay(Context context, int payMethod, String couponId, double totalFee, HashMap<String, String> extraField) {
        extraField.put("drugOrderId", getId());

        if (!couponId.equals("-1")) {
            extraField.put("couponId", couponId);
        }

        AppointmentModule api = Api.of(AppointmentModule.class);

        switch (payMethod) {
            case PayMethod.ALIPAY:
                api.buildAlipayGoodsOrder(totalFee, "alipay", extraField)
                        .enqueue(new AlipayCallback((Activity) context, totalFee, extraField));
                break;
            case PayMethod.WECHAT:
                api.buildWeChatGoodsOrder(totalFee, "wechat", extraField)
                        .enqueue(new WeChatPayCallback((Activity) context, totalFee, extraField));
                break;
            case PayMethod.SIMULATED:
                Toast.makeText(context, "模拟支付", Toast.LENGTH_SHORT).show();
        }
    }
}
