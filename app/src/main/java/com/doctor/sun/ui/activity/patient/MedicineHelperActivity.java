package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMedicineHelperBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.VoipAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.Messenger;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.activity.VoIPCallActivity;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.squareup.otto.Subscribe;

import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by lucas on 2/14/16.
 */
public class MedicineHelperActivity extends BaseActivity2 {
    public static final String ADMIN_DRUG = "[\"admin\"";
    private PActivityMedicineHelperBinding binding;
    private DrugModule api = Api.of(DrugModule.class);
    private SimpleAdapter mAppointmentAdapter;

    private SimpleAdapter<TextMsg, ViewDataBinding> mChatAdapter;
    private RealmQuery<TextMsg> query;
    private String sendTo;
    private RealmResults<TextMsg> results;
    private RealmChangeListener listener;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, MedicineHelperActivity.class);
        return i;
    }

    public static Intent makeIntent(Context context, int appointmentNumber) {
        Intent i = new Intent(context, MedicineHelperActivity.class);
        i.putExtra(Constants.NUMBER, appointmentNumber);
        return i;
    }

    private int getAppointmentNumber() {
        return getIntent().getIntExtra(Constants.NUMBER, -1);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getAccountInitChat();
        initAppointment();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_medicine_helper);
        HeaderViewModel header = new HeaderViewModel(this);
        if (getAppointmentNumber() == -1)
            header.setLeftTitle("就诊").setRightTitle("选择用药");
        else
            header.setLeftTitle("就诊(" + getAppointmentNumber() + ")").setRightTitle("选择用药");
        binding.setHeader(header);
    }

    private void getAccountInitChat() {
        api.serverAccount().enqueue(new SimpleCallback<VoipAccount>() {
            @Override
            protected void handleResponse(VoipAccount response) {
                sendTo = response.getVoipAccount();
                initChat(sendTo);
                initListener();
            }
        });
    }

    private void initChat(String sendTo) {
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mChatAdapter = new MessageAdapter(this, Messenger.getInstance().getMyAccount(), sendTo);
        binding.rvChat.setAdapter(mChatAdapter);

        query = realm.where(TextMsg.class)
                .equalTo("sessionId", sendTo).contains("userData", getUserData());
        results = query.findAllSorted("time", Sort.DESCENDING);
        setReadStatus(results);

        mChatAdapter.setData(results);
        mChatAdapter.onFinishLoadMore(true);
    }

    private void setReadStatus(RealmResults<TextMsg> results) {
        realm.beginTransaction();
        if (results == null) {
            realm.commitTransaction();
            return;
        }
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setHaveRead(true);
        }
        realm.commitTransaction();
    }

    private void initAppointment() {
        binding.rvPrescription.setLayoutManager(new LinearLayoutManager(this));
        mAppointmentAdapter = new SimpleAdapter(this);
        mAppointmentAdapter.mapLayout(R.layout.item_appointment, R.layout.item_medicine_helper);
        binding.rvPrescription.setAdapter(mAppointmentAdapter);
        PageCallback<Appointment> pageCallback = new PageCallback<Appointment>(mAppointmentAdapter) {
            @Override
            protected void handleResponse(PageDTO<Appointment> response) {
                super.handleResponse(response);
                if (response.getTotal().equals("0")) {
                    binding.rvPrescription.setVisibility(View.GONE);
                    binding.ivNoMedicine.setVisibility(View.VISIBLE);
                } else {
                    binding.rvPrescription.setVisibility(View.VISIBLE);
                    binding.ivNoMedicine.setVisibility(View.GONE);
                }
            }
        };
        api.appointments(pageCallback.getPage()).enqueue(pageCallback);
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        binding.drawerLayout.openDrawer(Gravity.RIGHT);
    }

    private void makePhoneCall() {
        Messenger.getInstance().makePhoneCall(sendTo);
        Intent i = VoIPCallActivity.makeIntent(MedicineHelperActivity.this, VoIPCallActivity.CALLING, sendTo);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listener = new RealmChangeListener() {
            @Override
            public void onChange() {
                mChatAdapter.notifyDataSetChanged();
            }
        };
        realm.addChangeListener(listener);
    }

    @Override
    protected void onStop() {
        if (!realm.isClosed()) {
            setReadStatus(results);
            realm.removeChangeListener(listener);
        }
        super.onStop();
    }


    private void initListener() {
        binding.btnSend.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (binding.inputText.getText().toString().equals("")) {
                    Toast.makeText(MedicineHelperActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Messenger.getInstance().isRIMLogin()) {
                    Messenger.getInstance().sentTextMsg(sendTo, getUserData(), binding.inputText.getText().toString());
                    binding.inputText.setText("");
                } else {
                    Toast.makeText(MedicineHelperActivity.this, "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
                    Messenger.getInstance().login();
                }
            }
        });
        binding.ivPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private String getUserData() {
        return ADMIN_DRUG;
    }

    @Subscribe
    public void closeDrawer(CloseDrawerEvent event) {
        binding.drawerLayout.closeDrawer(GravityCompat.END);
    }
}