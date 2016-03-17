package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.doctor.sun.databinding.FragmentApplyBinding;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.QuestionDefaultAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by lucas on 12/24/15.
 */
public class QuestionDefaultFragment extends ApplyFragment {
    private static QuestionDefaultFragment instance;
    private QuestionModule api = Api.of(QuestionModule.class);
    private HashSet<String> defaultQuestionId = new HashSet<String>();
    private FragmentApplyBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        return new QuestionDefaultAdapter(getContext());
    }

    public static QuestionDefaultFragment getInstance() {
        if (instance == null) {
            instance = new QuestionDefaultFragment();
        }
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
        SimpleAdapter adapter = getAdapter();
        adapter.clear();
        api.library("").enqueue(new PageCallback<Question>(adapter));
        adapter.onFinishLoadMore(true);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getBinding();
        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultQuestionId.clear();
                GetOldData oldData = (GetOldData) view.getContext();
                ArrayList<String> oldQuestionId = oldData.getOldQuestionId();
                QTemplate qTemplate = oldData.getOldData();
                String templateName = oldData.getTemplateName();
                defaultQuestionId.addAll(oldQuestionId);
                for (Object object : getAdapter()) {
                    Question question = (Question) object;
                    if (question.getIsSelected()) {
                        defaultQuestionId.add(String.valueOf(question.getId()));
                    }
                }
                ArrayList<String> questionId = new ArrayList<String>(defaultQuestionId);
                api.updateTemplate(String.valueOf(qTemplate.getId()), templateName, questionId).enqueue(new ApiCallback<QTemplate>() {
                    @Override
                    protected void handleResponse(QTemplate response) {
                        getActivity().finish();
                    }
                });
            }
        });
    }

    public interface GetOldData {
        ArrayList<String> getOldQuestionId();

        QTemplate getOldData();

        String getTemplateName();
    }
}
