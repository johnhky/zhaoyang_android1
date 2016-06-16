package com.doctor.sun.entity.handler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.CancelCallback;
import com.doctor.sun.http.callback.DoNothingCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.im.NimTeamId;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.ChattingActivityNoMenu;
import com.doctor.sun.ui.activity.VoIPCallActivity;
import com.doctor.sun.ui.activity.doctor.AfterServiceDoingActivity;
import com.doctor.sun.ui.activity.doctor.AfterServiceDoneActivity;
import com.doctor.sun.ui.activity.doctor.CancelAppointmentActivity;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
import com.doctor.sun.ui.activity.doctor.ConsultingDetailActivity;
import com.doctor.sun.ui.activity.doctor.FeedbackActivity;
import com.doctor.sun.ui.activity.doctor.PatientDetailActivity;
import com.doctor.sun.ui.activity.patient.FillForumActivity;
import com.doctor.sun.ui.activity.patient.FinishedOrderActivity;
import com.doctor.sun.ui.activity.patient.HistoryDetailActivity;
import com.doctor.sun.ui.activity.patient.PayFailActivity;
import com.doctor.sun.ui.activity.patient.PaySuccessActivity;
import com.doctor.sun.ui.activity.patient.PickDateActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.handler.PayMethodInterface;
import com.doctor.sun.ui.widget.PayMethodDialog;
import com.doctor.sun.util.ItemHelper;
import com.doctor.sun.util.PayCallback;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.vo.CustomActionViewModel;
import com.doctor.sun.vo.InputLayoutViewModel;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECUserState;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit2.Call;

/**
 * Created by rick on 11/20/15.
 * 所有关于预约的逻辑都在这里, 跳转界面,是否过期等,剩下的进行中时间
 */
public class AppointmentHandler implements PayMethodInterface, com.doctor.sun.util.PayInterface, NimTeamId {
    public static final int RECORD_AUDIO_PERMISSION = 40;
    public static final int PHONE_CALL_PERMISSION = 50;
    private static AppointmentModule api = Api.of(AppointmentModule.class);
    protected Appointment data;
    private DrugModule drugModule = Api.of(DrugModule.class);

    public AppointmentHandler(Appointment data) {
        this.data = data;
    }

    public String getConsultationType() {
        if (data.getType().equals("1")) {
            return "详细就诊";
        } else {
            return "简捷复诊";
        }
    }

    public String getPatientName() {
        if (null != data.getPatientName()) {
            return data.getPatientName();
        } else if (null != data.getMedicalRecord()) {
            return data.getMedicalRecord().getPatientName();
        } else if (null != data.getUrgentRecord()) {
            return data.getUrgentRecord().getPatientName();
        } else {
            return "";
        }
    }

    public String getRecordName() {
        if (null != data.getPatientName()) {
            return data.getRecordName();
        } else if (null != data.getMedicalRecord()) {
            return data.getMedicalRecord().getName();
        } else if (null != data.getUrgentRecord()) {
            return data.getUrgentRecord().getName();
        } else {
            return "";
        }
    }

    public String getBookTime() {
        if (null != data.getBookTime()) {
            return data.getBookTime();
        } else if (null != data.getCreatedAt()) {
            return data.getCreatedAt();
        } else {
            return "";
        }
    }

    public String getRelation() {
        String relation = data.getRelation();
        if (null != relation && !relation.equals("")) {
            return relation;
        } else if (null != data.getMedicalRecord()) {
            return "(" + data.getMedicalRecord().getRelation() + "/" + data.getMedicalRecord().getName() + ")";
        } else if (null != data.getUrgentRecord()) {
            return "(" + data.getUrgentRecord().getRelation() + "/" + data.getUrgentRecord().getName() + ")";
        } else {
            return "";
        }
    }

    public String getRelation2() {
        String relation = data.getRelation();
        if (null != relation && !relation.equals("")) {
            return relation;
        } else if (null != data.getMedicalRecord()) {
            return data.getMedicalRecord().getRelation() + data.getMedicalRecord().getName();
        } else if (null != data.getUrgentRecord()) {
            return data.getUrgentRecord().getRelation() + data.getUrgentRecord().getName();
        } else {
            return "";
        }
    }

