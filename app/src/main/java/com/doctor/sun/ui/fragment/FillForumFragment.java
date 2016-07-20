package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentListBinding;
import com.doctor.sun.databinding.ItemForumBarBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.QuestionCategory;
import com.doctor.sun.entity.QuestionStats;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AnswerModule;
import com.doctor.sun.ui.activity.doctor.HistoryRecordActivity;
import com.doctor.sun.ui.adapter.AnswerAdapter;
import com.doctor.sun.ui.adapter.ForumAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit2.Call;

/**
 * 填写问卷 只读(医生端) or (病人端)
 * Created by rick on 12/18/15.
 */
public class FillForumFragment extends ListFragment implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(FillForumFragment.class);
    public static final String TAG = FillForumFragment.class.getSimpleName();
    private static FillForumFragment instance;

    private AnswerModule api = Api.of(AnswerModule.class);
    private FragmentListBinding binding;
    private ItemForumBarBinding barBinding;
    private SimpleAdapter answerAdapter;

    private Appointment appointment;
    private boolean adapterStatus;

    public static FillForumFragment getInstance(Appointment appointment) {
        if (instance == null) {
            instance = new FillForumFragment();
            Bundle args = new Bundle();
            args.putParcelable(Constants.DATA, appointment);
            instance.setArguments(args);
        } else {
            instance.getArguments().putParcelable(Constants.DATA, appointment);
        }
        return instance;
    }

    public FillForumFragment() {
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        return new ForumAdapter(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = getArguments().getParcelable(Constants.DATA);
        adapterStatus = Constants.STATUS_QUESTION_LIST;
        answerAdapter = new AnswerAdapter(getActivity(), appointment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding = getBinding();
        barBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_forum_bar, binding.llRoot, false);
        binding.llRoot.addView(barBinding.getRoot(), 1);
        barBinding.tvBackCircle.setVisibility(View.GONE);
        barBinding.tvBackCircle.setOnClickListener(this);
        barBinding.tvCheck.setOnClickListener(this);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) barBinding.getRoot().getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        barBinding.getRoot().setLayoutParams(lp);
        super.onViewCreated(view, savedInstanceState);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadMore() {
        adapterStatus = Constants.STATUS_QUESTION_LIST;
        getAdapter().clear();
        api.category(appointment.getId()).enqueue(new ListCallback<QuestionCategory>(getAdapter()));
        QuestionCategory object = new QuestionCategory();
        object.setCategoryName("基础问卷");
        object.setQuestionCategoryId(-1);
        getAdapter().add(object);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check:
                Intent intent = HistoryRecordActivity.intentFor(getContext(), appointment.getRecordId());
                getContext().startActivity(intent);
                break;
            case R.id.tv_back_circle:
                binding.llRoot.findViewById(R.id.tv_back_circle).setVisibility(View.GONE);
                binding.llRoot.findViewById(R.id.lly_check).setVisibility(View.GONE);
                adapterStatus = Constants.STATUS_QUESTION_LIST;
                binding.recyclerView.setAdapter(getAdapter());
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public void loadQuestions(final QuestionCategory data) {
        final int questionCategoryId = data.getQuestionCategoryId();
        binding.llRoot.findViewById(R.id.lly_check).setVisibility(View.VISIBLE);
        binding.llRoot.findViewById(R.id.tv_back_circle).setVisibility(View.VISIBLE);

        adapterStatus = Constants.STATUS_ANSWER_DETAIL;
        binding.recyclerView.setAdapter(getAdapter());
        getAdapter().onFinishLoadMore(false);
        getAdapter().clear();
        //前面的位置少了1,加个空item占位
        getAdapter().add(new LayoutId() {
            @Override
            public int getItemLayoutId() {
                return R.layout.item_empty;
            }
        });
        if (questionCategoryId == -1) {
            getAdapter().setLoadMoreListener(new LoadMoreListener() {
                @Override
                protected void onLoadMore() {
                    api.answers(appointment.getId()).enqueue(new AnswerListCallback(""));
                }
            });
        } else {
            getAdapter().setLoadMoreListener(new LoadMoreListener() {
                @Override
                protected void onLoadMore() {
                    api.categoryDetail(appointment.getId(), String.valueOf(questionCategoryId))
                            .enqueue(new AnswerListCallback(data.getCategoryName()));
                }
            });
        }
        getAdapter().notifyDataSetChanged();
    }

    @Override
    public SimpleAdapter getAdapter() {
        //问卷列表 --> 问卷详情只读
        if (adapterStatus) {
            //STATUS_ANSWER_DETAIL
            return answerAdapter;
        } else {
            //STATUS_QUESTION_LIST
            return super.getAdapter();
        }
    }

    private class AnswerListCallback extends SimpleCallback<List<Answer>> {

        private final String categoryName;

        public AnswerListCallback(String categoryName) {
            this.categoryName = categoryName;
        }

        @Override
        protected void handleBody(ApiDTO<List<Answer>> body) {
            QuestionStats count = body.getCount();
            if (count != null) {
                count.setName(categoryName);
                getAdapter().add(count);
            }
            super.handleBody(body);
        }

        @Override
        protected void handleResponse(List<Answer> response) {
            Log.e(TAG, "handleResponse: " + response.size());
            for (int i = 0; i < response.size(); i++) {
                response.get(i).setPosition(i + 1);
            }
            getAdapter().addAll(response);
            getAdapter().onFinishLoadMore(true);
            getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            t.printStackTrace();
            getAdapter().onFinishLoadMore(true);
        }
    }
}
