package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.fragment.QuestionCustomFragment;
import com.doctor.sun.ui.fragment.QuestionDefaultFragment;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.QuestionBankPagerAdapter;

import java.util.ArrayList;

/**
 * Created by lucas on 12/24/15.
 */
public class QuestionActivity extends TabActivity implements QuestionCustomFragment.GetOldData, QuestionDefaultFragment.GetOldData {

    public static Intent makeIntent(Context context, QTemplate data, ArrayList<String> oldQuestionId, String templateName) {
        Intent i = new Intent(context, QuestionActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putStringArrayListExtra(Constants.QUESTION_ID, oldQuestionId);
        i.putExtra(Constants.TEMPLATE_NAME, templateName);
        return i;
    }

    private QTemplate getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private ArrayList<String> getQuestionId() {
        return getIntent().getStringArrayListExtra(Constants.QUESTION_ID);
    }

    private String newTemplateName() {
        return getIntent().getStringExtra(Constants.TEMPLATE_NAME);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new QuestionBankPagerAdapter(getSupportFragmentManager());
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setRightTitle("添加自定义题目");
        return header;
    }

    @Override
    public void onMenuClicked() {
        Intent intent = AddQuestionActivity.makeIntent(this);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public QTemplate getOldData() {
        return getData();
    }

    @Override
    public ArrayList<String> getOldQuestionId() {
        return getQuestionId();
    }

    @Override
    public String getTemplateName() {
        return newTemplateName();
    }
}
