package com.doctor.sun.ui.adapter;

import android.content.Context;

import com.doctor.sun.R;

/**
 * Created by lucas on 12/25/15.
 */
public class QuestionCustomAdapter extends SimpleAdapter {
    public QuestionCustomAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_question_bank;
    }
}
