package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityFeedbackBinding;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.util.JacksonUtils;


/**
 * Created by rick on 12/17/15.
 * 评价
 */
public class FeedbackActivity extends BaseFragmentActivity2 {

    private AppointmentModule api = Api.of(AppointmentModule.class);
    private ActivityFeedbackBinding binding;


    public static Intent makeIntent(Context context, Appointment data) {
        Intent i = new Intent(context, FeedbackActivity.class);
        i.putExtra(Constants.DATA, JacksonUtils.toJson(data));
        return i;
    }

    private Appointment getData() {
        return JacksonUtils.fromJson(getIntent().getStringExtra(Constants.DATA), Appointment.class);
    }

    private Messenger getHandler() {
        return getIntent().getParcelableExtra(Constants.HANDLER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback);

        binding.setData(getData());
//        binding.setHandler(new CommentDoctorHandler(this));
        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Eval eval = new Eval(binding.ratingBar0.getRating(), binding.ratingBar1.getRating(), binding.ratingBar2.getRating(), binding.ratingBar3.getRating());

                String comment = binding.comment.etOthers.getText().toString();
                api.evaluatePatient(String.valueOf(eval.mean()), getData().getId(), eval.getDetail(), comment).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        Toast.makeText(FeedbackActivity.this, "成功评价病人", Toast.LENGTH_SHORT).show();
                        try {
                            Message message = new Message();
                            message.obj = eval.mean();
                            getHandler().send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });

            }
        });
    }


    private static class Eval {
        private float punctuality;
        private float comunication;
        private float sincerity;
        private float respect;

        public Eval(float punctuality, float comunication, float sincerity, float respect) {
            this.punctuality = punctuality;
            this.comunication = comunication;
            this.sincerity = sincerity;
            this.respect = respect;
        }

        public double mean() {
            return (punctuality + comunication + sincerity + respect) / 4.0f;
        }

        public String getDetail() {
            return "{" +
                    "\"守时\":" + punctuality +
                    ", \"交流\":" + comunication +
                    ", \"真实\":" + sincerity +
                    ", \"尊重\":" + respect +
                    '}';
        }

        @Override
        public String toString() {
            return "Eval{" +
                    "punctuality=" + punctuality +
                    ", comunication=" + comunication +
                    ", sincerity=" + sincerity +
                    ", respect=" + respect +
                    '}';
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_feed_back;
    }
}
