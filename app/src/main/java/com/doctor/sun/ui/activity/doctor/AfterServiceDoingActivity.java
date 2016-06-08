package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.DoctorAfterServicePA;
import com.doctor.sun.ui.pager.PatientAfterServicePA;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceDoingActivity extends TabActivity {
    private PatientAfterServicePA patientAfterServicePA;
    private DoctorAfterServicePA doctorAfterServicePA;

    public static Intent intentFor(Context context, String id) {
        Intent intent = new Intent(context, AfterServiceDoingActivity.class);
        intent.putExtra(Constants.DATA, id);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        if (AppContext.isDoctor()) {
            doctorAfterServicePA = new DoctorAfterServicePA(getSupportFragmentManager(), getData());
            return doctorAfterServicePA;
        } else {
            patientAfterServicePA = new PatientAfterServicePA(getSupportFragmentManager(), getData());
            return patientAfterServicePA;
        }
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel header = new HeaderViewModel(this);
        header.setRightTitle("保存");
        return header;
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        if (AppContext.isDoctor()) {
            doctorAfterServicePA.saveAnswer();
        } else {
            patientAfterServicePA.saveAnswer();
        }
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
        if (AppContext.isDoctor()) {
            doctorAfterServicePA.handleImageResult(requestCode, resultCode, data);
        } else {
            patientAfterServicePA.handleImageResult(requestCode, resultCode, data);
        }
    }
}
