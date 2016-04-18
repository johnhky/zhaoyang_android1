package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityFillForumBinding;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.fragment.FillForumFragment;
import com.doctor.sun.ui.fragment.ModifyForumFragment;
import com.doctor.sun.ui.handler.QCategoryHandler;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * 填写问卷 只读 fragment
 * Created by rick on 25/1/2016.
 */
public class FillForumActivity extends BaseFragmentActivity2 implements QCategoryHandler.QCategoryCallback, FillForumFragment.SetHeaderListener {

    private boolean isFilling;

    private ActivityFillForumBinding binding;
    private ModifyForumFragment fragment;

    public static Intent makeIntent(Context context, int appointmentId) {
        Intent i = new Intent(context, FillForumActivity.class);
        i.putExtra(Constants.DATA, appointmentId);
        return i;
    }


    private int getData() {
        return getIntent().getIntExtra(Constants.DATA, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fill_forum);
        initHeader();
        initFragment();
    }

    private void initFragment() {
        fragment = ModifyForumFragment.getInstance(getData());
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.lly_content, fragment)
                .commit();
    }

    private void initHeader() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("填写问卷");
        binding.setHeader(header);
    }

    @Override
    public void onCategorySelect(QCategoryHandler data) {
        binding.getHeader().setRightTitle("保存");
        fragment.loadQuestions(data);
        isFilling = true;
    }

    @Override
    public void onMenuClicked() {
        if (isFilling) {
            fragment.save();
        }
    }

    @Override
    public void setHeaderRightTitle(String title) {
        binding.getHeader().setRightTitle(title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.DOCTOR_REQUEST_CODE:
            case Constants.PRESCRITION_REQUEST_CODE:
                break;
            case Constants.PATIENT_PRESCRITION_REQUEST_CODE:
                ModifyForumFragment.getInstance(getData()).handleResult(requestCode, resultCode, data);
                break;
            case Constants.UPLOAD_REQUEST_CODE:
            case Constants.UPLOAD_REQUEST_CODE / 2:
                ModifyForumFragment.getInstance(getData()).handleImageResult(requestCode, resultCode, data);
                break;
        }
    }
}
