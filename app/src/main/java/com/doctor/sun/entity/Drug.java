package com.doctor.sun.entity;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.ui.widget.PayMethodDialog;
import com.doctor.sun.util.PayInterface;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.HashMap;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 1/22/16.
 */
public class Drug extends BaseObservable implements LayoutId {

    /**
     * id : 2
     * drug : 利醅酮
     * to : 王小姐
     * phone : 18888888881
     * address : 广州
     * remark : test
     * money : 100
     * has_pay : 0
     * status : normal
     * created_at : 2015-12-07 14:20:35
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("drug")
    private String drug;
    @JsonProperty("to")
    private String to;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("address")
    private String address;
    @JsonProperty("remark")
    private String remark;
    @JsonProperty("money")
    private String money;
    @JsonProperty("need_pay")
    private double needPay = -1;
    @JsonProperty("has_pay")
    private int hasPay;
    @JsonProperty("status")
    private String status;
    @JsonProperty("created_at")
    private String createdAt;
    /**
     * appointment_id : 1055
     */

    @JsonProperty("appointment_id")
    private int appointmentId;

    public void setId(int id) {
        this.id = id;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public void setHasPay(int hasPay) {
        this.hasPay = hasPay;
    }


    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public String getDrug() {
        return drug;
    }

    public String getTo() {
        return to;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getRemark() {
        return remark;
    }

    public String getMoney() {
        return money;
    }

    public int getHasPay() {
        return hasPay;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Bindable
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    public double getNeedPay() {
        return needPay;
    }

    public void setNeedPay(double needPay) {
        this.needPay = needPay;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", drug='" + drug + '\'' +
                ", to='" + to + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", remark='" + remark + '\'' +
                ", money='" + money + '\'' +
                ", hasPay=" + hasPay +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.p_item_drug;
    }

    public String getStatuses() {
        String statuses = "";
        switch (getStatus()) {
            case "normal":
                switch (getHasPay()) {
                    case 0:
                        statuses = "未支付";
                        break;
                    case 1:
                        statuses = "已支付";
                        break;
                }
                break;
            case "cancel":
                statuses = "已关闭";
                break;
        }
        return statuses;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getDetail() {
        return drug + "，邮寄地址:" + address + "，收件人:" + to + "，" + phone;
    }

    public void getCoupon() {

    }

    public void showPayMethod(Context context, String money, int id) {
        final HashMap<String, String> extraField = DrugListFragment.getDrugExtraField();
        extraField.put("drugOrderId", String.valueOf(id));

        final String totalFee;
        if (needPay > 0) {
            totalFee = String.valueOf(needPay);
            extraField.remove(DrugListFragment.COUPON_ID);
        } else {
            totalFee = money;
        }
        final AppointmentModule appointmentModule = Api.of(AppointmentModule.class);
        new PayMethodDialog(context, new PayInterface() {
            @Override
            public void payWithAlipay(Activity activity, String couponId) {
                appointmentModule.buildAlipayGoodsOrder(totalFee, "alipay", extraField).enqueue(new AlipayCallback(activity, totalFee, extraField));
            }

            @Override
            public void payWithWeChat(Activity activity, String couponId) {
                appointmentModule.buildWeChatGoodsOrder(totalFee, "wechat", extraField).enqueue(new WeChatPayCallback(activity, totalFee, extraField));
            }

            @Override
            public void simulatedPay(BaseAdapter component, View view, BaseViewHolder vh) {
                ToastHelper.showMessage(view.getContext(), "模拟支付暂时未开放");
            }
        }).show();
    }

    public void cancelOrder(Context context, int id) {
        DrugModule api = Api.of(DrugModule.class);
        api.cancelOrder(id).enqueue(new ApiCallback<String>() {
            @Override
            protected void handleResponse(String response) {

            }

            @Override
            protected void handleApi(ApiDTO<String> body) {
                setStatus("已关闭");
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
        return "<font color='" + statusColor(status) + "'>" + getStatuses() + "</font>";
    }
}
