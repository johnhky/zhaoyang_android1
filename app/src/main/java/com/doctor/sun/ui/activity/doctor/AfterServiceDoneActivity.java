package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;

import com.doctor.sun.AppContext;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.DoctorAfterServiceDonePA;
import com.doctor.sun.ui.pager.PatientAfterServiceDonePA;

/**
 * Created by rick on 3/6/2016.
 */
public class AfterServiceDoneActivity extends TabActivity {
    public static Intent intentFor(Context context, String id) {
        Intent intent = new Intent(context, AfterServiceDoneActivity.class);
        intent.putExtra(Constants.DATA, id);
        return intent;
    }

    public String getData() {
        return getStringExtra(Constants.DATA);
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        if (AppContext.isDoctor()) {
            return new DoctorAfterServiceDonePA(getSupportFragmentManager(), getData());
        } else {
            return new PatientAfterServiceDonePA(getSupportFragmentManager(), getData());
        }
    }

    @Override
    protected HeaderViewModel createHeaderViewModel() {
        HeaderViewModel header = new HeaderViewModel(this);
        return header;
    }

}
