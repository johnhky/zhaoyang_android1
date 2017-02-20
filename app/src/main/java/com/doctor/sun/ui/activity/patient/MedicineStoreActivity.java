package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMedicineHelperBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.emoji.KeyboardWatcher;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.im.MsgHandler;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.CallServiceEvent;
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.PageCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NimMsgInfo;
import com.doctor.sun.immutables.PrescriptionOrder;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.MessageAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.widget.ExtendedEditText;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
import com.doctor.sun.util.FileChooser;
import com.doctor.sun.util.PermissionsUtil;
import com.doctor.sun.vm.BaseItem;
import com.doctor.sun.vm.CustomActionViewModel;
import com.doctor.sun.vm.InputLayoutViewModel;
import com.doctor.sun.vm.StickerViewModel;
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
import io.realm.RealmChangeListener;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by lucas on 2/14/16.
 */
public class MedicineStoreActivity extends BaseFragmentActivity2 implements NimMsgInfo, ExtendedEditText.KeyboardDismissListener, SwipeRefreshLayout.OnRefreshListener, KeyboardWatcher.OnKeyboardToggleListener {
    public static final String ADMIN_DRUG = "admin";
    public static final double FILE_REQUEST_CODE = FileChooser.FILE_REQUEST_CODE;
    public static final double IMAGE_REQUEST_CODE = CustomActionViewModel.IMAGE_REQUEST_CODE;
    public static final double VIDEO_REQUEST_CODE = CustomActionViewModel.VIDEO_REQUEST_CODE;
    private static final long ONE_DAY = 86400000;

    private PActivityMedicineHelperBinding binding;
    private DrugModule api = Api.of(DrugModule.class);
    private SimpleAdapter mAppointmentAdapter;

    private MessageAdapter mChatAdapter;
    private RealmQuery<TextMsg> query;
    private String sendTo;
    private RealmResults<TextMsg> results;
    private KeyboardWatcher keyboardWatcher;
    private RealmChangeListener<RealmResults<TextMsg>> listener;
    private int screenWidth;
    private PageCallback<PrescriptionOrder> prescriptionOrderPageCallback;
    private LinearLayoutManager layout;

    public static final int PHONE_CALL_REQUEST = 1;
    private PermissionsUtil permissionsUtil;

    public static Intent makeIntent(Context context) {
        return new Intent(context, MedicineStoreActivity.class);
    }

    public static Intent intentForCustomerService(Context context) {
        Intent i = new Intent(context, MedicineStoreActivity.class);
        i.putExtra(Constants.DATA, IntBoolean.NOT_GIVEN);
        return i;
    }

    public static Intent makeIntent(Context context, int appointmentNumber) {
        Intent i = new Intent(context, MedicineStoreActivity.class);
        i.putExtra(Constants.NUMBER, appointmentNumber);
        return i;
    }

