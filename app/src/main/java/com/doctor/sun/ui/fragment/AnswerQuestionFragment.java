package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.util.Function0;
import com.doctor.sun.util.JacksonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;

/**
 * Created by rick on 28/7/2016.
 */

public class AnswerQuestionFragment extends SortedListFragment {

    private String appointmentId;
    private QuestionsModel model;

    public static AnswerQuestionFragment getInstance(String appointmentId) {
        AnswerQuestionFragment fragment = new AnswerQuestionFragment();
        Bundle bundle = new Bundle();

        bundle.putString(Constants.DATA, appointmentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appointmentId = getArguments().getString(Constants.DATA);
        model = new QuestionsModel();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
//        model.questions(appointmentId, new Function0<List<? extends SortedItem>>() {
//            @Override
//            public void apply(List<? extends SortedItem> sortedItems) {
//                getAdapter().insertAll(sortedItems);
//                binding.swipeRefresh.setRefreshing(false);
//            }
//        });
        JavaType type = TypeFactory.defaultInstance().constructCollectionType(List.class, Questions2.class);
        List<Questions2> questions = JacksonUtils.fromResource(getContext(), R.raw.json, type);
        List<SortedItem> sortedItems = model.parseQuestions(questions);
        getAdapter().insertAll(sortedItems);
        binding.swipeRefresh.setRefreshing(false);
    }

    public void save() {

    }

    public void loadQuestions(QuestionCategory data) {

    }

    public void handleResult(int requestCode, int resultCode, Intent data) {

    }

    public void handleImageResult(int requestCode, int resultCode, Intent data) {

    }
}
