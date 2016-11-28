package com.doctor.sun.entity;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.fragment.PayPrescriptionsFragment;
import com.doctor.sun.vo.LayoutId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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


    /**
     * status
     * 0=>'未付款'
     * 1=>'候诊中'
     * 2=>'进行中'
     * 3=>'待建议'
     * 4=>'已完成'
     * 5=>'已关闭'
     * 6=>'医生取消'
     * 7=>'问卷已锁定'
     */

    @JsonProperty("id")
    private int id;
    @JsonProperty("drug_detail")
    private List<DrugEntity> drug;
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
    @JsonProperty("drug_money")
    public String drugMoney;
    @JsonProperty("need_pay")
    private double needPay = -1;
    @JsonProperty("has_pay")
    private int hasPay;
    @JsonProperty("created_at")
    private String createdAt;
    /**
     * appointment_id : 1055
     */

    @JsonProperty("appointment_id")
    private int appointmentId;
    @JsonProperty("doctor")
    private Doctor doctor;
    @JsonProperty("charge")
    private DrugExtraFee extraFee;

    @JsonIgnore
    private String status = "";

    public void setId(int id) {
        this.id = id;
    }

    public void setDrug(List<DrugEntity> drug) {
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

    public List<DrugEntity> getDrug() {
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public DrugExtraFee getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(DrugExtraFee extraFee) {
        this.extraFee = extraFee;
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


    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    @Bindable
    public String getStatus() {
        if (status.equals("")) {
            if (hasPay == 1) {
                return "已支付";
            } else {
                return "未支付";
            }
        } else {
            return status;
        }
    }

    public String getDetail() {
        return drug + "，邮寄地址:" + address + "，收件人:" + to + "，" + phone;
    }


    public void showPayMethod(Context context, String money, Drug drug) {
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

}
