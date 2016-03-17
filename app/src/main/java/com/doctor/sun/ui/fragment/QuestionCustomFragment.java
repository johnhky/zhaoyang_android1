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
import com.doctor.sun.ui.adapter.QuestionCustomAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by lucas on 12/24/15.
 */
public class QuestionCustomFragment extends ApplyFragment {
    private static QuestionCustomFragment instance;
    private QuestionModule api = Api.of(QuestionModule.class);
    private HashSet<String> customQuestionId = new HashSet<String>();
    private FragmentApplyBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getBinding();
        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customQuestionId.clear();
                GetOldData oldData = (GetOldData) view.getContext();
                ArrayList<String> oldQuestionId = oldData.getOldQuestionId();
                QTemplate qTemplate = oldData.getOldData();
                String templateName = oldData.getTemplateName();
                customQuestionId.addAll(oldQuestionId);
                for (Object object : getAdapter()) {
                    Question question = (Question) object;
                    if (question.getIsSelected()) {
                        customQuestionId.add(String.valueOf(question.getId()));
                    }
                }
                ArrayList<String> questionId = new ArrayList<String>(customQuestionId);
                api.updateTemplate(String.valueOf(qTemplate.getId()), templateName, questionId).enqueue(new ApiCallback<QTemplate>() {
                    @Override
                    protected void handleResponse(QTemplate response) {
                        getActivity().finish();
                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        return new QuestionCustomAdapter(getContext());
    }

    public static QuestionCustomFragment getInstance() {
        if (instance == null) {
            instance = new QuestionCustomFragment();
        }
        return instance;
    }

    @Override
    public void onStart() {
        super.onStart();
        SimpleAdapter adapter = getAdapter();
        adapter.clear();
        api.customs("").enqueue(new PageCallback<Question>(adapter));
        adapter.onFinishLoadMore(true);
    }

    public interface GetOldData {
        ArrayList<String> getOldQuestionId();

        QTemplate getOldData();

        String getTemplateName();
    }
}
