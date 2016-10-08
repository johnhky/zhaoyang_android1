package com.doctor.sun.ui.activity.doctor;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.databinding.ActivityConsultationBinding;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.model.DoctorFooterView;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.pager.ConsultingPagerAdapter;

/**
 * Created by rick on 11/30/15.
 */
public class ConsultingActivity extends BaseFragmentActivity2 {

    public static final int CODE = -1;
    private ActivityConsultationBinding binding;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, ConsultingActivity.class);
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
        return FooterViewModel.getInstance(new DoctorFooterView(this), R.id.tab_two);
    }

//    通讯录入口 1.3版本移除
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
//                Intent intent = ContactActivity.makeIntent(ConsultingActivity.this, ContactActivity.DOCTORS_CONTACT);
//                startActivity(intent);
//                return true;
//            }
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//

    @Override
    public void onResume() {
        super.onResume();
        getRealm().addChangeListener(getFooter());
    }

    @Override
    public int getMidTitle() {
        return R.string.title_consulting;
    }
}
