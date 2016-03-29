package com.doctor.sun.entity.handler;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.doctor.AddTemplateActivity;
import com.doctor.sun.ui.activity.doctor.TemplateActivity;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.TwoSelectorDialog;

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

    public TemplateHandler(){

    }

    public interface GetIsEditMode {
        boolean getIsEditMode();
    }

    public OnItemClickListener addTemplate() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                TemplateActivity context = (TemplateActivity) view.getContext();
                Intent intent = AddTemplateActivity.makeIntent(context, null, false);
                context.startActivityForResult(intent, 1);
            }
        };
    }

    public OnItemClickListener updateTemplate() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                isEditMode = (GetIsEditMode) view.getContext();
                editStatus = isEditMode.getIsEditMode();
                TemplateActivity context = (TemplateActivity) view.getContext();
                if (editStatus) {
                    Intent intent = AddTemplateActivity.makeIntent(context, data, true);
                    context.startActivityForResult(intent, 1);
                } else {
                    Intent intent = AddTemplateActivity.makeIntent(context, data, false);
                    context.startActivityForResult(intent, 1);
                }
            }
        };
    }

    public OnItemClickListener deleteDialog() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, View view, final BaseViewHolder vh) {
                String question = "确定删除该免模板？";
                String cancel = "取消";
                String apply = "删除";
                TwoSelectorDialog.showTwoSelectorDialog(view.getContext(), question, cancel, apply, new TwoSelectorDialog.GetActionButton() {
                    @Override
                    public void onClickPositiveButton(final TwoSelectorDialog dialog) {
                        api.deleteTemplate(String.valueOf(data.getId())).enqueue(new ApiCallback<String>() {
                            @Override
                            protected void handleResponse(String response) {

                            }

                            @Override
                            protected void handleApi(ApiDTO<String> body) {
                                dialog.dismiss();
                                adapter.remove(vh.getAdapterPosition());
                                adapter.notifyItemRemoved(vh.getAdapterPosition());
                            }
                        });
                    }

                    @Override
                    public void onClickNegativeButton(TwoSelectorDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        };
    }

    public OnItemClickListener selector(){
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, final View view, BaseViewHolder vh) {
                final ImageView selector = (ImageView) view.findViewById(R.id.iv_select);
                if(!selector.isSelected()){
                    TwoSelectorDialog.showTwoSelectorDialog(view.getContext(), "是否确认添加？", "取消", "确认", new TwoSelectorDialog.GetActionButton() {
                        @Override
                        public void onClickPositiveButton(final TwoSelectorDialog dialog) {
                            AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) view.getContext();
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
                        public void onClickNegativeButton(TwoSelectorDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        };
    }
}
