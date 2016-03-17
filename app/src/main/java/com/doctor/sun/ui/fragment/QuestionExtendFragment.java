package com.doctor.sun.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.databinding.FragmentQuestionesBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.AssignQuestionAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.handler.EnterBankHandler;

import java.util.ArrayList;

import io.ganguo.library.util.Systems;

/**
 * Created by lucas on 1/18/16.
 */
public class QuestionExtendFragment extends Fragment {
    public static final int SECONED = 1000;
    private static QuestionExtendFragment instance;
    private long lastInputTime = 0;
    private SimpleAdapter mAdapter;
    private FragmentQuestionesBinding binding;
    private QuestionModule api = Api.of(QuestionModule.class);

    public static QuestionExtendFragment getInstance() {
        if (instance == null) {
            instance = new QuestionExtendFragment();
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
        initListener();
        return binding.getRoot();
    }

    private void initListener() {
        binding.flSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.tvSearch.setVisibility(View.GONE);
                binding.etSearch.setVisibility(View.VISIBLE);
                binding.etSearch.requestFocus();
                Systems.showKeyboard(getActivity().getWindow(), binding.etSearch);
            }
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.d("TAG", "beforeTextChanged: " + binding.etSearch.getGravity());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                long now = System.currentTimeMillis();
                if (!s.toString().equals("")) {
                    ArrayList<Question> questionList = new ArrayList<Question>();
                    for (int i = 0; i < mAdapter.size(); i++) {
                        if (mAdapter.get(i).toString().contains(s)) {
                            questionList.add((Question) mAdapter.get(i));
                        }
                    }
                    mAdapter.clear();
                    mAdapter.addAll(questionList);
                    mAdapter.notifyDataSetChanged();
                } else {
                    if (now - lastInputTime > SECONED) {
                        mAdapter.clear();
                        loadMore();
                        lastInputTime = now;
                    }
                }
            }
        });
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentQuestionesBinding.inflate(inflater, container, false);
        binding.rvQuestion.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = createAdapter();
        mAdapter.mapLayout(R.layout.item_question, R.layout.item_question_extend);
        binding.rvQuestion.setAdapter(mAdapter);
    }

    private void loadMore() {
        AssignQuestionAdapter.GetAppointmentId getAppointmentId = (AssignQuestionAdapter.GetAppointmentId) getContext();
        String appointmentId = getAppointmentId.getAppointmentId();
        api.library(appointmentId).enqueue(new ApiCallback<PageDTO<Question>>() {
            @Override
            protected void handleResponse(PageDTO<Question> response) {
                mAdapter.add(new EnterBankHandler(getActivity()));
                mAdapter.addAll(response.getData());
                mAdapter.onFinishLoadMore(true);
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
