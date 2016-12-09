package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.HistoryDetailAdapter;
import com.doctor.sun.util.JacksonUtils;

/**
 * 病人端 历史纪录
 * <p/>
 * Created by lucas on 1/8/16.
 */
public class FinishedOrderActivity extends TabActivity {

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, FinishedOrderActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(appointment));
        return i;
    }

    public static Intent makeIntent(Context context, Appointment appointment, int position) {
        Intent i = new Intent(context, FinishedOrderActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(appointment));
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    private Appointment getData() {
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
    }

    @Override
    public int getPosition() {
        return getIntent().getIntExtra(Constants.POSITION, AppointmentDetailActivity.POSITION_ANSWER);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new HistoryDetailAdapter(getSupportFragmentManager(), getData());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPosition() == AppointmentDetailActivity.POSITION_SUGGESTION_READONLY) {
            binding.vp.setCurrentItem(1);
        }
    }

    public void onFirstMenuClicked() {
        Intent intent = MedicineStoreActivity.makeIntent(this);
        startActivity(intent);
    }

    public void onMenuClicked() {
        AppointmentHandler2.chatNoMenu(FinishedOrderActivity.this, getData());
    }

//    @Override
//    public void onCategorySelect(QuestionCategory data) {
//        ReadQuestionsFragment.getInstance(getData()).loadQuestions(data);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Settings.isDoctor()) {
            getMenuInflater().inflate(R.menu.menu_chat, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_appointment_history, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_chat: {
                onMenuClicked();
                return true;
            }
            case R.id.action_medicine_store: {
                onFirstMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}
