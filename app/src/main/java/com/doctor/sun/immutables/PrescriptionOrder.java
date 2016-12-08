package com.doctor.sun.immutables;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.adapter.MapLayoutIdInterceptor;
import com.doctor.sun.ui.fragment.PayPrescriptionsFragment;
import com.doctor.sun.vo.BaseItem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.immutables.value.Value;

import java.util.List;

import static com.doctor.sun.immutables.PrescriptionOrder.Status.EXPIRED;
import static com.doctor.sun.immutables.PrescriptionOrder.Status.GENERATING;
import static com.doctor.sun.immutables.PrescriptionOrder.Status.PROCESSING;
import static com.doctor.sun.immutables.PrescriptionOrder.Status.USED;
import static com.doctor.sun.immutables.PrescriptionOrder.Status.WAITING_PAYMENT;

/**
 * Created by rick on 30/11/2016.
 */
@Value.Immutable
@Value.Style(jdkOnly = true)
@JsonSerialize(as = ImmutablePrescriptionOrder.class)
@JsonDeserialize(as = ImmutablePrescriptionOrder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class PrescriptionOrder extends BaseItem {

    public abstract String getAppointment_id();

    @Value.Default
    public String getDrug_order_id(){
        return "";
    }

    public abstract String getRecord_id();

    public abstract String getDoctor_id();

    public abstract String getType();

    public abstract String getBook_time();

    public abstract String getVisit_time();

    public abstract String getEnd_time();

    public abstract int getStatus();

    public abstract String getDisplay_type();

    public abstract String getDiagnosis_time();

    public abstract String getExpire_date();

    public abstract int getPrescription_status();

    public abstract String getPrescription_display_status();

    public abstract String getTime_bucket();

    public abstract Doctor getDoctor();

    public abstract MedicalRecord getRecord();

    public abstract List<Prescription> getPrescription();

    @JsonIgnore
    @Value.Default
    @Override
    public int getItemLayoutId() {
        return R.layout.item_drug_order;
    }


    public String getStatusBackground() {
        switch (getPrescription_status()) {
            //灰色
            case EXPIRED:
            case USED: {
                return "#cdd4dd";
            }
            //天空蓝
            case GENERATING:
            case PROCESSING: {
                return "#8fdbe6";
            }
            //黄色
            case WAITING_PAYMENT: {
                return "#ffae00";
            }
            default: {
                return "#cdd4dd";
            }

        }
    }

    public MapLayoutIdInterceptor idInterceptor() {
        MapLayoutIdInterceptor mapLayoutIdInterceptor = new MapLayoutIdInterceptor();
        mapLayoutIdInterceptor.put(R.layout.item_prescription, R.layout.item_r_prescription_simple);
        return mapLayoutIdInterceptor;
    }

    @interface Status {
        //未生成订单
        int GENERATING = 0;
        //处方已使用
        int USED = 1;
        //订单处理中
        int PROCESSING = 2;
        //待用户支付
        int WAITING_PAYMENT = 3;
        //过期
        int EXPIRED = 4;
    }

    public void viewPrescriptionDetail(Context context) {
        if (getPrescription_status() == Status.WAITING_PAYMENT) {
            Bundle args = PayPrescriptionsFragment.getArgs(getDrug_order_id());
            Intent intent = SingleFragmentActivity.intentFor(context, "寄药支付", args);
            context.startActivity(intent);
        }
    }
}
