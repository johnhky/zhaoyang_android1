package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityChattingBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.emoji.KeyboardWatcher;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.handler.AppointmentHandler2;
import com.doctor.sun.entity.im.MsgHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.entity.im.TextMsgFactory;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.event.CallFailedShouldCallPhoneEvent;
import com.doctor.sun.event.FinishRefreshEvent;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.event.RefreshAppointmentEvent;
import com.doctor.sun.event.RejectInComingCallEvent;
import com.doctor.sun.event.SendMessageEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NimMsgInfo;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.widget.ExtendedEditText;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.FileChooser;
import com.doctor.sun.util.HistoryEventHandler;
import com.doctor.sun.util.ItemHelper;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.CustomActionViewModel;
import com.doctor.sun.vo.InputLayoutViewModel;
import com.doctor.sun.vo.StickerViewModel;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.List;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Systems;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;


/**
 * 聊天模块
 * Created by rick on 12/11/15.
 */
public class ChattingActivity extends BaseFragmentActivity2 implements NimMsgInfo, ExtendedEditText.KeyboardDismissListener, SwipeRefreshLayout.OnRefreshListener, KeyboardWatcher.OnKeyboardToggleListener {
    public static final int IMAGE_REQUEST_CODE = CustomActionViewModel.IMAGE_REQUEST_CODE;
    public static final int VIDEO_REQUEST_CODE = CustomActionViewModel.VIDEO_REQUEST_CODE;
    public static final int FILE_REQUEST_CODE = FileChooser.FILE_REQUEST_CODE;

    public static final int ONE_DAY = 86400000;

    private ActivityChattingBinding binding;
    private MessageAdapter mAdapter;
    private RealmQuery<TextMsg> query;
    private String sendTo;
    private DrugModule drugModule = Api.of(DrugModule.class);
    private RealmResults<TextMsg> results;
    private KeyboardWatcher keyboardWatcher;
    private RealmChangeListener<RealmResults<TextMsg>> listener;
    private CustomActionViewModel customActionViewModel;
    private boolean isLoadMore = false;

    private HistoryEventHandler eventHandler;
    private Appointment data;

    public static Intent makeIntent(Context context, Appointment appointment) {
        Intent i = new Intent(context, ChattingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(appointment));
        return i;
    }

