package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityChattingBinding;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.im.NimTeamId;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.im.Messenger;
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
import com.doctor.sun.ui.widget.ExtendedEditText;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.doctor.sun.util.FileChooser;
import com.doctor.sun.vo.CustomActionViewModel;
import com.doctor.sun.vo.InputLayoutViewModel;
import com.doctor.sun.vo.StickerViewModel;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.squareup.otto.Subscribe;

import java.io.File;
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
public class ChattingActivity extends BaseFragmentActivity2 implements NimTeamId, ExtendedEditText.KeyboardDismissListener, SwipeRefreshLayout.OnRefreshListener {
    public static final int IMAGE_REQUEST_CODE = CustomActionViewModel.IMAGE_REQUEST_CODE;
    public static final int FILE_REQUEST_CODE = FileChooser.FILE_REQUEST_CODE;

    public static final int ONE_DAY = 86400000;

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
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(this);
        binding.getRoot().getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                if (newFocus instanceof EditText) {
                    InputLayoutViewModel inputLayout = binding.getInputLayout();
                    if (inputLayout == null) {
                        return;
                    }

                    binding.getInputLayout().onShowSoftInput();
                }
            }
        });
    }

    private void initData() {
        Appointment data = getData();
        handler = new AppointmentHandler(data);
        data.setHandler(handler);
        binding.setHeader(getHeaderViewModel());
        binding.setData(data);

        getIsFinish();

        sendTo = handler.getTeamId();
        userData = getUserData();

        adapter = new MessageAdapter(this, data);
        binding.recyclerView.setAdapter(adapter);

        query = getRealm().where(TextMsg.class)
                .equalTo("sessionId", sendTo);
        RealmResults<TextMsg> results = query.findAllSorted("time", Sort.DESCENDING);
        if (results.isEmpty()) {
            loadFirstPage();
        }

        setHaveRead(results);

        adapter.setData(results);
        adapter.onFinishLoadMore(true);
        initCustomAction(data);
        initSticker();
        initInputLayout(data);
    }

    private void setHaveRead(RealmResults<TextMsg> results) {
        getRealm().beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            TextMsg textMsg = results.get(i);
            if (!textMsg.getType().equals(String.valueOf(TextMsg.AUDIO))) {
                textMsg.setHaveRead(true);
            }
        }
        getRealm().commitTransaction();
    }

    private void initInputLayout(Appointment data) {
        InputLayoutViewModel vm = new InputLayoutViewModel(binding.input, data.getHandler().getCallback());
        binding.setInputLayout(vm);
    }

    private void initSticker() {
        binding.sticker.setData(new StickerViewModel(this, binding.sticker));
    }

    private void initCustomAction(Appointment data) {
        binding.customAction.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        CustomActionViewModel customActionViewModel = new CustomActionViewModel(this, data.getHandler().getAudioChatCallback());
        SimpleAdapter adapter = customActionViewModel.getSimpleAdapter();

        binding.customAction.setAdapter(adapter);
    }


    private String getUserData() {
        Appointment data = getData();
        return "[" + data.getId() + "," + data.getRecordId() + "]";
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
        return new HeaderViewModel(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        listener = new RealmChangeListener() {
            @Override
            public void onChange() {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        };
        getRealm().addChangeListener(listener);
    }

    @Override
    protected void onStop() {
        if (!getRealm().isClosed()) {
            getRealm().removeChangeListener(listener);
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

    @Override
    public String getTeamId() {
        return handler.getTeamId();
    }

    public String getP2PId() {
        return handler.getP2PId();
    }

    @Override
    public SessionTypeEnum getType() {
        return SessionTypeEnum.Team;
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            handleImageRequest(requestCode, data);
            handleFileRequest(requestCode, data);
        }
    }

    private void handleFileRequest(int requestCode, Intent data) {
        if (FILE_REQUEST_CODE == requestCode) {
            // Get the Uri of the selected file
            File file = FileChooser.onActivityResult(this, requestCode, RESULT_OK, data);
            if (file != null) {
                Messenger.getInstance().sentFile(sendTo, getType(), file);
            }
        }
    }

    private void handleImageRequest(int requestCode, Intent data) {
        if (IMAGE_REQUEST_CODE == PickImageDialog.getRequestCode(requestCode)) {
            File file = PickImageDialog.handleRequest(this, data, requestCode);
            if (file != null) {
                Messenger.getInstance().sentImage(sendTo, getType(), file);
            }
        }
    }

    /**
     * TODO 换成接口,不要使用eventbus ,太反人类了
     *
     * @param event
     */
    @Subscribe
    public void hideIME(HideInputEvent event) {
        binding.getInputLayout().setKeyboardType(0);
    }

    @Override
    public void onBackPressed() {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout != null && inputLayout.getKeyboardType() != 0) {
            inputLayout.setKeyboardType(0);
            Systems.hideKeyboard(this);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onKeyboardDismiss() {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout.getKeyboardType() != 0) {
            inputLayout.setKeyboardType(0);
            Systems.hideKeyboard(this);
        }
    }

    @Override
    public void onRefresh() {
        long time = System.currentTimeMillis();
        if (adapter != null && adapter.size() > 0) {
            TextMsg msg = adapter.get(adapter.size() - 1);
            time = msg.getTime();
        }
        Log.e(TAG, "onRefresh: " + time);
        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.Team, time);
        InvocationFuture<List<IMMessage>> listInvocationFuture = service.pullMessageHistoryEx(emptyMessage, time - ONE_DAY, 10, QueryDirectionEnum.QUERY_OLD, false);
        listInvocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                NIMConnectionState.saveMsgs(imMessages, true);
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(int i) {
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onException(Throwable throwable) {
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadFirstPage() {
        long time = 0;
        if (adapter != null && adapter.size() > 0) {
            TextMsg msg = adapter.get(adapter.size() - 1);
            time = msg.getTime();
        }

        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.Team, time);
        InvocationFuture<List<IMMessage>> listInvocationFuture = service.pullMessageHistoryEx(emptyMessage, time + ONE_DAY, 10, QueryDirectionEnum.QUERY_OLD, false);
        listInvocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                NIMConnectionState.saveMsgs(imMessages, true);
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailed(int i) {
                binding.refreshLayout.setRefreshing(false);
            }

            @Override
            public void onException(Throwable throwable) {
                binding.refreshLayout.setRefreshing(false);
            }
        });
    }
}
