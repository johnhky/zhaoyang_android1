package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAddTemplateBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.entity.handler.TemplateHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.QuestionAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.util.ArrayList;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.util.Systems;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lucas on 12/15/15.
 */
@Deprecated
public class AddTemplateActivity extends BaseFragmentActivity2 implements TemplateHandler.GetIsEditMode {

    private HeaderViewModel header = new HeaderViewModel(this);

    private QuestionModule api = Api.of(QuestionModule.class);
    private ActivityAddTemplateBinding binding;
    private QuestionAdapter mAdapter;
    private QTemplate newData;
    private boolean isEditing;

    public static Intent makeIntent(Context context, QTemplate data, boolean editStatue) {
        Intent i = new Intent(context, AddTemplateActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.EDITSTATUE, editStatue);
        return i;
    }

    private QTemplate getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private boolean getEditStatue() {
        return getIntent().getBooleanExtra(Constants.EDITSTATUE, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        isAlreadyDefault();
        initListener();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_template);
        mAdapter = new QuestionAdapter(this);
        binding.rvTemplateDetail.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTemplateDetail.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.onFinishLoadMore(true);
    }

    private void isAlreadyDefault() {
        if (getData() != null)
            binding.tvDefault.setText(getData().getIsDefault() == 1 ? "取消设置默认" : "设置默认");
        else
            binding.tvDefault.setText("设置默认");

        isEditing = getData() == null;

        if (getEditStatue()) {
            editMode(header);
        } else {
            notEditMode(header);
        }
        binding.setHeader(header);
    }

