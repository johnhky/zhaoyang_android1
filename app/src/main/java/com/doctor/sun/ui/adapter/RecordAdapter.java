package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemTextBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.event.CloseDialogEvent;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.activity.doctor.PatientInfoActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * 病人列表项adapter
 * Created by Lynn on 12/30/15.
 */
public class RecordAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private Appointment appointment;

    public RecordAdapter(Context context) {
        super(context);
    }

    public RecordAdapter(Context context, Appointment appointment) {
        super(context);
        this.appointment = appointment;
    }

    @Override
    public void onBindViewBinding(BaseViewHolder<ViewDataBinding> vh, final int position) {
        if (vh.getItemViewType() == R.layout.item_text) {
            final ItemTextBinding binding = (ItemTextBinding) vh.getBinding();
            binding.llySelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    MedicalRecord medicalRecord = (MedicalRecord) get(position);
                    if (Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE) {
                        //病人端
                        if (appointment != null) {
                            appointment.setId(medicalRecord.getAppointmentId().get(
                                    medicalRecord.getAppointmentId().size() - 1));
                            appointment.setTid(medicalRecord.getTid());
                            v.getContext().startActivity(ChattingActivity.makeIntent(v.getContext(), appointment));
                        }
                    } else {
                        v.getContext().startActivity(new Intent(getContext(), PatientInfoActivity.class)
                                .putExtra(Constants.PARAM_PATIENT, medicalRecord));
                    }
                    EventHub.post(new CloseDialogEvent(true));
                }
            });
        }
        super.onBindViewBinding(vh, position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected int getItemLayoutId(int position) {
        return super.getItemLayoutId(position);
    }
}
