package com.doctor.sun.ui.widget;

import android.content.Context;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.StackingBehavior;
import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.patient.handler.MedicalRecordHandler;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.List;

/**
 * Created by lucas on 1/16/16.
 */
public class SelectRecordDialog {

    public static void showRecordDialog(final Context context, final AppointmentBuilder appointmentBuilder) {
        ProfileModule api = Api.of(ProfileModule.class);
        api.medicalRecordList().enqueue(new ApiCallback<List<MedicalRecord>>() {
            @Override
            protected void handleResponse(final List<MedicalRecord> response) {
                if (response.size() <= 0) {
                    Toast.makeText(context, "请先建立病历", Toast.LENGTH_SHORT).show();
                    MedicalRecordHandler.newRecord(context, response);
                    return;
                }
                MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
                builder.btnStackedGravity(GravityEnum.CENTER)
                        .stackingBehavior(StackingBehavior.ALWAYS)
                        .buttonsGravity(GravityEnum.CENTER)
                        .titleGravity(GravityEnum.CENTER)
                        .title("选择病历")
                        .neutralText("新建病历")
                        .onNeutral(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MedicalRecordHandler.newRecord(dialog.getContext(), response);
                            }
                        });

                SimpleAdapter mAdapter = new SimpleAdapter();
                final MaterialDialog dialog = builder.adapter(mAdapter, new LinearLayoutManager(context)).build();
                mAdapter.mapLayout(R.layout.item_r_medical_record, R.layout.item_medical_record);
                mAdapter.addAll(response);
                mAdapter.onFinishLoadMore(true);
                mAdapter.notifyDataSetChanged();
                dialog.show();
                if (appointmentBuilder != null) {
                    appointmentBuilder.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(Observable observable, int i) {
                            if (i == BR.record) {
                                dialog.dismiss();
                            }
                            appointmentBuilder.removeOnPropertyChangedCallback(this);
                        }
                    });
                }
            }
        });
    }
}
