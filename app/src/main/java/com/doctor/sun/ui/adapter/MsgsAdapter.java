package com.doctor.sun.ui.adapter;

import android.databinding.ViewDataBinding;

import com.doctor.sun.R;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.vm.LayoutId;

/**
 * Created by Lynn on 1/15/16.
 */
@Deprecated
public class MsgsAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> {
    private Appointment appointment;

    public MsgsAdapter( Appointment appointment) {
        this.appointment = appointment;
    }


    @Override
    public int getItemViewType(int position) {
        return R.layout.item_msgs;
    }
}
