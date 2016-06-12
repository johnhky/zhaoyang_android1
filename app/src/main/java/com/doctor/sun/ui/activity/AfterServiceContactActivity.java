package com.doctor.sun.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.doctor.ContactActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreAdapter;
import com.doctor.sun.util.NameComparator;

import java.util.Collections;
import java.util.List;

/**
 * Created by rick on 7/6/2016.
 */
public class AfterServiceContactActivity extends ContactActivity {
    private AfterServiceModule api = Api.of(AfterServiceModule.class);

    public static Intent intentFor(Context context, int code) {
        Intent i = new Intent(context, AfterServiceContactActivity.class);
        i.putExtra(Constants.REQUEST_CODE, code);
        return i;
    }

    @NonNull
    @Override
    protected SimpleAdapter createAdapter() {
        SimpleAdapter adapter = super.createAdapter();
        adapter.mapLayout(R.layout.item_doctor,R.layout.item_contact2);
        return adapter;
    }

    @Override
    protected void getPContactList() {
//        int id = TokenCallback.getPatientProfile().getId();
        int id = 57;
        api.doctorList(id).enqueue(new ApiCallback<List<Doctor>>() {
            @Override
            protected void handleResponse(List<Doctor> response) {
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
//        int id = TokenCallback.getDoctorProfile().getId();
        int id = 2;
        api.patientList(id).enqueue(new ApiCallback<List<Patient>>() {
            @Override
            protected void handleResponse(List<Patient> response) {
                getAdapter().clear();
                getAdapter().addAll(response);
                Collections.sort(getAdapter(), new NameComparator());
                getContactAdapter().updatePosition();
                getAdapter().notifyDataSetChanged();
            }
        });
    }
}
