package com.doctor.sun.ui.activity.patient;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityConsultationBinding;
import com.doctor.sun.event.MainTabChangedEvent;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.fragment.ConsultingFragment;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.pager.ConsultingPagerAdapter;
import com.squareup.otto.Subscribe;

/**
 * Created by rick on 11/30/15.
 */
public class PConsultingActivity extends BaseFragmentActivity2 {

    private ActivityConsultationBinding binding;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PConsultingActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_consultation);
        binding.setFooter(getFooter());
        binding.vp.setAdapter(new ConsultingPagerAdapter(getSupportFragmentManager()));
    }


    @NonNull
    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(R.id.tab_two);
    }

    @Subscribe
    public void onMainTabChangedEvent(MainTabChangedEvent e) {
        switch (e.getPosition()) {
            case 0: {
                startActivity(PMainActivity.class);
                break;
            }
            case 1: {
                startActivity(PConsultingActivity.class);
                break;
            }
            case 2: {
                startActivity(PMeActivity.class);
                break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getRealm().addChangeListener(getFooter());
    }

    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if (e.id.equals(ConsultingFragment.TAG)) {
                finish();
            }
        }
    }


    @Override
    public int getMidTitle() {
        return R.string.title_consulting;
    }
}
