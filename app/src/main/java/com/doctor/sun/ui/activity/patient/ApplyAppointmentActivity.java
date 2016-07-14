package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityApplyAppointmentBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


/**
 * 确认预约
 * <p/>
 * Created by lucas on 1/22/16.
 */
public class ApplyAppointmentActivity extends BaseActivity2 {
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm:ss";

    private PActivityApplyAppointmentBinding binding;
    private ProfileModule api = Api.of(ProfileModule.class);
    private AppointmentModule appointmentModule = Api.of(AppointmentModule.class);
    private List<Coupon> coupons;
    private AppointmentBuilder data;

    public static Intent makeIntent(Context context, AppointmentBuilder builder) {
        Intent i = new Intent(context, ApplyAppointmentActivity.class);
        i.putExtra(Constants.DATA, builder);
        return i;
    }

    private AppointmentBuilder getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getData();
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_apply_appointment);
        binding.setData(data);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("确认预约");
        binding.setHeader(header);
        binding.tvMedcialRecord.setText(data.getRecord().getHandler().getRecordDetail());
        data.setRecord(data.getRecord());
        Time time = data.getTime();
        StringBuilder builder = new StringBuilder();
        builder.append(time.getDate());
        if (data.getType() == AppointmentType.DETAIL) {
            builder.append(" ").append(time.getFrom()).append("-").append(time.getTo());
        }
        binding.tvTime.setText(String.format("预约时间:%s", builder));
        binding.tvType.setText(String.format("预约类型:%s", appointmentType()));
        binding.rbAlipay.setChecked(true);
        String dateFormat = YYYY_MM_DD_HH_MM;
        if (data.getType() == AppointmentType.QUICK) {
            if (!data.isToday()) {
                data.getTime().setFrom("00:00:00");
            } else {
                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
                String hours = format.format(date);
                data.getTime().setFrom(hours);
            }
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat, Locale.CHINA);
        Date parse = null;
        try {
            parse = format.parse(time.getDate() + " " + time.getFrom());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        final String timestamp;
        if (parse != null) {
            timestamp = String.valueOf(parse.getTime()).substring(0, 10);
        } else {
            timestamp = "";
        }
        binding.tvApplyAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int doctorId = data.getDoctor().getId();
                int type = data.getType();
                String couponId = "";
                if (binding.cbCouponCount.isChecked() && coupons != null && !coupons.isEmpty()) {
                    couponId = coupons.get(0).getId();
                }
                final String finalCouponId = couponId;
                HashMap<String, String> params = new HashMap<String, String>();
                if (type == AppointmentType.DETAIL) {
                    params.put("takeTime", String.valueOf(data.getDuration()));
                }
                final String medicalRecordId = String.valueOf(data.getRecord().getMedicalRecordId());
                //noinspection WrongConstant
                appointmentModule.orderAppointment(doctorId, timestamp, type, medicalRecordId, couponId, params).enqueue(new ApiCallback<Appointment>() {
                    @Override
                    protected void handleResponse(Appointment response) {
                        response.setRecordId(Integer.parseInt(medicalRecordId));
                        AppointmentHandler handler = new AppointmentHandler(response);

                        if (binding.rbWechat.isChecked()) {
                            handler.payWithWeChat(ApplyAppointmentActivity.this, finalCouponId);
                        } else {
                            handler.payWithAlipay(ApplyAppointmentActivity.this, finalCouponId);
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

                        data.setRecord(selected);
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
                    binding.cbCouponCount.setText(String.format(Locale.CHINA, "您有%d张可用优惠券", response.size()));
                } else {
                    coupons = null;
                    binding.cbCouponCount.setText("您暂时没有可用优惠券");
                    binding.cbCouponCount.setEnabled(false);
                }
            }
        });

        binding.cbCouponCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (coupons == null) {
                    return;
                }

                if (isChecked) {
                    binding.cbCouponCount.setText("使用一张优惠券");
                } else {
                    binding.cbCouponCount.setText(String.format(Locale.CHINA, "您有%d张可用优惠券", coupons.size()));
                }
            }
        });
    }

    @NonNull
    private String appointmentType() {
        String type = "";
        switch (data.getType()) {
            case AppointmentType.DETAIL:
                type = "专属咨询";
                break;
            case AppointmentType.QUICK:
                type = "留言咨询";
                break;
        }
        return type;
    }
}
