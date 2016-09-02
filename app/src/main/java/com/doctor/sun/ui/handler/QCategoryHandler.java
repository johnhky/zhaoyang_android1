package com.doctor.sun.ui.handler;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

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

    public void showAppendDialog(Context context, final ImageView selector) {
        if (!selector.isSelected()) {
            AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) context;
            final String appointmentId = getAppointmentId.getAppointmentId();
            TwoChoiceDialog.show(context, "是否确认添加？", "取消", "确认", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(final MaterialDialog dialog) {
                    QuestionModule api = Api.of(QuestionModule.class);
                    api.appendScale(appointmentId, String.valueOf(data.getId())).enqueue(new ApiCallback<List<String>>() {
                        @Override
                        protected void handleResponse(List<String> response) {
                            selector.setSelected(true);
                            dialog.dismiss();
                        }
                    });
                }

                @Override
                public void onCancelClick(MaterialDialog dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    public interface QCategoryCallback {
        void onCategorySelect(QuestionCategory questionCategoryId);
    }

}
