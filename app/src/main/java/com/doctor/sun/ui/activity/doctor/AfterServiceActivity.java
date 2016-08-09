package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.AfterServiceContactActivity;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 1/6/2016.
 */
public class AfterServiceActivity extends PageActivity2 {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, AfterServiceActivity.class);
        return intent;
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.doctorOrders("", getCallback().getPage()).enqueue(getCallback());
    }


    public void onMenuClicked() {
        Intent intent = AfterServiceContactActivity.intentFor(this, ContactActivity.DOCTORS_CONTACT);
//        Intent intent = ContactActivity.makeIntent(this, ContactActivity.DOCTORS_CONTACT, R.layout.item_contact2);
        startActivity(intent);
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何随访任务";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_patients: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_follow_up;
    }
}
