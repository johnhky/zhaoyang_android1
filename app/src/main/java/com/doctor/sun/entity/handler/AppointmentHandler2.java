package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.event.PayFailEvent;
import com.doctor.sun.event.PaySuccessEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.CancelCallback;
import com.doctor.sun.http.callback.DoNothingCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.module.AuthModule;
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
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.fragment.PayAppointmentFragment;
import com.doctor.sun.util.ItemHelper;
import com.doctor.sun.util.PayCallback;
import com.doctor.sun.vo.BaseItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;
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
public class AppointmentHandler2 {
    public static final int RECORD_AUDIO_PERMISSION = 40;
    private static AppointmentModule api = Api.of(AppointmentModule.class);
    private static DrugModule drugModule = Api.of(DrugModule.class);


    public static String getPatientName(Appointment data) {
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

    public static String getRecordName(Appointment data) {
        if (null != data.getPatientName()) {
            return data.getRecordName();
        } else if (null != data.getMedicalRecord()) {
            return data.getMedicalRecord().getRecordName();
        } else if (null != data.getUrgentRecord()) {
            return data.getUrgentRecord().getRecordName();
        } else {
            return "";
        }
    }

    public static String getBookTime(Appointment data) {
        if (null != data.getBookTime()) {
            return data.getBookTime();
        } else if (null != data.getCreatedAt()) {
            return data.getCreatedAt();
        } else {
            return "";
        }
    }


    public static String getRelation(Appointment data) {
        String relation = data.getRelation();
        if (null != relation && !relation.equals("")) {
            return relation;
        } else if (null != data.getMedicalRecord()) {
            return "(" + data.getMedicalRecord().getRelation() + "/" + data.getMedicalRecord().getRecordName() + ")";
        } else if (null != data.getUrgentRecord()) {
            return "(" + data.getUrgentRecord().getRelation() + "/" + data.getUrgentRecord().getRecordName() + ")";
        } else {
            return "";
        }
    }


    public static String getRelation3(Appointment data) {
        String relation = data.getRelation();
        if (null != relation && !relation.equals("")) {
            return relation;
        } else if (null != data.getMedicalRecord()) {
            return getPatientName(data) + "(患者的" + data.getMedicalRecord().getRelation() + ")";
        } else if (null != data.getUrgentRecord()) {
            return getPatientName(data) + "(患者的" + data.getUrgentRecord().getRelation() + ")";
        } else {
            return "";
        }
    }

    public static String getRecordName2(Appointment data) {
        if (null != data.getMedicalRecord()) {
            return data.getMedicalRecord().getRecordName();
        } else if (null != data.getUrgentRecord()) {
            return data.getUrgentRecord().getRecordName();
        } else {
            return "";
        }
    }

    public static String getGender(Appointment data) {
        if (data.getGender() == 0) {
            return "男";
        } else {
            return "女";
        }
    }

    public static String getBirthday(Appointment data) {
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

    public static boolean isCouponUsed(Appointment data) {
        return getDiscountMoney(data) > 0;
    }

    public static String getConsultingTitle(Appointment data) {
        return "病历: " + getRecordName2(data);
    }



    public static void cancel2(final BaseListAdapter adapter, final BaseViewHolder vh, final Appointment data) {
        Intent intent = CancelAppointmentActivity.makeIntent(vh.itemView.getContext(), data);
        final Handler target = new Handler(new Handler.Callback() {
            @Override
            public  boolean handleMessage(Message msg) {
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public  void run() {
                        adapter.removeItem(data);
                    }
                }, 1000);
                return false;
            }
        });
        Messenger msg = new Messenger(target);
        intent.putExtra(Constants.HANDLER, msg);
        vh.itemView.getContext().startActivity(intent);
    }

    public static void pCancel(BaseListAdapter component, BaseViewHolder vh, int id) {
        api.pCancel(id).enqueue(new CancelCallback(vh, component));
    }

