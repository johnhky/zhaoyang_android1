package com.doctor.sun.ui.activity.patient.handler;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.constans.ReviewStatus;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.doctor.SettingActivity;
import com.doctor.sun.ui.activity.patient.CouponTabActivity;
import com.doctor.sun.ui.activity.patient.FavDoctorActivity;
import com.doctor.sun.ui.activity.patient.FollowUpDoctorListActivity;
import com.doctor.sun.ui.activity.patient.RechargeActivity;
import com.doctor.sun.ui.activity.patient.RecordListActivity;
import com.doctor.sun.ui.fragment.EditPatientInfoFragment;

/**
 * Created by lucas on 1/4/16.
 */
public class MeHandler extends BaseObservable {
    private Patient data;

    public MeHandler(Patient patient) {
        data = patient;
    }

    public void info(View view) {
//        Intent intent = EditPatientInfoActivity.intentFor(view.getContext(), data);
//        view.getContext().startActivity(intent);
        Intent intent = SingleFragmentActivity.intentFor(view.getContext(), "我", EditPatientInfoFragment.getArgs(data));
        view.getContext().startActivity(intent);
    }

    public void History(View view) {
        Intent intent = FollowUpDoctorListActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Record(View view) {
        Intent intent = RecordListActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Document(View view) {
        Intent intent = FavDoctorActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Recharge(View view) {
        Intent intent = RechargeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void Setting(View view) {
        Intent intent = SettingActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void coupon(View view) {
        Intent intent = CouponTabActivity.intentFor(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void followUpDoctorList(Context context) {
        Intent intent = FollowUpDoctorListActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void setData(Patient data) {
        this.data = data;
        notifyChange();
    }
    public boolean reviewStatusVisible() {
        return ReviewStatus.STATUS_PENDING.equals(data.getReview_status());
    }

    public Patient getData() {
        return data;
    }
}
