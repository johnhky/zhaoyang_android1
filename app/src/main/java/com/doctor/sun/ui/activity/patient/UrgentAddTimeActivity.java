package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityAddTimeBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.EmergencyCall;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.EmergencyModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.util.TimeUtils;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 1/23/16.
 */
public class UrgentAddTimeActivity extends BaseActivity2 {
    private PActivityAddTimeBinding binding;
    private EmergencyModule api = Api.of(EmergencyModule.class);

    public static Intent makeIntent(Context context, EmergencyCall data) {
        Intent i = new Intent(context, UrgentAddTimeActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private EmergencyCall getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private Messenger getHandler() {
        return getIntent().getParcelableExtra(Constants.HANDLER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_add_time);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("增加等候时间");
        binding.setHeader(header);
        binding.tvCreatTime.setText(getData().getCreatedAt().substring(0, getData().getCreatedAt().length() - 3));
        binding.tvCurrentTime.setText("预设等候时间：" + changeTime() + "（已等候" + getPassTime() + "）");
        binding.tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api.addTime(getData().getId(), changeAddTime()).enqueue(new ApiCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {

                    }

                    @Override
                    protected void handleApi(ApiDTO<String> body) {
                        try {
                            Message message = new Message();
                            message.obj = changeAddTime();
                            getHandler().send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        ToastHelper.showMessage(UrgentAddTimeActivity.this, "已增加等待时间");
                        finish();
                    }
                });
            }
        });
    }

    public String getPassTime() {
        long time = System.currentTimeMillis() - getData().getPayTime() * 1000;
        String passTime = TimeUtils.getReadableTime(time);
        return passTime;
    }

    public String changeTime() {
        int hour;
        int minute;
        String waitingTime = "";
        minute = (int) (getData().getWaitingTime() / 60 % 60);
        hour = (int) (getData().getWaitingTime() / 60 / 60);
        if (hour != 0)
            waitingTime = hour + "小时" + minute + "分钟";
        else
            waitingTime = minute + "分钟";
        return waitingTime;
    }

    public int changeAddTime() {
        int addTime = 0;
        if (binding.rbHour.isChecked()) {
            addTime = Integer.parseInt(binding.etTime.getText().toString()) * 60 * 60;
        }
        if (binding.rbMinute.isChecked()) {
            addTime = Integer.parseInt(binding.etTime.getText().toString()) * 60;
        }
        return addTime;
    }
}
