package com.doctor.sun.ui.activity.patient;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityConsultationBinding;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.model.PatientFooterView;
import com.doctor.sun.ui.pager.ConsultingPagerAdapter;

/**
 * Created by rick on 11/30/15.
 */
public class ConsultingActivity extends BaseFragmentActivity2 {

    private ActivityConsultationBinding binding;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, ConsultingActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_consultation);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("就诊")
                .setRightTitle("通讯录");
        binding.setHeader(header);
        binding.setFooter(getFooter());
        binding.vp.setAdapter(new ConsultingPagerAdapter(getSupportFragmentManager()));

        binding.pagerTabs.setCustomTabView(R.layout.tab_custom, android.R.id.text1);
        binding.pagerTabs.setDistributeEvenly(true);
        binding.pagerTabs.setSelectedIndicatorColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.pagerTabs.setViewPager(binding.vp);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        //TODO
//        String json = Config.getString(Constants.VOIP_ACCOUNT);
//        VoipAccount account = JacksonUtils.fromJson(json, VoipAccount.class);
//        if (account != null) {
//            Messenger.getInstance().login(account);
//        }
    }

    @NonNull
    private FooterViewModel getFooter() {
        return  FooterViewModel.getInstance(new PatientFooterView(this), realm, R.id.tab_two);
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        Intent intent = ContactActivity.makeIntent(this, ContactActivity.PATIENTS_CONTACT);
        startActivity(intent);
    }

}
