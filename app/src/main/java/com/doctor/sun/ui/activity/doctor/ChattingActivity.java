package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityChattingBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.patient.HistoryDetailActivity;
import com.doctor.sun.ui.activity.patient.MedicineHelperActivity;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.doctor.sun.vo.ClickMenu;
import com.doctor.sun.vo.IntentMenu;
import com.doctor.sun.vo.StickerViewModel;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.util.Systems;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * 聊天模块
 * Created by rick on 12/11/15.
 */
public class ChattingActivity extends BaseFragmentActivity2 {
    public static final int CALL_PHONE_REQ = 1;
    public static final int DELAY_MILLIS = 300;
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

        sendTo = handler.getTID();
        userData = getUserData();

        adapter = new MessageAdapter(this, data);
        binding.recyclerView.setAdapter(adapter);

        query = realm.where(TextMsg.class)
                .equalTo("sessionId", sendTo);
        RealmResults<TextMsg> results = query.findAllSorted("time", Sort.DESCENDING);
        if (results.isEmpty()) {
            pullHistory();
        }

        realm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setHaveRead(true);
        }
        realm.commitTransaction();

        adapter.setData(results);
        adapter.onFinishLoadMore(true);
        initCustomAction();
        initSticker();
    }

    private void initSticker() {
        binding.sticker.setData(new StickerViewModel(this, binding.sticker));
    }

    private void initCustomAction() {
        binding.customAction.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        SimpleAdapter adapter = new SimpleAdapter(ChattingActivity.this);

        adapter.add(new ClickMenu(R.layout.item_menu2, R.drawable.nim_message_plus_photo_selector, "语音电话", null));
        adapter.add(new IntentMenu(R.layout.item_menu2, R.drawable.nim_message_plus_photo_selector, "相册", null));
        adapter.add(new IntentMenu(R.layout.item_menu2, R.drawable.nim_message_plus_video_selector, "拍摄", null));
        adapter.add(new IntentMenu(R.layout.item_menu2, R.drawable.message_plus_video_chat_selector, "视频聊天", null));
        adapter.add(new IntentMenu(R.layout.item_menu2, R.drawable.message_plus_file_selector, "文件传输", null));
        adapter.onFinishLoadMore(true);

        binding.customAction.setAdapter(adapter);
    }

    private void pullHistory() {
        InvocationFuture<List<IMMessage>> listInvocationFuture = NIMClient.getService(MsgService.class).pullMessageHistory(MessageBuilder.createTextMessage(sendTo, SessionTypeEnum.Team, ""), 10, false);
        listInvocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                for (IMMessage imMessage : imMessages) {
                    NIMConnectionState.saveMsg(imMessage, true);
                }
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    private String getUserData() {
        Appointment data = getData();
        return "[" + data.getId() + "," + data.getRecordId() + "]";
    }

    private void initListener() {
        binding.inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.setKeyboardType(0);
                    }
                }, DELAY_MILLIS);
            }
        });
        binding.inputText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.setKeyboardType(0);
                    }
                }, DELAY_MILLIS);
            }
        });
        binding.btnCustomAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Systems.hideKeyboard(ChattingActivity.this);
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.setKeyboardType(2);
                    }
                }, DELAY_MILLIS);
            }
        });
        binding.btnEmoticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Systems.hideKeyboard(ChattingActivity.this);
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.setKeyboardType(1);
                    }
                }, DELAY_MILLIS);
            }
        });
//        binding.btnSend.setOnClickListener(new OnSingleClickListener() {
//            @Override
//            public void onSingleClick(View v) {
//                int visibility = binding.emoji.getVisibility();
//                if (visibility == View.GONE) {
//                    binding.emoji.setVisibility(View.VISIBLE);
//                }else {
//                    binding.emoji.setVisibility(View.GONE);
//                }
//                if (binding.inputText.getText().toString().equals("")) {
//                    Toast.makeText(ChattingActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (NIMConnectionState.getInstance().isConnected()) {
//                    Messenger.getInstance().sentTextMsg(sendTo, userData, binding.inputText.getText().toString());
//                    binding.inputText.setText("");
//                } else {
//                    Toast.makeText(ChattingActivity.this, "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
//                    Messenger.getInstance().login();
//                }
//            }
//        });
//        binding.ivPhoneCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getData().getDoctor() == null) {
//                    makePhoneCall();
//                } else {
//                    api.patientCallTo(getData().getDoctor().getId()).enqueue(new ApiCallback<String>() {
//                        @Override
//                        protected void handleResponse(String response) {
//                            if (response.equals("false")) {
//                                TwoSelectorDialog.showTwoSelectorDialog(ChattingActivity.this, "你还没有通过授权\n请申请给对方打电话", "取消", "马上申请", new TwoSelectorDialog.GetActionButton() {
//                                    @Override
//                                    public void onClickPositiveButton(final TwoSelectorDialog dialog) {
//                                        dialog.dismiss();
//                                    }
//
//                                    @Override
//                                    public void onClickNegativeButton(TwoSelectorDialog dialog) {
//                                        dialog.dismiss();
//                                    }
//                                });
//                            } else {
//                                makePhoneCall();
//                            }
//                        }
//                    });
//                }
//            }
//        });
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
