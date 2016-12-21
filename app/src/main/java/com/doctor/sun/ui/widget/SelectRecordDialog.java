package com.doctor.sun.ui.widget;

import android.content.Context;
import android.content.Intent;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

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
import com.doctor.sun.ui.activity.patient.RecordListActivity;
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
            protected void handleResponse(List<MedicalRecord> response) {
                SimpleAdapter mAdapter = new SimpleAdapter();
                MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
                builder.title("选择病历");
                builder.btnStackedGravity(GravityEnum.CENTER);
                builder.neutralText("新建病历");
                builder.onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent intent = RecordListActivity.makeIntent(dialog.getContext(), true);
                        dialog.getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                });
                builder.stackingBehavior(StackingBehavior.ALWAYS);
                builder.buttonsGravity(GravityEnum.CENTER);
                builder.titleGravity(GravityEnum.CENTER);
                final MaterialDialog dialog = builder.adapter(mAdapter, new LinearLayoutManager(context)).build();
                mAdapter.mapLayout(R.layout.item_r_medical_record, R.layout.item_medical_record);
                mAdapter.addAll(response);
                mAdapter.onFinishLoadMore(true);
                mAdapter.notifyDataSetChanged();
                dialog.show();
                if (appointmentBuilder!=null) {
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
