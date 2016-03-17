package com.doctor.sun.ui.handler;

import android.view.View;

import com.doctor.sun.entity.QuestionCategory;

/**
 * Created by rick on 11/26/15.
 */
public class QCategoryHandler extends QuestionCategory {
    public void select(View view) {
        try {
            QCategoryCallback callback = (QCategoryCallback) view.getContext();
            callback.onCategorySelect(this);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Host activity must implement QCategoryCallback");
        }
    }

    public interface QCategoryCallback {
        void onCategorySelect(QCategoryHandler questionCategoryId);
    }

}
