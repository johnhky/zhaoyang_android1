package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.DoctorAfterServicePA;
import com.doctor.sun.ui.pager.PatientAfterServicePA;

import java.util.List;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceDoingActivity extends TabActivity implements Prescription.UrlToLoad {
    private PatientAfterServicePA patientAfterServicePA;
    private DoctorAfterServicePA doctorAfterServicePA;

    public static Intent intentFor(Context context, String id, String recordId) {
        Intent intent = new Intent(context, AfterServiceDoingActivity.class);
        intent.putExtra(Constants.DATA, id);
        intent.putExtra(Constants.PARAM_RECORD_ID, recordId);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    public String getRecordId() {
        return getStringExtra(Constants.PARAM_RECORD_ID);
    }

    @Override
    protected void initPagerTabs() {
        super.initPagerTabs();
        getBinding().vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String title = "";
                if (AppContext.isDoctor()) {
                    if (position == 1) {
                        title = "保存";
                    }
                } else {
                    if (position == 0) {
                        title = "保存";
                    }
                }
                getBinding().getHeader().setRightTitle(title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        if (!AppContext.isDoctor()) {
            header.setRightTitle("保存");
        }
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

    @Override
    public String url() {
        return "drug/record-last?recordId=" + getRecordId();
    }
}