    public static void startPayActivity(Context context, int id) {
        Bundle args = PayAppointmentFragment.getArgs(String.valueOf(id));
        Intent intent = SingleFragmentActivity.intentFor(context, "确认支付", args);
        context.startActivity(intent);
    }

    public static void payWithAlipay(final Activity activity, String couponId, Appointment data) {
        int id;
        if (returnNotPaid(data)) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        api.buildAliPayOrder(id, "alipay", couponId).enqueue(new AlipayCallback(activity, data));
    }

    public static void payWithWeChat(final Activity activity, String couponId, Appointment data) {
        int id;
        if (returnNotPaid(data)) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        api.buildWeChatOrder(id, "wechat", couponId).enqueue(new WeChatPayCallback(activity, data));
    }

    public static void simulatedPay(final BaseListAdapter component, final View view, final BaseViewHolder vh, Appointment data) {
        int id;
        if (returnNotPaid(data)) {
            id = data.getReturnInfo().getReturnAppointmentId();
        } else {
            id = data.getId();
        }
        simulatedPayImpl(view, id, data);
    }

    public static void simulatedPayImpl(final View view, int id, final Appointment data) {
        final PayCallback mCallback = new PayCallback() {
            @Override
            public  void onPaySuccess() {
                EventHub.post(new PaySuccessEvent(data));
            }

            @Override
            public  void onPayFail() {
                EventHub.post(new PayFailEvent(data, false));
            }
        };
        AppointmentModule api = Api.of(AppointmentModule.class);
        api.pay(String.valueOf(id)).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                mCallback.onPaySuccess();
            }

