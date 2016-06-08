package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.AfterServiceDTO;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.ItemSelectHospital;
import com.doctor.sun.ui.adapter.AnswerModifyAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemPickDate;

import java.util.List;

/**
 * Created by rick on 3/6/2016.
 */
public class EditForumFragment extends RefreshListFragment {
    private String orderId;
    private AfterServiceModule api = Api.of(AfterServiceModule.class);
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
                            List<Options> options = answer.getQuestion().getOptions();
                            for (Options option : options) {
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
                            FurtherConsultationVM vm = new FurtherConsultationVM();
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
        api.saveAnswer(orderId, adapter.toJsonAnswer()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(getContext(), "成功保存问卷", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getForumType() {
        return getArguments().getString(Constants.TYPE);
    }
}
