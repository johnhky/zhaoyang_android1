package com.doctor.sun.ui.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FabImportAnswerBinding;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.ImportType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.event.ImportDiagnosisEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.ui.fragment.BottomSheetTabFragment;
import com.doctor.sun.ui.pager.DoctorAfterServiceDonePA;
import com.doctor.sun.ui.pager.DoctorAfterServicePA;
import com.doctor.sun.ui.pager.DoctorAppointmentDonePA;
import com.doctor.sun.util.JacksonUtils;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.BaseApp;
import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 18/10/2016.
 */

public class AppointmentHistoryDialog extends BottomSheetTabFragment implements View.OnClickListener {
    public static final String TAG = AppointmentHistoryDialog.class.getSimpleName();

    public static final String HISTORY_INDEX = "HISTORY_INDEX";
    private DoctorAppointmentDonePA answerPagerAdapter;
    private DoctorAfterServiceDonePA questionPagerAdapter;
    DiagnosisModule api = Api.of(DiagnosisModule.class);

    private Appointment appointment;
    private String recordId;
    private List<Appointment> data = new ArrayList<>();
    private int currentIndex = 0;
    private FabImportAnswerBinding fabBinding;

    public static AppointmentHistoryDialog newInstance(Appointment data) {
        AppointmentHistoryDialog fragment = new AppointmentHistoryDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DATA, JacksonUtils.toJson(data));
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointment = getData();
        recordId = appointment.getRecord_id();
        currentIndex = getIndex(Config.getString(Constants.ADDRESS)+"");

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBinding().toolBar.setNavigationOnClickListener(this);
        getBinding().tvPrevious.setOnClickListener(this);
        getBinding().tvNext.setOnClickListener(this);
        getBinding().tbMenu.setOnClickListener(this);

        api.recordHistory(appointment.getRecord().getMedicalRecordId()).enqueue(new SimpleCallback<List<Appointment>>() {
            @Override
            protected void handleResponse(List<Appointment> response) {
                data.addAll(response);
                if (data.isEmpty()) {
                    Toast.makeText(getContext(), "暂时没有任何历史记录", Toast.LENGTH_SHORT).show();
                } else {

                    toggleVisibility();

                    initPagerAdapter();

                    initPagerTabs();

                    setCurrentItem();
                }
            }
        });

        fabBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.fab_import_answer, getBinding().flyContainer, false);
        final FloatingActionsMenu fabMenu = fabBinding.btnAppointmentHistory;

        fabBinding.fabAll.setVisibility(View.GONE);
        fabBinding.fabDiagnosis.setVisibility(View.GONE);
        fabBinding.fabAdvice.setVisibility(View.GONE);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                getBinding().setFabExpended(true);
                fabBinding.fabTitle.setVisibility(View.GONE);
                fabBinding.fabIcon.setVisibility(View.VISIBLE);
                fabBinding.fabAll.setVisibility(View.VISIBLE);
                fabBinding.fabDiagnosis.setVisibility(View.VISIBLE);
                fabBinding.fabAdvice.setVisibility(View.VISIBLE);

                if (data.get(currentIndex).getType() == AppointmentType.FollowUp) {
                    fabBinding.fabAll.setVisibility(View.GONE);
                    fabBinding.fabDiagnosis.setVisibility(View.GONE);
                } else {
                    fabBinding.fabAll.setVisibility(View.VISIBLE);
                    fabBinding.fabDiagnosis.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMenuCollapsed() {
                getBinding().setFabExpended(false);
                fabBinding.fabTitle.setVisibility(View.VISIBLE);
                fabBinding.fabAll.setVisibility(View.GONE);
                fabBinding.fabDiagnosis.setVisibility(View.GONE);
                fabBinding.fabAdvice.setVisibility(View.GONE);
                fabBinding.fabIcon.setVisibility(View.GONE);
            }
        });
        fabBinding.fabAll.setOnClickListener(this);
        fabBinding.fabAdvice.setOnClickListener(this);
        fabBinding.fabDiagnosis.setOnClickListener(this);

        getBinding().flyContainer.addView(fabBinding.getRoot());
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        if (data.size() == 0) {
            return null;
        }

        if (currentIndex > data.size() || currentIndex < 0) {
            currentIndex = 0;
        }
        String id;
        if (data.isEmpty()) {
            id = "";
        } else {
            id = data.get(currentIndex).getId();
            if (Config.getInt(Constants.CREATE_SUCCESS, 0) != 0) {
                fabBinding.flRoot.setVisibility(View.VISIBLE);
            } else {
                fabBinding.flRoot.setVisibility(View.GONE);
            }
        }
        if (data.get(currentIndex).getType() == 3) {
            questionPagerAdapter = new DoctorAfterServiceDonePA(getChildFragmentManager(), id);
            return questionPagerAdapter;
        } else {
            answerPagerAdapter = new DoctorAppointmentDonePA(getChildFragmentManager(), id);
            return answerPagerAdapter;
        }
    }

    private void refreshFabVisibility() {
        if (fabBinding != null) {
            if (isFollowUp()) {
                fabBinding.flRoot.setVisibility(View.GONE);
            } else {
                fabBinding.flRoot.setVisibility(View.VISIBLE);
            }
        }
    }

    private boolean notStartedYet() {
        return appointment.getStatus() == AppointmentHandler2.Status.PAID;
    }

    private boolean selectedItemIsFollowUp() {
        return AppointmentType.FollowUp == data.get(currentIndex).getType();
    }

    private boolean isFollowUp() {
        return AppointmentType.FollowUp == appointment.getType();
    }

    private boolean canNotEdit() {
        return appointment.getStatus() == AppointmentHandler2.Status.FINISHED
                && appointment.getCan_edit() == IntBoolean.FALSE;
    }

    private void toggleVisibility() {
        getBinding().tvNext.setVisibility(View.VISIBLE);
        getBinding().tvPrevious.setVisibility(View.VISIBLE);

        if (currentIndex >= data.size() - 1) {
            getBinding().tvNext.setVisibility(View.GONE);
        }

        if (currentIndex == 0) {
            getBinding().tvPrevious.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        setIndex(currentIndex, Config.getString(Constants.ADDRESS)+"");
    }

    public static void setIndex(int index, String id) {
        Config.putInt(HISTORY_INDEX + id, index);
    }

    public static int getIndex(String id) {
        return Config.getInt(HISTORY_INDEX + id, 0);
    }

    @Override
    public void onClick(View view) {
        if (data == null || data.isEmpty()) {
            Toast.makeText(getContext(), "暂时没有任何历史记录", Toast.LENGTH_SHORT).show();
            return;
        }
        setTabPosition();

        switch (view.getId()) {
            case R.id.tv_previous: {
                currentIndex -= 1;
                setIndex(currentIndex, Config.getString(Constants.ADDRESS)+"");
                toggleVisibility();
                setPagerAdapter(createPagerAdapter());
                setCurrentItem();
                break;
            }
            case R.id.tv_next: {
                currentIndex += 1;
                setIndex(currentIndex, Config.getString(Constants.ADDRESS)+"");
                toggleVisibility();
                setPagerAdapter(createPagerAdapter());
                setCurrentItem();
                break;
            }
            case R.id.tb_menu: {
                dismiss();
                EventHub.post(new AppointmentHistoryEvent(appointment, true));
                break;
            }
            case R.id.fab_all: {
                showImportAlert(ImportType.ALL);
                break;
            }
            case R.id.fab_diagnosis: {
                showImportAlert(ImportType.DIAGNOSIS);
                break;
            }
            case R.id.fab_advice: {
                showImportAlert(ImportType.ADVICE_AND_PRESCRIPTION);
                break;
            }
            default: {
                dismiss();
                break;
            }
        }
    }


    public void showImportAlert(final int type) {
        String typeString = "";
        switch (type) {
            case ImportType.ALL: {
                typeString = "整个病历记录";
                break;
            }
            case ImportType.DIAGNOSIS: {
                typeString = "病程记录";
                break;
            }
            case ImportType.ADVICE_AND_PRESCRIPTION: {
                typeString = "处方医嘱";
                break;
            }
        }
        if (canNotEdit()) {
            new MaterialDialog.Builder(getContext())
                    .content("当前就诊已完成并不可修改，不能进行导入操作。")
                    .positiveText("知道了")
                    .show();
            return;
        }
        if (Config.getInt(Constants.APPOINTMENT_MONEY,0)==AppointmentHandler2.Status.PAID) {
            new MaterialDialog.Builder(getContext())
                    .content("当前咨询未开始，您可以等待咨询开始后再进行导入操作。")
                    .positiveText("知道了")
                    .show();
            return;
        }

        String from = appointment.getRecord_id();
        String toId = data.get(currentIndex).getId();
        if (from != null && from.equals(toId)) {
            Toast.makeText(getContext(), "此次导入的病历记录跟填写的病历记录为同一个预约单", Toast.LENGTH_SHORT).show();
        }

        new MaterialDialog.Builder(getContext())
                .content(String.format("您将导入%s,此次导入会覆盖您已经填写的内容, 是否继续导入?", typeString))
                .negativeText("取消")
                .positiveText("继续导入")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        postImportEvent(type);
                        AppointmentHistoryDialog.this.dismiss();
                    }
                }).show();
    }

    public void postImportEvent(int importType) {
        String from = appointment.getId();
        String toId = data.get(currentIndex).getId();
        int appointmentType = data.get(currentIndex).getType();
        ImportDiagnosisEvent event = new ImportDiagnosisEvent(from, toId, importType,appointmentType);
        EventHub.post(event);
    }

    public Appointment getData() {
        return JacksonUtils.fromJson(getArguments().getString(Constants.DATA), Appointment.class);
    }

    @Override
    public int getPosition() {
        return getTabPosition();
    }

    private SharedPreferences getSharedPref() {
        BaseApp me = AppContext.me();
        return me.getSharedPreferences("APPOINTMENT_HISTORY", Context.MODE_PRIVATE);
    }

    private void setTabPosition() {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putInt(recordId + Constants.TAB_POSITION, getBinding().bottomSheetViewpager.getCurrentItem());
        editor.apply();
    }

    private int getTabPosition() {
        return getSharedPref().getInt(recordId + Constants.TAB_POSITION, 1);
    }

    @Override
    protected String getTitle() {
        try {
            return data.get(currentIndex).getTime_bucket().split(" ")[0];
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
