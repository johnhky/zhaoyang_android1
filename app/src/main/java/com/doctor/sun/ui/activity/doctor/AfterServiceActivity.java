package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

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
    protected void initHeader() {
        super.initHeader();
        getBinding().setHeader(getHeaderViewModel());
    }

    @Override
    protected void loadMore() {
        super.loadMore();
        api.doctorOrders("", getCallback().getPage()).enqueue(getCallback());
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        HeaderViewModel headerViewModel = new HeaderViewModel(this);
        headerViewModel.setMidTitle("随访列表");
        headerViewModel.setRightTitle("我的患者");
        return headerViewModel;
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        Intent intent = AfterServiceContactActivity.intentFor(this, ContactActivity.DOCTORS_CONTACT);
//        Intent intent = ContactActivity.makeIntent(this, ContactActivity.DOCTORS_CONTACT, R.layout.item_contact2);
        startActivity(intent);
    }
}