    public static Intent makeIntent(Context context, boolean openDrawer) {
        Intent i = new Intent(context, MedicineStoreActivity.class);
        i.putExtra(Constants.OPEN_DRAWER, openDrawer);
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
        screenWidth = Systems.getScreenWidth(this);
        initAppointment();
        permissionsUtil = new PermissionsUtil(this);
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_medicine_helper);
        keyboardWatcher = new KeyboardWatcher(this);
        keyboardWatcher.setListener(this);
    }


    @Override
    protected void onDestroy() {
        if (keyboardWatcher != null) {
            keyboardWatcher.destroy();
        }
        if (results != null) {
            results.removeChangeListeners();
        }
        InputLayoutViewModel.release();
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
        mChatAdapter = new MessageAdapter(IMManager.getInstance().getMyAccount(), sendTo);
        binding.rvChat.setAdapter(mChatAdapter);

        query = getRealm().where(TextMsg.class)
                .equalTo("sessionId", sendTo).or().equalTo("sessionId", "admin");
        results = query.findAllSorted("time", Sort.DESCENDING);
        if (results.isEmpty()) {
            pullHistory();
        }
        mChatAdapter.onFinishLoadMore(true);
        mChatAdapter.add(new Description(R.layout.divider_13dp));
        mChatAdapter.addAll(results);
        binding.refreshLayout.setOnRefreshListener(this);
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        listener = new RealmChangeListener<RealmResults<TextMsg>>() {
            @Override
            public void onChange(RealmResults<TextMsg> element) {
                mChatAdapter.clear();
                mChatAdapter.addAll(element);
                mChatAdapter.notifyDataSetChanged();
            }
        };
        results.addChangeListener(listener);
        initCustomAction();
        initSticker();
        initInputLayout();
    }

    @Override
    public void finish() {
        super.finish();
        setReadStatus();
    }

    private void initSticker() {
        binding.sticker.setData(new StickerViewModel(MedicineStoreActivity.this, binding.sticker));
    }

    private void initCustomAction() {
        binding.customAction.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        CustomActionViewModel customActionViewModel = new CustomActionViewModel(this);
        //TODO
        int intExtra = getIntent().getIntExtra(Constants.DATA, IntBoolean.TRUE);
        SimpleAdapter adapter = customActionViewModel.getSimpleAdapter(intExtra);

        binding.customAction.setAdapter(adapter);
    }

    private void initInputLayout() {
        InputLayoutViewModel vm = new InputLayoutViewModel(binding.input, this);
        binding.setInputLayout(vm);
    }

    private void pullHistory() {
        loadFirstPage();
    }

    private void setReadStatus() {
        if (query == null) return;
        getRealm().beginTransaction();
        RealmResults<TextMsg> haveRead = query.equalTo("haveRead", false).findAll();
        for (TextMsg msg : haveRead) {
            msg.setHaveRead(true);
        }
        getRealm().commitTransaction();
    }

    private void initAppointment() {
        layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvPrescription.setLayoutManager(layout);
        final LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.rvPrescription);
        mAppointmentAdapter = new SimpleAdapter();
        mAppointmentAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        mAppointmentAdapter.mapLayout(R.layout.item_appointment, R.layout.item_drug_order);
        binding.rvPrescription.setAdapter(mAppointmentAdapter);
        binding.flyPrescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.flyPrescription.setVisibility(View.GONE);
            }
        });

        binding.rvPrescription.addOnScrollListener(new ScaleCenterScrollListener(layout));


        //loadPrescriptionOrder();
    }

    private void loadMore() {
        api.myPrescriptions(prescriptionOrderPageCallback.getPage()).enqueue(prescriptionOrderPageCallback);
    }

    private void loadPrescriptionOrder() {
        prescriptionOrderPageCallback = new PageCallback<PrescriptionOrder>(mAppointmentAdapter) {

            @Override
            public void insertFooter() {
                super.insertFooter();
                mAppointmentAdapter.insert(new BaseItem(R.layout.space_vertical_45dp));
            }

            @Override
            public void onInitHeader() {
                super.onInitHeader();
                mAppointmentAdapter.insert(new BaseItem(R.layout.space_vertical_45dp));
            }

            @Override
            protected void handleResponse(PageDTO<PrescriptionOrder> response) {
                super.handleResponse(response);
                if (response.getTotal() == 0) {
                    Toast.makeText(MedicineStoreActivity.this, "您暂无任何药单", Toast.LENGTH_SHORT).show();
                    binding.flyPrescription.setVisibility(View.GONE);
                } else {
                    binding.flyPrescription.setVisibility(View.VISIBLE);
                    showPrescriptionIndicator(layout.findFirstVisibleItemPosition());
                }
            }
        };
        loadMore();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (results != null) {
            listener = new RealmChangeListener<RealmResults<TextMsg>>() {
                @Override
                public void onChange(RealmResults<TextMsg> element) {
                    if (mChatAdapter != null) {
                        mChatAdapter.clear();
                        mChatAdapter.add(new Description(R.layout.divider_13dp));
                        mChatAdapter.addAll(element);
                        mChatAdapter.notifyDataSetChanged();
                        binding.rvChat.scrollToPosition(0);
                    }
                }
            };
            results.addChangeListener(listener);
        }
    }

    @Override
    protected void onStop() {
        if (!getRealm().isClosed()) {
            setReadStatus();
        }
        super.onStop();
    }

    @Subscribe
    public void closeDrawer(CloseDrawerEvent event) {
//        binding.drawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public String getTeamId() {
        return sendTo;
    }

    @Override
    public String getTargetP2PId() {
        return sendTo;
    }

    @Override
    public boolean enablePush() {
        return true;
    }

    @Override
    public int appointmentId() {
        return -1;
    }

    @Override
    public boolean shouldAskServer() {
        return false;
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
        binding.getInputLayout().setKeyboardType(0);
    }

    @Override
    public void onBackPressed() {
        InputLayoutViewModel inputLayout = binding.getInputLayout();
        if (inputLayout != null && inputLayout.getKeyboardType() != 0) {
            inputLayout.setKeyboardType(0);
        } else if (binding.flyPrescription.getVisibility() == View.VISIBLE) {
            binding.flyPrescription.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void onRefresh() {
        long time = getReferenceTime(System.currentTimeMillis());
        Log.e(TAG, "onRefresh: " + time);
        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.P2P, time);
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

    private void loadFirstPage() {
        long time = getReferenceTime(0);

        MsgService service = NIMClient.getService(MsgService.class);
        IMMessage emptyMessage = MessageBuilder.createEmptyMessage(sendTo, SessionTypeEnum.P2P, time);
        InvocationFuture<List<IMMessage>> listInvocationFuture = service.pullMessageHistoryEx(emptyMessage, time + ONE_DAY, 10, QueryDirectionEnum.QUERY_OLD, false);
        listInvocationFuture.setCallback(new RequestCallback<List<IMMessage>>() {
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                if (!imMessages.isEmpty()) {
                    MsgHandler.saveMsgs(imMessages, true);
                }
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

    private long getReferenceTime(long initValue) {
        long time = initValue;
        if (mChatAdapter != null && mChatAdapter.size() > 0) {
            try {
                TextMsg msg = (TextMsg) mChatAdapter.get(mChatAdapter.size() - 1);
                time = msg.getTime();
            } catch (ClassCastException e) {

            }
        }
        return time;
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!Settings.isDoctor()) {
            getMenuInflater().inflate(R.menu.menu_pick_prescription, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_pick_prescription: {
                if (prescriptionOrderPageCallback != null) {
                    prescriptionOrderPageCallback.resetPage();
                }
                loadPrescriptionOrder();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        if (getIntent().getIntExtra(Constants.DATA, IntBoolean.TRUE) == IntBoolean.TRUE) {
            return R.string.title_medicine_store;
        } else {
            return R.string.title_customer_service;
        }
    }

    @Override
    public String getSubTitle() {
        if (getAppointmentNumber() == -1) {
            return "就诊";
        } else {
            return "就诊(" + getAppointmentNumber() + ")";
        }
    }

    @Override
    public int getDuration() {
        return 0;
    }

    private class ScaleCenterScrollListener extends RecyclerView.OnScrollListener {
        private final LinearLayoutManager layout;

        public ScaleCenterScrollListener(LinearLayoutManager layout) {
            this.layout = layout;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int firstVisibleItemPosition = layout.findFirstVisibleItemPosition();
            int lastVisibleItemPosition = layout.findLastVisibleItemPosition();

            for (int i = firstVisibleItemPosition; i <= lastVisibleItemPosition; i++) {
                View view = layout.findViewByPosition(i);

                float viewCenter = (float) view.getRight() - view.getWidth() / 2F;
                float screenCenter = screenWidth / 2F;

                float distance = Math.abs(screenCenter - viewCenter);

                float percentage = distance / screenCenter;
                float scale = 1F - percentage / 20F;
                view.setScaleX(scale);
                view.setScaleY(scale);
            }

            showPrescriptionIndicator(layout.findFirstVisibleItemPosition());
        }
    }

    /**
     * 显示当前item的位置与最后一个item的位置，1／13
     *
     * @param currentPosition
     */
    private void showPrescriptionIndicator(int currentPosition) {
        // position是从0开始的，加1
        currentPosition++;
        // 因为加了header，根据情况减1或减2
        int lastPosition;
        if (prescriptionOrderPageCallback.hasInsertedFooter) {
            lastPosition = mAppointmentAdapter.size() - 2;
        } else {
            lastPosition = mAppointmentAdapter.size() - 1;
        }

        String indicatorText = currentPosition + " / " + lastPosition;
        binding.tvIndicator.setText(indicatorText);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PHONE_CALL_REQUEST) {
            if (grantResults.length > 0 &&
                    grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                onCallServiceEvent(new CallServiceEvent());
            }
        }
    }

    @Subscribe
    public void onCallServiceEvent(CallServiceEvent event) {
        TwoChoiceDialog.show(this, "020-4008352600", "取消", "呼叫", new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(MaterialDialog dialog) {
                if (permissionsUtil.lacksPermissions(PermissionsUtil.PERMISSION_CALL)) {
                    permissionsUtil.requestPermissions(MedicineStoreActivity.this, PHONE_CALL_REQUEST, PermissionsUtil.PERMISSION_CALL);
                    return;
                }
                try {
                    Uri uri = Uri.parse("tel:4008352600");
                    Intent intent = new Intent(Intent.ACTION_CALL, uri);
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });
    }
}