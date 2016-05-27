package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by lucas on 1/22/16.
 */
public class Drug implements LayoutId {

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

    public void setStatus(String status) {
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
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
        return drug + "，邮寄地址:" + address + "，收件人:" + to + "，" + phone ;
    }
}
