package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.avchat.activity.AVChatActivity;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.QuestionsPath;
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
import com.doctor.sun.module.DiagnosisModule;
import com.doctor.sun.module.DrugModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.ChattingActivityNoMenu;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.doctor.AfterServiceDoingActivity;
import com.doctor.sun.ui.activity.doctor.AfterServiceDoneActivity;
import com.doctor.sun.ui.activity.doctor.CancelAppointmentActivity;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
import com.doctor.sun.ui.activity.doctor.PatientDetailActivity;
import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
import com.doctor.sun.ui.activity.patient.EditQuestionActivity;
import com.doctor.sun.ui.activity.patient.FinishedOrderActivity;
import com.doctor.sun.ui.activity.patient.HistoryDetailActivity;
import com.doctor.sun.ui.activity.patient.PayFailActivity;
import com.doctor.sun.ui.activity.patient.PaySuccessActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.fragment.PayAppointmentFragment;
import com.doctor.sun.ui.handler.PayMethodInterface;
import com.doctor.sun.util.ItemHelper;
import com.doctor.sun.util.PayCallback;
import com.doctor.sun.vo.BaseItem;
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

    public boolean isCouponUsed() {
        return getDiscountMoney() > 0;
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

    public void startPayActivity(Context context, int id) {
        Bundle args = PayAppointmentFragment.getArgs(String.valueOf(id));
        Intent intent = SingleFragmentActivity.intentFor(context, "确认支付", args);
        context.startActivity(intent);
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
        simulatedPayImpl(view, id);
    }

    public void simulatedPayImpl(final View view, int id) {
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
        AppointmentModule api = Api.of(AppointmentModule.class);
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

    @Deprecated
    public void refuse(View view) {
        api.refuseConsultation(String.valueOf(data.getReturnListId())).enqueue(new DoNothingCallback());
    }

    @Deprecated
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

//    public void comment(final BaseAdapter adapter, final BaseViewHolder vh) {
//        if (!hasPatientComment()) {
//            Context context = adapter.getContext();
//            Intent i = FeedbackActivity.makeIntent(context, data);
//            Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
//                @Override
//                public boolean handleMessage(Message msg) {
//                    data.setPatientPoint((Double) msg.obj);
//                    adapter.notifyItemChanged(vh.getAdapterPosition());
//                    return false;
//                }
//            }));
//            i.putExtra(Constants.HANDLER, messenger);
//            context.startActivity(i);
//        } else {
//            ToastHelper.showMessage(adapter.getContext(), "已经评价过此预约");
//        }
//    }

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
        result.put("id", String.valueOf(data.getAppointmentId()));
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
        if (Settings.isDoctor()) {
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
        if (Settings.isDoctor()) {
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
        return data.getAppointmentType() == AppointmentType.PREMIUM && Status.A_DOING.equals(data.getOrderStatus());
    }

    @Override
    public int getDuration() {
        try {
            return Integer.parseInt(data.getTakeTime());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public Intent getFirstMenu(Context context, int canEdit) {
        if (isAfterService()) {
            String id = String.valueOf(data.getId());
            switch (data.getDisplayStatus()) {
                case Status.A_WAITING:
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
            Appointment data = getData();
            data.canEdit = canEdit;
            return AppointmentDetailActivity.makeIntent(context, data, AppointmentDetailActivity.POSITION_ANSWER);
        }
        return null;
    }

    public Intent getMenu(Context context, int canEdit) {
        if (isAfterService()) {
            String id = String.valueOf(data.getId());
            switch (data.getDisplayStatus()) {
                case Status.A_WAITING:
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
            Appointment data = getData();
            data.canEdit = canEdit;
            return AppointmentDetailActivity.makeIntent(context, data, AppointmentDetailActivity.POSITION_SUGGESTION);
        }
        return null;
    }

    public int getDiscountMoney() {
        try {
            int money = Integer.parseInt(data.getMoney());
            int unpayMoney = Integer.parseInt(data.getNeedPay());
            return money - unpayMoney;
        } catch (ClassCastException e) {
            return 0;
        } catch (NumberFormatException intCast) {
            try {
                double money = Double.parseDouble(data.getMoney());
                double unpayMoney = Double.parseDouble(data.getNeedPay());
                return (int) (money - unpayMoney);
            } catch (NumberFormatException doubleCast) {
                return 0;
            }
        }
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


    public boolean returnNotPaid() {
        return data.getReturnInfo() != null && data.getReturnInfo().getReturnPaid() != 1 && data.getReturnInfo().getNeedReturn() == 1;
    }

    @Deprecated
    public void historyDetail(View view) {
        Intent intent = HistoryDetailActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public void historyList(Context context, Appointment data) {
        context.startActivity(AppointmentDetailActivity.makeIntent(context,
                data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY));
    }


    public void onPatientClickOrder(BaseAdapter adapter) {
        switch (data.getDisplayStatus()) {
            case Status.A_UNPAID:
            case Status.A_UNPAID_LOCALE2: {
                startPayActivity(adapter.getContext(), data.getId());
                break;
            }
            case Status.A_PAID:
            case Status.A_PAID_LOCALE2: {
                Intent intent = EditQuestionActivity.intentFor(adapter.getContext(), data.getIdString(), QuestionsPath.NORMAL);
                adapter.getContext().startActivity(intent);
                break;
            }
            case Status.A_FINISHED: {
                Intent intent = FinishedOrderActivity.makeIntent(adapter.getContext(), data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY);
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
                Intent intent = HistoryDetailActivity.makeIntent(adapter.getContext(), data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY);
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
        return data.getAppointmentType() == AppointmentType.STANDARD;
    }

    private boolean isDetail() {
        return data.getAppointmentType() == AppointmentType.PREMIUM;
    }


    public void alertAppointmentFinished(Context context) {
        if (!Settings.isDoctor()) {
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


    public void makePhoneCall(final Context context) {
        AVChatActivity.start(context, getP2PId(), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL);
    }

    public void callTelephone(final Context context) {
        ImModule imModule = Api.of(ImModule.class);
        imModule.makeYunXinPhoneCall(getMyPhoneNO(), getPhoneNO()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(context, "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getVoipAccount() {
        //假如是医生的话,就发消息给病人
        if (Settings.isDoctor()) {
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

    public String getMyPhoneNO() {
        if (Settings.isDoctor()) {
            return Settings.getDoctorProfile().getPhone();
        } else {
            Patient patientProfile = Settings.getPatientProfile();
            if (patientProfile != null) {
                return patientProfile.getPhone();
            } else {
                return "";
            }
        }
    }

    public String getPhoneNO() {
        if (Settings.isDoctor()) {
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


    public String chatStatus() {
        switch (data.getAppointmentType()) {
            case AppointmentType.AFTER_SERVICE:
                return "随访" + getStatusLabel();
        }
        if (Settings.isDoctor()) {
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

    public boolean showAnswerQuestionBtn() {
        return getCurrentStatus() == 1;
    }

    public void viewDetail(final Context context, final int tab) {
        DiagnosisModule api = Api.of(DiagnosisModule.class);
        api.diagnosisInfo(appointmentId()).enqueue(new SimpleCallback<DiagnosisInfo>() {
            @Override
            protected void handleResponse(DiagnosisInfo response) {
                int canEdit;
                if (response != null) {
                    canEdit = response.canEdit;
                } else {
                    canEdit = IntBoolean.TRUE;
                }
                answerQuestion(context, tab, canEdit);
            }
        });
    }

    public void answerQuestion(Context context, int tab, int canEdit) {
        if (tab == 0) {
            Intent i = getFirstMenu(context, canEdit);
            if (i != null) {
                context.startActivity(i);
            }
        } else {
            Intent i = getMenu(context, canEdit);
            if (i != null) {
                context.startActivity(i);
            }
        }
    }


    public Appointment getData() {
        return data;
    }

    public String getStatusLabel() {
        return data.getDisplayStatus();
    }

    public void showStatusTimeline(Context context) {
        RecyclerView recyclerView = new RecyclerView(context);
        SimpleAdapter adapter = new SimpleAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        String[] status = new String[]{"待付款", "填问卷", "进行中", "待建议", "已完成"};

        int currentStatus = getCurrentStatus();
        for (int i = 0; i < status.length; i++) {
            BaseItem object = new BaseItem(R.layout.item_appointment_status);
            boolean shouldHighLight = i == currentStatus;
            boolean currentItem = i <= currentStatus;
            object.setEnabled(currentItem);
            object.setUserSelected(shouldHighLight);
            object.setTitle(status[i]);
            adapter.add(object);
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        builder.title("订单状态");
        builder.positiveText("确定");
        builder.customView(recyclerView, true).show();
    }

    public int getCurrentStatus() {
        switch (data.getDisplayStatus()) {
            case Status.LOCKED:
            case Status.REJECTED:
            case Status.CLOSED:
            case Status.A_CANCEL:
            case Status.FINISHED:
            case Status.A_FINISHED:
                return 4;

            case Status.A_UNPAID:
            case Status.A_UNPAID_LOCALE2:
                return 0;

            case Status.TODO:
            case Status.A_PAID:
            case Status.A_PAID_LOCALE2:
                return 1;

            case Status.A_WAITING:
                return 3;

            case Status.A_DOING:
            case Status.DOING:
                return 2;

            default:
                return 5;
        }
    }


    public int getStatusBackground() {
        switch (data.getDisplayStatus()) {
            case Status.LOCKED:
            case Status.REJECTED:
            case Status.CLOSED:
            case Status.A_CANCEL:
            case Status.FINISHED:
            case Status.A_FINISHED:
                return R.drawable.shape_blue_rect_transparent;

            case Status.A_UNPAID:
            case Status.A_UNPAID_LOCALE2:
                return R.drawable.shape_red_rect_transparent;

            case Status.TODO:
            case Status.A_PAID:
            case Status.A_PAID_LOCALE2:
                return R.drawable.shape_orange_rect_transparent;

            case Status.A_WAITING:
                return R.drawable.shape_red_rect_transparent;

            case Status.A_DOING:
            case Status.DOING:
                return R.drawable.shape_green_rect_transparent;

            default:
                return R.drawable.shape_blue_rect_transparent;
        }
    }

    public int getStatusArrow() {
        switch (data.getDisplayStatus()) {
            case Status.LOCKED:
            case Status.REJECTED:
            case Status.CLOSED:
            case Status.A_CANCEL:
            case Status.FINISHED:
            case Status.A_FINISHED:
                return R.drawable.ic_chevron_right_blue_24dp;

            case Status.A_UNPAID:
            case Status.A_UNPAID_LOCALE2:
                return R.drawable.ic_chevron_right_red_24dp;

            case Status.TODO:
            case Status.A_PAID:
            case Status.A_PAID_LOCALE2:
                return R.drawable.ic_chevron_right_orange_24dp;

            case Status.A_WAITING:
                return R.drawable.ic_chevron_right_red_24dp;

            case Status.A_DOING:
            case Status.DOING:
                return R.drawable.ic_chevron_right_green_24dp;

            default:
                return R.drawable.ic_chevron_right_green_24dp;
        }
    }


    public String styledOrderStatus2() {
        return "<font color='" + getStatusColor2() + "'>" + getStatusLabel() + "</font>";
    }

    public String getStatusColor2() {
        switch (data.getDisplayStatus()) {
            case Status.FINISHED:
            case Status.A_FINISHED:
                return "#339de1";

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