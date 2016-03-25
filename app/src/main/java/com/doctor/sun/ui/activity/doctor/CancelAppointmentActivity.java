package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityCancelAppointmentBinding;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.SingleChoiceDialog;

import java.util.ArrayList;

import io.ganguo.library.common.ToastHelper;


/**
 * Created by rick on 14/2/2016.
 */
public class CancelAppointmentActivity extends BaseActivity2 {

    private AppointmentModule api = Api.of(AppointmentModule.class);

    private HeaderViewModel header;
    private ActivityCancelAppointmentBinding binding;
    private ArrayList<String> reasons = new ArrayList<String>();
    private AppointMent data;

    public static Intent makeIntent(Context context, AppointMent data) {
        Intent i = new Intent(context, CancelAppointmentActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    public AppointMent getData() {
        AppointMent data = getIntent().getParcelableExtra(Constants.DATA);
        return data;
    }

    public Messenger getHandler() {
        Messenger data = getIntent().getParcelableExtra(Constants.HANDLER);
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getData();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cancel_appointment);
        header = new HeaderViewModel(this);
        header.setRightTitle("确定");
        binding.setHeader(header);
        reasons.add("身体不适");
        reasons.add("出差公干");
        reasons.add("预约已满");
        binding.reason.setValues(reasons);
        binding.reason.setSelectedItem(0);
        binding.reason.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceDialog.show(CancelAppointmentActivity.this, binding.reason);
            }
        });
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        api.dCancel(String.valueOf(data.getId()), binding.reason.etInput.getText().toString()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                ToastHelper.showMessage(CancelAppointmentActivity.this, "成功取消预约");
                finish();
                try {
                    getHandler().send(new Message());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
