package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.DoctorAfterServicePA;
import com.doctor.sun.ui.pager.PatientAfterServicePA;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceDoingActivity extends TabActivity implements Prescription.UrlToLoad {
    private PatientAfterServicePA patientAfterServicePA;
    private DoctorAfterServicePA doctorAfterServicePA;

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
            doctorAfterServicePA = new DoctorAfterServicePA(getSupportFragmentManager(), getData());
            return doctorAfterServicePA;
        } else {
            patientAfterServicePA = new PatientAfterServicePA(getSupportFragmentManager(), getData());
            return patientAfterServicePA;
        }
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        return null;
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Settings.isDoctor()) {
            doctorAfterServicePA.handleImageResult(requestCode, resultCode, data);
        } else {
            patientAfterServicePA.handleImageResult(requestCode, resultCode, data);
        }
    }

    @Override
    public String url() {
        return "drug/record-last?recordId=" + getRecordId();
    }
}
