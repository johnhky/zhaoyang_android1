package com.doctor.sun.ui.adapter;

import android.content.Context;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;

/**
 * Created by lucas on 12/25/15.
 */
public class QuestionDefaultAdapter extends SimpleAdapter{
    public QuestionDefaultAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(int position) {
        return R.layout.item_question_bank;
    }
}