    public String getRelation3() {
        String relation = data.getRelation();
        if (null != relation && !relation.equals("")) {
            return relation;
        } else if (null != data.getMedicalRecord()) {
            return getPatientName() + "(患者的" + data.getMedicalRecord().getRelation() + ")";
        } else if (null != data.getUrgentRecord()) {
            return getPatientName() + "(患者的" + data.getUrgentRecord().getRelation() + ")";
        } else {
            return "";
        }
    }

    public String getRecordName2() {
        if (null != data.getMedicalRecord()) {
            return data.getMedicalRecord().getName();
        } else if (null != data.getUrgentRecord()) {
            return data.getUrgentRecord().getName();
        } else {
            return "";
        }
    }

    public String getGender() {
        if (data.getGender() == 0) {
            return "男";
        } else {
            return "女";
        }
    }

    public String getBirthday() {
        if (null != data.getBirthday()) {
            return data.getBirthday();
        }
        if (null != data.getMedicalRecord()) {
            return data.getMedicalRecord().getBirthday();
        } else if (null != data.getUrgentRecord()) {
            return data.getUrgentRecord().getBirthday();
        } else {
            return "";
        }
    }

    public String getConsultingTitle() {
        return "病历:" + getRecordName2();
    }

    public String getUserData() {
        return "[" + data.getId() + "," + data.getRecordId() + "]";
    }


