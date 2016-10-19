package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.pager.DoctorAfterServicePA;
import com.doctor.sun.ui.pager.PatientAfterServicePA;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceDoingActivity extends TabActivity implements Prescription.UrlToLoad {

    public static Intent intentFor(Context context, String id, String recordId, int position) {
        Intent intent = new Intent(context, AfterServiceDoingActivity.class);
        intent.putExtra(Constants.DATA, id);
        intent.putExtra(Constants.PARAM_RECORD_ID, recordId);
        intent.putExtra(Constants.POSITION, position);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    public String getRecordId() {
        return getStringExtra(Constants.PARAM_RECORD_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Settings.isDoctor()) {
            View historyButton = LayoutInflater.from(this).inflate(R.layout.item_history_button, binding.flContainer, false);
            historyButton.findViewById(R.id.btn_appointment_history).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: 替换为历史记录入口
                    Toast.makeText(AfterServiceDoingActivity.this, "Test", Toast.LENGTH_SHORT).show();
                }
            });
            binding.flContainer.addView(historyButton);
        }
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        if (Settings.isDoctor()) {
            return new DoctorAfterServicePA(getSupportFragmentManager(), getData());
        } else {
            return new PatientAfterServicePA(getSupportFragmentManager(), getData());
        }
    }

    @Override
    public String url() {
        return "drug/record-last?recordId=" + getRecordId();
    }
}
