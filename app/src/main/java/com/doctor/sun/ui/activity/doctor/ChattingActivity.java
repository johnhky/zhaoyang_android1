package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityChattingBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.im.Messenger;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.activity.patient.HistoryDetailActivity;
import com.doctor.sun.ui.activity.patient.MedicineHelperActivity;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.TwoSelectorDialog;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.extend.OnSingleClickListener;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * 聊天模块
 * Created by rick on 12/11/15.
 */
public class ChattingActivity extends BaseActivity2 {
    public static final int CALL_PHONE_REQ = 1;
    private ImModule api = Api.of(ImModule.class);
    private ActivityChattingBinding binding;
    private MessageAdapter adapter;
    private RealmQuery<TextMsg> query;
    private RealmChangeListener listener;
    private AppointmentHandler handler;
    private String sendTo;
    private String userData;
    private DrugModule drugModule = Api.of(DrugModule.class);

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, ChattingActivity.class);
        i.putExtra(Constants.DATA, appointment);
        return i;
    }

    private Appointment getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needSendDrug();
        initView();
        initListener();
        initData();
    }

    private void needSendDrug() {
        if (Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE)
            drugModule.needSendDrug(getData().getId()).enqueue(new ApiCallback<NeedSendDrug>() {
                @Override
                protected void handleResponse(NeedSendDrug response) {
                    if (Integer.parseInt(response.getNeed()) == 1) {
                        TwoSelectorDialog.showTwoSelectorDialog(ChattingActivity.this, "就诊已结束，\n是否需要邮寄药品？", "否", "是", new TwoSelectorDialog.GetActionButton() {
                            @Override
                            public void onClickPositiveButton(TwoSelectorDialog dialog) {
                                dialog.dismiss();
                                Intent intent = MedicineHelperActivity.makeIntent(ChattingActivity.this);
                                startActivity(intent);
                            }

                            @Override
                            public void onClickNegativeButton(TwoSelectorDialog dialog) {
                                dialog.dismiss();
                            }
                        });
                    }
                }
            });
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
    }

    private void initData() {
        Appointment data = getData();
        handler = new AppointmentHandler(data);
        data.setHandler(handler);
        binding.setHeader(getHeaderViewModel());

        getIsFinish();

        sendTo = getVoipAccount();
        userData = getUserData();

        adapter = new MessageAdapter(this, data);
        binding.recyclerView.setAdapter(adapter);

        query = realm.where(TextMsg.class)
                .equalTo("sessionId", sendTo);
        RealmResults<TextMsg> results = query.findAllSorted("time", Sort.DESCENDING);
        realm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setHaveRead(true);
        }
        realm.commitTransaction();

        adapter.setData(results);
        adapter.onFinishLoadMore(true);
    }

    private String getUserData() {
        Appointment data = getData();
        return "[" + data.getId() + "," + data.getRecordId() + "]";
    }

    private void initListener() {
        binding.btnSend.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (binding.inputText.getText().toString().equals("")) {
                    Toast.makeText(ChattingActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Messenger.getInstance().isLogin()) {
                    Messenger.getInstance().sentTextMsg(sendTo, userData, binding.inputText.getText().toString());
                    binding.inputText.setText("");
                } else {
                    Toast.makeText(ChattingActivity.this, "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
                    Messenger.getInstance().login();
                }
            }
        });
        binding.ivPhoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getData().getDoctor() == null) {
                    makePhoneCall();
                } else {
                    TwoSelectorDialog.showTwoSelectorDialog(ChattingActivity.this, "通话请求失败\n医生拒绝来电或处于免打扰状态", "取消", "确认", new TwoSelectorDialog.GetActionButton() {
                        @Override
                        public void onClickPositiveButton(final TwoSelectorDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onClickNegativeButton(TwoSelectorDialog dialog) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void makePhoneCall() {
        handler.makePhoneCall(binding.getRoot());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CALL_PHONE_REQ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private HeaderViewModel getHeaderViewModel() {
        HeaderViewModel header = new HeaderViewModel(this);
        return header;
    }

    @Override
    protected void onStart() {
        super.onStart();
        listener = new RealmChangeListener() {
            @Override
            public void onChange() {
                adapter.notifyDataSetChanged();
            }
        };
        realm.addChangeListener(listener);
    }

    @Override
    protected void onStop() {
        if (!realm.isClosed()) {
            realm.removeChangeListener(listener);
        }
        super.onStop();
    }


    @Override
    public void onFirstMenuClicked() {
        super.onFirstMenuClicked();
        Intent i;
        if (getData().getIsFinish() == 1) {
            i = HistoryDetailActivity.makeIntent(this, getData(), ConsultingDetailActivity.POSITION_ANSWER);
        } else {
            i = ConsultingDetailActivity.makeIntent(this, getData(), ConsultingDetailActivity.POSITION_ANSWER);
        }
        startActivity(i);
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        Intent i;
        if (getData().getIsFinish() == 1) {
            i = HistoryDetailActivity.makeIntent(this, getData(), ConsultingDetailActivity.POSITION_SUGGESTION_READONLY);
        } else {
            i = ConsultingDetailActivity.makeIntent(this, getData(), ConsultingDetailActivity.POSITION_SUGGESTION);

        }
        startActivity(i);
    }

    private String getVoipAccount() {
        //假如是医生的话,就发消息给病人
        if (AppContext.isDoctor()) {
            return getData().getVoipAccount();
        } else {
            //假如不是医生的话,就发消息给医生
            Doctor doctor = getData().getDoctor();
            if (doctor != null) {
                return doctor.getVoipAccount();
            } else {
                return getData().getVoipAccount();
            }
        }
    }

    private String getPhoneNO() {
        //假如是医生的话,就发消息给病人
        if (AppContext.isDoctor()) {
            return getData().getPhone();
        } else {
            //假如不是医生的话,就发消息给医生
            Doctor doctor = getData().getDoctor();
            if (doctor != null) {
                return doctor.getPhone();
            } else {
                return getData().getPhone();
            }
        }
    }

    private void getIsFinish() {
        api.finishStat(getData().getId()).enqueue(new ApiCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                if (response.equals("doing")) {
                    getData().setIsFinish(0);
                } else if (response.equals("finish")) {
                    getData().setIsFinish(1);
                }
                String rightFirstTitle;
                if (AppContext.isDoctor()) {
                    rightFirstTitle = "查看问卷";
                } else {
                    rightFirstTitle = "填写问卷";
                }
                binding.getHeader().setLeftTitle(handler.getTitle())
                        .setRightFirstTitle(rightFirstTitle)
                        .setRightTitle("医生建议");
            }
        });
    }
}
