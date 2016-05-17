package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityPaySuccessBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.EmergencyCall;
import com.doctor.sun.ui.activity.BaseActivity2;

import io.ganguo.library.AppManager;

/**
 * Created by lucas on 1/23/16.
 */
public class PaySuccessActivity extends BaseActivity2 implements View.OnClickListener {
    public static final int URGENT_CALL = 1;
    public static final int APPOINTMENT = 2;
    public static final int VOIP_PAY = 3;

    private PActivityPaySuccessBinding binding;

    public static Intent makeIntent(Context context, Appointment data) {
        Intent i = new Intent(context, PaySuccessActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.TYPE, APPOINTMENT);
        return i;
    }

    public static Intent makeIntent(Context context, EmergencyCall data) {
        Intent i = new Intent(context, PaySuccessActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.TYPE, URGENT_CALL);
        return i;
    }

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PaySuccessActivity.class);
        i.putExtra(Constants.TYPE, VOIP_PAY);
        return i;
    }

    private Appointment getAppointment() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private EmergencyCall getUrgentCall() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private int getType() {
        return getIntent().getIntExtra(Constants.TYPE, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_pay_success);
        binding.tvMain.setOnClickListener(this);
        binding.tvQuestion.setOnClickListener(this);
        if (getType() == VOIP_PAY) {
            binding.tvSystemTip.setVisibility(View.GONE);
            binding.tvQuestion.setVisibility(View.GONE);
            binding.tvTip.setVisibility(View.GONE);
        } else {
            setBookTime();
        }
    }

    private void setBookTime() {
        switch (getType()) {
            case URGENT_CALL: {
                String bookTime = getUrgentCall().getBookTime().substring(0, getUrgentCall().getBookTime().length() - 12);
                binding.setData(bookTime);
                break;
            }
            case APPOINTMENT: {
                String bookTime = getAppointment().getBookTime().substring(0, getAppointment().getBookTime().length() - 12);
                binding.setData(bookTime);
                break;
            }
            default: {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main: {
                Intent intent = MainActivity.makeIntent(this);
                startActivity(intent);
                AppManager.finishAllActivity();
                break;
            }
            case R.id.tv_question: {
                //TODO
                int id = getId();
                if (id != -1) {
                    finish();
                    Intent intent = FillForumActivity.makeIntent(this, id);
                    startActivity(intent);
                }
                break;
            }
        }
    }

    private int getId() {
        int id = -1;
        switch (getType()) {
            case URGENT_CALL: {
                id = getUrgentCall().getId();
                break;
            }
            case APPOINTMENT: {
                id = getAppointment().getId();
                break;
            }
            default: {

            }
        }
        return id;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = MainActivity.makeIntent(this);
        startActivity(intent);
        AppManager.finishAllActivity();

    }
}
