package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.AfterServiceDTO;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.entity.Question;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.ItemSelectHospital;
import com.doctor.sun.ui.adapter.AnswerModifyAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemReminderList;
import com.doctor.sun.vo.ItemSwitch;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by rick on 3/6/2016.
 */
public class EditForumFragment extends RefreshListFragment {
    public static final int TOGGLE_POSITION = 14;
    public static final int CUT_OFF_POSITION = TOGGLE_POSITION + 20;
    private String orderId;
    private AfterServiceModule api = Api.of(AfterServiceModule.class);
    private ToolModule uploadApi = Api.of(ToolModule.class);
    private AnswerModifyAdapter adapter;
    private String forumType;

    private ArrayList<LayoutId> allData = new ArrayList<>();

    public static EditForumFragment newInstance(String id, String forumType) {

        Bundle args = new Bundle();
        args.putString(Constants.TYPE, forumType);
        args.putString(Constants.DATA, id);

        EditForumFragment fragment = new EditForumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public String getData() {
        return getArguments().getString(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderId = getData();
        forumType = getForumType();
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        adapter = new AnswerModifyAdapter(getContext());
        if (getForumType().equals(AfterService.TYPE.DOCTOR)) {
            adapter.mapLayout(R.layout.item_after_service, R.layout.item_after_service_detail);
        } else {
            adapter.mapLayout(R.layout.item_after_service, R.layout.p_item_after_service_detail);
        }

        adapter.mapLayout(R.layout.item_options, R.layout.item_options_after_service);
        adapter.mapLayout(R.layout.item_pick_date, R.layout.item_pick_question_date);
//        adapter.mapLayout(R.layout.item_answer, R.layout.item_answer3);
        return adapter;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.questions(orderId, forumType).enqueue(new SimpleCallback<AfterServiceDTO>() {
            @Override
            protected void handleResponse(AfterServiceDTO response) {
                allData.clear();

                allData.addAll(response.followUpInfo);
                int answerPosition = 1;
                for (int i = 0; i < response.questions.size(); i++) {
                    addToggleVisibility(i);

                    Answer answer = response.questions.get(i);
                    answer.setPosition(answerPosition);
                    answer.setEditMode(true);
                    int parentPosition = allData.size();
                    switch (answer.getQuestion().getQuestionType()) {
                        case Question.TYPE_SEL:
                        case Question.TYPE_CHECKBOX:
                        case Question.TYPE_RADIO:
                            allData.add(answer);
                            List<com.doctor.sun.entity.Options> options = answer.getQuestion().getOptions();
                            for (com.doctor.sun.entity.Options option : options) {
                                option.setParentPosition(parentPosition);
                            }
                            allData.addAll(options);
                            break;
                        case Question.TYPE_TIME: {
                            allData.add(answer);
                            ItemPickDate object = new ItemPickDate(R.layout.item_pick_question_date, "", 0);
                            try {
                                List<String> answerContent = Answer.handler.answerContent(answer);
                                object.setDate(answerContent.get(0));
                            } catch (Exception e) {

                            }
                            allData.add(object);
                            break;
                        }
                        case Question.TYPE_DROP_DOWN: {
                            allData.add(answer);
                            List<String> type = null;
                            try {
                                if (answer.getAnswerType() != null && answer.getAnswerType() instanceof List) {
                                    type = Answer.handler.answerType(answer);
                                    int lv1Id = 0;
                                    int lv2Id = 0;
                                    int lv3Id = 0;
                                    if (type.size() >= 3) {
                                        lv1Id = Integer.parseInt(type.get(0));
                                        lv2Id = Integer.parseInt(type.get(1));
                                        lv3Id = Integer.parseInt(type.get(2));
                                    }
                                    ItemSelectHospital object = new ItemSelectHospital(lv1Id, lv2Id, lv3Id);
                                    allData.add(object);
                                }
                            } catch (Exception e) {

                            }
                            break;
                        }
                        case Question.TYPE_FURTHER_CONSULTATION: {
                            List<Object> content = null;
                            List<String> type = null;
                            try {
                                if (answer.getAnswerContent() != null && answer.getAnswerContent() instanceof List) {
                                    content = (List<Object>) answer.getAnswerContent();
                                    type = Answer.handler.answerType(answer);
                                }
                            } catch (Exception e) {

                            }
                            FurtherConsultationVM vm = new FurtherConsultationVM();
                            if (type != null && !type.isEmpty()) {
                                String s = type.get(0);
                                if (s != null) {

                                    vm.setHasAnswer(true);
                                    switch (s) {
                                        case "A": {
                                            vm.setBtnOneChecked(true);
                                            vm.setDate(content.get(0).toString());

                                            break;
                                        }
                                        case "B": {
                                            vm.setBtnTwoChecked(true);
                                            vm.setDate(content.get(0).toString());

                                            break;
                                        }
                                        case "C": {
                                            vm.setBtnThreeChecked(true);
                                            if (null != content && !content.isEmpty()) {
                                                HashMap<String, String> hashMap = (HashMap<String, String>) content.get(0);
                                                Doctor doctor = new Doctor();
                                                doctor.fromHashMap(hashMap);
                                                vm.setDoctor(doctor);
                                            }
                                            break;
                                        }

                                    }
                                }
                            }
                            vm.setQuestionId(answer.getQuestionId() + "");
                            vm.setPosition(answer.getPosition());
                            vm.setQuestionContent(answer.getQuestion().getQuestionContent());

                            allData.add(vm);
                            break;
                        }
                        case Question.REMINDER: {
                            answerPosition -= 1;
                            ItemReminderList object = new ItemReminderList();

                            if (answer.getAnswerContent() != null && answer.getAnswerContent() instanceof List) {
                                object.adapter(getContext());
                                List<Object> content = (List<Object>) answer.getAnswerContent();
                                for (int j = 0; j < content.size(); j++) {
                                    Reminder data = null;
                                    try {
                                        data = JacksonUtils.fromMap((LinkedHashMap) content.get(j), Reminder.class);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (data != null) {
                                        object.addReminder(data);
                                    }
                                }
                            }

                            object.setQuestionId(answer.getQuestionId());
                            allData.add(object);
                            break;
                        }
                        default: {
                            allData.add(answer);
                            break;
                        }
                    }
                    answerPosition += 1;
                }

                getAdapter().clear();
                getAdapter().onFinishLoadMore(true);
                if (AppContext.isDoctor()) {
                    getAdapter().addAll(allData.subList(0, CUT_OFF_POSITION));
                } else {
                    getAdapter().addAll(allData);
                }
                getAdapter().notifyDataSetChanged();
                getBinding().swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void addToggleVisibility(int i) {
        if (i == TOGGLE_POSITION) {
            if (AppContext.isDoctor()) {
                ItemSwitch object = new ItemSwitch(R.layout.item_answer_control, "病情记录(选填)");
                object.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        if (i == BR.isChecked) {
                            ItemSwitch o = (ItemSwitch) observable;
                            getAdapter().clear();
                            if (o.isChecked()) {
                                getAdapter().addAll(allData);
                                getAdapter().notifyDataSetChanged();
                            } else {
                                getAdapter().addAll(allData.subList(0, CUT_OFF_POSITION));
                                getAdapter().notifyDataSetChanged();
                            }
                        }
                    }
                });
                allData.add(object);
            }
        }
    }

    public void saveAnswer() {
        if (AppContext.isDoctor()) {
            TwoChoiceDialog.show(getActivity(), "是否结束本次随访",
                    "暂存", "保存并结束", new TwoChoiceDialog.Options() {
                        @Override
                        public void onApplyClick(final MaterialDialog dialog) {
                            saveAnswer(1);
                            dialog.dismiss();
                        }

                        @Override
                        public void onCancelClick(final MaterialDialog dialog) {
                            saveAnswer(0);
                            dialog.dismiss();
                        }
                    });
        } else {
            saveAnswer(0);
        }
    }

    public void saveAnswer(int isFinished) {
        api.saveAnswer(orderId, adapter.toJsonAnswer(), isFinished).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(getContext(), "成功保存问卷", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getForumType() {
        return getArguments().getString(Constants.TYPE);
    }

    public void handleImageResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (PickImageDialog.getRequestCode(requestCode)) {
                case Constants.UPLOAD_REQUEST_CODE:
                case Constants.UPLOAD_REQUEST_CODE / 2: {
                    File to = PickImageDialog.handleRequest(getContext(), data, requestCode);
                    RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), to);
                    uploadApi.uploadPhoto(body).enqueue(new ApiCallback<Photo>() {
                        @Override
                        protected void handleResponse(Photo response) {
                            if (getAdapter() instanceof AnswerModifyAdapter) {
                                ((AnswerModifyAdapter) getAdapter()).addImage(response.getUrl());
                            }
                        }
                    });
                }
                default: {

                }
            }
        }
    }
}
