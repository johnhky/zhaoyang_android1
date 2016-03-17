package com.doctor.sun.ui.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;

import com.doctor.sun.R;
import com.doctor.sun.databinding.DialogSelectRecordBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.patient.handler.CancelHandler;
import com.doctor.sun.ui.activity.patient.handler.NewRecordHandler;
import com.doctor.sun.ui.adapter.SelectRecordAdapter;

import java.util.List;

import io.ganguo.library.ui.dialog.BaseDialog;

/**
 * Created by lucas on 1/16/16.
 */
public class SelectRecordDialog extends BaseDialog {
    private final SelectRecordListener listener;
    private DialogSelectRecordBinding binding;
    private Context context;
    private SelectRecordAdapter mAdapter;
    private ProfileModule api = Api.of(ProfileModule.class);

    public SelectRecordDialog(Context context, SelectRecordListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void beforeInitView() {
        binding = DialogSelectRecordBinding.inflate(LayoutInflater.from(context));
        binding.rvRecord.setLayoutManager(new LinearLayoutManager(context));
        mAdapter = new SelectRecordAdapter(context, this);
        mAdapter.mapLayout(R.layout.item_text, R.layout.item_medical_record);
        binding.rvRecord.setAdapter(mAdapter);
    }

    @Override
    public void initView() {
        setContentView(binding.getRoot());
    }

    @Override
    public void initListener() {

    }

    public SelectRecordListener getListener() {
        return listener;
    }

    @Override
    public void initData() {
        api.medicalRecordList().enqueue(new ApiCallback<List<MedicalRecord>>() {
            @Override
            protected void handleResponse(List<MedicalRecord> response) {
                mAdapter.addAll(response);
                mAdapter.onFinishLoadMore(true);
                mAdapter.add(new NewRecordHandler(getOwnerActivity(),SelectRecordDialog.this));
                mAdapter.add(new CancelHandler(getOwnerActivity(), SelectRecordDialog.this));
            }
        });
    }

    public static void showRecordDialog(Context context, SelectRecordListener listener) {
        final SelectRecordDialog dialog = new SelectRecordDialog(context, listener);
        dialog.show();
    }

    public interface SelectRecordListener {
        void onSelectRecord(SelectRecordDialog dialog,MedicalRecord record);
    }
}
