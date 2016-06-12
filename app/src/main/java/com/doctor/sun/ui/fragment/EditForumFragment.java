package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.AfterServiceDTO;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.ItemSelectHospital;
import com.doctor.sun.ui.adapter.AnswerModifyAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemPickDate;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by rick on 3/6/2016.
 */
public class EditForumFragment extends RefreshListFragment {
    private String orderId;
    private AfterServiceModule api = Api.of(AfterServiceModule.class);
    private ToolModule uploadApi = Api.of(ToolModule.class);
    private AnswerModifyAdapter adapter;
    private String forumType;

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

        adapter.mapLayout(R.layout.item_pick_date, R.layout.item_pick_question_date);
//        adapter.mapLayout(R.layout.item_answer, R.layout.item_answer3);
        return adapter;
    }

    @Override
    protected void loadMore() {
        api.questions(orderId, forumType).enqueue(new SimpleCallback<AfterServiceDTO>() {
            @Override
            protected void handleResponse(AfterServiceDTO response) {
                getAdapter().clear();
                getAdapter().onFinishLoadMore(true);
                getAdapter().addAll(response.followUpInfo);
                for (int i = 0; i < response.questions.size(); i++) {
                    Answer answer = response.questions.get(i);
                    answer.setPosition(i + 1);
                    answer.setEditMode(true);
                    getAdapter().add(answer);
                    int parentPosition = getAdapter().size() - 1;
                    switch (answer.getQuestion().getQuestionType()) {
                        case Question.TYPE_SEL:
                        case Question.TYPE_CHECKBOX:
                        case Question.TYPE_RADIO:
                            List<com.doctor.sun.entity.Options> options = answer.getQuestion().getOptions();
                            for (com.doctor.sun.entity.Options option : options) {
                                option.setParentPosition(parentPosition);
                            }
                            getAdapter().addAll(options);
                            break;
                        case Question.TYPE_TIME: {
                            ItemPickDate object = new ItemPickDate(R.layout.item_pick_question_date, "", 0);
                            try {
                                List<String> answerContent = Answer.handler.answerContent(answer);
                                object.setDate(answerContent.get(0));
                            } catch (Exception e) {

                            }
                            getAdapter().add(object);
                            break;
                        }
                        case Question.TYPE_DROP_DOWN: {
                            ItemSelectHospital object = new ItemSelectHospital();
                            getAdapter().add(object);
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
                                        HashMap<String, String> hashMap = (HashMap<String, String>) content.get(0);
                                        Doctor doctor = new Doctor();
                                        doctor.fromHashMap(hashMap);
                                        vm.setDoctor(doctor);
                                        break;
                                    }

                                }
                            }

                            getAdapter().add(vm);
                            break;
                        }
                    }
                }

                getAdapter().notifyDataSetChanged();
                getBinding().swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void saveAnswer() {
        TwoChoiceDialog.show(getActivity(), "是否结束本次随访",
                "暂存", "保存并结束", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(final TwoChoiceDialog dialog) {
                        saveAnswer(1);
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelClick(final TwoChoiceDialog dialog) {
                        saveAnswer(0);
                        dialog.dismiss();
                    }
                });
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
    }
}
