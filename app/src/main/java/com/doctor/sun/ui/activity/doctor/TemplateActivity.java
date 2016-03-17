package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityTemplateBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.TemplateAdapter;
import com.doctor.sun.ui.handler.TemplateHandler;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 12/3/15.
 */
public class TemplateActivity extends BaseActivity2 implements TemplateHandler.GetIsEditMode {

    private HeaderViewModel header = new HeaderViewModel(this);

    private TemplateAdapter mAdapter;
    private ActivityTemplateBinding binding;
    private QuestionModule api = Api.of(QuestionModule.class);

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, TemplateActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_template);
        header.setLeftIcon(R.drawable.ic_back).setMidTitle("问诊模板").setRightTitle("编辑");
        binding.setHeader(header);
        mAdapter = new TemplateAdapter(this);
        binding.rvTemplate.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTemplate.setAdapter(mAdapter);
        binding.setHandler(new TemplateHandler());
    }

    private void initData() {
        mAdapter.clear();
        mAdapter.mapLayout(R.layout.item_question_template,R.layout.item_template);
        api.templates().enqueue(new PageCallback<QTemplate>(mAdapter) {
            @Override
            protected void handleResponse(PageDTO<QTemplate> response) {
                super.handleResponse(response);
                getAdapter().onFinishLoadMore(true);
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        if (mAdapter.getItemCount() == 0) {
            ToastHelper.showMessage(this, "没有问诊模板");
            header.setRightTitle("编辑");
            binding.llAdd.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            if (mAdapter.isEditMode()) {
                header.setRightTitle("保存");
                binding.llAdd.setVisibility(View.GONE);
            } else {
                header.setRightTitle("编辑");
                binding.llAdd.setVisibility(View.VISIBLE);
            }
        }
        binding.setHeader(header);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean getIsEditMode() {
        return mAdapter.isEditMode();
    }
}
