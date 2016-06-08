package com.doctor.sun.vo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;

import com.doctor.sun.BR;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 7/6/2016.
 */
public class FurtherConsultationVM extends BaseObservable implements LayoutId {

    public ItemPickDate date;
    private Doctor doctor;

    private boolean btnOneChecked = false;
    private boolean btnTwoChecked = false;
    private boolean btnThreeChecked = false;

    public FurtherConsultationVM() {
        date = new ItemPickDate(0, "");
    }

    public void chooseDoctor(Context context, final BaseAdapter adapter, final BaseViewHolder vh) {
        Intent intent = ContactActivity.makeIntent(context, Constants.DOCTOR_REQUEST_CODE);
        intent.putExtra(Constants.HANDLER, new Messenger(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case Constants.DOCTOR_REQUEST_CODE: {
                        int adapterPosition = vh.getAdapterPosition();
                        FurtherConsultationVM o = (FurtherConsultationVM) adapter.get(adapterPosition);
                        Doctor data = msg.getData().getParcelable(Constants.DATA);
                        o.setDoctor(data);
                        adapter.set(adapterPosition, o);
                        adapter.notifyItemChanged(adapterPosition);
                        break;
                    }
                }
                return false;
            }
        })));
        Activity activity = (Activity) context;
        activity.startActivityForResult(intent, Constants.DOCTOR_REQUEST_CODE);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_further_consultation;
    }

    @Bindable
    public boolean isBtnTwoChecked() {
        return btnTwoChecked;
    }

    public void setBtnTwoChecked(boolean btnTwoChecked) {
        this.btnTwoChecked = btnTwoChecked;
        notifyPropertyChanged(BR.btnTwoChecked);
    }

    @Bindable
    public boolean isBtnThreeChecked() {
        return btnThreeChecked;
    }

    public void setBtnThreeChecked(boolean btnThreeChecked) {
        this.btnThreeChecked = btnThreeChecked;
        notifyPropertyChanged(BR.btnThreeChecked);
    }

    @Bindable
    public boolean isBtnOneChecked() {
        return btnOneChecked;
    }

    public void setBtnOneChecked(boolean btnOneChecked) {
        this.btnOneChecked = btnOneChecked;
        notifyPropertyChanged(BR.btnOneChecked);
    }

    @Bindable
    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
        notifyPropertyChanged(BR.doctor);
    }
}
