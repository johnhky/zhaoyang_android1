package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.util.NameComparator;

import java.util.Collections;
import java.util.List;

/**
 * Created by rick on 7/6/2016.
 */
public class AfterServiceContactActivity extends ContactActivity {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static Intent intentFor(Context context, int code, @LayoutRes int layoutId) {
        Intent i = new Intent(context, AfterServiceContactActivity.class);
        i.putExtra(Constants.REQUEST_CODE, code);
        i.putExtra(Constants.LAYOUT_ID, layoutId);
        return i;
    }

    @Override
    protected void getPContactList() {
        api.doctorList(TokenCallback.getPatientProfile().getId()).enqueue(new ApiCallback<List<Contact>>() {
            @Override
            protected void handleResponse(List<Contact> response) {
                getAdapter().clear();
                getAdapter().addAll(response);
                Collections.sort(getAdapter(), new NameComparator());
                getContactAdapter().updatePosition();
                getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void getContactList() {
        api.patientList(TokenCallback.getDoctorProfile().getId()).enqueue(new ApiCallback<List<Contact>>() {
            @Override
            protected void handleResponse(List<Contact> response) {
                getAdapter().clear();
                getAdapter().addAll(response);
                Collections.sort(getAdapter(), new NameComparator());
                getContactAdapter().updatePosition();
                getAdapter().notifyDataSetChanged();
            }
        });
    }
}
