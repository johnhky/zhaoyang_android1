package com.doctor.sun.entity.handler;

import android.util.SparseBooleanArray;

import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Question;
import com.doctor.sun.ui.adapter.MultiSelectAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.SingleSelectAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 29/3/2016.
 */
public class AnswerHandler {

    public boolean isPills(Answer data) {
        return data.getQuestion().getQuestionType().equals(Question.TYPE_PILLS);
    }

    public boolean isFill(Answer data) {
        return data.getQuestion().getQuestionType().equals(Question.TYPE_FILL);
    }

    public boolean isCheckbox(Answer data) {
        return data.getQuestion().getQuestionType().equals(Question.TYPE_CHECKBOX);
    }

    public boolean isRadio(Answer data) {
        return data.getQuestion().getQuestionType().equals(Question.TYPE_RADIO);
    }

    public boolean isUpload(Answer data) {
        return data.getQuestion().getQuestionType().equals(Question.TYPE_UPLOADS);
    }

    public SimpleAdapter getOptions(final BaseAdapter rootAdapter, BaseViewHolder viewHolder, final Answer data) {
//        SimpleAdapter simpleAdapter = null;
//        final int position = viewHolder.getAdapterPosition();
//        if (isPills(data)) {
//            simpleAdapter = new SimpleAdapter(rootAdapter.getContext());
//            simpleAdapter.addAll(data.getPrescriptions());
//            simpleAdapter.onFinishLoadMore(true);
//        } else if (isRadio(data)) {
//            simpleAdapter = new SingleSelectAdapter(rootAdapter.getContext(), new SingleSelectAdapter.OnSelectionChange() {
//                @Override
//                public void onSelectionChange(BaseAdapter adapter, int newSelectItem) {
//                    data.setSelectedOptions(newSelectItem);
//                    rootAdapter.set(position, data);
//                }
//            },data.getSelectedOptions());
//            simpleAdapter.addAll(data.getQuestion().getOptions());
//            simpleAdapter.onFinishLoadMore(true);
//        } else if (isCheckbox(data)) {
//            simpleAdapter = new MultiSelectAdapter(rootAdapter.getContext(), new MultiSelectAdapter.OnSelectionChange() {
//                @Override
//                public void onSelectionChange(BaseAdapter adapter, SparseBooleanArray selectedItems) {
//                    data.setSelectedOptions(selectedItems);
//                    rootAdapter.set(position, data);
//                }
//            },data.getSelectedOptions());
//            simpleAdapter.addAll(data.getQuestion().getOptions());
//            simpleAdapter.onFinishLoadMore(true);
//        }

        return null;
    }
}
