package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.AppContext;
import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.avchat.activity.AVChatActivity;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalHistory;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Tags;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.Gender;
import com.doctor.sun.entity.constans.IntBoolean;
import com.doctor.sun.entity.constans.QuestionsPath;
import com.doctor.sun.entity.im.TextMsg;
import com.doctor.sun.event.AppointmentHistoryEvent;
import com.doctor.sun.event.CloseDrawerEvent;
import com.doctor.sun.event.DismissHistoryListDialogEvent;
import com.doctor.sun.event.PayFailEvent;
import com.doctor.sun.event.PaySuccessEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.CancelCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.immutables.Appointment;
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
import com.doctor.sun.ui.activity.doctor.SinglePatientHistoryActivity;
import com.doctor.sun.ui.activity.doctor.SurfaceDoingActivity;
import com.doctor.sun.ui.activity.doctor.SurfaceFinishedActivity;
import com.doctor.sun.ui.activity.patient.AppointmentDetailActivity;
import com.doctor.sun.ui.activity.patient.EditQuestionActivity;
import com.doctor.sun.ui.activity.patient.FinishedOrderActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.fragment.PayAppointmentFragment;
import com.doctor.sun.ui.widget.AppointmentHistoryDialog;
import com.doctor.sun.ui.widget.HistoryListDialog;
import com.doctor.sun.util.ItemHelper;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.util.PayCallback;
import com.doctor.sun.vm.BaseItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.LoadingHelper;
import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Tasks;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.doctor.sun.ui.widget.AppointmentHistoryDialog.HISTORY_INDEX;

/**
 * Created by rick on 11/20/15.
 * 所有关于预约的逻辑都在这里, 跳转界面,是否过期等,剩下的进行中时间
 */
public class AppointmentHandler2 {
    public static final int RECORD_AUDIO_PERMISSION = 40;
    private static AppointmentModule api = Api.of(AppointmentModule.class);
    private static DrugModule drugModule = Api.of(DrugModule.class);
    public static Appointment data;

    public AppointmentHandler2(Appointment appointment) {
        this.data = appointment;
    }

    public static String getPatientName(Appointment data) {
        return data.getRecord().getPatientName();
    }

    public static String getRecordName(Appointment data) {
        return data.getRecord().getRecordName();
    }


    public static String getRelationAndName(Appointment data) {
        return "(" + data.getRecord().getRelation() + "/" + data.getRecord().getRecordName() + ")";
    }

    public static String getRelationWithPatient(Appointment data) {
        return "(由患者" + data.getRecord().getRelation() + "咨询)";
    }

    public static String getRelationWithPatient2(Appointment data) {
        return "(由患者" + data.getRecord().getRelation() + data.getRecord().getPatientName() + "咨询)";
    }

    public static String getRelation(Appointment data) {
        return data.getRecord().getRelation();
    }

    public static String getGender(Appointment data) {
        if (data.getRecord().getGender() == Gender.MALE) {
            return "男";
        } else {
            return "女";
        }
    }

    public static String getGender2(MedicalHistory data) {
        if (data.getGender() == Gender.MALE) {
            return "男";
        } else {
            return "女";
        }
    }

    public static String getGenderAndAge(Appointment data) {
        return String.format("%s/%s岁", getGender(data), data.getRecord().getAge());
    }

    //返回医生界面病历库的第一层界面显示的数据
    public static String getMage(MedicalHistory data) {
        String content = String.format("%s/%s岁", getGender2(data), data.getAge());
        return data.record_name + "    (" + content + ")";
    }

    public static String getBirthday(Appointment data) {
        return data.getRecord().getBirthday();
    }

    public static boolean isCouponUsed(Appointment data) {
        return getDiscountMoney(data) > 0;
    }

    public static String getConsultingTitle(Appointment data) {
        return "病历: " + getRecordName(data);
    }

    public static String getTime(Context context, Appointment data) {
        switch (data.getType()) {
            case AppointmentType.FollowUp:
                return "随访时间: " + data.getTime_bucket();
            default:
                return data.getTime_bucket();
        }
    }

    public static String getInterviewer(Appointment data) {
        MedicalRecord record = data.getRecord();
        return String.format("交谈者:  %s(患者的%s)", record.getPatientName(), record.getRelation());
    }

