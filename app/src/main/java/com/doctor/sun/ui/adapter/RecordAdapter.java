package com.doctor.sun.ui.adapter;

import android.databinding.ViewDataBinding;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ItemRMedicalRecordBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.event.CloseDialogEvent;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.immutables.ImmutableAppointment;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.vm.LayoutId;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;

/**
 * 病人列表项adapter
 * Created by Lynn on 12/30/15.
 */
public class RecordAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private Appointment appointment;

    public RecordAdapter() {
        super();
    }

    public RecordAdapter( Appointment appointment) {
        super();
        this.appointment = appointment;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final int position) {
        if (holder.getItemViewType() == R.layout.item_r_medical_record) {
            final ItemRMedicalRecordBinding binding = (ItemRMedicalRecordBinding) holder.getBinding();
            binding.llySelector.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: ");
                    MedicalRecord medicalRecord = (MedicalRecord) get(position);
                    if (Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE) {
                        //病人端
                        if (appointment != null) {
                            String id = String.valueOf(medicalRecord.getAppointmentId().get(
                                    medicalRecord.getAppointmentId().size() - 1));
                            ImmutableAppointment immutableAppointment = ImmutableAppointment.copyOf(appointment)
                                    .withId(id)
                                    .withTid(String.valueOf(medicalRecord.getTid()));
                            v.getContext().startActivity(ChattingActivity.makeIntent(v.getContext(), immutableAppointment));
                        }
                    } else {
//                        v.getContext().startActivity(new Intent(v.getContext(), PatientInfoActivity.class)
//                                .putExtra(Constants.PARAM_PATIENT, medicalRecord));
                    }
                    EventHub.post(new CloseDialogEvent(true));
                }
            });
        }
        super.onBindViewHolder(holder, position);
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

}
