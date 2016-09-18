package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;

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
