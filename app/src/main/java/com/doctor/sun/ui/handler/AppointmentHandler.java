package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.AppointmentType;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
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
import com.doctor.sun.ui.widget.PayMethodDialog;
import com.doctor.sun.util.PayCallback;

import java.util.HashMap;

import io.ganguo.library.AppManager;
import io.ganguo.library.Config;
import io.ganguo.library.common.ToastHelper;

/**
 * Created by rick on 11/20/15.
 */
public class AppointmentHandler implements LayoutId, PayMethodInterface, com.doctor.sun.util.PayInterface {
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
        ApiCallback<String> callback = new GotoConsultingCallback(view);
        api.startConsulting(data.getId())
                .enqueue(callback);
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
            Intent intent = PickDateActivity.makeIntent(v.getContext(), doctor, AppointmentType.QUICK);
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
                drugModule.pushDrug(data.getId()).enqueue(new ApiCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {

                    }

                    @Override
                    protected void handleApi(ApiDTO<String> body) {
                        Log.e("TAG", "appointmentId: " + data.getId());
                    }
                });
            }
        };
    }
}