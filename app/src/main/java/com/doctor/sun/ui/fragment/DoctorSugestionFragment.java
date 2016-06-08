package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.vo.ItemDivider;
import com.doctor.sun.vo.ItemTextInput;

import java.util.List;

/**
 * Created by rick on 3/6/2016.
 */
public class DoctorSugestionFragment extends RefreshListFragment {
    private String orderId;
    private AfterServiceModule api = Api.of(AfterServiceModule.class);
    private SimpleAdapter adapter;
    private String forumType;

    public static DoctorSugestionFragment newInstance(String id, String forumType) {

        Bundle args = new Bundle();
        args.putString(Constants.TYPE, forumType);
        args.putString(Constants.DATA, id);

        DoctorSugestionFragment fragment = new DoctorSugestionFragment();
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
        adapter = new SimpleAdapter(getContext());
        if (getForumType().equals(AfterService.TYPE.DOCTOR)) {
            adapter.mapLayout(R.layout.item_after_service, R.layout.item_after_service_detail);
        } else {
            adapter.mapLayout(R.layout.item_after_service, R.layout.p_item_after_service_detail);
        }
        adapter.mapLayout(R.layout.item_options, R.layout.item_options3);
        adapter.mapLayout(R.layout.item_pick_date, R.layout.item_pick_question_date);
        adapter.mapLayout(R.layout.item_prescription, R.layout.item_prescription2);
//        adapter.mapLayout(R.layout.item_answer, R.layout.item_answer3);
        return adapter;
    }

    @Override
    protected void loadMore() {
        api.feedback(orderId).enqueue(new SimpleCallback<List<Answer>>() {
            @Override
            protected void handleResponse(List<Answer> response) {
                getAdapter().clear();
                getAdapter().onFinishLoadMore(true);
                for (int i = 0; i < response.size(); i++) {
                    Answer answer = response.get(i);
                    answer.setPosition(i + 1);
                    answer.setEditMode(false);
                    int parentPosition = getAdapter().size() - 1;
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
                            ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option, "");
                            try {
                                List<String> answerContent = Answer.handler.answerContent(answer);
                                textInput.setInput(answerContent.get(0));
                            } catch (Exception e) {

                            }
                            getAdapter().add(textInput);
                            break;
                        }
                        case Question.TYPE_DROP_DOWN: {
                            ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option, "");
                            try {
                                List<String> answerContent = Answer.handler.answerContent(answer);
                                String optionContent = answerContent.get(0) + answerContent.get(1) + answerContent.get(2);
                                textInput.setInput(optionContent);

                            } catch (Exception e) {

                            }
                            getAdapter().add(textInput);
                            break;
                        }
                        case Question.TYPE_FILL: {
                            if (answer.getAnswerContent() instanceof List) {
                                List<String> content = (List<String>) answer.getAnswerContent();
                                if (!content.isEmpty()) {
                                    ItemDivider divider = new ItemDivider(R.layout.item_divider2, answer.getQuestion().getQuestionContent());
                                    getAdapter().add(divider);
                                    ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                                    textInput.setInput(content.get(0));
                                    getAdapter().add(textInput);
                                }
                            }
                            break;
                        }
                        case Question.TYPE_PILLS: {
                            ItemDivider divider = new ItemDivider(R.layout.item_divider2, answer.getQuestion().getQuestionContent());
                            Answer result = Answer.handler.initPrescriptions(answer);
                            List<Prescription> prescriptions = result.getPrescriptions();
                            if (!prescriptions.isEmpty()) {
                                getAdapter().add(divider);
                                getAdapter().addAll(prescriptions);
                            }
                            break;
                        }
                        case Question.TYPE_FURTHER_CONSULTATION: {
                            if (answer.getAnswerContent() instanceof List) {
                                List<String> type = (List<String>) answer.getAnswerType();
                                List<String> content = (List<String>) answer.getAnswerContent();
                                if (!content.isEmpty() && !type.isEmpty()) {
                                    ItemDivider divider = new ItemDivider(R.layout.item_divider2, answer.getQuestion().getQuestionContent());
                                    getAdapter().add(divider);
                                    if (content.get(0) != null && type.get(0) != null) {

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

                                            }
                                        }
                                    }
                                }
                            }
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
