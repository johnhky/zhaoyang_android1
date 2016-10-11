package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityViewFeedbackBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;


/**
 * Created by rick on 12/17/15.
 */
public class ViewFeedbackActivity extends BaseFragmentActivity2 {
    private PActivityViewFeedbackBinding binding;

    public static Intent intentFor(Context context, Doctor doctor) {
        Intent intent = new Intent(context, ViewFeedbackActivity.class);
        intent.putExtra(Constants.DATA, doctor);
        return intent;
    }

    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_view_feedback);

        binding.setData(getData());


    }

    @Override
    public int getMidTitle() {
        return R.string.title_feed_back;
    }
}