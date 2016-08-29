package com.doctor.sun.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.event.SaveAnswerSuccessEvent;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.Function0;
import com.doctor.sun.vo.ItemPickImages;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 28/7/2016.
 */

public class AnswerQuestionFragment extends SortedListFragment {

    protected String id;
    private QuestionsModel model;
    private String path;
    private String questionType;

    public static AnswerQuestionFragment getInstance(String id, String type) {

        return getInstance(id, type, "");
    }

    public static AnswerQuestionFragment getInstance(String id, String path, String questionType) {
        AnswerQuestionFragment fragment = new AnswerQuestionFragment();
        Bundle bundle = new Bundle();

        bundle.putString(Constants.DATA, id);
        bundle.putString(Constants.PATH, path);
        bundle.putString(Constants.QUESTION_TYPE, questionType);
        fragment.setArguments(bundle);
        return fragment;
    }

    protected String getAppointmentId() {
        return id;
    }

    protected String getPath() {
        return path;
    }

    protected String getQuestionType() {
        return questionType;
    }


    @Override
    public void onResume() {
        super.onResume();
        EventHub.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventHub.unregister(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        id = 138;
        id = getArguments().getString(Constants.DATA);
        path = getArguments().getString(Constants.PATH);
        questionType = getArguments().getString(Constants.QUESTION_TYPE);

        model = new QuestionsModel();
        setHasOptionsMenu(true);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        model.questions(path, id, questionType, new Function0<List<? extends SortedItem>>() {
            @Override
            public void apply(List<? extends SortedItem> sortedItems) {
                getAdapter().insertAll(sortedItems);
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void save(int isFinished) {
        model.saveAnswer(id, path, questionType, isFinished, getAdapter());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                if (Settings.isDoctor()) {
                    TwoChoiceDialog.show(getActivity(), "是否结束本次随访",
                            "暂存", "保存并结束", new TwoChoiceDialog.Options() {
                                @Override
                                public void onApplyClick(final MaterialDialog dialog) {
                                    save(1);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancelClick(final MaterialDialog dialog) {
                                    save(0);
                                    dialog.dismiss();
                                }
                            });
                } else {
                    save(0);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            ItemPickImages.handleRequest(getActivity(), getAdapter(), data, requestCode);
        }
    }

    @Override
    public void onRefresh() {
        getBinding().swipeRefresh.setRefreshing(false);
    }

    @Subscribe
    public void onEventMainThread(SaveAnswerSuccessEvent event) {
        /**
         * 医生端保存问卷后不会弹窗提示要不要返回上一页,可以修改。
         * TODO:需要弹窗吗?
         */
        if (Settings.isDoctor()) {
            return;
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());

        builder.content("问卷已保存成功，请留意信息提醒及保持电话通畅，医生可能会要求您补充、修改或进行提前就诊。")
                .positiveText("留在本页")
                .negativeText("返回上一页")

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })

                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().finish();
                        dialog.dismiss();
                    }
                }).show();
    }

}
