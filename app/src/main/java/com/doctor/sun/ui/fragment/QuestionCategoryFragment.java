package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.databinding.ItemCategoryExtendBinding;
import com.doctor.sun.entity.QuestionCategory2;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

/**
 * Created by lucas on 1/19/16.
 */
public class QuestionCategoryFragment extends Fragment {
    private ItemCategoryExtendBinding binding;
    private SimpleAdapter mAdapter;
    private static QuestionCategoryFragment instance;
    private QuestionModule api = Api.of(QuestionModule.class);

    public static QuestionCategoryFragment getInstance() {
        if (instance == null) {
            instance = new QuestionCategoryFragment();
        }
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public SimpleAdapter createAdapter() {
        return new SimpleAdapter(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView(inflater, container);
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        return binding.getRoot();
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        binding = ItemCategoryExtendBinding.inflate(inflater, container, false);
        mAdapter = createAdapter();
        binding.rvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCategory.setAdapter(mAdapter);
    }

    private void loadMore() {
        AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) getContext();
        String appointmentId = getAppointmentId.getAppointmentId();
        api.scaleCategory(appointmentId).enqueue(new ListCallback<QuestionCategory2>(mAdapter));
    }
}
