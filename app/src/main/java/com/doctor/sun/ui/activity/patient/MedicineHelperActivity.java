package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
import com.doctor.sun.emoji.KeyboardWatcher;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.im.NimTeamId;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.VoIPCallActivity;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.ExtendedEditText;
import com.doctor.sun.ui.widget.PickImageDialog;
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

import io.ganguo.library.util.Systems;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by lucas on 2/14/16.
 */
public class MedicineHelperActivity extends BaseFragmentActivity2 implements NimTeamId, ExtendedEditText.KeyboardDismissListener, SwipeRefreshLayout.OnRefreshListener, KeyboardWatcher.OnKeyboardToggleListener {
    public static final String ADMIN_DRUG = "admin";
    public static final double FILE_REQUEST_CODE = FileChooser.FILE_REQUEST_CODE;
    public static final double IMAGE_REQUEST_CODE = CustomActionViewModel.IMAGE_REQUEST_CODE;
    private static final long ONE_DAY = 86400000;

    private PActivityMedicineHelperBinding binding;
    private DrugModule api = Api.of(DrugModule.class);
    private SimpleAdapter mAppointmentAdapter;

    private SimpleAdapter<TextMsg, ViewDataBinding> mChatAdapter;
    private RealmQuery<TextMsg> query;
    private String sendTo;
    private RealmResults<TextMsg> results;
    private RealmChangeListener listener;
    private KeyboardWatcher keyboardWatcher;

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
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }

    @Override
    public void onFirstMenuClicked() {
        super.onFirstMenuClicked();
        getRealm().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results.deleteAllFromRealm();
            }
        });
    }

    @Override
    protected void onDestroy() {
        keyboardWatcher.destroy();
        super.onDestroy();
    }

    private void getAccountInitChat() {
        api.serverAccount().enqueue(new SimpleCallback<ImAccount>() {
            @Override
            protected void handleResponse(ImAccount response) {
                sendTo = response.getYunxinAccid();
                initChat(sendTo);
            }
        });
    }

    private void initChat(String sendTo) {
        binding.rvChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mChatAdapter = new MessageAdapter(this, IMManager.getInstance().getMyAccount(), sendTo);
        binding.rvChat.setAdapter(mChatAdapter);

        query = getRealm().where(TextMsg.class)
                .equalTo("sessionId", sendTo).or().equalTo("sessionId", "admin");
        results = query.findAllSorted("time", Sort.DESCENDING);
        setReadStatus(results);
        if (results.isEmpty()) {
            pullHistory();
        }
        mChatAdapter.setData(results);
        mChatAdapter.onFinishLoadMore(true);
        binding.refreshLayout.setOnRefreshListener(this);
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        results.addChangeListener(new RealmChangeListener<RealmResults<TextMsg>>() {
            @Override
            public void onChange(RealmResults<TextMsg> element) {
                mChatAdapter.notifyDataSetChanged();
            }
        });
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
        loadFirstPage();
    }

    private void setReadStatus(RealmResults<TextMsg> results) {
        getRealm().beginTransaction();
        if (results == null) {
            getRealm().commitTransaction();
            return;
        }
        for (int i = 0; i < results.size(); i++) {
            results.get(i).setHaveRead(true);
        }
        getRealm().commitTransaction();
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
                if (response.getTotal() == 0) {
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
        IMManager.getInstance().makePhoneCall(sendTo);
        Intent i = VoIPCallActivity.makeIntent(MedicineHelperActivity.this, VoIPCallActivity.CALLING, sendTo);
        startActivity(i);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (results != null) {
            results.addChangeListener(new RealmChangeListener<RealmResults<TextMsg>>() {
                @Override
                public void onChange(RealmResults<TextMsg> element) {
                    if (mChatAdapter != null) {
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    protected void onStop() {
        if (!getRealm().isClosed()) {
            setReadStatus(results);
            getRealm().removeChangeListener(listener);
        }
        super.onStop();
    }


    private void sendMessage(TextView inputText) {
        if (inputText.getText().toString().equals("")) {
            Toast.makeText(MedicineHelperActivity.this, "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (IMManager.getInstance().isNIMLogin()) {
            IMManager.getInstance().sentTextMsg(sendTo, SessionTypeEnum.P2P, inputText.getText().toString());
            inputText.setText("");
        } else {
            Toast.makeText(MedicineHelperActivity.this, "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
            IMManager.getInstance().login();
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
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onRefresh() {
        long time = System.currentTimeMillis();
        if (mChatAdapter != null && mChatAdapter.size() > 0) {
            TextMsg msg = mChatAdapter.get(mChatAdapter.size() - 1);
            time = msg.getTime();
        }
        Log.e(TAG, "onRefresh: " + time);
        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.P2P, time);
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
        if (mChatAdapter != null && mChatAdapter.size() > 0) {
            TextMsg msg = mChatAdapter.get(mChatAdapter.size() - 1);
            time = msg.getTime();
        }

        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.P2P, time);
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


    @Override
    public void onKeyboardShown(int keyboardSize) {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout != null) {
            inputLayout.onHideSoftInput();
        }
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
}