    private void initListener() {
        binding.tvDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIsEditMode()) {
                    if (getData() != null) {
                        api.updateTemplate(String.valueOf(getData().getId()), binding.etName.getText().toString(), getQuestionId())
                                .enqueue(new ApiCallback<QTemplate>() {
                                    @Override
                                    protected void handleResponse(QTemplate response) {
                                        loadData(String.valueOf(getData().getId()));
                                    }
                                });
                    } else {
                        try {
                            api.updateTemplate(String.valueOf(newData.getId()), binding.etName.getText().toString(), getQuestionId())
                                    .enqueue(new ApiCallback<QTemplate>() {
                                        @Override
                                        protected void handleResponse(QTemplate response) {
                                            loadData(String.valueOf(newData.getId()));
                                        }
                                    });
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (getData() != null) {
                        setDefaultTemplate(getData());
                    } else {
                        setDefaultTemplate(newData);
                    }
                }
            }
        });

        binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIsEditMode()) {
                    if (getData() == null) {
                        if (binding.etName.getText().toString().equals("")) {
                            ToastHelper.showMessage(AddTemplateActivity.this, "模板名称不能为空！");
                        } else {
                            if (newData == null) {
                                api.addTemplate(binding.etName.getText().toString(), getQuestionId()).enqueue(new ApiCallback<QTemplate>() {
                                    @Override
                                    protected void handleResponse(QTemplate response) {
                                        newData = response;
                                        addQuestion(newData);
                                    }
                                });
                            } else {
                                addQuestion(newData);
                            }
                        }
                    } else {
                        addQuestion(getData());
                    }

                } else {
                    final int id;
                    if (getData() != null) {
                        id = getData().getId();
                    } else {
                        id = newData.getId();
                    }
                    new com.afollestad.materialdialogs.MaterialDialog.Builder(AddTemplateActivity.this)
                            .content("确定删除该模板?")
                            .positiveText("删除")
                            .negativeText("取消")
                            .onPositive(new com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull com.afollestad.materialdialogs.MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                    api.deleteTemplate(String.valueOf(id)).enqueue(new SimpleCallback<String>() {
                                        @Override
                                        protected void handleResponse(String response) {
                                            finish();
                                        }
                                    });
                                }
                            }).show();

                }
            }
        });
    }

    private void setDefaultTemplate(final QTemplate data) {
        if (data.getIsDefault() == 1) {
            TwoChoiceDialog.show(AddTemplateActivity.this, "取消默认", "取消", "确定", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(MaterialDialog dialog) {
                    api.setNoDefaultTemplate(String.valueOf(data.getId())).enqueue(new ApiCallback<QTemplate>() {
                        @Override
                        protected void handleResponse(QTemplate response) {
                            ToastHelper.showMessage(AddTemplateActivity.this, "取消默认成功");
                        }
                    });
                    dialog.dismiss();
                    finish();
                }

                @Override
                public void onCancelClick(MaterialDialog dialog) {
                    dialog.dismiss();
                }
            });
        } else {
            TwoChoiceDialog.show(AddTemplateActivity.this, "确认设置默认", "取消", "确定", new TwoChoiceDialog.Options() {
                @Override
                public void onApplyClick(MaterialDialog dialog) {
                    api.setDefaultTemplate(String.valueOf(data.getId())).enqueue(new ApiCallback<QTemplate>() {
                        @Override
                        protected void handleResponse(QTemplate response) {
                            ToastHelper.showMessage(AddTemplateActivity.this, "成功设置为默认");
                        }
                    });
                    dialog.dismiss();
                    finish();
                }

                @Override
                public void onCancelClick(MaterialDialog dialog) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        if (getData() == null) {
            mAdapter.setIsEditMode(true);
            editMode(header);
            binding.setHeader(header);
            if (newData != null) {
                loadData(String.valueOf(newData.getId()));
            }
            Log.e(TAG, "onStart: " + mAdapter.isEditMode());
        } else {
            loadData(String.valueOf(getData().getId()));
        }
        super.onStart();
    }

    private void loadData(String id) {
        api.getTemplate(id).enqueue(new Callback<ApiDTO<QTemplate>>() {

            @Override
            public void onResponse(Call<ApiDTO<QTemplate>> call, Response<ApiDTO<QTemplate>> response) {
                mAdapter.clear();
                binding.rvTemplateDetail.removeAllViews();
                if (getData() != null)
                    binding.etName.setText(getData().getTemplateName());
                mAdapter.addAll(response.body().getData().getQuestion());
                mAdapter.notifyDataSetChanged();
                mAdapter.onFinishLoadMore(true);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                mAdapter.onFinishLoadMore(true);
            }
        });
    }

    //    @Override
    public void onMenuClicked() {
//        super.onMenuClicked();
        if (getData() == null && newData == null) {
            editMode(header);
            if (binding.etName.getText().toString().equals("")) {
                ToastHelper.showMessage(AddTemplateActivity.this, "模板名称不能为空！");
            } else {
                api.addTemplate(binding.etName.getText().toString(), getQuestionId()).enqueue(new ApiCallback<QTemplate>() {
                    @Override
                    protected void handleResponse(QTemplate response) {
                        ToastHelper.showMessage(AddTemplateActivity.this, "保存成功");
                        notEditMode(header);
                        newData = response;
                    }
                });
            }
        } else {
            mAdapter.setIsEditMode(!mAdapter.isEditMode());
            if (mAdapter.isEditMode()) {
                editMode(header);
            } else {
                notEditMode(header);
                if (getData() == null) {
                    api.updateTemplate(String.valueOf(newData.getId()), binding.etName.getText().toString(), getQuestionId())
                            .enqueue(new ApiCallback<QTemplate>() {
                                @Override
                                protected void handleResponse(QTemplate response) {
                                    ToastHelper.showMessage(AddTemplateActivity.this, "保存成功");
                                }
                            });
                } else {
                    api.updateTemplate(String.valueOf(getData().getId()), binding.etName.getText().toString(), getQuestionId())
                            .enqueue(new ApiCallback<QTemplate>() {
                                @Override
                                protected void handleResponse(QTemplate response) {
                                    ToastHelper.showMessage(AddTemplateActivity.this, "保存成功");
                                }
                            });
                }
                Systems.hideKeyboard(this);
            }
        }
        binding.setHeader(header);
        mAdapter.notifyDataSetChanged();
    }

    @NonNull
    private ArrayList<String> getQuestionId() {
        ArrayList<String> questionId = new ArrayList<>();
        questionId.clear();
        for (Object object : mAdapter) {
//            Question question = (Question) object;
//            if (!question.getIsSelected()) {
//                questionId.add(String.valueOf(question.getId()));
//            }
        }
        if (questionId.isEmpty()) {
            questionId.add("");
        }
        return questionId;
    }

    @Override
    public boolean getIsEditMode() {
        return mAdapter.isEditMode();
    }

    private void notEditMode(HeaderViewModel header) {
        mAdapter.setIsEditMode(false);
        binding.etName.setFocusable(false);
//        header.setRightTitle("编辑");
        binding.llyEdit.setBackgroundResource(R.drawable.bg_template_notedit);
        binding.flDefault.setBackgroundResource(R.drawable.bg_transparent);
        binding.tvDefault.setTextColor(Color.parseColor("#339de1"));
        if (getData() != null)
            binding.tvDefault.setText(getData().getIsDefault() == 1 ? "取消设置默认" : "设置默认");
        else
            binding.tvDefault.setText("设置默认");
        binding.flDelete.setBackgroundResource(R.drawable.shape_template);
        binding.tvDelete.setTextColor(Color.parseColor("#339de1"));
        binding.tvDelete.setText("删除模板");
    }

    private void editMode(HeaderViewModel header) {
//        header.setRightTitle("保存");
        binding.etName.setFocusableInTouchMode(true);
        binding.llyEdit.setBackgroundResource(R.drawable.bg_template_edit);
        binding.flDefault.setBackgroundResource(R.drawable.shape_button);
        binding.tvDefault.setTextColor(Color.parseColor("#ffffff"));
        binding.tvDefault.setText("删除问题");
        binding.flDelete.setBackgroundResource(R.drawable.shape_button);
        binding.tvDelete.setTextColor(Color.parseColor("#ffffff"));
        binding.tvDelete.setText("添加问题");
    }

    public void addQuestion(QTemplate data) {
        Intent intent = QuestionActivity.makeIntent(AddTemplateActivity.this, data, getQuestionId(), String.valueOf(binding.etName.getText()));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                isEditing = true;
                onMenuClicked();
                invalidateOptionsMenu();
                return true;
            }
            case R.id.action_save: {
                isEditing = false;
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
        if (isEditing) {
            menuInflater.inflate(R.menu.menu_save, menu);
        } else {
            menuInflater.inflate(R.menu.menu_edit, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}