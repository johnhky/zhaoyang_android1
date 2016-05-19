package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.activity.doctor.ConsultingDetailActivity;
import com.doctor.sun.ui.fragment.FillForumFragment;
import com.doctor.sun.ui.fragment.ListFragment;
import com.doctor.sun.ui.handler.QCategoryHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.HistoryDetailAdapter;

/**
 * 病人端 历史纪录
 * <p>
 * Created by lucas on 1/8/16.
 */
public class FinishedOrderActivity extends TabActivity
        implements ListFragment.SetHeaderListener, QCategoryHandler.QCategoryCallback {

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

    private int getPosition() {
        return getIntent().getIntExtra(Constants.POSITION, ConsultingDetailActivity.POSITION_ANSWER);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new HistoryDetailAdapter(getSupportFragmentManager(), getData());
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setRightFirstTitle("寄药小助手");
        headerViewModel.setRightTitle("即时聊天");
        return headerViewModel;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPosition() == ConsultingDetailActivity.POSITION_SUGGESTION_READONLY) {
            binding.vp.setCurrentItem(1);
        }
    }

    @Override
    public void setHeaderRightTitle(String title) {
        binding.getHeader().setRightTitle(title);
    }

    @Override
    public void onFirstMenuClicked() {
        Intent intent = MedicineHelperActivity.makeIntent(this);
        startActivity(intent);
    }

    @Override
    public void onMenuClicked() {
        getData().getHandler().chat().onClick(binding.getRoot());
    }

    @Override
    public void onCategorySelect(QuestionCategory data) {
        FillForumFragment.getInstance(getData()).loadQuestions(data);
    }
}
