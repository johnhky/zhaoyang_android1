package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityApplyAppointmentBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.constans.CouponType;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.SelectRecordDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * 确认预约
 * <p/>
 * Created by lucas on 1/22/16.
 */
public class ApplyAppointmentActivity extends BaseActivity2 {
    private PActivityApplyAppointmentBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);
    private AppointmentModule appointmentModule = Api.of(AppointmentModule.class);
    private MedicalRecord record;
    private List<Coupon> response;
    private List<Coupon> coupons;

    public static Intent makeIntent(Context context, Doctor doctor, String bookTime, String type, String recordId) {
        Intent i = new Intent(context, ApplyAppointmentActivity.class);
        i.putExtra(Constants.DOCTOR, doctor);
        i.putExtra(Constants.BOOKTIME, bookTime);
        i.putExtra(Constants.TYPE, type);
        i.putExtra(Constants.RECORDID, recordId);
        return i;
    }

    private Doctor getDoctorData() {
        return getIntent().getParcelableExtra(Constants.DOCTOR);
    }

    private String getBookTime() {
        return getIntent().getStringExtra(Constants.BOOKTIME);
    }

    private String getType() {
        return getIntent().getStringExtra(Constants.TYPE);
    }

    private String getRecordId() {
        return getIntent().getStringExtra(Constants.RECORDID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_apply_appointment);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("确认预约");
        binding.setHeader(header);
        api.recordDetail(getRecordId()).enqueue(new ApiCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                binding.tvMedcialRecord.setText(response.getHandler().getRecordDetail());
                record = response;
            }
        });
        Doctor doctorData = getDoctorData();
        doctorData.setType(Integer.parseInt(getType()));
        binding.setData(doctorData);
        binding.tvTime.setText(String.format("预约时间：%s", getBookTime()));
        binding.tvType.setText(String.format("预约类型：%s", appointmentType()));
        binding.rbAlipay.setChecked(true);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date parse = null;
        try {
            parse = format.parse(getBookTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        final String time;
        if (parse != null) {
            time = String.valueOf(parse.getTime()).substring(0, 10);
        } else {
            time = "";
        }
        Log.e(TAG, "onCreate: time" + time);
        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Doctor doctorData = getDoctorData();
                if (record != null) {
                    doctorData.setRecordId(String.valueOf(record.getMedicalRecordId()));
                }
                String doctorId = String.valueOf(doctorData.getId());
                int type = Integer.parseInt(getType());
                //noinspection WrongConstant
                appointmentModule.orderAppointment(doctorId, time, type, doctorData.getRecordId()).enqueue(new ApiCallback<Appointment>() {
                    @Override
                    protected void handleResponse(Appointment response) {
                        response.setRecordId(Integer.parseInt(doctorData.getRecordId()));
                        AppointmentHandler handler = new AppointmentHandler(response);
                        if (binding.cbCouponCount.isSelected()) {
                            handler.couponId = coupons.get(0).getId();
                        }
                        if (binding.rbWechat.isChecked()) {
                            handler.payWithWeChat(ApplyAppointmentActivity.this);
                        } else {
                            handler.payWithAlipay(ApplyAppointmentActivity.this);
                        }
                    }
                });
            }
        });
        binding.llyMedicalRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SelectRecordDialog.showRecordDialog(ApplyAppointmentActivity.this, new SelectRecordDialog.SelectRecordListener() {
                    @Override
                    public void onSelectRecord(SelectRecordDialog dialog, MedicalRecord selected) {
                        binding.tvMedcialRecord.setText(selected.getHandler().getRecordDetail());

                        record = selected;
                        dialog.dismiss();
                    }
                });
            }
        });
        api.coupons(CouponType.CAN_USE_NOW).enqueue(new SimpleCallback<List<Coupon>>() {
            @Override
            protected void handleResponse(List<Coupon> response) {
                if (response != null && !response.isEmpty()) {
                    coupons = response;
                    binding.llyCoupon.setVisibility(View.VISIBLE);
                    binding.cbCouponCount.setText(String.format(Locale.CHINA, "您有%d张可用优惠券", response.size()));
                }
            }
        });
    }

    @NonNull
    private String appointmentType() {
        String type = "";
        switch (getType()) {
            case "1":
                type = "详细咨询";
                break;
            case "2":
                type = "简捷复诊";
                break;
        }
        return type;
    }
}
