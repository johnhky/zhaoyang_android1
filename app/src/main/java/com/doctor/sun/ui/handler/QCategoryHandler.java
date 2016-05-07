package com.doctor.sun.ui.handler;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.doctor.sun.R;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.TwoSelectorDialog;

import java.util.List;

/**
 * Created by rick on 11/26/15.
 */
public class QCategoryHandler {
    private QuestionCategory data;

    public QCategoryHandler(QuestionCategory data) {
        this.data = data;
    }

    public View.OnClickListener select(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    QCategoryCallback callback = (QCategoryCallback) context;
                    callback.onCategorySelect(data);
                } catch (ClassCastException e) {
                    throw new IllegalArgumentException("Host activity must implement QCategoryCallback");
                }
            }
        };
    }

    public OnItemClickListener selector() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                final ImageView selector = (ImageView) view.findViewById(R.id.iv_select);
                if (!selector.isSelected()) {
                    AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) view.getContext();
                    final String appointmentId = getAppointmentId.getAppointmentId();
                    TwoSelectorDialog.showTwoSelectorDialog(view.getContext(), "是否确认添加？", "取消", "确认", new TwoSelectorDialog.GetActionButton() {
                        @Override
                        public void onClickPositiveButton(final TwoSelectorDialog dialog) {
                            QuestionModule api = Api.of(QuestionModule.class);
                            api.appendScale(appointmentId, String.valueOf(data.getId())).enqueue(new ApiCallback<List<Answer>>() {
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

    public interface QCategoryCallback {
        void onCategorySelect(QuestionCategory questionCategoryId);
    }

}
