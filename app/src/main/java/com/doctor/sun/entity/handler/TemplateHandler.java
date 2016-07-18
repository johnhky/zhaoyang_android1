package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.doctor.AddTemplateActivity;
import com.doctor.sun.ui.activity.doctor.TemplateActivity;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.util.List;

/**
 * Created by lucas on 12/15/15.
 */
public class TemplateHandler {
    private QTemplate data;
    private QuestionModule api = Api.of(QuestionModule.class);
    private GetIsEditMode isEditMode;
    private boolean editStatus;

    public TemplateHandler(QTemplate qTemplate) {
        data = qTemplate;
    }

    public TemplateHandler() {

    }

    public interface GetIsEditMode {
        boolean getIsEditMode();
    }

    public void addTemplate(Context context) {
        Activity activity = (Activity) context;
        Intent intent = AddTemplateActivity.makeIntent(context, null, false);
        activity.startActivityForResult(intent, 1);
    }

    public void updateTemplate(Context context) {
        isEditMode = (GetIsEditMode) context;
        editStatus = isEditMode.getIsEditMode();
        TemplateActivity activity = (TemplateActivity) context;
        if (editStatus) {
            Intent intent = AddTemplateActivity.makeIntent(activity, data, true);
            activity.startActivityForResult(intent, 1);
        } else {
            Intent intent = AddTemplateActivity.makeIntent(activity, data, false);
            activity.startActivityForResult(intent, 1);
        }
    }


    public void showDeleteDialog(final BaseAdapter adapter, final BaseViewHolder vh) {
        String question = "确定删除该问诊模板？";
        String cancel = "取消";
        String apply = "删除";
        TwoChoiceDialog.show(adapter.getContext(), question, cancel, apply, new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(final MaterialDialog dialog) {
                api.deleteTemplate(String.valueOf(data.getId())).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        dialog.dismiss();
                        adapter.remove(vh.getAdapterPosition());
                        adapter.notifyItemRemoved(vh.getAdapterPosition());
                    }
                });
            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    public void selector(final Context context, final ImageView selector) {
        if (!selector.isSelected()) {
            TwoChoiceDialog.show(context, "是否确认添加？", "取消", "确认", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(final MaterialDialog dialog) {
                    AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) context;
                    String appointmentId = getAppointmentId.getAppointmentId();
                    api.appendTemplate(appointmentId, String.valueOf(data.getId())).enqueue(new ApiCallback<List<Answer>>() {
                        @Override
                        protected void handleResponse(List<Answer> response) {
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
}
