package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityAddQuestionBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Question;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.OptionAdapter;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.vo.ItemTextInput;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeMap;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 12/25/15.
 */
public class AddQuestionActivity extends BaseFragmentActivity2 {
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
//        HeaderViewModel header = new HeaderViewModel(this);
//        header.setRightTitle("保存");
//        binding.setHeader(header);
        mAdapter = new OptionAdapter(this);
        binding.rvOptions.setLayoutManager(new LinearLayoutManager(this));
        binding.rvOptions.setAdapter(mAdapter);
        mAdapter.onFinishLoadMore(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                onMenuClicked();
            }
        }
        return true;
    }

    public void onMenuClicked() {
        getType();
    }

    private void getType() {
        String question = "单选题还是多选题?";
        String single = "单选题";
        String multiple = "多选题";

        TwoChoiceDialog.show(this, question, single, multiple, new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(MaterialDialog dialog) {
                questionType = "checkbox";
                saveQuestion();
                dialog.dismiss();
            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                questionType = "radio";
                saveQuestion();
                dialog.dismiss();
            }
        });
    }

    private void saveQuestion() {
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

    private TreeMap<String, String> getOptions() {
        TreeMap<String, String> options = new TreeMap<>();
        Collection filter = Collections2.filter(mAdapter, new Predicate() {
            @Override
            public boolean apply(Object input) {
                ItemTextInput itemTextInput = (ItemTextInput) input;
                String content = itemTextInput.getInput();
                return content != null && !content.equals("");
            }
        });
        ArrayList<ItemTextInput> temp = new ArrayList<>();
        temp.addAll(filter);
        for (int i = 0; i < temp.size(); i++) {
            ItemTextInput itemTextInput = temp.get(i);
            options.put("options[" + i + "][option_type]", getCharForNumber(i + 1));
            options.put("options[" + i + "][option_content]", itemTextInput.getInput());
        }
        if (binding.tvAnswer.isSelected()) {
            options.put("options[" + temp.size() + "][option_type]", getCharForNumber(temp.size() + 1));
            options.put("options[" + temp.size() + "][option_content]", "{fill}");
        }
        return options;
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char) (i + 64)) : null;
    }
}
