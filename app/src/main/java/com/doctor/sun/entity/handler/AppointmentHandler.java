//package com.doctor.sun.entity.handler;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.os.Messenger;
//import android.support.annotation.IntDef;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.Toast;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.doctor.sun.BuildConfig;
//import com.doctor.sun.R;
//import com.doctor.sun.Settings;
//import com.doctor.sun.avchat.activity.AVChatActivity;
//import com.doctor.sun.bean.Constants;
//import com.doctor.sun.dto.ApiDTO;
//import com.doctor.sun.immutables.Appointment;
//import com.doctor.sun.entity.Doctor;
//import com.doctor.sun.entity.Patient;
//import com.doctor.sun.entity.constans.AppointmentType;
//import com.doctor.sun.entity.constans.IntBoolean;
//import com.doctor.sun.entity.constans.QuestionsPath;
//import com.doctor.sun.entity.im.TextMsg;
//import com.doctor.sun.event.CloseDrawerEvent;
//import com.doctor.sun.event.PayFailEvent;
//import com.doctor.sun.event.PaySuccessEvent;
//import com.doctor.sun.http.Api;
//import com.doctor.sun.http.callback.AlipayCallback;
//import com.doctor.sun.http.callback.ApiCallback;
//import com.doctor.sun.http.callback.CancelCallback;
//import com.doctor.sun.http.callback.DoNothingCallback;
//import com.doctor.sun.http.callback.SimpleCallback;
//import com.doctor.sun.http.callback.WeChatPayCallback;
//import com.doctor.sun.im.NimMsgInfo;
//import com.doctor.sun.module.AppointmentModule;
//import com.doctor.sun.module.AuthModule;
//import com.doctor.sun.module.DrugModule;
//import com.doctor.sun.module.ImModule;
//import com.doctor.sun.ui.activity.ChattingActivityNoMenu;
//import com.doctor.sun.ui.activity.SingleFragmentActivity;
//import com.doctor.sun.ui.activity.doctor.AfterServiceDoingActivity;
//import com.doctor.sun.ui.activity.doctor.AfterServiceDoneActivity;
//import com.doctor.sun.ui.activity.doctor.CancelAppointmentActivity;
//import com.doctor.sun.ui.activity.doctor.ChattingActivity;
//import com.doctor.sun.ui.activity.doctor.ConsultingActivity;
//import com.doctor.sun.ui.activity.doctor.PatientDetailActivity;
//import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
//import com.doctor.sun.ui.activity.patient.EditQuestionActivity;
//import com.doctor.sun.ui.activity.patient.FinishedOrderActivity;
//import com.doctor.sun.ui.activity.patient.HistoryDetailActivity;
//import com.doctor.sun.ui.adapter.SimpleAdapter;
//import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
//import com.doctor.sun.ui.adapter.core.BaseListAdapter;
//import com.doctor.sun.ui.fragment.PayAppointmentFragment;
//import com.doctor.sun.ui.handler.PayMethodInterface;
//import com.doctor.sun.util.ItemHelper;
//import com.doctor.sun.util.PayCallback;
//import com.doctor.sun.vo.BaseItem;
//import com.netease.nimlib.sdk.avchat.constant.AVChatType;
//import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//
//import io.ganguo.library.AppManager;
//import io.ganguo.library.Config;
//import io.ganguo.library.common.LoadingHelper;
//import io.ganguo.library.core.event.EventHub;
//import io.ganguo.library.util.Tasks;
//import io.realm.Realm;
//import io.realm.RealmQuery;
//import io.realm.RealmResults;
//import retrofit2.Call;
//
///**
// * Created by rick on 11/20/15.
// * 所有关于预约的逻辑都在这里, 跳转界面,是否过期等,剩下的进行中时间
// */
//public class AppointmentHandler implements PayMethodInterface, NimMsgInfo {
//    public static final int RECORD_AUDIO_PERMISSION = 40;
//    private static AppointmentModule api = Api.of(AppointmentModule.class);
//    protected Appointment data;
//    private DrugModule drugModule = Api.of(DrugModule.class);
//
//    public AppointmentHandler(Appointment data) {
//        this.data = data;
//    }
//
//    public String getPatientName() {
//        if (null != data.getPatientName()) {
//            return data.getPatientName();
//        } else if (null != data.getMedicalRecord()) {
//            return data.getMedicalRecord().getPatientName();
//        } else if (null != data.getUrgentRecord()) {
//            return data.getUrgentRecord().getPatientName();
//        } else {
//            return "";
//        }
//    }
//
//    public String getRecordName() {
//        if (null != data.getPatientName()) {
//            return data.getRecordName();
//        } else if (null != data.getMedicalRecord()) {
//            return data.getMedicalRecord().getRecordName();
//        } else if (null != data.getUrgentRecord()) {
//            return data.getUrgentRecord().getRecordName();
//        } else {
//            return "";
//        }
//    }
//
//    public String getBookTime() {
//        if (null != data.getBookTime()) {
//            return data.getBookTime();
//        } else if (null != data.getCreatedAt()) {
//            return data.getCreatedAt();
//        } else {
//            return "";
//        }
//    }
//
//
//    public String getRelation() {
//        String relation = data.getRelation();
//        if (null != relation && !relation.equals("")) {
//            return relation;
//        } else if (null != data.getMedicalRecord()) {
//            return "(" + data.getMedicalRecord().getRelation() + "/" + data.getMedicalRecord().getRecordName() + ")";
//        } else if (null != data.getUrgentRecord()) {
//            return "(" + data.getUrgentRecord().getRelation() + "/" + data.getUrgentRecord().getRecordName() + ")";
//        } else {
//            return "";
//        }
//    }
//
//
//    public String getRelation3() {
//        String relation = data.getRelation();
//        if (null != relation && !relation.equals("")) {
//            return relation;
//        } else if (null != data.getMedicalRecord()) {
//            return getPatientName() + "(患者的" + data.getMedicalRecord().getRelation() + ")";
//        } else if (null != data.getUrgentRecord()) {
//            return getPatientName() + "(患者的" + data.getUrgentRecord().getRelation() + ")";
//        } else {
//            return "";
//        }
//    }
//
//    public String getRecordName2() {
//        if (null != data.getMedicalRecord()) {
//            return data.getMedicalRecord().getRecordName();
//        } else if (null != data.getUrgentRecord()) {
//            return data.getUrgentRecord().getRecordName();
//        } else {
//            return "";
//        }
//    }
//
//    public String getGender() {
//        if (data.getGender() == 0) {
//            return "男";
//        } else {
//            return "女";
//        }
//    }
//
//    public String getBirthday() {
//        if (null != data.getBirthday()) {
//            return data.getBirthday();
//        }
//        if (null != data.getMedicalRecord()) {
//            return data.getMedicalRecord().getBirthday();
//        } else if (null != data.getUrgentRecord()) {
//            return data.getUrgentRecord().getBirthday();
//        } else {
//            return "";
//        }
//    }
//
//    public boolean isCouponUsed() {
//        return getDiscountMoney() > 0;
//    }
//
//    public String getConsultingTitle() {
//        return "病历: " + getRecordName2();
//    }
//
//    public String getUserData() {
//        return "[" + data.getId() + "," + data.getRecordId() + "]";
//    }
//
//
//    public void cancel2(final BaseListAdapter adapter, final BaseViewHolder vh, final Appointment data) {
//        Intent intent = CancelAppointmentActivity.makeIntent(vh.itemView.getContext(), data);
//        final Handler target = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                Tasks.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        adapter.removeItem(data);
//                    }
//                }, 1000);
//                return false;
//            }
//        });
//        Messenger msg = new Messenger(target);
//        intent.putExtra(Constants.HANDLER, msg);
//        vh.itemView.getContext().startActivity(intent);
//    }
//
//    public void pCancel(BaseListAdapter component, BaseViewHolder vh, int id) {
//        api.pCancel(id).enqueue(new CancelCallback(vh, component));
//    }
//
//    public void startPayActivity(Context context, int id) {
//        Bundle args = PayAppointmentFragment.getArgs(String.valueOf(id));
//        Intent intent = SingleFragmentActivity.intentFor(context, "确认支付", args);
//        context.startActivity(intent);
//    }
//
//    public void payWithAlipay(final Activity activity, String couponId) {
//        int id;
//        if (returnNotPaid()) {
//            id = data.getReturnInfo().getReturnAppointmentId();
//        } else {
//            id = data.getId();
//        }
//        api.buildAliPayOrder(String.valueOf(id), "alipay", couponId).enqueue(new AlipayCallback(activity, data));
//    }
//
//    public void payWithWeChat(final Activity activity, String couponId) {
//        int id;
//        if (returnNotPaid()) {
//            id = data.getReturnInfo().getReturnAppointmentId();
//        } else {
//            id = data.getId();
//        }
//        api.buildWeChatOrder(String.valueOf(id), "wechat", couponId).enqueue(new WeChatPayCallback(activity, data));
//    }
//
//    public void simulatedPay(final BaseListAdapter component, final View view, final BaseViewHolder vh) {
//        int id;
//        if (returnNotPaid()) {
//            id = data.getReturnInfo().getReturnAppointmentId();
//        } else {
//            id = data.getId();
//        }
//        simulatedPayImpl(view, id);
//    }
//
//    public void simulatedPayImpl(final View view, int id) {
//        final PayCallback mCallback = new PayCallback() {
//            @Override
//            public void onPaySuccess() {
//                EventHub.post(new PaySuccessEvent(data));
//            }
//
//            @Override
//            public void onPayFail() {
//                EventHub.post(new PayFailEvent(data, false));
//            }
//        };
//        AppointmentModule api = Api.of(AppointmentModule.class);
//        api.pay(String.valueOf(id)).enqueue(new SimpleCallback<String>() {
//            @Override
//            protected void handleResponse(String response) {
//                mCallback.onPaySuccess();
//            }
//
//            @Override
//            public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
//                super.onFailure(call, t);
//                mCallback.onPayFail();
//            }
//        });
//    }
//
//
//    /**
//     * android:onClick="@{()->data.handler.remind(context,data.id,data.medicalRecord.patientId)}"
//     *
//     * @param context
//     * @param appointmentId
//     * @param patientId
//     */
//    public void remind(final Context context, int appointmentId, int patientId) {
//        api.remind(String.valueOf(appointmentId), patientId)
//                .enqueue(new SimpleCallback<String>() {
//                    @Override
//                    protected void handleResponse(String response) {
//                        Toast.makeText(context, "成功提醒患者", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    @Deprecated
//    public void refuse(View view) {
//        api.refuseConsultation(String.valueOf(data.getReturnListId())).enqueue(new DoNothingCallback());
//    }
//
//    @Deprecated
//    public void accept(final View view) {
//        new MaterialDialog.Builder(view.getContext()).content("若需要提前进行就诊，请先与患者确认。（点击下方通话键可联系患者）是否确认提前就诊？")
//                .positiveText("确认")
//                .negativeText("取消")
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        ApiCallback<String> callback = new GotoConsultingCallback(view);
//                        api.startConsulting(String.valueOf(data.getId()))
//                                .enqueue(callback);
//                    }
//                }).show();
//    }
//
//    public void acceptReturn(final View view) {
//        ApiCallback<String> callback = new GotoConsultingCallback(view);
//
//        api.acceptConsultation(toParams())
//                .enqueue(callback);
//    }
//
//    public void acceptUrgentCall(final View view) {
//        ApiCallback<String> callback = new GotoConsultingCallback(view);
//
//        api.acceptUrgentCall(data.getId())
//                .enqueue(callback);
//    }
//
//    /**
//     * android:onClick="@{()->data.handler.detail(context,vh.itemViewType)}"
//     */
//    public void detail(Context context, int type) {
//        Intent i = PatientDetailActivity.makeIntent(context, data, type);
//        context.startActivity(i);
//    }
//
////    public void comment(final BaseAdapter adapter, final BaseViewHolder vh) {
////        if (!hasPatientComment()) {
////            Context context = vh.itemView.getContext();
////            Intent i = FeedbackActivity.makeIntent(context, data);
////            Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
////                @Override
////                public boolean handleMessage(Message msg) {
////                    data.setPatientPoint((Double) msg.obj);
////                    adapter.notifyItemChanged(vh.getAdapterPosition());
////                    return false;
////                }
////            }));
////            i.putExtra(Constants.HANDLER, messenger);
////            context.startActivity(i);
////        } else {
////            ToastHelper.showMessage(vh.itemView.getContext(), "已经评价过此预约");
////        }
////    }
//
//    public void pComment(final BaseListAdapter adapter, final BaseViewHolder vh) {
//        if (!hasDoctorComment()) {
//            Context context = vh.itemView.getContext();
//            Intent i = com.doctor.sun.ui.activity.patient.FeedbackActivity.makeIntent(context, data);
//            Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
//                @Override
//                public boolean handleMessage(Message msg) {
//                    data.setDoctorPoint((Double) msg.obj);
//                    adapter.notifyItemChanged(vh.getAdapterPosition());
//                    return false;
//                }
//            }));
//            i.putExtra(Constants.HANDLER, messenger);
//            context.startActivity(i);
//        } else {
//            Toast.makeText(vh.itemView.getContext(), "已经评价过此预约", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public HashMap<String, String> toParams() {
//        HashMap<String, String> result = new HashMap<>();
//        result.put("id", String.valueOf(data.getReturnListId()));
//        result.put("recordId", String.valueOf(data.getRecordId()));
//        result.put("id", String.valueOf(data.getAppointmentId()));
//        result.put("money", String.valueOf(data.getMoney()));
//        return result;
//    }
//
//    public String getTitle() {
//        int userType = Config.getInt(Constants.USER_TYPE, -1);
//        if (userType == AuthModule.PATIENT_TYPE) {
//            Doctor doctor = data.getDoctor();
//            if (doctor == null) {
//                return "";
//            }
//            return doctor.getName();
//        } else {
//            return getPatientName();
//        }
//    }
//
//    public void chat(Context context, Appointment data) {
//        if (data.getTid() != 0) {
//            Intent intent = ChattingActivity.makeIntent(context, data);
//            context.startActivity(intent);
//        } else {
//            if (BuildConfig.DEV_MODE) {
//                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public void chat(BaseListAdapter adapter, BaseViewHolder vh) {
//        if (data.getTid() != 0) {
//            Intent intent = ChattingActivity.makeIntent(vh.itemView.getContext(), data);
//            if (adapter != null && vh != null) {
//                ItemHelper.initCallback(intent, adapter, vh);
//            }
//            vh.itemView.getContext().startActivity(intent);
//        } else {
//            if (BuildConfig.DEV_MODE) {
//                Toast.makeText(vh.itemView.getContext(), "云信群id为0", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public void chatNoMenu(Context context) {
//        if (data.getTid() != 0) {
//            Intent intent = ChattingActivityNoMenu.makeIntent(context, data);
//            context.startActivity(intent);
//        } else {
//            if (BuildConfig.DEV_MODE) {
//                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//
//    @Override
//    public SessionTypeEnum getType() {
//        return null;
//    }
//
//    @Override
//    public String getTeamId() {
//        return String.valueOf(data.getTid());
//    }
//
//    @Override
//    public String getTargetP2PId() {
//        if (Settings.isDoctor()) {
//            return data.getYunxinAccid();
//        } else {
//            if (data.getDoctor() != null) {
//                return data.getDoctor().getYunxinAccid();
//            } else {
//                return data.getYunxinAccid();
//            }
//        }
//    }
//
//    @Override
//    public boolean enablePush() {
//        if (Settings.isDoctor()) {
//            return true;
//        }
//        boolean isDetailAppointment = isDetail();
//        return isDetailAppointment && data.getStatus() == Status.DOING;
//    }
//
//    @Override
//    public int appointmentId() {
//        return data.getId();
//    }
//
//    @Override
//    public boolean shouldAskServer() {
//        return data.getAppointmentType() == AppointmentType.PREMIUM && data.getStatus() == Status.DOING;
//    }
//
//    @Override
//    public int getDuration() {
//        try {
//            return Integer.parseInt(data.getTakeTime());
//        } catch (NumberFormatException e) {
//            return 0;
//        }
//    }
//
//    public Intent getFirstMenu(Context context, int canEdit, int tab) {
//        if (isAfterService()) {
//            String id = String.valueOf(data.getId());
//            if (canEdit == IntBoolean.FALSE) {
//                return AfterServiceDoneActivity.intentFor(context, id, tab);
//            } else {
//                String recordId = String.valueOf(data.getUrgentRecord().getMedicalRecordId());
//                return AfterServiceDoingActivity.intentFor(context, id, recordId, tab);
//            }
//        } else {
//            Appointment data = getData();
//            data.canEdit = canEdit;
//            return AppointmentDetailActivity.makeIntent(context, data, tab);
//        }
//    }
//
//
//    public int getDiscountMoney() {
//        try {
//            int money = Integer.parseInt(data.getMoney());
//            int unpayMoney = Integer.parseInt(data.getNeedPay());
//            return money - unpayMoney;
//        } catch (ClassCastException e) {
//            return 0;
//        } catch (NumberFormatException intCast) {
//            try {
//                double money = Double.parseDouble(data.getMoney());
//                double unpayMoney = Double.parseDouble(data.getNeedPay());
//                return (int) (money - unpayMoney);
//            } catch (NumberFormatException doubleCast) {
//                return 0;
//            }
//        }
//    }
//
//    private static class GotoConsultingCallback extends SimpleCallback<String> {
//        private final View view;
//
//        public GotoConsultingCallback(View view) {
//            this.view = view;
//        }
//
//        @Override
//        protected void handleResponse(String response) {
//            LoadingHelper.showMaterLoading(view.getContext(), "正在提示患者开始就诊");
//            Tasks.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    LoadingHelper.hideMaterLoading();
//                    Intent i = ConsultingActivity.makeIntent(view.getContext());
//                    view.getContext().startActivity(i);
//                    AppManager.finishAllActivity();
//                }
//            }, 350);
//        }
//    }
//
//
//    public boolean payVisible() {
//        return data.getStatus() == Status.UNPAID;
//    }
//
//    public boolean isPayed() {
//        return data.getStatus() == Status.PAID;
//    }
//
//    public boolean hasDoctorComment() {
//        return data.getDoctorPoint() > 0;
//    }
//
//    public boolean hasPatientComment() {
//        return data.getPatientPoint() > 0;
//    }
//
//
//    public boolean returnNotPaid() {
//        return data.getReturnInfo() != null && data.getReturnInfo().getReturnPaid() != 1 && data.getReturnInfo().getNeedReturn() == 1;
//    }
//
//    @Deprecated
//    public void historyDetail(View view) {
//        Intent intent = HistoryDetailActivity.makeIntent(view.getContext(), data);
//        view.getContext().startActivity(intent);
//    }
//
//    public void historyList(Context context, Appointment data) {
//        context.startActivity(AppointmentDetailActivity.makeIntent(context,
//                data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY));
//    }
//
//
//    public void onPatientClickOrder(BaseListAdapter adapter, BaseViewHolder vh) {
//        switch (data.getStatus()) {
//            case Status.UNPAID: {
//                startPayActivity(vh.itemView.getContext(), data.getId());
//                break;
//            }
//            case Status.PAID: {
//                Intent intent = EditQuestionActivity.intentFor(vh.itemView.getContext(), data.getIdString(), QuestionsPath.NORMAL);
//                vh.itemView.getContext().startActivity(intent);
//                break;
//            }
//            case Status.FINISHED: {
//                Intent intent = FinishedOrderActivity.makeIntent(vh.itemView.getContext(), data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY);
//                vh.itemView.getContext().startActivity(intent);
//                break;
//            }
//            case Status.DOING:
//            case Status.WAITING: {
//                chat(vh.itemView.getContext(), data);
//                break;
//            }
//            default: {
//                break;
//            }
//        }
//    }
//
//    public void onDoctorClickOrder(final BaseViewHolder vh, final BaseListAdapter adapter) {
//        switch (data.getStatus()) {
//            case Status.PAID: {
//                detail(vh.itemView.getContext(), vh.getItemViewType());
//                break;
//            }
//            case Status.FINISHED: {
//                chat(adapter, vh);
//                Intent intent = HistoryDetailActivity.makeIntent(vh.itemView.getContext(), data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY);
//                vh.itemView.getContext().startActivity(intent);
//                break;
//            }
//            case Status.DOING:
//            case Status.WAITING: {
//                chat(adapter, vh);
//                break;
//            }
//            default: {
//                break;
//            }
//        }
//    }
//
//    public void drugPush(int id) {
//        Call<ApiDTO<String>> apiDTOCall;
//        if (isAfterService()) {
//            apiDTOCall = drugModule.pushFollowUpDrug(String.valueOf(id));
//        } else {
//            apiDTOCall = drugModule.pushDrug(String.valueOf(id));
//        }
//
//        apiDTOCall.enqueue(new SimpleCallback<String>() {
//            @Override
//            protected void handleResponse(String response) {
//                EventHub.post(new CloseDrawerEvent());
//            }
//        });
//    }
//
//    private boolean isAfterService() {
//        return data.getAppointmentType() == AppointmentType.AFTER_SERVICE;
//    }
//
//    private boolean isQuick() {
//        return data.getAppointmentType() == AppointmentType.STANDARD;
//    }
//
//    private boolean isDetail() {
//        return data.getAppointmentType() == AppointmentType.PREMIUM;
//    }
//
//
//    public void alertAppointmentFinished(Context context) {
//        if (!Settings.isDoctor()) {
//            switch (data.getStatus()) {
//                case Status.LOCKED:
//                case Status.FINISHED:
//                case Status.UNPAID:
//                    switch (data.getAppointmentType()) {
//                        case AppointmentType.AFTER_SERVICE:
//                            Toast.makeText(context, "随访已经结束,请耐心等待下次随访", Toast.LENGTH_SHORT).show();
//                            break;
//                        default: {
//                            Toast.makeText(context, "预约已经结束,请重新预约", Toast.LENGTH_SHORT).show();
//                            break;
//                        }
//                    }
//                    break;
//                default:
//            }
//        }
//    }
//
//
//    public void makePhoneCall(final Context context) {
//        AVChatActivity.start(context, getTargetP2PId(), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL);
//    }
//
//    public void callTelephone(final Context context) {
////        ImModule imModule = Api.of(ImModule.class);
////        imModule.makeYunXinPhoneCall(getMyPhoneNO(), getPhoneNO()).enqueue(new SimpleCallback<String>() {
////            @Override
////            protected void handleResponse(String response) {
////                Toast.makeText(context, "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
////            }
////        });
//        ImModule imModule = Api.of(ImModule.class);
//        imModule.makeYunXinPhoneCall(String.valueOf(data.getId())).enqueue(new SimpleCallback<String>() {
//            @Override
//            protected void handleResponse(String response) {
//                Toast.makeText(context, "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    public String getVoipAccount() {
//        //假如是医生的话,就发消息给病人
//        if (Settings.isDoctor()) {
//            return data.getVoipAccount();
//        } else {
//            //假如不是医生的话,就发消息给医生
//            Doctor doctor = data.getDoctor();
//            if (doctor != null) {
//                return doctor.getVoipAccount();
//            } else {
//                return data.getVoipAccount();
//            }
//        }
//    }
//
//    public String getMyPhoneNO() {
//        if (Settings.isDoctor()) {
//            return Settings.getDoctorProfile().getPhone();
//        } else {
//            Patient patientProfile = Settings.getPatientProfile();
//            if (patientProfile != null) {
//                return patientProfile.getPhone();
//            } else {
//                return "";
//            }
//        }
//    }
//
//    public String getPhoneNO() {
//        if (Settings.isDoctor()) {
//            return data.getPhone();
//        } else {
//            Doctor doctor = data.getDoctor();
//            if (doctor != null) {
//                return doctor.getPhone();
//            } else {
//                return data.getPhone();
//            }
//        }
//    }
//
//    public String styledOrderStatus() {
//        return "<font color='" + getStatusColor() + "'>" + getStatusLabel() + "</font>";
//    }
//
//    public String styledOrderTypeAndStatus() {
//        return "<font color='" + getStatusColor() + "'>" + data.getDisplayType() + "-" + getStatusLabel() + "</font>";
//    }
//
//
//    public String getStatusColor() {
//        switch (data.getStatus()) {
//            case Status.FINISHED:
//                return "#363636";
//
//            case Status.UNPAID:
//                return "#ff1800";
//
//            case Status.PAID:
//                return "#ff8e43";
//
//            case Status.WAITING:
//                return "#ff1800";
//
//            case Status.DOING:
//                return "#88cb5a";
//
//            case Status.LOCKED:
//            case Status.CANCEL:
//                return "#898989";
//            default:
//                return "#acacac";
//        }
//    }
//
//    public boolean isFinished() {
//        return getFinishedTime() < System.currentTimeMillis();
//    }
//
//    /**
//     * 将bookTime: 2016-04-19 15:55－16:10 转换为毫秒
//     *
//     * @return
//     */
//    public long getFinishedTime() {
//        int orderStatus = data.getStatus();
////        if (orderStatus == null) {
////            orderStatus = "";
////        }
//        if (orderStatus == Status.FINISHED) {
//            return 0;
//        }
//
//        String bookTime = data.getBookTime();
//        try {
//            String substring;
//            if (bookTime != null) {
//                substring = bookTime.substring(17, bookTime.length());
//                String date = bookTime.substring(0, 11) + substring;
//                return parseDate(date);
//            }
//        } catch (Exception ignored) {
//        }
//        return 0;
//    }
//
//    public long getStartTime() {
//        String bookTime = data.getBookTime();
//        if (bookTime == null) {
//            return 0;
//        } else {
//            try {
//                String date = bookTime.substring(0, 11) + bookTime.substring(11, 17);
//                return parseDate(date);
//            } catch (Exception e) {
//                return 0;
//            }
//        }
//    }
//
//    public long getRemainingTime() {
//        return getFinishedTime() - System.currentTimeMillis();
//    }
//
//
//    public void setFinish() {
//        String bookTime = data.getBookTime();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
//        String format = dateFormat.format(new Date(System.currentTimeMillis()));
//        String finalBookTime = bookTime.substring(0, 17) + format;
//
//        data.setBookTime(finalBookTime);
//    }
//
//    private long parseDate(String date) throws ParseException {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
//        Date parse = dateFormat.parse(date);
//        return parse.getTime();
//    }
//
//
//    public String chatStatus() {
//        switch (data.getAppointmentType()) {
//            case AppointmentType.AFTER_SERVICE:
//                return "随访" + getStatusLabel();
//        }
//        if (Settings.isDoctor()) {
//            return "本次咨询已结束";
//        } else {
//            return "本次咨询已结束,如需咨询,请再次预约";
//        }
//    }
//
//    public String bookTimeStatus() {
//        switch (data.getAppointmentType()) {
//            case AppointmentType.AFTER_SERVICE:
//                return "发起时间:" + getBookTime();
//            default:
//                return "预约时间:" + getBookTime();
//        }
//    }
//
//
//    @NonNull
//    public RealmQuery<TextMsg> queryAllMsg(Realm realm) {
//        return realm.where(TextMsg.class)
//                .equalTo("sessionId", String.valueOf(data.getTid()));
//    }
//
//    public RealmResults<TextMsg> sortedByTime(RealmQuery<TextMsg> query) {
//        RealmResults<TextMsg> results = query.findAllSorted("time");
//        return results;
//    }
//
//    public RealmQuery<TextMsg> unreadMsg(RealmQuery<TextMsg> query) {
//        return query.equalTo("haveRead", false);
//    }
//
//    public long unreadMsgCount() {
//        return unreadMsg(queryAllMsg(Realm.getDefaultInstance())).count();
//    }
//
//    public long msgCount() {
//        return queryAllMsg(Realm.getDefaultInstance()).count();
//    }
//
//    public TextMsg lastMsg() {
//        RealmResults<TextMsg> results = sortedByTime(queryAllMsg(Realm.getDefaultInstance()));
//        if (results.isEmpty()) {
//            return new TextMsg();
//        }
//        return results.last();
//    }
//
//    public long lastMsgTime(Realm realm) {
//        return lastMsg().getTime();
//    }
//
//    public String lastMsgTime() {
//        Date date = new Date(lastMsg().getTime());
//        return date.toString();
//    }
//
//    public long lastMsgTimeMillis() {
//        return lastMsg().getTime();
//    }
//
//
//    public boolean showCommentBtn() {
//        return data.getStatus() == Status.FINISHED;
//    }
//
//    public boolean showAnswerQuestionBtn() {
//        return getCurrentStatus() == 1;
//    }
//
//    public void viewDetail(final Context context, final int tab) {
////        DiagnosisModule api = Api.of(DiagnosisModule.class);
////        String type = "appointment";
////        if (isAfterService()) {
////            type = "followUp";
////        }
////        api.appointmentStatus(appointmentId(), type).enqueue(new SimpleCallback<AppointmentStatus>() {
////            @Override
////            protected void handleResponse(AppointmentStatus response) {
////                int canEdit;
////                if (response != null) {
////                    canEdit = response.canEdit;
////                    String orderStatus = response.displayStatus;
////                    boolean isCanEditStatus = !orderStatus.equals(Status.FINISHED)
////                            && !orderStatus.equals(Status.A_FINISHED)
////                            && !orderStatus.equals(Status.REJECTED)
////                            && !orderStatus.equals(Status.A_UNPAID)
////                            && !orderStatus.equals(Status.CLOSED)
////                            && !orderStatus.equals(Status.A_CANCEL);
////                    if (isCanEditStatus) {
////                        canEdit = IntBoolean.TRUE;
////                    }
////                } else {
////                    canEdit = IntBoolean.NOT_GIVEN;
////                }
////                answerQuestion(context, tab, canEdit);
////            }
////        });
//
//        AppointmentModule api = Api.of(AppointmentModule.class);
//        api.appointmentDetail(String.valueOf(appointmentId())).enqueue(new SimpleCallback<Appointment>() {
//            @Override
//            protected void handleResponse(Appointment response) {
//                answerQuestion(context, tab, response.canEdit);
//            }
//        });
//    }
//
//    public void answerQuestion(Context context, int tab, int canEdit) {
//        Intent i = getFirstMenu(context, canEdit, tab);
//        if (i != null) {
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(i);
//        }
//    }
//
//
//    public Appointment getData() {
//        return data;
//    }
//
//    public String getStatusLabel() {
//        return data.getDisplayStatus();
//    }
//
//    public void showStatusTimeline(Context context) {
//        RecyclerView recyclerView = new RecyclerView(context);
//        SimpleAdapter adapter = new SimpleAdapter(context);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        String[] status = new String[]{"待付款", "填问卷", "进行中", "待建议", "已完成"};
//
//        int currentStatus = getCurrentStatus();
//        for (int i = 0; i < status.length; i++) {
//            BaseItem object = new BaseItem(R.layout.item_appointment_status);
//            boolean shouldHighLight = i == currentStatus;
//            boolean currentItem = i <= currentStatus;
//            object.setEnabled(currentItem);
//            object.setUserSelected(shouldHighLight);
//            object.setTitle(status[i]);
//            adapter.add(object);
//        }
//
//        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
//        builder.title("订单状态");
//        builder.positiveText("确定");
//        builder.customView(recyclerView, true).show();
//    }
//
//    public int getCurrentStatus() {
//        switch (data.getStatus()) {
//            case Status.LOCKED:
//            case Status.CANCEL:
//            case Status.FINISHED:
//                return 4;
//
//            case Status.UNPAID:
//                return 0;
//
//            case Status.PAID:
//                return 1;
//
//            case Status.WAITING:
//                return 3;
//
//            case Status.DOING:
//                return 2;
//
//            default:
//                return 5;
//        }
//    }
//
//
//    public int getStatusBackground() {
//        switch (data.getStatus()) {
//            case Status.LOCKED:
//            case Status.CANCEL:
//            case Status.FINISHED:
//                return R.drawable.shape_blue_rect_transparent;
//
//            case Status.UNPAID:
//                return R.drawable.shape_red_rect_transparent;
//
//            case Status.PAID:
//                return R.drawable.shape_orange_rect_transparent;
//
//            case Status.WAITING:
//                return R.drawable.shape_red_rect_transparent;
//
//            case Status.DOING:
//                return R.drawable.shape_green_rect_transparent;
//
//            default:
//                return R.drawable.shape_blue_rect_transparent;
//        }
//    }
//
//    public int getStatusArrow() {
//        switch (data.getStatus()) {
//            case Status.LOCKED:
//            case Status.CANCEL:
//            case Status.FINISHED:
//                return R.drawable.ic_chevron_right_blue_24dp;
//
//            case Status.UNPAID:
//                return R.drawable.ic_chevron_right_red_24dp;
//
//            case Status.PAID:
//                return R.drawable.ic_chevron_right_orange_24dp;
//
//            case Status.WAITING:
//                return R.drawable.ic_chevron_right_red_24dp;
//
//            case Status.DOING:
//                return R.drawable.ic_chevron_right_green_24dp;
//
//            default:
//                return R.drawable.ic_chevron_right_green_24dp;
//        }
//    }
//
//
//    public String styledOrderStatus2() {
//        return "<font color='" + getStatusColor2() + "'>" + getStatusLabel() + "</font>";
//    }
//
//    public String getStatusColor2() {
//        switch (data.getStatus()) {
//            case Status.FINISHED:
//                return "#339de1";
//
//            case Status.UNPAID:
//                return "#ff1800";
//
//            case Status.PAID:
//                return "#ff8e43";
//
//            case Status.WAITING:
//                return "#ff1800";
//
//            case Status.DOING:
//                return "#88cb5a";
//
//            case Status.LOCKED:
//            case Status.CANCEL:
//                return "#898989";
//            default:
//                return "#acacac";
//        }
//    }
//
//
//    @IntDef
//    public @interface Status {
//        int UNPAID = 0;
//        int PAID = 1;
//        int DOING = 2;
//        int WAITING = 3;
//        int FINISHED = 4;
//        int CANCEL = 6;
//        int LOCKED = 7;
//    }
//}