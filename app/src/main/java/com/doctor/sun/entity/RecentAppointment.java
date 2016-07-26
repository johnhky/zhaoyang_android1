package com.doctor.sun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by rick on 12/9/15.
 */
public class RecentAppointment {
    private int id;
    private String doctor_name;
    private String book_time;
    private String take_time;
    private String progress;
    private List<Object> return_info;
    /**
     * visit_time : 1469931600
     * end_time : 0
     * cancel_reason :
     * has_pay : 1
     * is_finish : 0
     * status : 1
     * order_status : 已付款
     */

    @JsonProperty("visit_time")
    private String visitTime;
    @JsonProperty("end_time")
    private String endTime;
    @JsonProperty("cancel_reason")
    private String cancelReason;
    @JsonProperty("has_pay")
    private String hasPay;
    @JsonProperty("is_finish")
    private String isFinish;
    @JsonProperty("status")
    private String status;
    @JsonProperty("order_status")
    private String orderStatus;


    public void setId(int id) {
        this.id = id;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public void setBook_time(String book_time) {
        this.book_time = book_time;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public void setReturn_info(List<Object> return_info) {
        this.return_info = return_info;
    }

    public int getId() {
        return id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getBook_time() {
        return book_time;
    }

    public String getProgress() {
        return progress;
    }

    public String getTake_time() {
        return take_time;
    }

    public void setTake_time(String take_time) {
        this.take_time = take_time;
    }

    public List<Object> getReturn_info() {
        return return_info;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getHasPay() {
        return hasPay;
    }

    public void setHasPay(String hasPay) {
        this.hasPay = hasPay;
    }

    public String getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
}
