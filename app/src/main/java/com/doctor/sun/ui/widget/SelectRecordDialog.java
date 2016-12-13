package com.doctor.sun.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.patient.handler.NewRecordHandler;
import com.doctor.sun.ui.adapter.SimpleAdapter;

import java.util.List;

/**
 * Created by lucas on 1/16/16.
 */
public class SelectRecordDialog {

    public static void showRecordDialog(final Context context) {
        ProfileModule api = Api.of(ProfileModule.class);
        api.medicalRecordList().enqueue(new ApiCallback<List<MedicalRecord>>() {
            @Override
            protected void handleResponse(List<MedicalRecord> response) {
                SimpleAdapter mAdapter = new SimpleAdapter();
                MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
                builder.title("选择病历");
                builder.titleGravity(GravityEnum.CENTER);
                MaterialDialog dialog = builder.adapter(mAdapter, new LinearLayoutManager(context)).build();
                mAdapter.mapLayout(R.layout.item_r_medical_record, R.layout.item_medical_record);
                mAdapter.addAll(response);
                mAdapter.onFinishLoadMore(true);
                mAdapter.add(new NewRecordHandler(dialog));
                mAdapter.notifyDataSetChanged();
                dialog.show();
            }
        });
    }
}