    private Appointment getData() {
        if (data == null) {
            String json = getIntent().getStringExtra(Constants.DATA);
            if (json != null) {
                data = JacksonUtils.fromJson(json, Appointment.class);
            }
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        needSendDrug();
        initView();
        initData();
        registerRealmChangeListener();

        addHistoryButton();
    }

    private void addHistoryButton() {
        if (Settings.isDoctor()) {
            View historyButton = LayoutInflater.from(this).inflate(R.layout.item_history_button, binding.flContainer, false);
            historyButton.findViewById(R.id.btn_appointment_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventHub.post(new AppointmentHistoryEvent(getData(), false));
                }
            });
            binding.flContainer.addView(historyButton);
        }
    }

    private void registerRealmChangeListener() {
        createRealmChangeListener();
        if (results != null) {
            results.addChangeListener(listener);
        }
    }

    private void createRealmChangeListener() {
        if (listener == null) {
            listener = new RealmChangeListener<RealmResults<TextMsg>>() {
                @Override
                public void onChange(RealmResults<TextMsg> element) {
                    boolean empty = element.isEmpty();
                    boolean shouldRefresh = false;
                    if (!empty) {
                        TextMsg first = element.first();
                        shouldRefresh = TextMsgFactory.isRefreshMsg(first.getType());
                    }
                    if (shouldRefresh) {
                        Api.of(AppointmentModule.class).appointmentInTid("[" + getTeamId() + "]", "1").enqueue(new RefreshAppointmentCallback());
                    }
                    if (mAdapter != null) {
                        mAdapter.clear();
                        addExtraItems();
                        mAdapter.addAll(element);
                        mAdapter.notifyDataSetChanged();
                        if (isLoadMore) {
                            isLoadMore = false;
                        } else {
                            binding.recyclerView.scrollToPosition(0);
                        }
                    }
                }
            };
        }
    }

    public void addExtraItems() {
        mAdapter.add(new BaseItem(R.layout.divider_13dp));
    }

    private void needSendDrug() {
        //TODO:
        Appointment data = getData();
        if (data == null || data.getType() == AppointmentType.FollowUp) {
            return;
        }

        if (!Settings.isDoctor())
            if (AppointmentHandler2.Status.FINISHED == data.getStatus()) {
                drugModule.needSendDrug(Integer.parseInt(data.getId())).enqueue(new ApiCallback<NeedSendDrug>() {
                    @Override
                    protected void handleResponse(NeedSendDrug response) {
                        if (Integer.parseInt(response.getNeed()) == 1) {
                            TwoChoiceDialog.show(ChattingActivity.this, "医生已给出建议，就诊结束。请问你是否需要代取并邮寄药物？", "否", "是", new TwoChoiceDialog.Options() {
                                @Override
                                public void onApplyClick(MaterialDialog dialog) {
                                    dialog.dismiss();
                                    Intent intent = MedicineStoreActivity.makeIntent(ChattingActivity.this, true);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelClick(MaterialDialog dialog) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    }
                });
            }
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chatting);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(this);
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventHandler = HistoryEventHandler.register(getSupportFragmentManager());
    }

    @Override
    protected void onPause() {
        super.onPause();
        HistoryEventHandler.unregister(eventHandler);
    }

    @Override
    protected void onDestroy() {
        keyboardWatcher.destroy();
        if (!getRealm().isClosed()) {
            results.removeChangeListeners();
        }
        InputLayoutViewModel.release();
        super.onDestroy();
    }


    private void initData() {
        Appointment data = getData();
        if (data == null) {
            return;
        }
        binding.setData(data);


        sendTo = data.getTid();

        mAdapter = new MessageAdapter(data);
        binding.recyclerView.setAdapter(mAdapter);

        query = getRealm().where(TextMsg.class)
                .equalTo("sessionId", sendTo);
        results = query.findAllSorted("time", Sort.DESCENDING);
        if (results.isEmpty()) {
            loadFirstPage();
        }

//        setHaveRead(results);

        addExtraItems();
        mAdapter.addAll(results);
        mAdapter.onFinishLoadMore(true);
        initCustomAction(data);
        initSticker();
        initInputLayout();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Settings.isDoctor()) {
            getMenuInflater().inflate(R.menu.menu_chatting, menu);
            return true;
        } else {
            getMenuInflater().inflate(R.menu.p_menu_chatting, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int tab = 0;
        switch (item.getItemId()) {
            case R.id.action_view: {
                tab = 0;
                break;
            }
            case R.id.action_edit: {
                tab = 1;
                break;
            }
            default: {
            }
        }

        AppointmentHandler2.viewDetail(this, tab, getData());
        return true;
    }

    private void setHaveRead(RealmResults<TextMsg> results) {
        if (results.isEmpty()) {
            return;
        }

        getRealm().beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            TextMsg textMsg = results.get(i);
            textMsg.setHaveRead(true);
        }
        getRealm().commitTransaction();
    }

    @Override
    public void finish() {
        super.finish();
        setHaveRead(query.or().equalTo("sessionId", getTargetP2PId()).equalTo("haveRead", false).findAll());
        ItemHelper.changeItem(getIntent(), JacksonUtils.toJson(binding.getData()));
    }

    private void initInputLayout() {
        InputLayoutViewModel vm = new InputLayoutViewModel(binding.input, this);
        binding.setInputLayout(vm);
    }

    private void initSticker() {
        binding.sticker.setData(new StickerViewModel(this, binding.sticker));
    }

    private void initCustomAction(Appointment data) {
        binding.customAction.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        customActionViewModel = new CustomActionViewModel(this);
        SimpleAdapter adapter = customActionViewModel.getSimpleAdapter();

        binding.customAction.setAdapter(adapter);
    }

    private void makePhoneCall() {
        AppointmentHandler2.makePhoneCall(this, getData());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == AppointmentHandler2.RECORD_AUDIO_PERMISSION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                makePhoneCall();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public String getTeamId() {
        return getData().getTid();
    }

    public String getTargetP2PId() {
        return AppointmentHandler2.getTargetP2PId(getData());
    }

    @Override
    public boolean enablePush() {
        return AppointmentHandler2.enablePush(getData());
    }

    @Override
    public int appointmentId() {
        return Integer.parseInt(getData().getId());
    }

    @Override
    public boolean shouldAskServer() {
        return AppointmentHandler2.shouldAskServer(getData());
    }

    @Override
    public int getDuration() {
        return AppointmentHandler2.getDuration(getData());
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
            handleVideoRequest(requestCode, data);
        }
    }

    private void handleFileRequest(int requestCode, Intent data) {
        if (FILE_REQUEST_CODE == requestCode) {
            // Get the Uri of the selected file
            File file = FileChooser.onActivityResult(this, requestCode, RESULT_OK, data);
            if (file != null) {
                IMManager.getInstance().sentFile(sendTo, getType(), file, enablePush());
            }
        }
    }

    private void handleImageRequest(int requestCode, Intent data) {
        if (IMAGE_REQUEST_CODE == PickImageDialog.getRequestCode(requestCode)) {
            File file = PickImageDialog.handleRequest(this, data, requestCode);
            if (file != null) {
                IMManager.getInstance().sentImage(sendTo, getType(), file, enablePush());
            }
        }
    }

    private void handleVideoRequest(int requestCode, Intent data) {
        if (VIDEO_REQUEST_CODE == requestCode) {
            File file = CustomActionViewModel.getVideoTempFile();
            IMManager.getInstance().sentVideo(sendTo, getType(), file, enablePush());
        }
    }

    /**
     * TODO 换成接口,不要使用eventbus ,太反人类了
     *
     * @param event
     */
    @Subscribe
    public void hideIME(HideInputEvent event) {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout == null) {
            return;
        }
        inputLayout.setKeyboardType(0);
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
    public void onRefresh() {
        isLoadMore = true;
        long time = getRefreshReferenceTime(System.currentTimeMillis());
        Log.e(TAG, "onRefresh: " + time);
        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.Team, time);
        InvocationFuture<List<IMMessage>> listInvocationFuture = service.pullMessageHistoryEx(emptyMessage, time - ONE_DAY, 10, QueryDirectionEnum.QUERY_OLD, false);
        listInvocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                MsgHandler.saveMsgs(imMessages, true);
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

    private long getRefreshReferenceTime(long initValue) {
        long time = initValue;
        if (mAdapter != null && mAdapter.size() > 0) {
            try {
                TextMsg msg = (TextMsg) mAdapter.get(mAdapter.size() - 1);

                time = msg.getTime();
            } catch (ClassCastException e) {
                time = System.currentTimeMillis();
            }
        }
        return time;
    }

    private void loadFirstPage() {
        long time = getRefreshReferenceTime(0);

        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.Team, time);
        InvocationFuture<List<IMMessage>> listInvocationFuture = service.pullMessageHistoryEx(emptyMessage, time + ONE_DAY, 10, QueryDirectionEnum.QUERY_OLD, false);
        listInvocationFuture.setCallback(new FirstPageCallback());
    }

    @Subscribe
    public void onSendMessageEvent(SendMessageEvent e) {
        AppointmentHandler2.alertAppointmentFinished(this, getData());
    }

    @Override
    public void onKeyboardShown(int keyboardSize) {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout == null) {
            return;
        }
        inputLayout.onHideSoftInput();
        Log.e(TAG, "onKeyboardShown: ");
    }

    @Override
    public void onKeyboardClosed() {
    }

    @Override
    public void onKeyboardDismiss() {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout == null) {
            return;
        }
        if (inputLayout.getKeyboardType() != 0) {
            inputLayout.setKeyboardType(0);
            Systems.hideKeyboard(this);
        }
    }

    private static class RefreshAppointmentCallback extends SimpleCallback<PageDTO<Appointment>> {

        @Override
        protected void handleResponse(PageDTO<Appointment> response) {
            List<Appointment> data = response.getData();
            if (data != null && !data.isEmpty()) {
                EventHub.post(new RefreshAppointmentEvent(data.get(0)));
            }
        }
    }

    @Subscribe
    public void onRefreshAppointmentEvent(RefreshAppointmentEvent e) {
        Appointment appointment = e.getData();
        Intent intent = getIntent();
        intent.putExtra(Constants.DATA, JacksonUtils.toJson(appointment));
        setIntent(intent);
        binding.setData(appointment);
        binding.appointmentStatus.setData(appointment);
        mAdapter.setFinishedTime(AppointmentHandler2.getFinishedTime(getData()));
    }


    @Subscribe
    public void onRejectIncomingCallEvent(RejectInComingCallEvent e) {
        if (!Settings.isDoctor()) {
            AppointmentModule appointmentModule = Api.of(AppointmentModule.class);
            appointmentModule.rejectCommunication(e.getType(), Integer.parseInt(getData().getId())).enqueue(new SimpleCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    Log.d(TAG, "handleResponse() called with: response = [" + response + "]");
                }
            });
        }
    }

    @Subscribe
    public void onCallFailed(CallFailedShouldCallPhoneEvent e) {
        if (getData() != null) {
            if (AVChatType.AUDIO.getValue() == e.getChatType()) {
                String text;
                if (Settings.isDoctor()) {
                    text = "网络电话未被接听,现在转为专线020-81212600回拨,请放心接听。";
                } else {
                    text = "网络电话未被接听,现在转为专线020-81212600回拨,接通后限时%ld分钟,请放心接听。";
                }
                Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
                toast.show();
                AppointmentHandler2.callTelephone(this, getData());
            }
        }
    }

    @Subscribe
    public void onFinishRefresh(FinishRefreshEvent event) {
        binding.refreshLayout.setRefreshing(false);
    }

    private static class FirstPageCallback implements RequestCallback<List<IMMessage>> {
        @Override
        public void onSuccess(List<IMMessage> imMessages) {
            MsgHandler.saveMsgs(imMessages, true);
            EventHub.post(new FinishRefreshEvent());
        }

        @Override
        public void onFailed(int i) {
            EventHub.post(new FinishRefreshEvent());
        }

        @Override
        public void onException(Throwable throwable) {
            EventHub.post(new FinishRefreshEvent());
        }
    }
}
