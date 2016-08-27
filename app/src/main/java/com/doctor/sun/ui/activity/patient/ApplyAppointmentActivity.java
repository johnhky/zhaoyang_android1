package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityApplyAppointmentBinding;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;


/**
 * 确认预约
 * <p/>
 * Created by lucas on 1/22/16.
 */
public class ApplyAppointmentActivity extends BaseFragmentActivity2 {
    private PActivityApplyAppointmentBinding binding;
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

        binding.rbAlipay.setChecked(true);
        data.loadCoupons();
        data.loadTags();
    }


    @Override
    public int getMidTitle() {
        return R.string.title_confirm_order;
    }
}
