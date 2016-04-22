package com.doctor.sun.entity.handler;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Question;
import com.doctor.sun.ui.activity.doctor.EditPrescriptionActivity;
import com.doctor.sun.ui.adapter.AnswerModifyAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.fragment.DiagnosisFragment;

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

    public boolean drugCountGreaterThen(Answer answer, int count) {
        return answer.getPrescriptions().size() < count;
    }

    public View.OnClickListener addDrug(final AnswerModifyAdapter adapter, final RecyclerView.ViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EditPrescriptionActivity.makeIntent(v.getContext(), null);
                Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        switch (msg.what) {
                            case DiagnosisFragment.EDIT_PRESCRITPION: {
                                Prescription parcelable = msg.getData().getParcelable(Constants.DATA);
                                Answer answer = (Answer) adapter.get(vh.getAdapterPosition());
                                answer.getPrescriptions().add(parcelable);
                                adapter.set(vh.getAdapterPosition(), answer);
                                adapter.notifyItemChanged(vh.getAdapterPosition());
                            }
                        }
                        return false;
                    }
                }));
                intent.putExtra(Constants.HANDLER, messenger);
                v.getContext().startActivity(intent);
            }
        };
    }
}
