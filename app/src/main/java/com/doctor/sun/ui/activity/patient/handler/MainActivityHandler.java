package com.doctor.sun.ui.activity.patient.handler;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.AppointmentType;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.RecentAppointment;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.ui.activity.patient.AppointmentListActivity;
import com.doctor.sun.ui.activity.patient.DrugActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.handler.BaseHandler;
import com.doctor.sun.ui.widget.AppointmentTypeDialog;
import com.doctor.sun.ui.widget.SelectRecordDialog;

/**
 * Created by lucas on 1/16/16.
 */
public class MainActivityHandler extends BaseHandler implements LayoutId {
    private RecentAppointment appointment;

    public MainActivityHandler(Activity context) {
        super(context);
        appointment = TokenCallback.getRecentAppointment();
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.p_include_main_activity2;
    }

    public OnItemClickListener record() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {

            }
        };
    }

    public OnItemClickListener appointmentList() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                Intent i = AppointmentListActivity.makeIntent(view.getContext());
                view.getContext().startActivity(i);
            }
        };
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
        Intent intent = DrugActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void searchDoctor(final View view) {
        Intent intent = SearchDoctorActivity.makeIntent(view.getContext(), AppointmentType.DETAIL);
        view.getContext().startActivity(intent);
    }

    public void searchDoctorQuick(final View view) {
        String question = view.getContext().getString(R.string.quick_appointment);
        new MaterialDialog.Builder(view.getContext()).content(question)
                .positiveText("下一步")
                .negativeText("返回")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = SearchDoctorActivity.makeIntent(view.getContext(), AppointmentType.QUICK);
                        view.getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                }).build().show();
    }


    public String getRecentAppointment() {
        if (appointment == null) {
            return "最近预约:  无";
        } else {
            return "最近预约:  " + appointment.getDoctor_name() + "/" + appointment.getBook_time();
        }
    }

    public void doctorType(View view) {
        new AppointmentTypeDialog(view.getContext(), this).show();
    }

    public void showWarning(final View view) {
        String question = view.getContext().getString(R.string.emergency_call_warn);
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

}
