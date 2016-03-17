package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityAddQuestionBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.ItemTextInput;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.OptionAdapter;
import com.doctor.sun.ui.handler.QuestionHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.TwoSelectorDialog;

import java.util.HashMap;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 12/25/15.
 */
public class AddQuestionActivity extends BaseActivity2 {
    private ActivityAddQuestionBinding binding;
    private OptionAdapter mAdapter;
    private ItemTextInput optionAnswer;
    private ItemTextInput optionSelect;
    private int num = 0;
    private QuestionModule api = Api.of(QuestionModule.class);

    private String questionType;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AddQuestionActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initListener() {
        binding.tvAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());
                if (v.isSelected()) {
                    optionAnswer = new ItemTextInput(R.layout.item_option_answer, null);
                    mAdapter.add(mAdapter.size(), optionAnswer);
                    mAdapter.notifyItemChanged(mAdapter.size());
                } else {
                    mAdapter.remove(mAdapter.size() - 1);
                    mAdapter.notifyItemRemoved(mAdapter.size());
                }
            }
        });

        binding.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionSelect = new ItemTextInput(R.layout.item_option_select, null);
                mAdapter.add(num, optionSelect);
                mAdapter.notifyItemChanged(num);
                num++;
            }
        });
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_question);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setRightTitle("保存");
        binding.setHeader(header);
        mAdapter = new OptionAdapter(this);
        binding.rvOptions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOptions.setAdapter(mAdapter);
        binding.setHandler(new QuestionHandler());
        mAdapter.onFinishLoadMore(true);
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        getType();
    }

    private void getType() {
        String question = "单选题还是多选题?";
        String single = "单选题";
        String multiple = "多选题";

        TwoSelectorDialog.showTwoSelectorDialog(this, question, single, multiple, new TwoSelectorDialog.GetActionButton() {
            @Override
            public void onClickPositiveButton(TwoSelectorDialog dialog) {
                questionType = "checkbox";
                apiSend();
                dialog.dismiss();
            }

            @Override
            public void onClickNegativeButton(TwoSelectorDialog dialog) {
                questionType = "radio";
                apiSend();
                dialog.dismiss();
            }
        });
    }

    private void apiSend() {
        api.addQuestion(binding.etQuestion.getText().toString(), questionType, getOptions()).enqueue(new ApiCallback<PageDTO<Question>>() {
            @Override
            protected void handleResponse(PageDTO<Question> response) {
                ToastHelper.showMessage(AddQuestionActivity.this, "成功添加自编题");
                finish();
            }

            @Override
            protected void handleApi(ApiDTO<PageDTO<Question>> body) {
                finish();
            }
        });
    }

    private HashMap<String, String> getOptions() {
        HashMap<String, String> options = new HashMap<>();
        for (int i = mAdapter.size() - 1; i >= 0; i--) {
            ItemTextInput itemTextInput = (ItemTextInput) mAdapter.get(i);
            if (itemTextInput.getInput() != null) {
                options.put("options[" + i + "][option_type]", itemTextInput.getTitle());
                options.put("options[" + i + "][option_content]", itemTextInput.getInput());
            } else {
                options.put("options[" + i + "][option_type]", itemTextInput.getTitle());
                options.put("options[" + i + "][option_content]", "{fill}");
            }
        }
        Log.e(TAG, "getOptions: "+options );
        return options;
    }
}
