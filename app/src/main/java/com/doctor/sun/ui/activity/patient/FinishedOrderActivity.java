package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.activity.doctor.ConsultingDetailActivity;
import com.doctor.sun.ui.handler.QCategoryHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.HistoryDetailAdapter;

/**
 * 病人端 历史纪录
 * <p/>
 * Created by lucas on 1/8/16.
 */
public class FinishedOrderActivity extends TabActivity
        implements QCategoryHandler.QCategoryCallback {

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, FinishedOrderActivity.class);
        i.putExtra(Constants.DATA, appointment);
        return i;
    }

    public static Intent makeIntent(Context context, Appointment appointment, int position) {
        Intent i = new Intent(context, FinishedOrderActivity.class);
        i.putExtra(Constants.DATA, appointment);
        i.putExtra(Constants.POSITION, position);
        return i;
    }

    private Appointment getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public int getPosition() {
        return getIntent().getIntExtra(Constants.POSITION, ConsultingDetailActivity.POSITION_ANSWER);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new HistoryDetailAdapter(getSupportFragmentManager(), getData());
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPosition() == ConsultingDetailActivity.POSITION_SUGGESTION_READONLY) {
            binding.vp.setCurrentItem(1);
        }
    }

    public void onFirstMenuClicked() {
        Intent intent = MedicineStoreActivity.makeIntent(this);
        startActivity(intent);
    }

    public void onMenuClicked() {
        getData().getHandler().chatNoMenu(FinishedOrderActivity.this);
    }

    @Override
    public void onCategorySelect(QuestionCategory data) {
//        ReadQuestionFragment.getInstance(getData()).loadQuestions(data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appointment_history, menu);
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
