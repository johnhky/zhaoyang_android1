package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityChattingBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.emoji.KeyboardWatcher;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.entity.handler.AppointmentHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.event.SendMessageEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.im.NimTeamId;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.ExtendedEditText;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.FileChooser;
import com.doctor.sun.util.ItemHelper;
import com.doctor.sun.util.PermissionUtil;
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
public class ChattingActivity extends BaseFragmentActivity2 implements NimTeamId, ExtendedEditText.KeyboardDismissListener, SwipeRefreshLayout.OnRefreshListener, KeyboardWatcher.OnKeyboardToggleListener {
    public static final int IMAGE_REQUEST_CODE = CustomActionViewModel.IMAGE_REQUEST_CODE;
    public static final int VIDEO_REQUEST_CODE = CustomActionViewModel.VIDEO_REQUEST_CODE;
    public static final int FILE_REQUEST_CODE = FileChooser.FILE_REQUEST_CODE;

    public static final int ONE_DAY = 86400000;

    private ActivityChattingBinding binding;
    private MessageAdapter adapter;
    private RealmQuery<TextMsg> query;
    private AppointmentHandler handler;
    private String sendTo;
    private DrugModule drugModule = Api.of(DrugModule.class);
    private RealmResults<TextMsg> results;
    private KeyboardWatcher keyboardWatcher;
    private RealmChangeListener<RealmResults<TextMsg>> listener;
    private CustomActionViewModel customActionViewModel;

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
        registerRealmChangeListener();
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
                        String body = first.getBody();
                        shouldRefresh = Constants.refreshMsg.contains(body);
                    }
                    if (shouldRefresh) {
                        Api.of(AppointmentModule.class).appointmentInTid("[" + getTeamId() + "]", "1").enqueue(new RefreshAppointmentCallback());
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
            };
        }
    }


    private void needSendDrug() {
        if (getData().getType().equals("诊后随访")) {
            return;
        }

        if (Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE)
            drugModule.needSendDrug(getData().getId()).enqueue(new ApiCallback<NeedSendDrug>() {
                @Override
                protected void handleResponse(NeedSendDrug response) {
                    if (Integer.parseInt(response.getNeed()) == 1) {
                        TwoChoiceDialog.show(ChattingActivity.this, "就诊已结束，\n是否需要邮寄药品？", "否", "是", new TwoChoiceDialog.Options() {
                            @Override
                            public void onApplyClick(TwoChoiceDialog dialog) {
                                dialog.dismiss();
                                Intent intent = MedicineStoreActivity.makeIntent(ChattingActivity.this);
                                startActivity(intent);
                            }

                            @Override
                            public void onCancelClick(TwoChoiceDialog dialog) {
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
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }

    @Override
    protected void onDestroy() {
        keyboardWatcher.destroy();
        if (!getRealm().isClosed()) {
            results.removeChangeListeners();
        }
        super.onDestroy();
    }


    private void initData() {
        Appointment data = getData();
        handler = new AppointmentHandler(data);
        data.setHandler(handler);
        binding.setData(data);

        initActionbar();

        sendTo = handler.getTeamId();

        adapter = new MessageAdapter(this, data);
        binding.recyclerView.setAdapter(adapter);

        query = getRealm().where(TextMsg.class)
                .equalTo("sessionId", sendTo);
        results = query.findAllSorted("time", Sort.DESCENDING);
        if (results.isEmpty()) {
            loadFirstPage();
        }

//        setHaveRead(results);

        adapter.setData(results);
        adapter.onFinishLoadMore(true);
        initCustomAction(data);
        initSticker();
        initInputLayout();
    }

    private void initActionbar() {
        binding.setHeader(getHeaderViewModel());
        inflateMenu();
    }

    private HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this);
    }

    protected void inflateMenu() {
        binding.getHeader().setLeftTitle(handler.getTitle())
                .setRightFirstTitle(handler.getRightFirstTitle())
                .setRightTitle(handler.getRightTitle());
    }


    @Override
    public void onFirstMenuClicked() {
        super.onFirstMenuClicked();
        Intent i = handler.getFirstMenu(this);
        if (i != null) {
            startActivity(i);
        }
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        Intent i = handler.getMenu(this);
        if (i != null) {
            startActivity(i);
        }
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
        setHaveRead(query.or().equalTo("sessionId", getP2PId()).equalTo("haveRead", false).findAll());
        ItemHelper.changeItem(getIntent(), getData());
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
        customActionViewModel = new CustomActionViewModel(this, data.getHandler().getAudioChatCallback());
        SimpleAdapter adapter = customActionViewModel.getSimpleAdapter();

        binding.customAction.setAdapter(adapter);
    }

    private void makePhoneCall() {
        handler.makePhoneCall(binding.getRoot());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == AppointmentHandler.RECORD_AUDIO_PERMISSION) {
            if (PermissionUtil.verifyPermissions(grantResults)) {
                makePhoneCall();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
            handleVideoRequest(requestCode, data);
        }
    }

    private void handleFileRequest(int requestCode, Intent data) {
        if (FILE_REQUEST_CODE == requestCode) {
            // Get the Uri of the selected file
            File file = FileChooser.onActivityResult(this, requestCode, RESULT_OK, data);
            if (file != null) {
                IMManager.getInstance().sentFile(sendTo, getType(), file);
            }
        }
    }

    private void handleImageRequest(int requestCode, Intent data) {
        if (IMAGE_REQUEST_CODE == PickImageDialog.getRequestCode(requestCode)) {
            File file = PickImageDialog.handleRequest(this, data, requestCode);
            if (file != null) {
                IMManager.getInstance().sentImage(sendTo, getType(), file);
            }
        }
    }

    private void handleVideoRequest(int requestCode, Intent data) {
        if (VIDEO_REQUEST_CODE == requestCode) {
            File file = CustomActionViewModel.getVideoTempFile();
            IMManager.getInstance().sentVideo(sendTo, getType(), file);
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

    @Subscribe
    public void onSendMessageEvent(SendMessageEvent e) {
        if (handler != null) {
            handler.alertAppointmentFinished(this);
        }
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
//        InputLayoutViewModel inputLayout = binding.getInputLayout();
//        if (inputLayout.getKeyboardType() != 0) {
//            inputLayout.setKeyboardType(0);
//            Systems.hideKeyboard(this);
//        }
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

    private class RefreshAppointmentCallback extends SimpleCallback<PageDTO<Appointment>> {
        @Override
        protected void handleResponse(PageDTO<Appointment> response) {
            List<Appointment> data = response.getData();
            if (data != null) {
                Appointment appointment = data.get(0);
                Intent intent = getIntent();
                intent.putExtra(Constants.DATA, appointment);
                setIntent(intent);
                handler = appointment.getHandler();
                binding.setData(appointment);
                binding.appointmentStatus.setData(appointment);
                adapter.setFinishedTime(handler.getFinishedTime());
            }
        }
    }


}
