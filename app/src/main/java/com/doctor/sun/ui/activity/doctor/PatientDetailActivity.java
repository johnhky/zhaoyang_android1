package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPatientDetailBinding;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.event.CallFailedShouldCallPhoneEvent;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.ReadQuestionsFragment;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.vo.ItemPatientDetail;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.squareup.otto.Subscribe;


/**
 * Created by rick on 11/24/15.
 */
public class PatientDetailActivity extends BaseFragmentActivity2 {

    private ActivityPatientDetailBinding binding;
    private Appointment data;
    private ReadQuestionsFragment instance;

    public static Intent makeIntent(Context context, Appointment data, int layout) {
        Intent i = new Intent(context, PatientDetailActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        i.putExtra(Constants.LAYOUT_ID, layout);
        return i;
    }

    private Appointment getData() {
        String json = getIntent().getStringExtra(Constants.DATA);
        return JacksonUtils.fromJson(json, Appointment.class);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_detail);

        data = getData();

        binding.setData(data);
        instance = ReadQuestionsFragment.getInstance(data.getId(), QuestionsPath.NORMAL, false);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fly_content, instance)
                .commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        ItemPatientDetail item = new ItemPatientDetail(R.layout.include_patient_detail, data);
        item.setPosition(Long.MAX_VALUE);
        SortedListAdapter adapter = instance.getAdapter();
        if (adapter != null) {
            adapter.insert(item);
        }
    }

    public void onMenuClicked() {
        String appointmentId = getData().getId();
        Intent intent = TemplatesInventoryActivity.intentFor(this, appointmentId);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == AppointmentHandler2.RECORD_AUDIO_PERMISSION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                AppointmentHandler2.makePhoneCall(this);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Subscribe
    public void onRejectIncomingCallEvent(CallFailedShouldCallPhoneEvent e) {
        if (data != null) {
            if (AVChatType.AUDIO.getValue() == e.getChatType()) {
                AppointmentHandler2.callTelephone(this, getData());
            }
        }
    }
}