    public OnItemClickListener cancel() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter component, final View view, final BaseViewHolder vh) {
                Intent intent = CancelAppointmentActivity.makeIntent(view.getContext(), data);
                final Handler target = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                component.remove(vh.getAdapterPosition());
                                component.notifyItemRemoved(vh.getAdapterPosition());
                            }
                        }, 1000);
                        return false;
                    }
                });
                android.os.Messenger msg = new android.os.Messenger(target);
                intent.putExtra(Constants.HANDLER, msg);
                view.getContext().startActivity(intent);
            }
        };
    }

    public OnItemClickListener pCancel() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter component, View view, final BaseViewHolder vh) {
                api.pCancel(String.valueOf(data.getId())).enqueue(new CancelCallback(vh, component));
            }
        };
    }

    public OnItemClickListener pay() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter component, final View view, final BaseViewHolder vh) {
                new PayMethodDialog(component.getContext(), AppointmentHandler.this).show();
            }
        };
    }

    @Override
    public void payWithAlipay(final Activity activity, String couponId) {
        int id;
        if (returnNotPaid()) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        api.buildAliPayOrder(id, "alipay", couponId).enqueue(new AlipayCallback(activity, data));
    }

    @Override
    public void payWithWeChat(final Activity activity, String couponId) {
        int id;
        if (returnNotPaid()) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        api.buildWeChatOrder(id, "wechat", couponId).enqueue(new WeChatPayCallback(activity, data));
    }

    @Override
    public void simulatedPay(final BaseAdapter component, final View view, final BaseViewHolder vh) {
        int id;
        if (returnNotPaid()) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        AppointmentModule api = Api.of(AppointmentModule.class);
        final PayCallback mCallback = new PayCallback() {
            @Override
            public void onPaySuccess() {
                Intent intent = PaySuccessActivity.makeIntent(view.getContext(), data);
                view.getContext().startActivity(intent);
            }

            @Override
            public void onPayFail() {
                Intent intent = PayFailActivity.makeIntent(view.getContext(), data, true);
                view.getContext().startActivity(intent);
            }
        };
        api.pay(String.valueOf(id)).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                mCallback.onPaySuccess();
            }

            @Override
            public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                super.onFailure(call, t);
                mCallback.onPayFail();
            }
        });
    }

    public void remind(final View view) {
        String appointmentId = String.valueOf(data.getId());
        String patientId = String.valueOf(data.getMedicalRecord().getPatientId());

        api.remind(appointmentId, patientId)
                .enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        Toast.makeText(view.getContext(), "成功提醒患者", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void refuse(View view) {
        api.refuseConsultation(String.valueOf(data.getReturnListId())).enqueue(new DoNothingCallback());
    }

    public void accept(final View view) {
        new MaterialDialog.Builder(view.getContext()).content("若需要提前进行就诊，请先与患者确认。（点击下方通话键可联系患者）是否确认提前就诊？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ApiCallback<String> callback = new GotoConsultingCallback(view);
                        api.startConsulting(data.getId())
                                .enqueue(callback);
                    }
                }).show();
    }

    public void acceptReturn(final View view) {
        ApiCallback<String> callback = new GotoConsultingCallback(view);

        api.acceptConsultation(toParams())
                .enqueue(callback);
    }

    public void acceptUrgentCall(final View view) {
        ApiCallback<String> callback = new GotoConsultingCallback(view);

        api.acceptUrgentCall(data.getId())
                .enqueue(callback);
    }

    public OnItemClickListener detail() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                detailImpl(view, vh.getItemViewType());
            }
        };
    }

    public void detailImpl(View view, int type) {
        Context context = view.getContext();
        Intent i = PatientDetailActivity.makeIntent(context, data, type);
        context.startActivity(i);
    }

    public OnItemClickListener comment() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, View view, final BaseViewHolder vh) {
                if (!hasPatientComment()) {
                    Context context = view.getContext();
                    Intent i = FeedbackActivity.makeIntent(context, data);
                    Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            data.setPatientPoint((Double) msg.obj);
                            adapter.notifyItemChanged(vh.getAdapterPosition());
                            return false;
                        }
                    }));
                    i.putExtra(Constants.HANDLER, messenger);
                    context.startActivity(i);
                } else {
                    ToastHelper.showMessage(view.getContext(), "已经评价过此预约");
                }
            }
        };
    }

    public OnItemClickListener pComment() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, View view, final BaseViewHolder vh) {
                if (!hasDoctorComment()) {
                    Context context = view.getContext();
                    Intent i = com.doctor.sun.ui.activity.patient.FeedbackActivity.makeIntent(context, data);
                    Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            data.setDoctorPoint((Double) msg.obj);
                            adapter.notifyItemChanged(vh.getAdapterPosition());
                            return false;
                        }
                    }));
                    i.putExtra(Constants.HANDLER, messenger);
                    context.startActivity(i);
                } else {
                    ToastHelper.showMessage(view.getContext(), "已经评价过此预约");
                }
            }
        };
    }

    public HashMap<String, String> toParams() {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", String.valueOf(data.getReturnListId()));
        result.put("recordId", String.valueOf(data.getRecordId()));
        result.put("appointmentId", String.valueOf(data.getAppointmentId()));
        result.put("money", String.valueOf(data.getMoney()));
        return result;
    }

    public String getTitle() {
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        if (userType == AuthModule.PATIENT_TYPE) {
            return data.getDoctor().getName();
        } else {
            return data.getPatientName() + getRelation();
        }
    }

    public View.OnClickListener chat() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getTid() != 0) {
                    Intent intent = ChattingActivity.makeIntent(view.getContext(), data);
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public View.OnClickListener chatNoMenu() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getTid() != 0) {
                    Intent intent = ChattingActivityNoMenu.makeIntent(view.getContext(), data);
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public View.OnClickListener chat(final BaseAdapter adapter, final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (data.getTid() != 0) {
                    Intent intent = ChattingActivity.makeIntent(view.getContext(), data);
                    if (adapter != null && vh != null) {
                        ItemHelper.initCallback(intent, adapter, vh);
                    }
                    view.getContext().startActivity(intent);
                } else {
                    Toast.makeText(view.getContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public OnItemClickListener consultedChat() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, final View view, BaseViewHolder vh) {
                Intent intent = ChattingActivity.makeIntent(view.getContext(), data);
                view.getContext().startActivity(intent);
            }
        };
    }


    @Override
    public SessionTypeEnum getType() {
        return null;
    }

    @Override
    public String getTeamId() {
        return String.valueOf(data.getTid());
    }

    @Override
    public String getP2PId() {
        return data.getYunxinAccid();
    }

    public InputLayoutViewModel.SendMessageCallback getCallback() {
        return new InputLayoutViewModel.SendMessageCallback() {
            @Override
            public void sendMessage(EditText editText) {
                AppointmentHandler.this.sendMessage(editText);
            }

            @Override
            public TextView.OnEditorActionListener sendMessageAction() {
                return AppointmentHandler.this.sendMessageAction();
            }
        };
    }

    public CustomActionViewModel.AudioChatCallback getAudioChatCallback() {
        return new CustomActionViewModel.AudioChatCallback() {
            @Override
            public void startAudioChat(View v) {
                checkCallStatus(v);
            }
        };
    }

    public String getRightFirstTitle() {
        if (data.getType().equals("诊后随访")) {
            return "";
        }
        switch (data.getOrderStatus()) {
            case "进行中":
            case "待建议": {
                return "填写问卷";
            }
            default: {
                return "查看问卷";
            }
        }
    }

    public String getRightTitle() {
        if (data.getType().equals("诊后随访")) {
            switch (data.getOrderStatus()) {
                case Status.DOING: {
                    if (AppContext.isDoctor()) {
                        return "医生建议";
                    } else {
                        return "填写问卷";
                    }
                }
                default: {
                    if (AppContext.isDoctor()) {
                        return "查看问卷";
                    } else {
                        return "查看建议";
                    }
                }
            }
        }

        switch (data.getOrderStatus()) {
            case "进行中":
            case "待建议": {
                return "医生建议";
            }
            default: {
                return "医生建议";
            }
        }
    }

    public Intent getFirstMenu(Context context) {
        if (data.getType().equals("诊后随访")) {
            switch (data.getOrderStatus()) {
                case Status.DOING: {
                    Intent intent = AfterServiceDoingActivity.intentFor(context, String.valueOf(data.getId()), String.valueOf(data.getUrgentRecord().getMedicalRecordId()));
                    context.startActivity(intent);
                    break;
                }
                default: {
                    Intent intent = AfterServiceDoneActivity.intentFor(context, String.valueOf(data.getId()));
                    context.startActivity(intent);
                }
            }

        } else {
            switch (data.getOrderStatus()) {
                case "进行中":
                case "待建议": {
                    return ConsultingDetailActivity.makeIntent(context, getData(), ConsultingDetailActivity.POSITION_ANSWER);
                }
                default: {
                    return HistoryDetailActivity.makeIntent(context, getData(), ConsultingDetailActivity.POSITION_ANSWER);
                }
            }
        }
        return null;
    }

    public Intent getMenu(Context context) {
        if (data.getType().equals("诊后随访")) {
            switch (data.getOrderStatus()) {
                case Status.DOING: {
                    Intent intent = AfterServiceDoingActivity.intentFor(context, String.valueOf(data.getId()), String.valueOf(data.getUrgentRecord().getMedicalRecordId()));
                    context.startActivity(intent);
                    break;
                }
                default: {
                    Intent intent = AfterServiceDoneActivity.intentFor(context, String.valueOf(data.getId()));
                    context.startActivity(intent);
                }
            }
        } else {
            switch (data.getOrderStatus()) {
                case "进行中":
                case "待建议": {
                    return ConsultingDetailActivity.makeIntent(context, getData(), ConsultingDetailActivity.POSITION_SUGGESTION);
                }
                default: {
                    return HistoryDetailActivity.makeIntent(context, getData(), ConsultingDetailActivity.POSITION_SUGGESTION_READONLY);
                }
            }
        }
        return null;
    }

    private static class GotoConsultingCallback extends SimpleCallback<String> {
        private final View view;

        public GotoConsultingCallback(View view) {
            this.view = view;
        }

        @Override
        protected void handleResponse(String response) {
            LoadingHelper.showMaterLoading(view.getContext(), "正在提示患者开始就诊");
            Tasks.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingHelper.hideMaterLoading();
                    Intent i = ConsultingActivity.makeIntent(view.getContext());
                    view.getContext().startActivity(i);
                    AppManager.finishAllActivity();
                }
            }, 350);
        }
    }


    public boolean payVisible() {
        return data.getOrderStatus().equals("未付款") || data.getOrderStatus().equals("未支付");
    }

    public boolean isPayed() {
        return data.getOrderStatus().equals("已付款") || data.getOrderStatus().equals("已支付");
    }

    public boolean hasDoctorComment() {
        return data.getDoctorPoint() > 0;
    }

    public boolean hasPatientComment() {
        return data.getPatientPoint() > 0;
    }


    public boolean needReturn() {
        return data.getReturnInfo() != null && data.getReturnInfo().getNeedReturn() == 1;
    }

    public boolean returnNotPaid() {
        return data.getReturnInfo() != null && data.getReturnInfo().getReturnPaid() != 1 && data.getReturnInfo().getNeedReturn() == 1;
    }

    public View.OnClickListener newOrPayAppointment(final Context context) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (needReturn() && returnNotPaid()) {
                    new PayMethodDialog(context, AppointmentHandler.this).show();
                } else {
                    //复诊支付
                    Doctor doctor = data.getDoctor();
                    doctor.setRecordId(String.valueOf(data.getRecordId()));
                    Intent intent = PickDateActivity.makeIntent(context, doctor, AppointmentType.QUICK);
                    v.getContext().startActivity(intent);
                }
            }
        };
    }

    public void historyDetail(View view) {
        Intent intent = HistoryDetailActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }


    public View.OnClickListener onPatientClickOrder(final BaseAdapter adapter, final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (data.getOrderStatus()) {
                    case "未付款":
                    case "未支付": {
                        new PayMethodDialog(adapter.getContext(), AppointmentHandler.this).show();
                        break;
                    }
                    case "已付款":
                    case "已支付": {
                        Intent intent = FillForumActivity.makeIntent(adapter.getContext(), data.getId());
                        view.getContext().startActivity(intent);
                        break;
                    }
                    case "已完成": {
                        Intent intent = FinishedOrderActivity.makeIntent(adapter.getContext(), data, ConsultingDetailActivity.POSITION_SUGGESTION_READONLY);
                        view.getContext().startActivity(intent);
                        break;
                    }
                    case "进行中":
                    case "待建议": {
                        Intent intent = ChattingActivity.makeIntent(adapter.getContext(), data);
                        view.getContext().startActivity(intent);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        };
    }

    public View.OnClickListener onDoctorClickOrder(final BaseAdapter adapter, final BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (data.getOrderStatus()) {
                    case "已付款":
                    case "已支付": {
                        detailImpl(view, vh.getItemViewType());
                        break;
                    }
                    case "已完成": {
                        Intent chat = ChattingActivity.makeIntent(view.getContext(), data);
                        view.getContext().startActivity(chat);
                        Intent intent = HistoryDetailActivity.makeIntent(view.getContext(), data, ConsultingDetailActivity.POSITION_SUGGESTION_READONLY);
                        view.getContext().startActivity(intent);
                        break;
                    }
                    case "进行中":
                    case "待建议": {
                        Intent intent = ChattingActivity.makeIntent(view.getContext(), data);
                        intent.putExtra(Constants.HANDLER, new Messenger(new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                adapter.notifyItemChanged(vh.getAdapterPosition());
                                return false;
                            }
                        })));
                        view.getContext().startActivity(intent);
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        };
    }

    public OnItemClickListener drugPush() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                drugModule.pushDrug(data.getId()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        EventHub.post(new CloseDrawerEvent());
                    }
                });
            }
        };
    }

    public EditText.OnEditorActionListener sendMessageAction() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage(v);
                }
                return true;
            }
        };
    }

    public void sendMessage(final TextView inputText) {
        if (inputText.getText().toString().equals("")) {
            Toast.makeText(inputText.getContext(), "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NIMConnectionState.getInstance().isLogin()) {
            if (getTeamId() != null && !getTeamId().isEmpty()) {
                IMManager.getInstance().sentTextMsg(getTeamId(), SessionTypeEnum.Team, inputText.getText().toString());
                inputText.setText("");
            } else if (getP2PId() != null && !getP2PId().isEmpty()) {
                IMManager.getInstance().sentTextMsg(getP2PId(), SessionTypeEnum.P2P, inputText.getText().toString());
                inputText.setText("");
            }
        } else {
            NIMConnectionState.getInstance().setCallback(new RequestCallback() {
                @Override
                public void onSuccess(Object o) {
                    sendMessage(inputText);
                }

                @Override
                public void onFailed(int i) {
                    Toast.makeText(inputText.getContext(), "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onException(Throwable throwable) {
                    Toast.makeText(inputText.getContext(), "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
                }
            });
            IMManager.getInstance().login();
        }
    }

    public void alertAppointmentFinished(Context context) {
        if (data.getType().equals("诊后随访")) {
            return;
        }
        if (isFinished() && !AppContext.isDoctor()) {
            Toast.makeText(context, "预约已经结束,请重新预约", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkCallStatus(View view) {
        if (AppContext.isDoctor()) {
            makePhoneCall(view);
        } else {
            showNotAvailableDialog(view);
        }
    }

    private void showNotAvailableDialog(View view) {
        String question = "通话请求失败:\n①医生拒绝来电\n②医生处于免打扰状态";
        MaterialDialog.Builder builder = new MaterialDialog.Builder(view.getContext())
                .content(question)
                .positiveText("确认")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void makePhoneCall(final View view) {
        if (!IMManager.getInstance().isRIMLogin()) {
            IMManager.getInstance().login();
        }

        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.VIBRATE};
        boolean hasSelfPermission = PermissionUtil.hasSelfPermission((Activity) view.getContext(), permissions);
        if (hasSelfPermission) {
            final String sendTo = getVoipAccount();
            try {
                ECDevice.getUserState(sendTo, new ECDevice.OnGetUserStateListener() {
                    @Override
                    public void onGetUserState(ECError ecError, ECUserState ecUserState) {
                        if (ecUserState != null && ecUserState.isOnline()) {
                            IMManager.getInstance().makePhoneCall(sendTo);
                            Intent i = VoIPCallActivity.makeIntent(view.getContext(), VoIPCallActivity.CALLING, sendTo);
                            view.getContext().startActivity(i);
                        } else {
                            callTelephone(view);
                        }
                    }
                });
            } catch (NullPointerException e) {
                callTelephone(view);
            }
        } else {
            ActivityCompat.requestPermissions((Activity) view.getContext(), permissions, RECORD_AUDIO_PERMISSION);
            return;
        }
    }

    private void callTelephone(final View view) {
        ImModule imModule = Api.of(ImModule.class);
        imModule.makePhoneCall(getPhoneNO()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(view.getContext(), "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getVoipAccount() {
        //假如是医生的话,就发消息给病人
        if (AppContext.isDoctor()) {
            return data.getVoipAccount();
        } else {
            //假如不是医生的话,就发消息给医生
            Doctor doctor = data.getDoctor();
            if (doctor != null) {
                return doctor.getVoipAccount();
            } else {
                return data.getVoipAccount();
            }
        }
    }

    public String getPhoneNO() {
        if (AppContext.isDoctor()) {
            return data.getPhone();
        } else {
            Doctor doctor = data.getDoctor();
            if (doctor != null) {
                return doctor.getPhone();
            } else {
                return data.getPhone();
            }
        }
    }

    public String getOrderStatus() {
        return "(" + getStatusLabel() + ")";
    }

    public String styledOrderStatus() {
        return "<font color='" + getStatusColor() + "'>" + getStatusLabel() + "</font>";
    }

    public String styledOrderTypeAndStatus() {
        return "<font color='" + getStatusColor() + "'>" + data.getType() + "-" + getStatusLabel() + "</font>";
    }


    public String getStatusColor() {
        switch (data.getOrderStatus()) {
            case "已完成":
                return "#363636";
            case "未支付":
            case "未付款":
                return "#ff1800";
            case "已支付":
            case "已付款":
                return "#ff8e43";
            case "待建议":
                return "#ff1800";
            case "进行中":
                return "#88cb5a";
            case Status.TODO: {
                return "#ff8e43";
            }
            case Status.DOING: {
                return "#88cb5a";
            }
            case Status.REJECTED: {
                return "#898989";
            }
            case Status.CLOSED: {
                return "#898989";
            }
            case Status.FINISHED: {
                return "#363636";
            }
            case Status.LOCKED: {
                return "#FFFFFF";
            }
            default:
                return "#acacac";
        }
    }

    public boolean isFinished() {
        return getFinishedTime() < System.currentTimeMillis();
    }

    /**
     * 将bookTime: 2016-04-19 15:55－16:10 转换为毫秒
     *
     * @return
     */
    public long getFinishedTime() {
        String bookTime = data.getBookTime();
        try {
            String substring;
            if (bookTime != null) {
                substring = bookTime.substring(17, bookTime.length());
                String date = bookTime.substring(0, 11) + substring;
                return parseDate(date);
            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    public long getStartTime() {
        String bookTime = data.getBookTime();
        if (bookTime == null) {
            return 0;
        } else {
            try {
                String date = bookTime.substring(0, 11) + bookTime.substring(11, 17);
                return parseDate(date);
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public long getRemainingTime() {
        return getFinishedTime() - System.currentTimeMillis();
    }


    public void setFinish() {
        String bookTime = data.getBookTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
        String format = dateFormat.format(new Date(System.currentTimeMillis()));
        String finalBookTime = bookTime.substring(0, 17) + format;

        data.setBookTime(finalBookTime);
    }

    private long parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date parse = dateFormat.parse(date);
        return parse.getTime();
    }

    public int getDefaultAvatar() {
        int result;
        if (AppContext.isDoctor()) {
            if (data.getGender() == Gender.FEMALE) {
                result = R.drawable.female_patient_avatar;
            } else {
                result = R.drawable.male_patient_avatar;
            }
        } else {
            if (data.getGender() == Gender.MALE) {
                result = R.drawable.female_doctor_avatar;
            } else {
                result = R.drawable.male_doctor_avatar;
            }
        }
        return result;
    }

    public String chatStatus() {
        switch (data.getType()) {
            case "诊后随访":
                return "随访" + getStatusLabel();
        }
        if (AppContext.isDoctor()) {
            return "本次咨询已结束";
        } else {
            return "本次咨询已结束,如需咨询,请再次预约";
        }
    }


    @NonNull
    public RealmQuery<TextMsg> queryAllMsg(Realm realm) {
        return realm.where(TextMsg.class)
                .equalTo("sessionId", String.valueOf(data.getTid()));
    }

    public RealmResults<TextMsg> sortedByTime(RealmQuery<TextMsg> query) {
        RealmResults<TextMsg> results = query.findAllSorted("time");
        return results;
    }

    public RealmQuery<TextMsg> unreadMsg(RealmQuery<TextMsg> query) {
        return query.equalTo("haveRead", false);
    }

    public long unreadMsgCount() {
        return unreadMsg(queryAllMsg(Realm.getDefaultInstance())).count();
    }

    public long msgCount() {
        return queryAllMsg(Realm.getDefaultInstance()).count();
    }

    public TextMsg lastMsg() {
        RealmResults<TextMsg> results = sortedByTime(queryAllMsg(Realm.getDefaultInstance()));
        if (results.isEmpty()) {
            return new TextMsg();
        }
        return results.last();
    }

    public long lastMsgTime(Realm realm) {
        return lastMsg().getTime();
    }

    public String lastMsgTime() {
        Date date = new Date(lastMsg().getTime());
        return date.toString();
    }

    public boolean showCommentBtn() {
        return data.getOrderStatus().equals("已完成");
    }

    public Appointment getData() {
        return data;
    }

    public String getStatusLabel() {
        switch (data.getOrderStatus()) {
            case Status.TODO: {
                return "申请中";
            }
            case Status.DOING: {
                return "进行中";
            }
            case Status.REJECTED: {
                return "已拒绝";
            }
            case Status.CLOSED: {
                return "已关闭";
            }
            case Status.FINISHED: {
                return "已完成";
            }
            case Status.LOCKED: {
                return "问卷已锁定";
            }
        }
        return data.getOrderStatus();
    }

    @StringDef
    public @interface Status {
        String ALL = "all";
        String TODO = "todo";
        String DOING = "doing";
        String REJECTED = "rejected";
        String CLOSED = "closed";
        String FINISHED = "finished";
        String LOCKED = "locked";
    }
}