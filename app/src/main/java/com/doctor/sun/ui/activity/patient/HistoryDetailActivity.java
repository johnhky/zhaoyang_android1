package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.doctor.sun.Settings;
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
import com.doctor.sun.util.ShowCaseUtil;

/**
 * 病人端 历史纪录
 * <p/>
 * Created by lucas on 1/8/16.
 */
public class HistoryDetailActivity extends TabActivity {

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, HistoryDetailActivity.class);
        i.putExtra(Constants.DATA, appointment);
        return i;
    }

    public static Intent makeIntent(Context context, Appointment appointment, int position) {
        Intent i = new Intent(context, HistoryDetailActivity.class);
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
        return new HeaderViewModel(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getPosition() == ConsultingDetailActivity.POSITION_SUGGESTION_READONLY) {
            binding.vp.setCurrentItem(1);
        }
        showCase();
    }

    private void showCase() {
        View childAt = binding.showcase;
        if (childAt != null) {
            if (Settings.isDoctor()) {
                ShowCaseUtil.showCase(childAt, "记录病历和给患者建议和调药", "diagnosisResult", 1, 0, true);
            } else {
                ShowCaseUtil.showCase(childAt, "您可以在这里看到医生的医嘱和用药建议", "diagnosisResult", 1, 0, true);
            }
        }
    }

//    @Override
//    public void setHeaderRightTitle(String title) {
////        binding.getHeader().setRightTitle(title);
//    }

//    @Override
//    public void onCategorySelect(QuestionCategory data) {
//        FillForumFragment.getInstance(getData()).loadQuestions(data);
//    }
}
