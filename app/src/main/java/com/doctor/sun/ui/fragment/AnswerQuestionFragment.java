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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.constans.QuestionsType;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.event.ActivityResultEvent;
import com.doctor.sun.event.EditEndEvent;
import com.doctor.sun.event.FinishRefreshEvent;
import com.doctor.sun.event.LoadDrugEvent;
import com.doctor.sun.event.ModifyStatusEvent;
import com.doctor.sun.event.RefreshQuestionsEvent;
import com.doctor.sun.event.SaveAnswerSuccessEvent;
import com.doctor.sun.model.QuestionsModel;
import com.doctor.sun.ui.adapter.MapLayoutIdInterceptor;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.Function0;
import com.doctor.sun.vo.ItemPickImages;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 28/7/2016.
 */

public class AnswerQuestionFragment extends SortedListNoRefreshFragment {
    public static final String TAG = AnswerQuestionFragment.class.getSimpleName();

    private QuestionsModel model;

    protected String id;
    private String path;
    private String questionType;
    private String templateType;

    public static AnswerQuestionFragment getInstance(String id, String type) {

        return getInstance(id, type, "");
    }

    public static AnswerQuestionFragment getInstance(String id, @QuestionsPath String path, @QuestionsType @Nullable String questionType) {
        AnswerQuestionFragment fragment = new AnswerQuestionFragment();

        Bundle args = getArgs(id, path, questionType);
        fragment.setArguments(args);
        return fragment;
    }

    public static Bundle getArgs(String id, @QuestionsPath String path, @QuestionsType @Nullable String questionType) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_NAME, TAG);
        bundle.putString(Constants.DATA, id);
        bundle.putString(Constants.PATH, path);
        bundle.putString(Constants.QUESTION_TYPE, questionType);
        return bundle;
    }

    protected String getAppointmentId() {
        return id;
    }

    protected String getPath() {
        return path;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id = getArguments().getString(Constants.DATA);
        path = getArguments().getString(Constants.PATH);
        questionType = getArguments().getString(Constants.QUESTION_TYPE);
        templateType = getArguments().getString(Constants.IS_TEMPLATE, "");

        model = new QuestionsModel();
        setHasOptionsMenu(true);
        loadMore();
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        model.questions(path, id, questionType, templateType, new Function0<List<? extends SortedItem>>() {
            @Override
            public void apply(List<? extends SortedItem> sortedItems) {
                onFinishLoadMore(sortedItems);
                getAdapter().clear();
                getAdapter().insertAll(sortedItems);
                EventHub.post(new FinishRefreshEvent());
            }
        });
    }

    protected void onFinishLoadMore(List<? extends SortedItem> sortedItems) {

    }

    @NonNull
    @Override
    public SortedListAdapter createAdapter() {
        SortedListAdapter adapter = super.createAdapter();
        if (Settings.isDoctor()) {
            MapLayoutIdInterceptor idInterceptor = new MapLayoutIdInterceptor();
            idInterceptor.put(R.layout.item_scales, R.layout.item_r_scales);
            adapter.setLayoutIdInterceptor(idInterceptor);
        }
        return adapter;
    }

    public void save(int endAppointment) {
        model.saveAnswer(id, path, questionType, endAppointment, getAdapter());
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
                    showEndAppointmentDialog();
                } else {
                    save(IntBoolean.FALSE);
                }
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void showEndAppointmentDialog() {

        TwoChoiceDialog.show(getActivity(), getString(R.string.save_record_dialog),
                "存为草稿", "保存并结束", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(final MaterialDialog dialog) {
                        save(IntBoolean.TRUE);
                        EventHub.post(new ModifyStatusEvent(getAppointmentId(), AppointmentHandler2.Status.FINISHED));
                        dialog.dismiss();
                        getActivity().finish();
                    }

                    @Override
                    public void onCancelClick(final MaterialDialog dialog) {
                        save(IntBoolean.FALSE);
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (isVisible()) {
                ItemPickImages.handleRequest(getActivity(), getAdapter(), data, requestCode);
            }
        }
    }


    @Subscribe
    public void onActivityResultEvent(ActivityResultEvent event) {
        onActivityResult(event.getRequestCode(), event.getResultCode(), event.getData());
    }


    @Subscribe
    public void onEventMainThread(SaveAnswerSuccessEvent event) {
        if (!event.getId().equals(getAppointmentId())) {
            return;
        }

        /**
         * 医生端保存问卷后不会弹窗提示要不要返回上一页,可以修改。
         * TODO:需要弹窗吗?
         */
        if (Settings.isDoctor()) {
            Toast.makeText(getContext(), "保存问卷成功", Toast.LENGTH_SHORT).show();
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

    @Subscribe
    public void onLoadDrugEvent(LoadDrugEvent event) {
        Toast.makeText(getContext(), "暂无上次用药记录", Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onEventMainThread(RefreshQuestionsEvent event) {
        if (event.getId().equals(getAppointmentId())) {
            loadMore();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHub.post(new EditEndEvent(getAppointmentId()));
    }

}
