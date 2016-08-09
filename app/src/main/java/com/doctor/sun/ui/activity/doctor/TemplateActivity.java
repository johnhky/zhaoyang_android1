package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityTemplateBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.entity.handler.TemplateHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.TemplateAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by lucas on 12/3/15.
 */
public class TemplateActivity extends BaseFragmentActivity2 implements TemplateHandler.GetIsEditMode {

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
        mAdapter = new TemplateAdapter(this);
        binding.rvTemplate.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTemplate.setAdapter(mAdapter);
        binding.setHandler(new TemplateHandler());
    }

    private void initData() {
        mAdapter.clear();
        mAdapter.mapLayout(R.layout.item_question_template, R.layout.item_template);
        api.templates().enqueue(new PageCallback<QTemplate>(mAdapter) {
            @Override
            protected void handleResponse(PageDTO<QTemplate> response) {
                super.handleResponse(response);
                getAdapter().onFinishLoadMore(true);
            }
        });
        mAdapter.notifyDataSetChanged();
    }

    public void onMenuClicked() {
//        super.onMenuClicked();
        if (mAdapter.getItemCount() == 0) {
            binding.llAdd.setVisibility(View.VISIBLE);
            binding.emptyIndicator.setText("您还没有任何问诊模块");
            binding.emptyIndicator.setVisibility(View.VISIBLE);
        } else {
            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            if (mAdapter.isEditMode()) {
                binding.llAdd.setVisibility(View.GONE);
            } else {
                binding.llAdd.setVisibility(View.VISIBLE);
            }
            binding.emptyIndicator.setVisibility(View.GONE);
        }
        binding.setHeader(header);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean getIsEditMode() {
        return mAdapter.isEditMode();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAdapter.setIsEditMode(!mAdapter.isEditMode());
        switch (item.getItemId()) {
            case R.id.action_edit: {
                onMenuClicked();
                invalidateOptionsMenu();
                return true;
            }
            case R.id.action_save: {
                onMenuClicked();
                invalidateOptionsMenu();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.clear();
        MenuInflater menuInflater = getMenuInflater();
        if (mAdapter.isEditMode()) {
            menuInflater.inflate(R.menu.menu_save, menu);
        } else {
            menuInflater.inflate(R.menu.menu_edit, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_template;
    }
}
