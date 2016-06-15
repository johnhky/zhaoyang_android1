package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.AfterServiceDTO;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.adapter.AnswerDetailAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.vo.ItemDivider;
import com.doctor.sun.vo.ItemTextInput;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by rick on 3/6/2016.
 */
public class ViewForumFragment extends RefreshListFragment {
    private String orderId;
    private AfterServiceModule api = Api.of(AfterServiceModule.class);
    private AnswerDetailAdapter adapter;
    private String forumType;

    public static ViewForumFragment newInstance(String id, String forumType) {

        Bundle args = new Bundle();
        args.putString(Constants.TYPE, forumType);
        args.putString(Constants.DATA, id);

        ViewForumFragment fragment = new ViewForumFragment();
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
        adapter = new AnswerDetailAdapter(getContext());
        if (getForumType().equals(AfterService.TYPE.DOCTOR)) {
            adapter.mapLayout(R.layout.item_after_service, R.layout.item_after_service_detail);
        } else {
            adapter.mapLayout(R.layout.item_after_service, R.layout.p_item_after_service_detail);
        }
        adapter.mapLayout(R.layout.item_options, R.layout.item_options3);
        adapter.mapLayout(R.layout.item_pick_date, R.layout.item_pick_question_date);
//        adapter.mapLayout(R.layout.item_answer, R.layout.item_answer3);
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_transfer_doctor);
        adapter.mapLayout(R.layout.item_prescription, R.layout.item_prescription2);
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
                    answer.setEditMode(false);

                    int parentPosition = getAdapter().size();
                    switch (answer.getQuestion().getQuestionType()) {
                        case Question.TYPE_SEL:
                        case Question.TYPE_CHECKBOX:
                        case Question.TYPE_RADIO:
                            getAdapter().add(answer);
                            List<Options> options = answer.getQuestion().getOptions();
                            for (Options option : options) {
                                if (answer.getSelectedOptions().containsKey(option.getOptionType())) {
                                    option.setParentPosition(parentPosition);
                                    getAdapter().add(option);
                                }
                            }
                            break;
                        case Question.TYPE_TIME: {
                            getAdapter().add(answer);
                            ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option, "");
                            try {
                                List<String> answerContent = Answer.handler.answerContent(answer);
                                if (answerContent != null && !answerContent.isEmpty()) {
                                    String input = answerContent.get(0);
                                    if (input != null && !input.equals("")) {
                                        textInput.setInput(input);
                                        getAdapter().add(textInput);
                                    }
                                }
                            } catch (Exception e) {

                            }
                            break;
                        }
                        case Question.TYPE_DROP_DOWN: {
                            getAdapter().add(answer);
                            ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option, "");
                            try {
                                List<String> answerContent = Answer.handler.answerContent(answer);
                                if (answerContent != null && !answerContent.isEmpty()) {
                                    String input = answerContent.get(0) + answerContent.get(1) + answerContent.get(2);
                                    if (input != null && !input.equals("")) {
                                        textInput.setInput(input);
                                        getAdapter().add(textInput);
                                    }
                                }

                            } catch (Exception e) {

                            }
                            break;
                        }
                        case Question.TYPE_FILL: {
                            getAdapter().add(answer);
                            if (answer.getAnswerContent() instanceof List) {
                                List<String> content = (List<String>) answer.getAnswerContent();
                                if (!content.isEmpty()) {
                                    String input = content.get(0);
                                    if (input != null && !input.equals("")) {
                                        ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option, "");
                                        textInput.setInput(input);
                                        getAdapter().add(textInput);
                                    }
                                }
                            }
                            break;
                        }
                        case Question.TYPE_FURTHER_CONSULTATION: {
                            if (Answer.handler.isAnswerValide(answer)) {
                                List<String> type = (List<String>) answer.getAnswerType();
                                List<Object> content = (List<Object>) answer.getAnswerContent();
                                if (!content.isEmpty() && !type.isEmpty()) {
                                    if (content.get(0) != null && type.get(0) != null) {
                                        ItemDivider divider = new ItemDivider(R.layout.item_divider2, answer.getQuestion().getQuestionContent());
                                        getAdapter().add(divider);

                                        String s = "";
                                        switch (type.get(0)) {
                                            case "A": {
                                                s = "详细就诊: ";
                                                ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                                                textInput.setInput(s + content.get(0));
                                                getAdapter().add(textInput);
                                                break;
                                            }
                                            case "B": {
                                                s = "简捷复诊: ";
                                                ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                                                textInput.setInput(s + content.get(0));
                                                getAdapter().add(textInput);
                                                break;
                                            }
                                            case "C": {
                                                s = "转诊: ";
                                                ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                                                textInput.setInput(s);
                                                getAdapter().add(textInput);

                                                Object o = content.get(0);
                                                if (o instanceof LinkedHashMap) {
                                                    HashMap<String, String> map = (HashMap<String, String>) o;
                                                    Doctor doctor = new Doctor();
                                                    doctor.fromHashMap(map);
                                                    getAdapter().add(doctor);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        }
                        case Question.TYPE_UPLOADS: {
                            getAdapter().add(answer);
                            break;
                        }
                        case Question.TYPE_PILLS: {
                            getAdapter().add(answer);
                            break;
                        }
                    }
                }

                getAdapter().notifyDataSetChanged();
                getBinding().swipeRefresh.setRefreshing(false);
            }
        });
    }


    public String getForumType() {
        return getArguments().getString(Constants.TYPE);
    }
}
