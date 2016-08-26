package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Messenger;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityViewFeedbackBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;


/**
 * Created by rick on 12/17/15.
 */
public class ViewFeedbackActivity extends BaseFragmentActivity2 {
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private PActivityViewFeedbackBinding binding;
    private Doctor doctor;

    public static Intent intentFor(Context context, Doctor doctor) {
        Intent intent = new Intent(context, ViewFeedbackActivity.class);
        intent.putExtra(Constants.DATA, doctor);
        return intent;
    }

    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private Messenger getHandler() {
        return getIntent().getParcelableExtra(Constants.HANDLER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_view_feedback);

        binding.setData(getData());


    }

    private static class Eval {
        private float punctuality;
        private float professional;
        private float sincerity;

        public Eval(float punctuality, float professional, float sincerity) {
            this.punctuality = punctuality;
            this.professional = professional;
            this.sincerity = sincerity;
        }

        public double mean() {
            return (punctuality + professional + sincerity) / 3.0f;
        }

        public String getDetail() {
            return "{" +
                    "\"守时\":" + punctuality +
                    ", \"专业\":" + professional +
                    ", \"态度\":" + sincerity +
                    '}';
        }

        @Override
        public String toString() {
            return "Eval{" +
                    "punctuality=" + punctuality +
                    ", professional=" + professional +
                    ", sincerity=" + sincerity +
                    '}';
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_feed_back;
    }
}