package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.AfterServiceContactActivity;
import com.doctor.sun.ui.activity.PageActivity2;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 1/6/2016.
 */
public class PAfterServiceActivity extends PageActivity2 {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, PAfterServiceActivity.class);
        return intent;
    }

    @Override
    protected void initHeader() {
        super.initHeader();
        getBinding().setHeader(getHeaderViewModel());
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.patientOrders("", getCallback().getPage()).enqueue(getCallback());
    }

    @NonNull
    @Override
    public SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_after_service, R.layout.p_item_after_service);
        return adapter;
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        return null;
    }

    public void onMenuClicked() {
        Intent intent = AfterServiceContactActivity.intentFor(this, ContactActivity.PATIENTS_CONTACT);
//        Intent intent = ContactActivity.makeIntent(this, ContactActivity.PATIENTS_CONTACT, R.layout.item_contact3);
        startActivity(intent);
    }

    @NonNull
    @Override
    public String getEmptyIndicatorText() {
        return "没有任何随访请求";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_doctor_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_doctor_list: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_p_follow_up;
    }
}
