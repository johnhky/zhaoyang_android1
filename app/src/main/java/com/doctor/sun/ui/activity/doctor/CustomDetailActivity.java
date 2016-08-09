package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityCustomDetailBinding;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 12/26/15.
 */
public class CustomDetailActivity extends BaseFragmentActivity2 {
    private ActivityCustomDetailBinding binding;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, CustomDetailActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_custom_detail);
    }
    @Override
    public int getMidTitle() {
        return  R.string.title_question_description;
    }
}
