package com.doctor.sun.ui.activity.patient;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityConsultationBinding;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.fragment.ConsultingFragment2;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.model.PatientFooterView;
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
//
//        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
//        binding.pagerTabs.setDistributeEvenly(true);
//        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
//        binding.pagerTabs.setViewPager(binding.vp);
    }


    @NonNull
    private FooterViewModel getFooter() {
        return FooterViewModel.getInstance(new PatientFooterView(this), R.id.tab_two);
    }


    @Override
    public void onResume() {
        super.onResume();
        getRealm().addChangeListener(getFooter());
    }

    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if (e.id.equals(ConsultingFragment2.TAG)) {
                finish();
            }
        }
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_consulting, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_contact: {
//                onMenuClicked();
//                return true;
//            }
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//    public void onMenuClicked() {
//        Intent intent = ContactActivity.makeIntent(this, ContactActivity.PATIENTS_CONTACT);
//        startActivity(intent);
//    }

    @Override
    public int getMidTitle() {
        return R.string.title_consulting;
    }
}
