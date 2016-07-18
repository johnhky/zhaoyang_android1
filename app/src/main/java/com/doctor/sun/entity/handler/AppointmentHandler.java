package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.avchat.activity.AVChatActivity;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.AppointmentBuilder;
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
import com.doctor.sun.im.NimMsgInfo;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.ChattingActivityNoMenu;
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
import com.doctor.sun.ui.handler.PayMethodInterface;
import com.doctor.sun.ui.widget.PayMethodDialog;
import com.doctor.sun.util.ItemHelper;
import com.doctor.sun.util.PayCallback;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

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
public class AppointmentHandler implements PayMethodInterface, com.doctor.sun.util.PayInterface, NimMsgInfo {
    public static final int RECORD_AUDIO_PERMISSION = 40;
    private static AppointmentModule api = Api.of(AppointmentModule.class);
    protected Appointment data;
    private DrugModule drugModule = Api.of(DrugModule.class);

    public AppointmentHandler(Appointment data) {
        this.data = data;
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
//            if (isQuick()) {
//                return splitBookTime();
//            }
            return data.getBookTime();
        } else if (null != data.getCreatedAt()) {
            return data.getCreatedAt();
        } else {
            return "";
        }
    }

    private String splitBookTime() {
        String bookTime = data.getBookTime();
        try {
            String[] split = bookTime.split(" ");
            return split[0];
        } catch (Exception e) {
            return bookTime;
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
        return "病历: " + getRecordName2();
    }

    public String getUserData() {
        return "[" + data.getId() + "," + data.getRecordId() + "]";
    }


    public void cancel2(final BaseAdapter adapter, final BaseViewHolder vh, Appointment data) {
        Intent intent = CancelAppointmentActivity.makeIntent(adapter.getContext(), data);
        final Handler target = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.remove(vh.getAdapterPosition());
                        adapter.notifyItemRemoved(vh.getAdapterPosition());
                    }
                }, 1000);
                return false;
            }
        });
        Messenger msg = new Messenger(target);
        intent.putExtra(Constants.HANDLER, msg);
        adapter.getContext().startActivity(intent);
    }

    public void pCancel(BaseAdapter component, BaseViewHolder vh, int id) {
        api.pCancel(id).enqueue(new CancelCallback(vh, component));
    }

    public void showPayDialog(BaseAdapter component) {
        new PayMethodDialog(component.getContext(), AppointmentHandler.this).show();
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


    /**
     * android:onClick="@{()->data.handler.remind(context,data.id,data.medicalRecord.patientId)}"
     *
     * @param context
     * @param appointmentId
     * @param patientId
     */
    public void remind(final Context context, int appointmentId, int patientId) {
        api.remind(appointmentId, patientId)
                .enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        Toast.makeText(context, "成功提醒患者", Toast.LENGTH_SHORT).show();
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

    /**
     * android:onClick="@{()->data.handler.detail(context,vh.itemViewType)}"
     */
    public void detail(Context context, int type) {
        Intent i = PatientDetailActivity.makeIntent(context, data, type);
        context.startActivity(i);
    }

    public void comment(final BaseAdapter adapter, final BaseViewHolder vh) {
        if (!hasPatientComment()) {
            Context context = adapter.getContext();
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
            ToastHelper.showMessage(adapter.getContext(), "已经评价过此预约");
        }
    }

    public void pComment(final BaseAdapter adapter, final BaseViewHolder vh) {
        if (!hasDoctorComment()) {
            Context context = adapter.getContext();
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
            ToastHelper.showMessage(adapter.getContext(), "已经评价过此预约");
        }
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
            Doctor doctor = data.getDoctor();
            if (doctor == null) {
                return "";
            }
            return doctor.getName();
        } else {
            return getPatientName();
        }
    }

    public void chat(Context context, Appointment data) {
        if (data.getTid() != 0) {
            Intent intent = ChattingActivity.makeIntent(context, data);
            context.startActivity(intent);
        } else {
            if (BuildConfig.DEV_MODE) {
                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void chat(BaseAdapter adapter, BaseViewHolder vh) {
        if (data.getTid() != 0) {
            Intent intent = ChattingActivity.makeIntent(adapter.getContext(), data);
            if (adapter != null && vh != null) {
                ItemHelper.initCallback(intent, adapter, vh);
            }
            adapter.getContext().startActivity(intent);
        } else {
            if (BuildConfig.DEV_MODE) {
                Toast.makeText(adapter.getContext(), "云信群id为0", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void chatNoMenu(Context context) {
        if (data.getTid() != 0) {
            Intent intent = ChattingActivityNoMenu.makeIntent(context, data);
            context.startActivity(intent);
        } else {
            if (BuildConfig.DEV_MODE) {
                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
            }
        }
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
        if (AppContext.isDoctor()) {
            return data.getYunxinAccid();
        } else {
            if (data.getDoctor() != null) {
                return data.getDoctor().getYunxinAccid();
            } else {
                return data.getYunxinAccid();
            }
        }
    }

    @Override
    public boolean enablePush() {
        if (AppContext.isDoctor()) {
            return true;
        }
        boolean isDetailAppointment = isDetail();
        return isDetailAppointment && data.getOrderStatus().equals(Status.A_DOING);
    }

    @Override
    public int appointmentId() {
        return data.getId();
    }

    @Override
    public boolean shouldAskServer() {
        return data.getAppointmentType() == AppointmentType.DETAIL && Status.A_DOING.equals(data.getOrderStatus());
    }
//
//    public CustomActionViewModel.AudioChatCallback getAudioChatCallback() {
//        return new CustomActionViewModel.AudioChatCallback() {
//            @Override
//            public void startAudioChat(View v) {
//                checkCallStatus(v);
//            }
//        };
//    }

    public String getRightFirstTitle() {
        if (AppContext.isDoctor()) {
            return "患者问卷";
        }
        return "我的问卷";
    }

    public String getRightTitle() {
        if (AppContext.isDoctor()) {
            return "病历记录";
        }
        return "医生建议";
    }

    public Intent getFirstMenu(Context context) {
        if (isAfterService()) {
            String id = String.valueOf(data.getId());
            switch (data.getDisplayStatus()) {
                case Status.A_DOING: {
                    String recordId = String.valueOf(data.getUrgentRecord().getMedicalRecordId());
                    Intent intent = AfterServiceDoingActivity.intentFor(context, id, recordId, 0);
                    context.startActivity(intent);
                    break;
                }
                default: {
                    Intent intent = AfterServiceDoneActivity.intentFor(context, id, 0);
                    context.startActivity(intent);
                }
            }

        } else {
            switch (data.getDisplayStatus()) {
                case Status.A_DOING:
                case Status.A_WAITING: {
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
        if (isAfterService()) {
            String id = String.valueOf(data.getId());
            switch (data.getDisplayStatus()) {
                case Status.A_DOING: {
                    String recordId = String.valueOf(data.getUrgentRecord().getMedicalRecordId());
                    Intent intent = AfterServiceDoingActivity.intentFor(context, id, recordId, 1);
                    context.startActivity(intent);
                    break;
                }
                default: {
                    Intent intent = AfterServiceDoneActivity.intentFor(context, id, 1);
                    context.startActivity(intent);
                }
            }
        } else {
            switch (data.getDisplayStatus()) {
                case Status.A_DOING:
                case Status.A_WAITING: {
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
        return data.getOrderStatus().equals(Status.A_UNPAID_LOCALE2) || data.getOrderStatus().equals(Status.A_UNPAID);
    }

    public boolean isPayed() {
        return data.getOrderStatus().equals(Status.A_PAID_LOCALE2) || data.getOrderStatus().equals(Status.A_PAID);
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

    public void newOrPayAppointment(Context context) {
        if (needReturn() && returnNotPaid()) {
            new PayMethodDialog(context, AppointmentHandler.this).show();
        } else {
            //复诊支付
            Doctor doctor = data.getDoctor();
            doctor.setRecordId(String.valueOf(data.getRecordId()));
            AppointmentBuilder appointmentBuilder = new AppointmentBuilder();
            appointmentBuilder.setDoctor(doctor);
            appointmentBuilder.setType(AppointmentType.QUICK);
            Intent intent = PickDateActivity.makeIntent(context, appointmentBuilder);
            context.startActivity(intent);
        }
    }

    public void historyDetail(View view) {
        Intent intent = HistoryDetailActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }


    public void onPatientClickOrder(BaseAdapter adapter) {
        switch (data.getDisplayStatus()) {
            case Status.A_UNPAID:
            case Status.A_UNPAID_LOCALE2: {
                showPayDialog(adapter);
                break;
            }
            case Status.A_PAID:
            case Status.A_PAID_LOCALE2: {
                Intent intent = FillForumActivity.makeIntent(adapter.getContext(), data.getId());
                adapter.getContext().startActivity(intent);
                break;
            }
            case Status.A_FINISHED: {
                Intent intent = FinishedOrderActivity.makeIntent(adapter.getContext(), data, ConsultingDetailActivity.POSITION_SUGGESTION_READONLY);
                adapter.getContext().startActivity(intent);
                break;
            }
            case Status.A_DOING:
            case Status.A_WAITING: {
                chat(adapter.getContext(), data);
                break;
            }
            default: {
                break;
            }
        }
    }

    public void onDoctorClickOrder(final BaseViewHolder vh, final BaseAdapter adapter) {
        switch (data.getDisplayStatus()) {
            case Status.A_PAID:
            case Status.A_PAID_LOCALE2: {
                detail(adapter.getContext(), vh.getItemViewType());
                break;
            }
            case Status.A_FINISHED: {
                chat(adapter, vh);
                Intent intent = HistoryDetailActivity.makeIntent(adapter.getContext(), data, ConsultingDetailActivity.POSITION_SUGGESTION_READONLY);
                adapter.getContext().startActivity(intent);
                break;
            }
            case Status.A_DOING:
            case Status.A_WAITING: {
                chat(adapter, vh);
                break;
            }
            default: {
                break;
            }
        }
    }

    public void drugPush(int id) {
        Call<ApiDTO<String>> apiDTOCall;
        if (isAfterService()) {
            apiDTOCall = drugModule.pushFollowUpDrug(String.valueOf(id));
        } else {
            apiDTOCall = drugModule.pushDrug(String.valueOf(id));
        }

        apiDTOCall.enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                EventHub.post(new CloseDrawerEvent());
            }
        });
    }

    private boolean isAfterService() {
        return data.getAppointmentType() == AppointmentType.AFTER_SERVICE;
    }

    private boolean isQuick() {
        return data.getAppointmentType() == AppointmentType.QUICK;
    }

    private boolean isDetail() {
        return data.getAppointmentType() == AppointmentType.DETAIL;
    }


    public void alertAppointmentFinished(Context context) {
        if (!AppContext.isDoctor()) {
            switch (data.getDisplayStatus()) {
                case Status.REJECTED:
                case Status.CLOSED:
                case Status.FINISHED:
                case Status.LOCKED:
                case Status.A_FINISHED:
                case Status.A_UNPAID:
                case Status.A_UNPAID_LOCALE2:
                    switch (data.getAppointmentType()) {
                        case AppointmentType.AFTER_SERVICE:
                            Toast.makeText(context, "随访已经结束,请耐心等待下次随访", Toast.LENGTH_SHORT).show();
                            break;
                        default: {
                            Toast.makeText(context, "预约已经结束,请重新预约", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    break;
                default:
            }
        }
    }

//    public void checkCallStatus(View view) {
//        if (AppContext.isDoctor()) {
//            makePhoneCall(view);
//        } else {
//            showNotAvailableDialog(view);
//        }
//    }

//    private void showNotAvailableDialog(final View view) {
//        if (shouldAskServer()) {
//            api.canUse(ComunicationType.PHONE_CALL, appointmentId()).enqueue(new SimpleCallback<String>() {
//                @Override
//                protected void handleResponse(String response) {
//                    if ("1".equals(response)) {
//                        makePhoneCall(view);
//                    } else {
//                        showConfirmDialog(view, "医生因个人原因暂时停止该功能，请用文字、图片等继续与医生咨询");
//                    }
//                }
//            });
//        } else {
//            showConfirmDialog(view, "该功能仅限于专属实时咨询的就诊时间内使用");
//        }
//    }

    private void showConfirmDialog(View view, String question) {
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

    //    public void makePhoneCall(final View view) {
//        if (!IMManager.getInstance().isRIMLogin()) {
//            IMManager.getInstance().login();
//        }
//
//        String[] permissions = {Manifest.permission.RECORD_AUDIO, Manifest.permission.VIBRATE};
//        boolean hasSelfPermission = PermissionUtil.hasSelfPermission((Activity) view.getContext(), permissions);
//        if (hasSelfPermission) {
//            final String sendTo = getVoipAccount();
//            try {
//                ECDevice.getUserState(sendTo, new ECDevice.OnGetUserStateListener() {
//                    @Override
//                    public void onGetUserState(ECError ecError, ECUserState ecUserState) {
//                        if (ecUserState != null && ecUserState.isOnline()) {
//                            IMManager.getInstance().makePhoneCall(sendTo);
//                            Intent i = VoIPCallActivity.makeIntent(view.getContext(), VoIPCallActivity.CALLING, sendTo);
//                            view.getContext().startActivity(i);
//                        } else {
//                            callTelephone(view.getContext());
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                callTelephone(view.getContext());
//            }
//        } else {
//            ActivityCompat.requestPermissions((Activity) view.getContext(), permissions, RECORD_AUDIO_PERMISSION);
//            return;
//        }
//    }
    public void makePhoneCall(final Context context) {
        AVChatActivity.start(context, getP2PId(), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL);
    }

    public void callTelephone(final Context context) {
        ImModule imModule = Api.of(ImModule.class);
        imModule.makePhoneCall(getPhoneNO()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(context, "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
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
        return "<font color='" + getStatusColor() + "'>" + data.getDisplayType() + "-" + getStatusLabel() + "</font>";
    }


    public String getStatusColor() {
        switch (data.getDisplayStatus()) {
            case Status.FINISHED:
            case Status.A_FINISHED:
                return "#363636";

            case Status.A_UNPAID:
            case Status.A_UNPAID_LOCALE2:
                return "#ff1800";

            case Status.TODO:
            case Status.A_PAID:
            case Status.A_PAID_LOCALE2:
                return "#ff8e43";

            case Status.A_WAITING:
                return "#ff1800";

            case Status.A_DOING:
            case Status.DOING:
                return "#88cb5a";

            case Status.LOCKED:
            case Status.REJECTED:
            case Status.CLOSED:
            case Status.A_CANCEL:
                return "#898989";
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
        String orderStatus = data.getOrderStatus();
        if (orderStatus == null) {
            orderStatus = "";
        }
        if (orderStatus.equals(Status.FINISHED) || orderStatus.equals(Status.A_FINISHED)) {
            return 0;
        }

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
        switch (data.getAppointmentType()) {
            case AppointmentType.AFTER_SERVICE:
                return "随访" + getStatusLabel();
        }
        if (AppContext.isDoctor()) {
            return "本次咨询已结束";
        } else {
            return "本次咨询已结束,如需咨询,请再次预约";
        }
    }

    public String bookTimeStatus() {
        switch (data.getAppointmentType()) {
            case AppointmentType.AFTER_SERVICE:
                return "发起时间:" + getBookTime();
            default:
                return "预约时间:" + getBookTime();
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

    public long lastMsgTimeMillis() {
        return lastMsg().getTime();
    }


    public boolean showCommentBtn() {
        return data.getOrderStatus().equals(Status.A_FINISHED);
    }

    public Appointment getData() {
        return data;
    }

    public String getStatusLabel() {
        return data.getDisplayStatus();
    }

    @StringDef
    public @interface Status {
        String ALL = "all";
        String TODO = "todo";
        String DOING = "doing";
        String REJECTED = "rejected";
        String CLOSED = "closed";
        String FINISHED = "finished";
        String LOCKED = "问卷已锁定";
        String A_DOING = "进行中";
        String A_FINISHED = "已完成";
        String A_UNPAID = "未支付";
        String A_UNPAID_LOCALE2 = "未付款";
        String A_PAID = "已支付";
        String A_PAID_LOCALE2 = "已付款";
        String A_WAITING = "待建议";
        String A_CANCEL = "医生取消";
    }
}