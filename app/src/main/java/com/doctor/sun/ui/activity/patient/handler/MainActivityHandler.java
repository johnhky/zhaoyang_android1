package com.doctor.sun.ui.activity.patient.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.RecentAppointment;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.patient.PAfterServiceActivity;
import com.doctor.sun.ui.activity.patient.PAppointmentListActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.handler.BaseHandler;
import com.doctor.sun.ui.widget.SelectRecordDialog;
import com.doctor.sun.util.JacksonUtils;

import io.ganguo.library.Config;

/**
 * Created by lucas on 1/16/16.
 */
public class MainActivityHandler extends BaseHandler implements LayoutId {
    private RecentAppointment appointment;
    private ObservableInt doingAfterServiceNum = new ObservableInt(0);

    public MainActivityHandler(Activity context) {
        super(context);
        appointment = TokenCallback.getRecentAppointment();
        updatePatientInfo();
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.p_include_main_activity2;
    }


    public void appointmentList(Context context) {
        Intent i = PAppointmentListActivity.makeIntent(context);
        context.startActivity(i);
    }

    public void showRecordList(final View view) {
        SelectRecordDialog.showRecordDialog(view.getContext(), new SelectRecordDialog.SelectRecordListener() {
            @Override
            public void onSelectRecord(SelectRecordDialog dialog, MedicalRecord record) {
                record.getHandler().applyAppointment(view);
                dialog.dismiss();
            }
        });
    }

    public void drugList(View view) {
//        Intent intent = DrugActivity.makeIntent(view.getContext());
//        view.getContext().startActivity(intent);
    }

    public void searchDoctor(final View view) {
        Intent intent = SearchDoctorActivity.makeIntent(view.getContext(), AppointmentType.DETAIL);
        view.getContext().startActivity(intent);
    }

    public void searchDoctorQuick(final View view) {
        Spanned question = Html.fromHtml(view.getContext().getString(R.string.normal_product_brief));
        new MaterialDialog.Builder(view.getContext()).content(question)
                .positiveText("下一步")
                .negativeText("返回")
                .onNegative(dismissCallback())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = SearchDoctorActivity.makeIntent(view.getContext(), AppointmentType.QUICK);
                        view.getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                }).build().show();
    }

    @NonNull
    private MaterialDialog.SingleButtonCallback dismissCallback() {
        return new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        };
    }


    public String getRecentAppointment() {
        if (appointment == null) {
            return "最近预约:  无";
        } else {
            return "最近预约:  " + appointment.getDoctor_name() + "/" + appointment.getBook_time();
        }
    }

    public void doctorType(final View view) {
//        new AppointmentTypeDialog(view.getContext(), this).show();
        Spanned question = Html.fromHtml(view.getContext().getString(R.string.premium_product_brief));
        new MaterialDialog.Builder(view.getContext()).content(question)
                .positiveText("挑选医生")
                .negativeText("取消")
                .onNegative(dismissCallback())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = SearchDoctorActivity.makeIntent(view.getContext(), AppointmentType.DETAIL);
                        view.getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                }).build().show();
    }

    public void showWarning(final View view) {
        Spanned question = Html.fromHtml(view.getContext().getString(R.string.emergency_call_warn));
        new MaterialDialog.Builder(view.getContext()).content(question)
                .positiveText("下一步")
                .negativeText("返回")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        doctorType(view);
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        showRecordList(view);
                    }
                }).build().show();
    }

    public void afterService(Context context) {
        Intent intent = PAfterServiceActivity.intentFor(getContext());
        getContext().startActivity(intent);
    }

    public void updatePatientInfo() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.patientProfile().enqueue(new ApiCallback<PatientDTO>() {
            @Override
            protected void handleResponse(PatientDTO response) {
                Config.putString(Constants.PATIENT_PROFILE, JacksonUtils.toJson(response));
                setDoingAfterServiceNum(response.followUpDoingNum);
            }
        });
    }

    public String getDoingAfterServiceNum() {
        return String.valueOf(doingAfterServiceNum.get());
    }

    public void setDoingAfterServiceNum(int doingAfterServiceNum) {
        this.doingAfterServiceNum.set(doingAfterServiceNum);
        this.doingAfterServiceNum.notifyChange();
    }
}
