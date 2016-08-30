package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.ui.fragment.ReadQuestionFragment;


/**
 * Created by rick on 11/24/15.
 */
public class ReadQuestionActivity extends BaseFragmentActivity2 {

    private ReadQuestionFragment instance;

    public static Intent intentFor(Context context, String scalesId, boolean isDone) {
        Intent i = new Intent(context, ReadQuestionActivity.class);
        i.putExtra(Constants.DATA, scalesId);
        i.putExtra(Constants.READ_ONLY, isDone);
        return i;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_fragment_wraper);


        String intExtra = getIntent().getStringExtra(Constants.DATA);
        instance = ReadQuestionFragment.getInstance(intExtra, QuestionsPath.SCALES, getBooleanExtra(Constants.READ_ONLY, true));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fly_content, instance)
                .commit();
    }
}
