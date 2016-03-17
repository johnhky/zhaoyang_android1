package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.EmergencyCall;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.AlipayCallback;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.http.callback.WeChatPayCallback;
import com.doctor.sun.module.EmergencyModule;
import com.doctor.sun.ui.activity.patient.PayFailActivity;
import com.doctor.sun.ui.activity.patient.PaySuccessActivity;
import com.doctor.sun.ui.activity.patient.UrgentAddFeeActivity;
import com.doctor.sun.ui.activity.patient.UrgentAddTimeActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.PayMethodDialog;
import com.doctor.sun.util.PayCallback;
import com.doctor.sun.util.PayInterface;
import com.doctor.sun.util.TimeUtils;

import java.util.HashMap;

/**
 * Created by rick on 22/1/2016.
 */
public class EmergencyCallHandler implements PayInterface {

    public static final int NOT_VALID = 0;
    public static final int NOT_PAY = 1;
    public static final int PAY = 2;
    private static EmergencyModule api = Api.of(EmergencyModule.class);

    private final EmergencyCall data;

    public EmergencyCallHandler(EmergencyCall emergencyCall) {
        this.data = emergencyCall;
    }

    public OnItemClickListener click() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, final View view, final BaseViewHolder vh) {
                switch (view.getId()) {
                    case R.id.delete:
                    case R.id.cancel: {
                        api.cancel(data.getId()).enqueue(new ApiCallback<HashMap<String, String>>() {
                            @Override
                            protected void handleResponse(HashMap<String, String> response) {
                                data.setIsValid(0);
                                adapter.notifyItemChanged(vh.getAdapterPosition());
                            }
                        });
                        break;
                    }
                    case R.id.pay: {
                        new PayMethodDialog(view.getContext(), EmergencyCallHandler.this).show();
                        break;
                    }
                    case R.id.add_money: {
                        Intent intent = UrgentAddFeeActivity.makeIntent(view.getContext(), data);
                        view.getContext().startActivity(intent);
                        break;
                    }
                    case R.id.add_time: {
                        Intent intent = UrgentAddTimeActivity.makeIntent(view.getContext(), data);
                        Messenger messenger = new Messenger(new Handler(new Handler.Callback() {
                            @Override
                            public boolean handleMessage(Message msg) {
                                data.setWaitingTime(data.getWaitingTime() + Long.parseLong(String.valueOf(msg.obj)));
                                adapter.notifyItemChanged(vh.getAdapterPosition());
                                return false;
                            }
                        }));
                        intent.putExtra(Constants.HANDLER, messenger);
                        view.getContext().startActivity(intent);
                        break;
                    }
                }
            }
        };
    }

    public String displayTime() {
        String result = data.getCreatedAt().substring(0, data.getCreatedAt().length() - 3);
        if (data.getIsPay() == 1) {
            long time = System.currentTimeMillis() - data.getPayTime() * 1000;
            result += "(已等候" + TimeUtils.getReadableTime(time) + ")";
        } else {
            result += "(支付后计算等候时间)";
        }
        return result;
    }

    public String willingToWait() {
        return TimeUtils.getReadableTime(data.getWaitingTime() * 1000);
    }

    public int payStatus() {
        if (data.getIsValid() == 1) {
            if (data.getIsPay() == 1) {
                if (data.getIsPayAdd() == 1) {
                    //已支付所有
                    return PAY;
                } else {
                    //追加诊金未支付
                    return NOT_PAY;
                }
            } else {
                //未支付
                return NOT_PAY;
            }
        } else {
            //已关闭
            return NOT_VALID;
        }
    }

    public boolean addOperationVisible() {
        return data.getIsValid() == 1 && (data.getIsPay() == 1 && data.getIsPayAdd() == 1);
    }

    public boolean payVisible() {
        boolean b = data.getIsValid() == 1 && !(data.getIsPay() == 1 && data.getIsPayAdd() == 1);
        return b;
    }

    public String status() {
        if (data.getIsValid() == 1) {
            if (data.getIsPay() == 1) {
                if (data.getIsPayAdd() == 1) {
                    return "<font color='#88cb5a'>已支付</font>";
                } else {
                    return "<font color='#f76d02'>未支付</font>";
                }
            } else {
                return "<font color='#f76d02'>未支付</font>";
            }
        } else {
            return "<font color='#898989'>已关闭</font>";
        }
    }

    public String titleLabel() {
        StringBuilder sb = new StringBuilder();
        if (data.getSearchTitle().equals("0")) {
            return "不限";
        }
        if (data.getSearchTitle().equals("12345")) {
            return "不限";
        }
        if (data.getSearchTitle().contains("1")) {
            sb.append("主任/副主任医师,");
        }

        if (data.getSearchTitle().contains("2")) {
            sb.append("主治医师,");
        }
        if (data.getSearchTitle().contains("3")) {
            sb.append("心理咨询师,");
        }
        if (data.getSearchTitle().contains("4")) {
            sb.append("医师,");
        }
        if (data.getSearchTitle().contains("5")) {
            sb.append("心理治疗师");
        }
        return sb.toString();
    }

    public boolean addMoneyVisible() {
        return data.getAddMoney() > 0;
    }

    @Override
    public void payWithAlipay(final Activity activity) {
        api.buildOrder(data.getId(), "alipay").enqueue(new AlipayCallback(activity, data));
    }

    @Override
    public void payWithWeChat(final Activity activity) {
        api.buildWeChatOrder(data.getId(), "wechat").enqueue(new WeChatPayCallback(activity, data));
    }

    @Override
    public void simulatedPay(final BaseAdapter component, final View view, final BaseViewHolder vh) {
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
        api.pay(data.getId()).enqueue(new SimpleCallback<String>() {
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
}
