package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityCancelAppointmentBinding;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.widget.SingleChoiceDialog;
import com.doctor.sun.util.JacksonUtils;

import java.util.ArrayList;


/**
 * Created by rick on 14/2/2016.
 */
public class CancelAppointmentActivity extends BaseFragmentActivity2 {

    private AppointmentModule api = Api.of(AppointmentModule.class);

    private ActivityCancelAppointmentBinding binding;
    private ArrayList<String> reasons = new ArrayList<String>();
    private Appointment data;

    public static Intent makeIntent(Context context, Appointment data) {
        Intent i = new Intent(context, CancelAppointmentActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        return i;
    }

    public Appointment getData() {
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
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

    //    @Override
    public void onMenuClicked() {
//        super.onMenuClicked();
        api.dCancel(String.valueOf(data.getId()), binding.reason.etInput.getText().toString()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(CancelAppointmentActivity.this, "成功取消预约", Toast.LENGTH_SHORT).show();
                finish();
                try {
                    getHandler().send(new Message());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
