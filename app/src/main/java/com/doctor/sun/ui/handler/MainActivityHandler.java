package com.doctor.sun.ui.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.doctor.AfterServiceActivity;
import com.doctor.sun.ui.activity.doctor.AppointmentListActivity;

import io.ganguo.library.Config;

/**
 * Created by rick on 11/20/15.
 */
public class MainActivityHandler {
    /**
     * 已预约患者
     *
     * @param context
     */
    public void appointment(Context context) {
        Config.putBoolean(Constants.DATE,true);
        Bundle bundle = AppointmentListActivity.getArgs();
        Intent intent = SingleFragmentActivity.intentFor(context,"已预约患者",bundle);
        context.startActivity(intent);
    }

    /**
     * 紧急咨询
     *
     * @param view
     */
    public void emergencyCall(View view) {
//        Intent intent = UrgentListActivity.intentFor(getContext());
//        getContext().startActivity(intent);
    }

    /**
     * 随访患者
     *
     * @param context
     */
    public void consultation(Context context) {
        Bundle bundle = AfterServiceActivity.getArgs();
        Intent intent = SingleFragmentActivity.intentFor(context,"随访列表",bundle);
        context.startActivity(intent);
    }
}
