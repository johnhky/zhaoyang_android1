package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMedicineHelperBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.NimTeamId;
import com.doctor.sun.entity.VoipAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.Messenger;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.VoIPCallActivity;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
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
import com.squareup.otto.Subscribe;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by lucas on 2/14/16.
 */
public class MedicineHelperActivity extends BaseFragmentActivity2 implements NimTeamId {
    public static final String ADMIN_DRUG = "[\"admin\"";
    public static final int TYPE_CUSTOM_ACTION = 2;
    public static final int TYPE_EMOTICON = 1;
    public static final long DELAY_MILLIS = 300;

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
                sendTo = response.getYunxinAccid();
                initChat(sendTo);
            }
        });
    }

    private void initChat(String sendTo) {
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mChatAdapter = new MessageAdapter(this, Messenger.getInstance().getMyAccount(), sendTo);
        binding.rvChat.setAdapter(mChatAdapter);

        query = realm.where(TextMsg.class)
                .equalTo("sessionId", sendTo).or().equalTo("sessionId", "admin");
        results = query.findAllSorted("time", Sort.DESCENDING);
        setReadStatus(results);
        if (results.isEmpty()) {
            pullHistory();
        }
        mChatAdapter.setData(results);
        mChatAdapter.onFinishLoadMore(true);
        initCustomAction();
        initSticker();
        initInputLayout();
    }

    private void initSticker() {
        binding.sticker.setData(new StickerViewModel(MedicineHelperActivity.this, binding.sticker));
    }

    private void initCustomAction() {
        binding.customAction.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        CustomActionViewModel customActionViewModel = new CustomActionViewModel(this, new CustomActionViewModel.AudioChatCallback() {
            @Override
            public void startAudioChat(View v) {
                makePhoneCall();
            }
        });
        SimpleAdapter adapter = customActionViewModel.getSimpleAdapter();

        binding.customAction.setAdapter(adapter);
    }

    private void initInputLayout() {
        InputLayoutViewModel vm = new InputLayoutViewModel(binding.input, new InputLayoutViewModel.SendMessageCallback() {
            @Override
            public void sendMessage(EditText editText) {
                MedicineHelperActivity.this.sendMessage(editText);
            }

            @Override
            public TextView.OnEditorActionListener sendMessageAction() {
                return new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            MedicineHelperActivity.this.sendMessage(v);
                        }
                        return true;
                    }
                };
            }
        });
        binding.setInputLayout(vm);
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
                if (mChatAdapter != null) {
                    mChatAdapter.notifyDataSetChanged();
                }
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


    private void sendMessage(TextView inputText) {
        if (inputText.getText().toString().equals("")) {
            Toast.makeText(MedicineHelperActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Messenger.getInstance().isNIMLogin()) {
            Messenger.getInstance().sentTextMsg(sendTo, SessionTypeEnum.P2P, inputText.getText().toString());
            inputText.setText("");
        } else {
            Toast.makeText(MedicineHelperActivity.this, "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
            Messenger.getInstance().login();
        }
    }

    private String getUserData() {
        return ADMIN_DRUG;
    }

    @Subscribe
    public void closeDrawer(CloseDrawerEvent event) {
        binding.drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public String getTeamId() {
        return sendTo;
    }

    @Override
    public String getP2PId() {
        return sendTo;
    }

    @Override
    public SessionTypeEnum getType() {
        return SessionTypeEnum.P2P;
    }


    @Override
    public void onBackPressed() {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout != null && inputLayout.getKeyboardType() != 0) {
            inputLayout.setKeyboardType(0);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * TODO 换成接口,不要使用eventbus ,太反人类了
     * @param event
     */
    @Subscribe
    public void hideIME(HideInputEvent event) {
        binding.getInputLayout().setKeyboardType(0);
    }
}