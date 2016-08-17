package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPatientDetailBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.constans.QTemplateType;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.event.BidirectionalEvent;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.ui.fragment.ReadQuestionFragment;
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
    private ReadQuestionFragment instance;

    public static Intent makeIntent(Context context, Appointment data, int layout) {
        Intent i = new Intent(context, PatientDetailActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.LAYOUT_ID, layout);
        return i;
    }

    private Appointment getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_detail);

        data = getData();

        data.setHandler(new AppointmentHandler(data));
        binding.setData(data);
        instance = ReadQuestionFragment.getInstance(data.getIdString(), QTemplateType.NORMAL, false);
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
        Intent intent = AssignQuestionActivity.makeIntent(this, getData());
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == AppointmentHandler.RECORD_AUDIO_PERMISSION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                if (data != null) {
                    data.getHandler().makePhoneCall(this);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Subscribe
    public void onRejectIncomingCallEvent(BidirectionalEvent e) {
        if (data != null) {
            if (AVChatType.AUDIO.getValue() == e.getChatType()) {
                data.getHandler().callTelephone(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_delete_question: {
//                onFirstMenuClicked();
//                return true;
//            }
            case R.id.action_add_template: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