            @Override
            public  void onFailure(Call<ApiDTO<String>> call, Throwable t) {
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
    public static void remind(final Context context, int appointmentId, int patientId) {
        api.remind(appointmentId, patientId)
                .enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        Toast.makeText(context, "成功提醒患者", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Deprecated
    public static void refuse(View view, Appointment data) {
        api.refuseConsultation(String.valueOf(data.getReturnListId())).enqueue(new DoNothingCallback());
    }

    @Deprecated
    public static void accept(final View view, final Appointment data) {
        new MaterialDialog.Builder(view.getContext()).content("若需要提前进行就诊，请先与患者确认。（点击下方通话键可联系患者）是否确认提前就诊？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public  void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ApiCallback<String> callback = new GotoConsultingCallback(view);
                        api.startConsulting(data.getId())
                                .enqueue(callback);
                    }
                }).show();
    }

    public static void acceptReturn(final View view,Appointment data) {
        ApiCallback<String> callback = new GotoConsultingCallback(view);

        api.acceptConsultation(toParams(data))
                .enqueue(callback);
    }


    /**
     * android:onClick="@{()->data.handler.detail(context,vh.itemViewType)}"
     */
    public static void detail(Context context, int type, Appointment data) {
        Intent i = PatientDetailActivity.makeIntent(context, data, type);
        context.startActivity(i);
    }

//    public static void comment(final BaseAdapter adapter, final BaseViewHolder vh) {
//        if (!hasPatientComment()) {
//            Context context = vh.itemView.getContext();
//            Intent i = FeedbackActivity.makeIntent(context, data);
//            Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
//                @Override
//                public static boolean handleMessage(Message msg) {
//                    data.setPatientPoint((Double) msg.obj);
//                    adapter.notifyItemChanged(vh.getAdapterPosition());
//                    return false;
//                }
//            }));
//            i.putExtra(Constants.HANDLER, messenger);
//            context.startActivity(i);
//        } else {
//            ToastHelper.showMessage(vh.itemView.getContext(), "已经评价过此预约");
//        }
//    }

    public static void pComment(final BaseListAdapter adapter, final BaseViewHolder vh, final Appointment data) {
        if (!hasDoctorComment(data)) {
            Context context = vh.itemView.getContext();
            Intent i = com.doctor.sun.ui.activity.patient.FeedbackActivity.makeIntent(context, data);
            Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                @Override
                public  boolean handleMessage(Message msg) {
                    data.setDoctorPoint((Double) msg.obj);
                    adapter.notifyItemChanged(vh.getAdapterPosition());
                    return false;
                }
            }));
            i.putExtra(Constants.HANDLER, messenger);
            context.startActivity(i);
        } else {
            Toast.makeText(vh.itemView.getContext(), "已经评价过此预约", Toast.LENGTH_SHORT).show();
        }
    }

    public static HashMap<String, String> toParams(Appointment data) {
        HashMap<String, String> result = new HashMap<>();
        result.put("id", String.valueOf(data.getReturnListId()));
        result.put("recordId", String.valueOf(data.getRecordId()));
        result.put("id", String.valueOf(data.getAppointmentId()));
        result.put("money", String.valueOf(data.getMoney()));
        return result;
    }

    public static String getTitle(Appointment data) {
        int userType = Config.getInt(Constants.USER_TYPE, -1);
        if (userType == AuthModule.PATIENT_TYPE) {
            Doctor doctor = data.getDoctor();
            if (doctor == null) {
                return "";
            }
            return doctor.getName();
        } else {
            return getPatientName(data);
        }
    }

    public static void chat(Context context, Appointment data) {
        if (data.getTid() != 0) {
            Intent intent = ChattingActivity.makeIntent(context, data);
            context.startActivity(intent);
        } else {
            if (BuildConfig.DEV_MODE) {
                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void chat(BaseListAdapter adapter, BaseViewHolder vh, Appointment data) {
        if (data.getTid() != 0) {
            Intent intent = ChattingActivity.makeIntent(vh.itemView.getContext(), data);
            if (adapter != null && vh != null) {
                ItemHelper.initCallback(intent, adapter, vh);
            }
            vh.itemView.getContext().startActivity(intent);
        } else {
            if (BuildConfig.DEV_MODE) {
                Toast.makeText(vh.itemView.getContext(), "云信群id为0", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void chatNoMenu(Context context, Appointment data) {
        if (data.getTid() != 0) {
            Intent intent = ChattingActivityNoMenu.makeIntent(context, data);
            context.startActivity(intent);
        } else {
            if (BuildConfig.DEV_MODE) {
                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
            }
        }
    }




    public static boolean enablePush(Appointment data) {
        if (Settings.isDoctor()) {
            return true;
        }
        boolean isDetailAppointment = isDetail(data);
        return isDetailAppointment && data.getStatus() == Status.DOING;
    }

    public static int appointmentId(Appointment data) {
        return data.getId();
    }

    public static boolean shouldAskServer(Appointment data) {
        return data.getAppointmentType() == AppointmentType.PREMIUM && data.getStatus() == Status.DOING;
    }

    public static int getDuration(Appointment data) {
        try {
            return Integer.parseInt(data.getTakeTime());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static Intent getFirstMenu(Context context, int canEdit, int tab, Appointment data) {
        if (isAfterService(data)) {
            String id = String.valueOf(data.getId());
            if (canEdit == IntBoolean.FALSE) {
                return AfterServiceDoneActivity.intentFor(context, id, tab);
            } else {
                String recordId = String.valueOf(data.getUrgentRecord().getMedicalRecordId());
                return AfterServiceDoingActivity.intentFor(context, id, recordId, tab);
            }
        } else {
            data.canEdit = canEdit;
            return AppointmentDetailActivity.makeIntent(context, data, tab);
        }
    }


    public static int getDiscountMoney(Appointment data) {
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

        public  GotoConsultingCallback(View view) {
            this.view = view;
        }

        @Override
        protected void handleResponse(String response) {
            LoadingHelper.showMaterLoading(view.getContext(), "正在提示患者开始就诊");
            Tasks.runOnUiThread(new Runnable() {
                @Override
                public  void run() {
                    LoadingHelper.hideMaterLoading();
                    Intent i = ConsultingActivity.makeIntent(view.getContext());
                    view.getContext().startActivity(i);
                    AppManager.finishAllActivity();
                }
            }, 350);
        }
    }


    public static boolean payVisible(Appointment data) {
        return data.getStatus() == Status.UNPAID;
    }

    public static boolean isPayed(Appointment data) {
        return data.getStatus() == Status.PAID;
    }

    public static boolean hasDoctorComment(Appointment data) {
        return data.getDoctorPoint() > 0;
    }


    public static boolean returnNotPaid(Appointment data) {
        return data.getReturnInfo() != null && data.getReturnInfo().getReturnPaid() != 1 && data.getReturnInfo().getNeedReturn() == 1;
    }

    @Deprecated
    public static void historyDetail(View view, Appointment data) {
        Intent intent = HistoryDetailActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public static void historyList(Context context, Appointment data) {
        context.startActivity(AppointmentDetailActivity.makeIntent(context,
                data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY));
    }


    public static void onPatientClickOrder(BaseListAdapter adapter, BaseViewHolder vh, Appointment data) {
        switch (data.getStatus()) {
            case Status.UNPAID: {
                startPayActivity(vh.itemView.getContext(), data.getId());
                break;
            }
            case Status.PAID: {
                Intent intent = EditQuestionActivity.intentFor(vh.itemView.getContext(), data.getIdString(), QuestionsPath.NORMAL);
                vh.itemView.getContext().startActivity(intent);
                break;
            }
            case Status.FINISHED: {
                Intent intent = FinishedOrderActivity.makeIntent(vh.itemView.getContext(), data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY);
                vh.itemView.getContext().startActivity(intent);
                break;
            }
            case Status.DOING:
            case Status.WAITING: {
                chat(vh.itemView.getContext(), data);
                break;
            }
            default: {
                break;
            }
        }
    }

    public static void onDoctorClickOrder(final BaseViewHolder vh, final BaseListAdapter adapter,Appointment data) {
        switch (data.getStatus()) {
            case Status.PAID: {
                detail(vh.itemView.getContext(), vh.getItemViewType(),data);
                break;
            }
            case Status.FINISHED: {
                chat(adapter, vh,data);
                Intent intent = HistoryDetailActivity.makeIntent(vh.itemView.getContext(), data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY);
                vh.itemView.getContext().startActivity(intent);
                break;
            }
            case Status.DOING:
            case Status.WAITING: {
                chat(adapter, vh,data);
                break;
            }
            default: {
                break;
            }
        }
    }

    public static void drugPush(Appointment data) {
        Call<ApiDTO<String>> apiDTOCall;
        if (isAfterService(data)) {
            apiDTOCall = drugModule.pushFollowUpDrug(data.getIdString());
        } else {
            apiDTOCall = drugModule.pushDrug(data.getIdString());
        }

        apiDTOCall.enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                EventHub.post(new CloseDrawerEvent());
            }
        });
    }

    private static boolean isAfterService(Appointment data) {
        return data.getAppointmentType() == AppointmentType.AFTER_SERVICE;
    }


    private static boolean isDetail(Appointment data) {
        return data.getAppointmentType() == AppointmentType.PREMIUM;
    }


    public static void alertAppointmentFinished(Context context,Appointment data) {
        if (!Settings.isDoctor()) {
            switch (data.getStatus()) {
                case Status.LOCKED:
                case Status.FINISHED:
                case Status.UNPAID:
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


    public static void makePhoneCall(final Context context) {
//        AVChatActivity.start(context, getP2PId(), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL);
    }

    public static void callTelephone(final Context context,Appointment data) {
//        ImModule imModule = Api.of(ImModule.class);
//        imModule.makeYunXinPhoneCall(getMyPhoneNO(), getPhoneNO()).enqueue(new SimpleCallback<String>() {
//            @Override
//            protected void handleResponse(String response) {
//                Toast.makeText(context, "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
//            }
//        });
        ImModule imModule = Api.of(ImModule.class);
        imModule.makeYunXinPhoneCall(data.getId()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(context, "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String getVoipAccount(Appointment data) {
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

    public static String styledOrderStatus(Appointment data) {
        return "<font color='" + getStatusColor(data) + "'>" + getStatusLabel(data) + "</font>";
    }

    public static String styledOrderTypeAndStatus(Appointment data) {
        return "<font color='" + getStatusColor(data) + "'>" + data.getDisplayType() + "-" + getStatusLabel(data) + "</font>";
    }


    public static String getStatusColor(Appointment data) {
        switch (data.getStatus()) {
            case Status.FINISHED:
                return "#363636";

            case Status.UNPAID:
                return "#ff1800";

            case Status.PAID:
                return "#ff8e43";

            case Status.WAITING:
                return "#ff1800";

            case Status.DOING:
                return "#88cb5a";

            case Status.LOCKED:
            case Status.CANCEL:
                return "#898989";
            default:
                return "#acacac";
        }
    }

    public static boolean isFinished(Appointment data) {
        return getFinishedTime(data) < System.currentTimeMillis();
    }

    /**
     * 将bookTime: 2016-04-19 15:55－16:10 转换为毫秒
     *
     * @return
     */
    public static long getFinishedTime(Appointment data) {
        int orderStatus = data.getStatus();
//        if (orderStatus == null) {
//            orderStatus = "";
//        }
        if (orderStatus == Status.FINISHED) {
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


    private static long parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        Date parse = dateFormat.parse(date);
        return parse.getTime();
    }


    public static String chatStatus(Appointment data) {
        switch (data.getAppointmentType()) {
            case AppointmentType.AFTER_SERVICE:
                return "随访" + getStatusLabel(data);
        }
        if (Settings.isDoctor()) {
            return "本次咨询已结束";
        } else {
            return "本次咨询已结束,如需咨询,请再次预约";
        }
    }

    public static String bookTimeStatus(Appointment data) {
        switch (data.getAppointmentType()) {
            case AppointmentType.AFTER_SERVICE:
                return "发起时间:" + getBookTime(data);
            default:
                return "预约时间:" + getBookTime(data);
        }
    }


    @NonNull
    public static RealmQuery<TextMsg> queryAllMsg(Realm realm, Appointment data) {
        return realm.where(TextMsg.class)
                .equalTo("sessionId", String.valueOf(data.getTid()));
    }

    public static RealmResults<TextMsg> sortedByTime(RealmQuery<TextMsg> query) {
        RealmResults<TextMsg> results = query.findAllSorted("time");
        return results;
    }

    public static RealmQuery<TextMsg> unreadMsg(RealmQuery<TextMsg> query) {
        return query.equalTo("haveRead", false);
    }

    public static long unreadMsgCount(Appointment data) {
        return unreadMsg(queryAllMsg(Realm.getDefaultInstance(), data)).count();
    }

    public static long msgCount(Appointment data) {
        return queryAllMsg(Realm.getDefaultInstance(), data).count();
    }

    public static TextMsg lastMsg(Appointment data) {
        RealmResults<TextMsg> results = sortedByTime(queryAllMsg(Realm.getDefaultInstance(), data));
        if (results.isEmpty()) {
            return new TextMsg();
        }
        return results.last();
    }


    public static boolean showCommentBtn(Appointment data) {
        return data.getStatus() == Status.FINISHED;
    }

    public static boolean showAnswerQuestionBtn(Appointment data) {
        return getCurrentStatus(data) == 1;
    }

    public static void viewDetail(final Context context, final int tab,Appointment data) {
//        DiagnosisModule api = Api.of(DiagnosisModule.class);
//        String type = "appointment";
//        if (isAfterService()) {
//            type = "followUp";
//        }
//        api.appointmentStatus(appointmentId(), type).enqueue(new SimpleCallback<AppointmentStatus>() {
//            @Override
//            protected void handleResponse(AppointmentStatus response) {
//                int canEdit;
//                if (response != null) {
//                    canEdit = response.canEdit;
//                    String orderStatus = response.displayStatus;
//                    boolean isCanEditStatus = !orderStatus.equals(Status.FINISHED)
//                            && !orderStatus.equals(Status.A_FINISHED)
//                            && !orderStatus.equals(Status.REJECTED)
//                            && !orderStatus.equals(Status.A_UNPAID)
//                            && !orderStatus.equals(Status.CLOSED)
//                            && !orderStatus.equals(Status.A_CANCEL);
//                    if (isCanEditStatus) {
//                        canEdit = IntBoolean.TRUE;
//                    }
//                } else {
//                    canEdit = IntBoolean.NOT_GIVEN;
//                }
//                answerQuestion(context, tab, canEdit);
//            }
//        });

        AppointmentModule api = Api.of(AppointmentModule.class);
        api.appointmentDetail(data.getIdString()).enqueue(new SimpleCallback<Appointment>() {
            @Override
            protected void handleResponse(Appointment response) {
                answerQuestion(context, tab, response.canEdit, response);
            }
        });
    }

    public static void answerQuestion(Context context, int tab, int canEdit, Appointment data) {
        Intent i = getFirstMenu(context, canEdit, tab, data);
        if (i != null) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }


    public static String getStatusLabel(Appointment data) {
        return data.getDisplayStatus();
    }

    public static void showStatusTimeline(Context context,Appointment data) {
        RecyclerView recyclerView = new RecyclerView(context);
        SimpleAdapter adapter = new SimpleAdapter(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        String[] status = new String[]{"待付款", "填问卷", "进行中", "待建议", "已完成"};

        int currentStatus = getCurrentStatus(data);
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

    public static int getCurrentStatus(Appointment data) {
        switch (data.getStatus()) {
            case Status.LOCKED:
            case Status.CANCEL:
            case Status.FINISHED:
                return 4;

            case Status.UNPAID:
                return 0;

            case Status.PAID:
                return 1;

            case Status.WAITING:
                return 3;

            case Status.DOING:
                return 2;

            default:
                return 5;
        }
    }


    public static int getStatusBackground(Appointment data) {
        switch (data.getStatus()) {
            case Status.LOCKED:
            case Status.CANCEL:
            case Status.FINISHED:
                return R.drawable.shape_blue_rect_transparent;

            case Status.UNPAID:
                return R.drawable.shape_red_rect_transparent;

            case Status.PAID:
                return R.drawable.shape_orange_rect_transparent;

            case Status.WAITING:
                return R.drawable.shape_red_rect_transparent;

            case Status.DOING:
                return R.drawable.shape_green_rect_transparent;

            default:
                return R.drawable.shape_blue_rect_transparent;
        }
    }

    public static int getStatusArrow(Appointment data) {
        switch (data.getStatus()) {
            case Status.LOCKED:
            case Status.CANCEL:
            case Status.FINISHED:
                return R.drawable.ic_chevron_right_blue_24dp;

            case Status.UNPAID:
                return R.drawable.ic_chevron_right_red_24dp;

            case Status.PAID:
                return R.drawable.ic_chevron_right_orange_24dp;

            case Status.WAITING:
                return R.drawable.ic_chevron_right_red_24dp;

            case Status.DOING:
                return R.drawable.ic_chevron_right_green_24dp;

            default:
                return R.drawable.ic_chevron_right_green_24dp;
        }
    }


    public static String styledOrderStatus2(Appointment data) {
        return "<font color='" + getStatusColor2(data) + "'>" + getStatusLabel(data) + "</font>";
    }

    public static String getStatusColor2(Appointment data) {
        switch (data.getStatus()) {
            case Status.FINISHED:
                return "#339de1";

            case Status.UNPAID:
                return "#ff1800";

            case Status.PAID:
                return "#ff8e43";

            case Status.WAITING:
                return "#ff1800";

            case Status.DOING:
                return "#88cb5a";

            case Status.LOCKED:
            case Status.CANCEL:
                return "#898989";
            default:
                return "#acacac";
        }
    }


    @IntDef
    public @interface Status {
        int UNPAID = 0;
        int PAID = 1;
        int DOING = 2;
        int WAITING = 3;
        int FINISHED = 4;
        int CANCEL = 6;
        int LOCKED = 7;
    }
}