    public static String getConsultee(Appointment data) {
        MedicalRecord record = data.getRecord();
        return String.format("咨询者:  %s(患者的%s)", record.getPatientName(), record.getRelation());
    }

    public static boolean isCanceled(Appointment data) {
        return data.getStatus() == Status.CANCEL;
    }


    public static void cancel2(final BaseListAdapter adapter, final BaseViewHolder vh, final Appointment data) {
        Intent intent = CancelAppointmentActivity.makeIntent(vh.itemView.getContext(), data);
        final Handler target = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

    public static void pCancel(BaseListAdapter component, BaseViewHolder vh, String id) {
        api.pCancel(id).enqueue(new CancelCallback(vh, component));
    }

    public static void startPayActivity(Context context, String id) {
        Bundle args = PayAppointmentFragment.getArgs(String.valueOf(id));
        Intent intent = SingleFragmentActivity.intentFor(context, "确认支付", args);
        context.startActivity(intent);
    }

    public static void payWithAlipay(final Activity activity, String couponId, Appointment data) {
        String id = data.getId();
        LoadingHelper.showMaterLoading(activity, "正在预约...");
        api.buildAliPayOrder(id, "alipay", couponId).enqueue(new AlipayCallback(activity, data));
    }

    public static void payWithWeChat(final Activity activity, String couponId, Appointment data) {
        String id = data.getId();
        LoadingHelper.showMaterLoading(activity, "正在预约...");
        api.buildWeChatOrder(id, "wechat", couponId).enqueue(new WeChatPayCallback(activity, data));
    }


    public static void simulatedPayImpl(final Appointment data) {
        final PayCallback mCallback = new PayCallback() {
            @Override
            public void onPaySuccess() {
                EventHub.post(new PaySuccessEvent(data));
            }

            @Override
            public void onPayFail() {
                EventHub.post(new PayFailEvent(data, false));
            }
        };
        AppointmentModule api = Api.of(AppointmentModule.class);
        api.pay(data.getId()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                if (mCallback != null) {
                    mCallback.onPaySuccess();
                }
            }

            @Override
            public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                super.onFailure(call, t);
                if (mCallback != null) {
                    mCallback.onPayFail();
                }
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
    public static void remind(final Context context, String appointmentId, int patientId) {
        api.remind(appointmentId, patientId)
                .enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        Toast.makeText(context, "成功提醒患者", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Deprecated
    public static void accept(final Context context, final Appointment data) {
        new MaterialDialog.Builder(context)
                .content("若需要提前进行就诊，请先与患者确认。（点击下方通话键可联系患者）是否确认提前就诊？")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ApiCallback<String> callback = new GotoConsultingCallback(context);
                        api.startConsulting(data.getId())
                                .enqueue(callback);
                    }
                }).show();
    }


    /**
     * android:onClick="@{()->data.handler.detail(context,vh.itemViewType)}"
     */
    public static void detail(Context context, int type, Appointment data) {
        Intent i = PatientDetailActivity.makeIntent(context, data, type);
        context.startActivity(i);
    }


    public static void pComment(final BaseListAdapter adapter, final BaseViewHolder vh, final Appointment data) {
        if (!hasDoctorComment(data)) {
            Context context = vh.itemView.getContext();
            Intent i = com.doctor.sun.ui.activity.patient.FeedbackActivity.makeIntent(context, data);
            Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    String obj = String.valueOf(msg.obj);
                    data.getDoctor().setPoint(Float.valueOf(obj));
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
        if (hasTid(data)) {
            Intent intent = ChattingActivity.makeIntent(context, data);
            context.startActivity(intent);
        } else {
            if (data.getType() == 4) {
                Intent toSurface = SurfaceDoingActivity.makeIntent(context, data);
                context.startActivity(toSurface);
            }
           /* if (BuildConfig.DEV_MODE) {
                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    private static boolean hasTid(Appointment data) {
        return !"0".equals(data.getTid());
    }

    public static void chat(BaseListAdapter adapter, BaseViewHolder vh, Appointment data) {
        AppContext.getInstance().setData(data);
        if (hasTid(data)) {
            Intent intent = ChattingActivity.makeIntent(vh.itemView.getContext(), data);
            if (adapter != null) {
                intent.putExtra(ItemHelper.HANDLER, new Messenger(new Handler(new Callback(adapter, vh.getAdapterPosition()))));
            }
            vh.itemView.getContext().startActivity(intent);
        } else {
         /*   if (data.getType() == 4) {
                Intent toSurface = SurfaceDoingActivity.makeIntent(vh.itemView.getContext(), data);
                vh.itemView.getContext().startActivity(toSurface);
            }*/
          /*  if (BuildConfig.DEV_MODE) {
                Toast.makeText(vh.itemView.getContext(), "云信群id为0", Toast.LENGTH_SHORT).show();
            }*/
        }
    }

    public static void chatNoMenu(Context context, Appointment data) {
        if (hasTid(data)) {
            Intent intent = ChattingActivityNoMenu.makeIntent(context, data);
            context.startActivity(intent);
        } else {
            if (data.getType() == 4) {
                Intent toSurface = SurfaceDoingActivity.makeIntent(context, data);
                context.startActivity(toSurface);
            }
           /* if (BuildConfig.DEV_MODE) {
                Toast.makeText(context, "云信群id为0", Toast.LENGTH_SHORT).show();
            }*/
        }
    }


    public static boolean enablePush(Appointment data) {
        if (Settings.isDoctor()) {
            return true;
        }
        if (isDetail(data)) {
            return data.getStatus() == Status.DOING;
        } else {
            return data.getStatus() == Status.DOING && data.getDoctor().push_enable == IntBoolean.TRUE;
        }
    }


    public static boolean shouldAskServer(Appointment data) {
        return data.getType() == AppointmentType.PREMIUM && data.getStatus() == Status.DOING;
    }

    public static int getDuration(Appointment data) {
        return (int) data.getTake_time();
    }

    public static Intent getFirstMenu(Context context, int tab, Appointment data) {
        AppContext.getInstance().setData(data);
        if (data.getType() == AppointmentType.FollowUp) {
            String id = String.valueOf(data.getId());
            if (Status.FINISHED == data.getStatus()) {
                if (Settings.isDoctor()) {
                    if (data.getCan_edit() != 0) {
                        AppContext.getInstance().setPosition(data.getCan_edit());
                        return AfterServiceDoingActivity.intentFor(context, id, data.getRecord_id(), tab);
                    } else {
                        return AfterServiceDoneActivity.intentFor(context, id, tab);
                    }

                } else {
                    return AfterServiceDoneActivity.intentFor(context, id, tab);
                }
            } else {
                String recordId = String.valueOf(data.getRecord_id());
                AppContext.getInstance().setPosition(0);
                return AfterServiceDoingActivity.intentFor(context, id, recordId, tab);
            }
        } else {
            if (data.getStatus() == Status.PAID) {
                if (Settings.isDoctor()) {
                    return PatientDetailActivity.makeIntent(context, data, 0);
                } else {
                    return EditQuestionActivity.intentFor(context, data.getId(), QuestionsPath.NORMAL);
                }
            }
//            data.canEdit = canEdit;
            return AppointmentDetailActivity.makeIntent(context, data, tab);

        }
    }


    public static int getDiscountMoney(Appointment data) {
        try {
            double money = data.getMoney();
            double needPayMoney = data.getNeed_pay();
            return (int) (money - needPayMoney);
        } catch (ClassCastException e) {
            return 0;
        }
    }

    private static class GotoConsultingCallback extends SimpleCallback<String> {
        private final Context context;

        public GotoConsultingCallback(Context context) {
            this.context = context;
        }

        @Override
        protected void handleResponse(String response) {
            LoadingHelper.showMaterLoading(context, "正在提示患者开始就诊");
            Tasks.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    LoadingHelper.hideMaterLoading();
                    Intent i = ConsultingActivity.makeIntent(context);
                    context.startActivity(i);
                    AppManager.finishAllActivity();
                }
            }, 350);
        }
    }


    public static boolean payVisible(Appointment data) {
        return data.getStatus() == Status.UNPAID || data.getStatus() == Status.PAID;
    }

    public static boolean hasPay(Appointment data) {
        return data.getStatus() == Status.UNPAID;
    }

    public static boolean isPayed(Appointment data) {
        return data.getStatus() == Status.PAID;
    }

    public static boolean hasDoctorComment(Appointment data) {
        return data.getDoctor_point() > 0;
    }


    public static void onPatientClickOrder(BaseListAdapter adapter, BaseViewHolder vh, Appointment data) {
        switch (data.getStatus()) {
            case Status.UNPAID: {
                startPayActivity(vh.itemView.getContext(), data.getId());

                break;
            }
            case Status.PAID: {
                Intent intent = EditQuestionActivity.intentFor(vh.itemView.getContext(), data.getId(), QuestionsPath.NORMAL);
                vh.itemView.getContext().startActivity(intent);
                break;
            }
            case Status.FINISHED: {
                if (data.getType() == AppointmentType.FollowUp) {
                    Intent intent = AfterServiceDoneActivity.intentFor(vh.itemView.getContext(), data.getId(), 0);
                    vh.itemView.getContext().startActivity(intent);
                } /*else if (data.getType() == 4) {
                    Intent intent = SurfaceFinishedActivity.makeIntent(vh.itemView.getContext(), data);
                    vh.itemView.getContext().startActivity(intent);
                } */ else {
                    Intent intent = FinishedOrderActivity.makeIntent(vh.itemView.getContext(), data, AppointmentDetailActivity.POSITION_SUGGESTION_READONLY);
                    vh.itemView.getContext().startActivity(intent);
                }
                break;
            }
            case Status.DOING:
            case Status.WAITING: {
                chat(adapter, vh, data);
                break;
            }
            default: {
                break;
            }
        }
    }

    public static void onDoctorClickOrder(final BaseViewHolder vh, final BaseListAdapter adapter, final Appointment data) {
        AppContext.getInstance().setType(data.getType());
        AppContext.getInstance().setKeepState(null);
        AppContext.getInstance().setKeepState(data);
        AppContext.getInstance().setAppointment(data);
        switch (data.getStatus()) {
            case Status.PAID: {
                detail(vh.itemView.getContext(), vh.getItemViewType(), data);
                break;
            }
            case Status.FINISHED: {
                getAppointmentDetail(data.getId(), new SimpleCallback<Appointment>() {
                    @Override
                    protected void handleResponse(Appointment response) {
                        chat(adapter, vh, response);
                        answerQuestion(vh.itemView.getContext(), 2, response);
                    }
                });
            /*    if (data.getType() == 4) {
                    Intent intent = SurfaceFinishedActivity.makeIntent(vh.itemView.getContext(), data);
                    vh.itemView.getContext().startActivity(intent);
                } else {

                }*/
                break;
            }
            case Status.DOING: {
                chat(adapter, vh, data);
                break;
            }
            case Status.WAITING: {
                chat(adapter, vh, data);
                break;
            }
            default: {
                break;
            }
        }
    }

    //点击跳转到某个病人的病历历史记录
    public static void onSingleOrder(final BaseViewHolder vh, String id) {
        Context context = vh.itemView.getContext();
        Intent intent = new Intent(context, SinglePatientHistoryActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    public static void showHistoryDetail(final BaseViewHolder vh, final BaseListAdapter adapter, Appointment data) {
        Config.putInt("HISTORY_INDEX" + Config.getString(Constants.ADDRESS) + "", vh.getAdapterPosition());
        EventHub.post(new AppointmentHistoryEvent(data, false));
        EventHub.post(new DismissHistoryListDialogEvent());
    }


    public static void drugPush(Appointment data) {
        Call<ApiDTO<String>> apiDTOCall;
        apiDTOCall = drugModule.pushDrug(data.getId());

        apiDTOCall.enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                EventHub.post(new CloseDrawerEvent());
            }
        });
    }

    private static boolean isAfterService(Appointment data) {
        return data.getType() == AppointmentType.FollowUp;
    }


    private static boolean isDetail(Appointment data) {
        return data.getType() == AppointmentType.PREMIUM;
    }


    public static void alertAppointmentFinished(Context context, Appointment data) {
        if (!Settings.isDoctor()) {
            switch (data.getStatus()) {
                case Status.LOCKED:
                case Status.FINISHED:
                case Status.UNPAID:
                    switch (data.getType()) {
                        case AppointmentType.FollowUp:
                            Toast.makeText(context, "随访已经结束,请耐心等待下次随访", Toast.LENGTH_SHORT).show();
                            break;
                        default: {
                            Toast.makeText(context, "预约已经结束,请重新预约", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    break;
                case Status.PAID: {
                    Toast.makeText(context, "新的预约时间未到,请耐心等待", Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
            }
        }
    }


    public static void makePhoneCall(final Context context, Appointment data) {
        AVChatActivity.start(context, getTargetP2PId(data), AVChatType.AUDIO.getValue(), AVChatActivity.FROM_INTERNAL);
    }

    public static void callTelephone(final Context context, Appointment data) {
        ImModule imModule = Api.of(ImModule.class);
        imModule.makeYunXinPhoneCall(data.getId()).enqueue(new retrofit2.Callback<ApiDTO>() {
            @Override
            public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "回拨呼叫成功,请耐心等待来电", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiDTO> call, Throwable t) {

            }
        });
    }

    public static void askServer(Context context) {
        Intent intent = MedicineStoreActivity.intentForCustomerService(context);
        context.startActivity(intent);
    }


    public static String getTargetP2PId(Appointment data) {
        if (Settings.isDoctor()) {
            if (data.getRecord() != null) {
                return data.getRecord().getYunxinAccid();
            } else {
                return data.getYunxin_accid();
            }
        } else {
            if (data.getDoctor() != null) {
                return data.getDoctor().getYunxinAccid();
            } else {
                return data.getYunxin_accid();
            }
        }
    }


    public static boolean isFinished(Appointment data) {
        if (data.getStatus() == Status.DOING) {
            return false;
        }
        return getFinishedTime(data) < System.currentTimeMillis();
    }

    /**
     * @return
     */
    public static long getFinishedTime(Appointment data) {
        if (data.getType() == 4) {
            return 0;
        }
        if (data.getType() == 2) {
            if (!Settings.isDoctor()) {
                return 0;
            }
        }
        if (data.getStatus() == Status.FINISHED) {
            return 0;
        }
        if (data.getStatus() == Status.PAID) {
            return 0;
        }
        String visitTime = data.getVisit_time();
        long duration = (long) (data.getTake_time() * 60000L);
        try {
            return parseDate(visitTime) + duration;
        } catch (Exception ignored) {
        }
        return 0;
    }


    private static long parseDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date parse = dateFormat.parse(date);
        return parse.getTime();
    }


    public static String chatStatus(Appointment data) {
        switch (data.getType()) {
            case AppointmentType.FollowUp: {
                return "随访" + data.getDisplay_status();
            }
            case AppointmentType.FACE: {
                return "面诊" + data.getDisplay_status();
            }
            case AppointmentType.STANDARD: {
                if (data.getStatus() == 2) {
                    return "复诊咨询已开始,可立即留言咨询医生";
                } else {
                    return "复诊" + data.getDisplay_status();
                }

            }
        }

        int status = data.getStatus();
        switch (status) {
            case Status.FINISHED:
                if (Settings.isDoctor()) {
                    return "本次咨询已结束";
                } else {
                    return "本次咨询已结束,如需咨询,请再次预约";
                }
            case Status.PAID:
                return "非就诊时段,已预约" + data.getTime_bucket() + "   " + data.getDisplay_type();
            default:
                return "本次咨询" + data.getDisplay_status();
        }
    }

    public static int chatStatusTextColor(Appointment data, Context context) {
        if (data == null) {
            return context.getResources().getColor(R.color.white);
        }
        int status = data.getStatus();
        switch (status) {
            case Status.DOING:
                return context.getResources().getColor(R.color.brown);
            default:
                return context.getResources().getColor(R.color.white);
        }
    }

    public static int chatStatusBackgroundColor(Appointment data) {
        if (data == null) {
            return R.color.grey_77;
        }
        int status = data.getStatus();
        switch (status) {
            case Status.PAID:
                return R.color.yellow_dark;
            case Status.DOING:
                return R.color.yellow;
            default:
                return R.color.grey_77;
        }
    }

    @NonNull
    public static RealmQuery<TextMsg> queryAllMsg(Realm realm, Appointment data) {
        return realm.where(TextMsg.class)
                .equalTo("sessionId", String.valueOf(data.getTid()));
    }

    public static RealmResults<TextMsg> sortedByTime(RealmQuery<TextMsg> query) {
        return query.findAllSorted("time");
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

    public static TextMsg appLastMsg(Appointment data) {
        RealmResults<TextMsg> results = sortedByTimes(queryAllMsg(Realm.getDefaultInstance(), data));
        if (results.isEmpty()) {
            return new TextMsg();
        }
        return results.last();
    }

    public static RealmResults<TextMsg> sortedByTimes(RealmQuery<TextMsg> query) {
        return query.findAllSorted("time", Sort.DESCENDING);
    }


    public static boolean showCommentBtn(Appointment data) {
        return data.getStatus() == Status.FINISHED;
    }

    public static boolean showAnswerQuestionBtn(Appointment data) {
        return getCurrentStatus(data) == 1 || getCurrentStatus(data) == 2 || getCurrentStatus(data) == 3;
    }

    public static boolean showCancelButton(Appointment data) {
        return getCurrentStatus(data) == 1;
    }

    public static void viewDetail(final Context context, Appointment data) {
        if (isFinished(data)) {
            viewDetail(context, 1, data);
        } else {
            viewDetail(context, 0, data);
        }
    }

    public static void viewDetail(final Context context, final int tab, Appointment data) {
        LoadingHelper.showMaterLoading(context, "正在加载...");
        getAppointmentDetail(data.getId(), new SimpleCallback<Appointment>() {
            @Override
            protected void handleResponse(Appointment response) {
                LoadingHelper.hideMaterLoading();
                answerQuestion(context, tab, response);
            }

            @Override
            public void onFailure(Call<ApiDTO<Appointment>> call, Throwable t) {
                super.onFailure(call, t);
                LoadingHelper.hideMaterLoading();
            }
        });
    }

    public static void getAppointmentDetail(String id, SimpleCallback<Appointment> callback) {
        AppointmentModule api = Api.of(AppointmentModule.class);
        api.appointmentDetail(id).enqueue(callback);
    }

    public static void answerQuestion(Context context, int tab, Appointment data) {
        Intent i = getFirstMenu(context, tab, data);
        if (i != null) {
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

    public static void showCancelDialog(Context context, Appointment data) {
        final Dialog dialog = new Dialog(context, R.style.transparentFrameWindowStyle);
        View view = LayoutInflater.from(context).inflate(R.layout.cancel_dialog, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView tv_positive = (TextView) view.findViewById(R.id.tv_positive);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Window dialogWindow = dialog.getWindow();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display d = windowManager.getDefaultDisplay(); // 获取屏幕宽、高度
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.3); // 高度设置为屏幕的0.3，根据实际情况调整
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8，根据实际情况调整
        dialogWindow.setAttributes(p);
        dialog.setContentView(view);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static void showStatusTimeline(Context context, Appointment data) {
        RecyclerView recyclerView = new RecyclerView(context);
        SimpleAdapter adapter = new SimpleAdapter();
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

    public static String styledOrderStatus(Appointment data) {
        return String.format("<font color='%s'>%s</font>", getStatusColor(data), data.getDisplay_status());
    }

    public static String styledOrderTypeAndStatus(Appointment data) {
        return String.format("<font color='%s'>%s-%s</font>", getStatusColor(data), data.getDisplay_type(), data.getDisplay_status());
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

    private static class Callback implements Handler.Callback {

        private final int position;
        private BaseListAdapter mAdapter;

        public Callback(BaseListAdapter mAdapter, int position) {
            this.mAdapter = mAdapter;
            this.position = position;
        }

        @Override
        public boolean handleMessage(Message msg) {
            if (mAdapter != null) {
                if (position >= 0 && position < mAdapter.size()) {
                    Appointment data = JacksonUtils.fromJson(msg.obj.toString(), Appointment.class);
                    mAdapter.update(position, data);
                    mAdapter.notifyItemChanged(position);
                    mAdapter = null;
                }
            }
            return false;
        }
    }


    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }

    public static int showDrugMain(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state != 0) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int showDrugDoing(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state == 1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int showDrug(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state == 2) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int showDrugPayed(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state == 3) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int showDrugSented(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state == 4) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int showDrugReseived(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state == 5) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int showDrugRefunding(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state == -1) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }

    public static int showDrugRefunded(Appointment data) {
        if (AppContext.getInstance().getKeepState() != null) {
            data = AppContext.getInstance().getKeepState();
        }
        if (data.getDrug_orders().state == -2) {
            return View.VISIBLE;
        } else {
            return View.GONE;
        }
    }
}