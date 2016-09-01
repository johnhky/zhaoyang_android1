package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.ui.activity.AfterServiceContactActivity;
import com.doctor.sun.ui.activity.TabActivity;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.pager.PatientRelationshipListPA;

/**
 * Created by rick on 1/6/2016.
 */
public class PAfterServiceActivity extends TabActivity {

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, PAfterServiceActivity.class);
        return intent;
    }


    public void onMenuClicked() {
        Intent intent = AfterServiceContactActivity.intentFor(this, ContactActivity.PATIENTS_CONTACT);
//        Intent intent = ContactActivity.intentFor(this, ContactActivity.PATIENTS_CONTACT, R.layout.item_contact3);
        startActivity(intent);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_doctor_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_doctor_list: {
//                onMenuClicked();
//                return true;
//            }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public int getMidTitle() {
        return R.string.title_p_follow_up;
    }

    @Override
    protected PagerAdapter createPagerAdapter() {
        return new PatientRelationshipListPA(getSupportFragmentManager());
    }
}
