package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemForumBarBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.Options;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AnswerModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.adapter.AnswerModifyAdapter;
import com.doctor.sun.ui.adapter.ForumAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.handler.QCategoryHandler;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.vo.ItemDivider;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 填写问卷 修改 病人端 or 医生端
 * Created by Lynn on 1/26/16.
 */
public class ModifyForumFragment extends ListFragment implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(ModifyForumFragment.class);

    private static ModifyForumFragment instance;
    private AnswerModule api = Api.of(AnswerModule.class);
    private ToolModule uploadApi = Api.of(ToolModule.class);
    private AnswerModifyAdapter modifyAdapter;

    private ItemForumBarBinding barBinding;
    private SetHeaderListener setHeaderListener;

    private boolean adapterStatus;
    private int appointmentId;

    public static ModifyForumFragment getInstance(int appointmentId) {
        if (instance == null) {
            instance = new ModifyForumFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.DATA, appointmentId);
            //arguments 只能在attach to activity 之前调用, 贯穿整个fragment生命周期
            instance.setArguments(bundle);
        } else {
            instance.getArguments().putInt(Constants.DATA, appointmentId);
        }
        return instance;
    }

    public ModifyForumFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentId = getData();
        modifyAdapter = new AnswerModifyAdapter(getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        binding = getBinding();
        barBinding = ItemForumBarBinding.inflate(LayoutInflater.from(getContext()), binding.llRoot, false);
        binding.llRoot.addView(barBinding.getRoot(), 1);
        barBinding.tvBackCircle.setVisibility(View.GONE);
        barBinding.tvBackCircle.setOnClickListener(this);
        barBinding.tvCheck.setOnClickListener(this);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) barBinding.getRoot().getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        barBinding.getRoot().setLayoutParams(lp);
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        return new ForumAdapter(getContext());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void loadMore() {
        adapterStatus = Constants.STATUS_QUESTION_LIST;
        getAdapter().clear();
        api.category(appointmentId).enqueue(new ListCallback<QCategoryHandler>(getAdapter()));
        QCategoryHandler object = new QCategoryHandler();
        object.setCategoryName("基础问卷");
        object.setQuestionCategoryId(-1);
        getAdapter().add(object);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check:
                if (barBinding.tvCheck.getText() == "查看用药") {
                    binding.recyclerView.scrollToPosition(0);
                }
                break;
            case R.id.tv_back_circle:
                binding.llRoot.findViewById(R.id.tv_back_circle).setVisibility(View.GONE);
                adapterStatus = Constants.STATUS_QUESTION_LIST;
                binding.recyclerView.setAdapter(getAdapter());
                //回调隐藏标题
                setHeaderListener.setHeaderRightTitle("");
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        try {
            setHeaderListener = (SetHeaderListener) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Host must implement SetHeaderListener");
        }
        super.onAttach(context);
    }

    @SuppressWarnings("unchecked")
    public void loadQuestions(final QCategoryHandler data) {
        final int questionCategoryId = data.getQuestionCategoryId();
        ((TextView) binding.llRoot.findViewById(R.id.tv_check)).setText("查看用药");
        binding.llRoot.findViewById(R.id.tv_back_circle).setVisibility(View.VISIBLE);

        adapterStatus = Constants.STATUS_ANSWER_MODIFY;
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
                    api.answers(appointmentId).enqueue(new ModifyAnswerCallback());
                }
            });
        } else {
            getAdapter().setLoadMoreListener(new LoadMoreListener() {
                @Override
                protected void onLoadMore() {
                    api.categoryDetail(appointmentId, String.valueOf(questionCategoryId))
                            .enqueue(new ModifyAnswerCallback());
                }
            });
        }
        getAdapter().notifyDataSetChanged();
    }

    private int getData() {
        return getArguments().getInt(Constants.DATA, -1);
    }

    @Override
    public SimpleAdapter getAdapter() {
        //问卷列表 --> 编辑问卷
        if (adapterStatus) {
            //STATUS_ANSWER_MODIFY
            return modifyAdapter;
        } else {
            //STATUS_QUESTION_LIST
            return super.getAdapter();
        }
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.PATIENT_PRESCRITION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                final Prescription prescription = data.getParcelableExtra(Constants.DATA);
                ((AnswerModifyAdapter) getAdapter()).addPrescription(prescription);
            }
        }
    }

    public void handleImageResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            File to = PickImageDialog.handleRequest(getContext(), data, requestCode);
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), to);
            uploadApi.uploadPhoto(body).enqueue(new ApiCallback<Photo>() {
                @Override
                protected void handleResponse(Photo response) {
                    if (getAdapter() instanceof AnswerModifyAdapter) {
                        ((AnswerModifyAdapter) getAdapter()).addImage(response.getUrl());
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    public void save() {
        //保存当前填写答案
        if (getAdapter() instanceof AnswerModifyAdapter) {
            String answer = ((AnswerModifyAdapter) getAdapter()).toJsonAnswer();
            logger.d("answer: " + answer);
            if (answer != null) {
                Call<ApiDTO<List<Answer>>> call = api.saveAnswers(appointmentId, answer);
                call.enqueue(new ApiCallback<List<Answer>>() {
                    @Override
                    protected void handleResponse(List<Answer> response) {
                        Toast.makeText(getActivity(), "问卷已保存成功，请留意信息提醒及保持电话畅通，医生可能会要求您补充、修改或进行提前就诊。", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    private class ModifyAnswerCallback extends SimpleCallback<List<Answer>> {
        @Override
        protected void handleResponse(List<Answer> data) {
            for (int i = 0; i < data.size(); i++) {

                Answer answer = data.get(i);
                answer.setEditMode(true);
                answer.setPosition(i + 1);
                int parentPosition = getAdapter().getItemCount() - 1;
                getAdapter().add(answer);

                for (Options options : answer.getQuestion().getOptions()) {
                    options.setParentPosition(parentPosition);
                }

                getAdapter().addAll(answer.getQuestion().getOptions());
                getAdapter().add(new ItemDivider(R.layout.divider_1px));
            }
            getAdapter().add(new ItemDivider(R.layout.space_30dp));
            getAdapter().onFinishLoadMore(true);
            getAdapter().notifyDataSetChanged();
        }

        @Override
        public void onFailure(Throwable t) {
            getAdapter().onFinishLoadMore(true);
        }
    }
}
