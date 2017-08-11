package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.DoctorAfterServiceDonePA;
import com.doctor.sun.ui.pager.PatientAfterServiceDonePA;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 3/6/2016.
 * 不可修改医生建议
 */
public class AfterServiceDoneActivity extends TabActivity {
    public static Intent intentFor(Context context, String orderId, int position) {
        Intent intent = new Intent(context, AfterServiceDoneActivity.class);
        intent.putExtra(Constants.DATA, orderId);
        intent.putExtra(Constants.POSITION, position);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addHistoryButton();
    }

    private void addHistoryButton() {
        if (Settings.isDoctor()) {
            View historyButton = LayoutInflater.from(this).inflate(R.layout.item_fab_view_history, binding.flContainer, false);
            historyButton.findViewById(R.id.btn_appointment_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = getStringExtra(Constants.DATA);
                    AppointmentModule api = Api.of(AppointmentModule.class);
                    api.appointmentDetail(id).enqueue(new SimpleCallback<Appointment>() {
                        @Override
                        protected void handleResponse(Appointment response) {
                            Config.putInt(Constants.CREATE_SUCCESS,1);
                            Config.putString(Constants.ADDRESS,response.getId()+"");
                            Config.putInt(Constants.APPOINTMENT_MONEY,response.getStatus());
                            EventHub.post(new AppointmentHistoryEvent(response, false));

                        }
                    });
                }
            });
            binding.flContainer.addView(historyButton);
        }
    }



    @Override
    protected PagerAdapter createPagerAdapter() {
        if (Settings.isDoctor()) {
            return new DoctorAfterServiceDonePA(getSupportFragmentManager(), getData());
        } else {
            return new PatientAfterServiceDonePA(getSupportFragmentManager(), getData());
        }
    }


}
