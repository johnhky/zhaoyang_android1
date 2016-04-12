package com.doctor.sun.entity.handler;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
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
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.CancelCallback;
import com.doctor.sun.http.callback.DoNothingCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.ui.activity.VoIPCallActivity;
import com.doctor.sun.ui.activity.doctor.CancelAppointmentActivity;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
import com.doctor.sun.ui.activity.doctor.FeedbackActivity;
import com.doctor.sun.ui.activity.doctor.PatientDetailActivity;
import com.doctor.sun.ui.activity.patient.FillForumActivity;
import com.doctor.sun.ui.activity.patient.HistoryDetailActivity;
import com.doctor.sun.ui.activity.patient.PayFailActivity;
import com.doctor.sun.ui.activity.patient.PaySuccessActivity;
import com.doctor.sun.ui.activity.patient.PickDateActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.handler.PayMethodInterface;
import com.doctor.sun.ui.widget.PayMethodDialog;
import com.doctor.sun.ui.widget.TwoSelectorDialog;
import com.doctor.sun.util.PayCallback;
import com.doctor.sun.util.PermissionUtil;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECUserState;

import java.util.HashMap;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.event.EventHub;

/**
 * Created by rick on 11/20/15.
 */
public class AppointmentHandler implements LayoutId, PayMethodInterface, com.doctor.sun.util.PayInterface {
    public static final int RECORD_AUDIO_PERMISSION = 400;
    public static final int PHONE_CALL_PERMISSION = 500;
    private static AppointmentModule api = Api.of(AppointmentModule.class);
    protected Appointment data;
    private static final int layoutId = R.layout.item_appointment;
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
        return "患者" + getPatientName() + getRelation2() + "就诊中";
    }

    public String getUserData() {
        return "[" + data.getId() + "," + data.getRecordId() + "]";
    }

    @Override
    public int getItemLayoutId() {
        return layoutId;
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
                new PayMethodDialog(view.getContext(), AppointmentHandler.this).show();
            }
        };
    }

    @Override
    public void payWithAlipay(final Activity activity) {
        int id;
        if (returnNotPaid()) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        api.buildOrder(id, "alipay").enqueue(new AlipayCallback(activity, data));
    }

    @Override
    public void payWithWeChat(final Activity activity) {
        int id;
        if (returnNotPaid()) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        api.buildWeChatOrder(id, "wechat").enqueue(new WeChatPayCallback(activity, data));
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
            public void onFailure(Throwable t) {
                super.onFailure(t);
                mCallback.onPayFail();
            }
        });
    }

    public void remind(View view) {
        String appointmentId = String.valueOf(data.getId());
        String patientId = String.valueOf(data.getMedicalRecord().getPatientId());

        api.remind(appointmentId, patientId)
                .enqueue(new DoNothingCallback());
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

    public OnItemClickListener pcomment() {
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

    public OnItemClickListener chat() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                Intent intent = ChattingActivity.makeIntent(view.getContext(), data);
                view.getContext().startActivity(intent);
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

    public String getTID() {
        return String.valueOf(data.getTid());
    }

    private static class GotoConsultingCallback extends ApiCallback<String> {
        private final View view;

        public GotoConsultingCallback(View view) {
            this.view = view;
        }

        @Override
        protected void handleResponse(String response) {
            Intent i = ConsultingActivity.makeIntent(view.getContext());
            view.getContext().startActivity(i);
            AppManager.finishAllActivity();
        }

        @Override
        protected void handleApi(ApiDTO<String> body) {
            super.handleApi(body);
            Intent i = ConsultingActivity.makeIntent(view.getContext());
            view.getContext().startActivity(i);
            AppManager.finishAllActivity();
        }
    }

    public String status() {
        if (data.getStatus() == 1) {
            if (data.getHasPay() == 1) {
                return "<font color='#88cb5a'>已支付</font>";
            } else {
                return "<font color='#f76d02'>未支付</font>";
            }
        } else {
            return "<font color='#898989'>已关闭</font>";
        }
    }

    public boolean payVisible() {
        return data.getStatus() == 1 && data.getHasPay() != 1;
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

    public void newOrPayAppointment(View v) {
        if (needReturn() && returnNotPaid()) {
            new PayMethodDialog(v.getContext(), AppointmentHandler.this).show();
        } else {
            //复诊支付
            Doctor doctor = data.getDoctor();
            doctor.setRecordId(String.valueOf(data.getRecordId()));
            Intent intent = PickDateActivity.makeIntent(v.getContext(), doctor, Appointment.QUICK);
            v.getContext().startActivity(intent);
        }
    }

    public void historyDetail(View view) {
        Intent intent = HistoryDetailActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    //病人端预约item点击,根据状态跳转页面
    public void fillForum(View view) {
        if (data.getStatus() == 1) {
            if (data.getHasPay() == 1) {
                Intent intent = FillForumActivity.makeIntent(view.getContext(), data.getId());
                view.getContext().startActivity(intent);
            } else {
                new PayMethodDialog(view.getContext(), AppointmentHandler.this).show();
            }
        } else {
            //TODO
            data.getDoctor().viewDetail(view, 1);
        }
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

    public void sendMessage(TextView inputText) {
        if (inputText.getText().toString().equals("")) {
            Toast.makeText(inputText.getContext(), "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (NIMConnectionState.getInstance().isConnected()) {
            com.doctor.sun.im.Messenger.getInstance().sentTextMsg(getTID(), "", inputText.getText().toString());
            inputText.setText("");
        } else {
            Toast.makeText(inputText.getContext(), "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
            com.doctor.sun.im.Messenger.getInstance().login();
        }
    }

    public void checkCallStatus(View view) {
        if (data.getDoctor() == null) {
            makePhoneCall(view);
        } else {
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
    }

    public void makePhoneCall(final View view) {

        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        boolean hasSelfPermission = PermissionUtil.hasSelfPermission((Activity) view.getContext(), permissions);
        if (hasSelfPermission) {
            final String sendTo = getVoipAccount();
            try {
                ECDevice.getUserState(sendTo, new ECDevice.OnGetUserStateListener() {
                    @Override
                    public void onGetUserState(ECError ecError, ECUserState ecUserState) {
                        if (ecUserState.isOnline()) {
                            com.doctor.sun.im.Messenger.getInstance().makePhoneCall(sendTo);
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

    private void callTelephone(View view) {
        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_PERMISSION);
            return;
        }
        Uri uri = Uri.parse("tel:" + getPhoneNO());
        Intent intent = new Intent(Intent.ACTION_CALL, uri);
        view.getContext().startActivity(intent);
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
        //假如是医生的话,就发消息给病人
        if (AppContext.isDoctor()) {
            return data.getPhone();
        } else {
            //假如不是医生的话,就发消息给医生
            Doctor doctor = data.getDoctor();
            if (doctor != null) {
                return doctor.getPhone();
            } else {
                return data.getPhone();
            }
        }
    }
}