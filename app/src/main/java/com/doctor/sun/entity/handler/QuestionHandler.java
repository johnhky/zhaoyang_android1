//package com.doctor.sun.entity.handler;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.View;
//import android.widget.ImageView;
//
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.doctor.sun.entity.Question;
//import com.doctor.sun.http.Api;
//import com.doctor.sun.module.QuestionModule;
//import com.doctor.sun.ui.activity.doctor.CustomDetailActivity;
//import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
//import com.doctor.sun.ui.widget.TwoChoiceDialog;
//
///**
// * Created by lucas on 12/17/15.
// */
//public class QuestionHandler {
//    private Question data;
//    private QuestionModule api = Api.of(QuestionModule.class);
//
//    public QuestionHandler(Question question) {
//        data = question;
//    }
//
//    public QuestionHandler() {
//    }
//
//    public void select(View view) {
//        data.setIsSelected(!data.getIsSelected());
//        view.setSelected(data.getIsSelected());
//    }
//
//    public void customQuestionDetail(View view) {
//        Intent intent = CustomDetailActivity.makeIntent(view.getContext());
//        view.getContext().startActivity(intent);
//    }
//
//
//    public void selector(final Context context, final ImageView selector) {
//        if (!selector.isSelected()) {
//            TwoChoiceDialog.show(context, "是否确认添加？", "取消", "确认", new TwoChoiceDialog.Options() {
//                @Override
//                public void onApplyClick(final MaterialDialog dialog) {
//                    AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) context;
//                    String appointmentId = getAppointmentId.getAppointmentId();
//                    api.appendQuestion(appointmentId, String.valueOf(data.getId())).enqueue(new ApiCallback<List<Answer>>() {
//                        @Override
//                        protected void handleResponse(List<Answer> response) {
//                            selector.setSelected(true);
//                            dialog.dismiss();
//                        }
//                    });
//                }
//
//                @Override
//                public void onCancelClick(MaterialDialog dialog) {
//                    dialog.dismiss();
//                }
//            });
//        }
//    }
//}
