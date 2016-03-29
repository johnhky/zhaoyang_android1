package com.doctor.sun.entity.handler;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.doctor.sun.R;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.doctor.CustomDetailActivity;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.TwoSelectorDialog;

import java.util.List;

/**
 * Created by lucas on 12/17/15.
 */
public class QuestionHandler {
    private Question data;
    private QuestionModule api = Api.of(QuestionModule.class);

    public QuestionHandler(Question question) {
        data = question;
    }

    public QuestionHandler(){
    }

    public OnItemClickListener select() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                data.setIsSelected(!data.getIsSelected());
                view.setSelected(data.getIsSelected());
            }
        };
    }

    public void customQuestionDetail(View view) {
        Intent intent = CustomDetailActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public OnItemClickListener selector() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, final View view, BaseViewHolder vh) {
                final ImageView selector = (ImageView) view.findViewById(R.id.iv_select);
                if (!selector.isSelected()) {
                    TwoSelectorDialog.showTwoSelectorDialog(view.getContext(), "是否确认添加？", "取消", "确认", new TwoSelectorDialog.GetActionButton() {
                        @Override
                        public void onClickPositiveButton(final TwoSelectorDialog dialog) {
                            AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) view.getContext();
                            String appointmentId = getAppointmentId.getAppointmentId();
                            api.appendQuestion(appointmentId, String.valueOf(data.getId())).enqueue(new ApiCallback<List<Answer>>() {
                                @Override
                                protected void handleResponse(List<Answer> response) {
                                    selector.setSelected(true);
                                    dialog.dismiss();
                                }
                            });
                        }

                        @Override
                        public void onClickNegativeButton(TwoSelectorDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        };
    }
}
