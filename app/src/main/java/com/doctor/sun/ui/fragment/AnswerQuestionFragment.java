package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.PickImageViewModel;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.util.List;

import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 28/7/2016.
 */

public class AnswerQuestionFragment extends SortedListFragment {

    private int appointmentId;
    private QuestionsModel model;

    public static AnswerQuestionFragment getInstance(int appointmentId) {
        AnswerQuestionFragment fragment = new AnswerQuestionFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(Constants.DATA, appointmentId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        appointmentId = getArguments().getInt(Constants.DATA);
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getAdapter().size(); i++) {
            SortedItem sortedItem = getAdapter().get(i);
            sb.append(sortedItem.toJson(getAdapter()));
        }
        Log.e(TAG, "save: " + sb.toString());
    }

    public void loadQuestions(QuestionCategory data) {

    }

    public void handleResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public HeaderViewModel getHeader() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setRightTitle("保存");
        return headerViewModel;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Tasks.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PickImageViewModel.handleRequest(getActivity(), getAdapter(), data, requestCode);
                }
            }, 100);
        }
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        save();
    }
}
