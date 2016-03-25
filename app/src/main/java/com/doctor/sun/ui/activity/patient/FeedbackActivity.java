package com.doctor.sun.ui.activity.patient;

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
import com.doctor.sun.databinding.PActivityFeedbackBinding;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;


/**
 * Created by rick on 12/17/15.
 */
public class FeedbackActivity extends BaseActivity2 {
    private AppointmentModule api = Api.of(AppointmentModule.class);
    private PActivityFeedbackBinding binding;


    public static Intent makeIntent(Context context, AppointMent data) {
        Intent i = new Intent(context, FeedbackActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private AppointMent getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private Messenger getHandler() {
        return getIntent().getParcelableExtra(Constants.HANDLER);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_feedback);

        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("评价");
        binding.setHeader(header);

        binding.setData(getData());

        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Eval eval = new Eval(binding.ratingBar0.getRating(), binding.ratingBar1.getRating(), binding.ratingBar2.getRating());

                api.evaluateDoctor(String.valueOf(eval.mean()), getData().getId(), eval.getDetail(), binding.comment.etOthers.getText().toString()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        Toast.makeText(FeedbackActivity.this, "成功评价医生", Toast.LENGTH_SHORT).show();
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
}