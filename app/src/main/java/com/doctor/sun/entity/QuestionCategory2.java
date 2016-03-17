package com.doctor.sun.entity;

import com.doctor.sun.R;
import com.doctor.sun.ui.handler.QuestionCategoryHandle;

/**
 * Created by rick on 11/28/15.
 */
public class QuestionCategory2 extends QuestionCategory {
    private QuestionCategoryHandle handler = new QuestionCategoryHandle(this);

    public QuestionCategoryHandle getHandler() {
        return handler;
    }

    public void setHandler(QuestionCategoryHandle handler) {
        this.handler = handler;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_question_category2;
    }
}
