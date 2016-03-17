package com.doctor.sun.ui.activity.patient.handler;

import android.app.Activity;
import android.view.View;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.patient.AddMedicalRecordActivity;
import com.doctor.sun.ui.handler.BaseHandler;

import java.util.HashMap;

/**
 * Created by rick on 4/1/2016.
 */
public class AddMedicalRecordHandler extends BaseHandler {
    private ProfileModule api = Api.of(ProfileModule.class);

    private int type;
    private MedicalRecordInput mInput;


    public AddMedicalRecordHandler(Activity context, int type) {
        super(context);
        this.type = type;
        try {
            mInput = (MedicalRecordInput) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The host Activity must implement PatientInfoInput");
        }

    }

    public void done(View view) {
        HashMap<String, String> param = mInput.getParam();
        if (param != null) {
            switch (type) {
                case AddMedicalRecordActivity.TYPE_OTHERS: {
                    api.setRelativeMedicalRecord(param).enqueue(new ApiCallback<String>() {
                        @Override
                        protected void handleResponse(String response) {
                        }

                        @Override
                        protected void handleApi(ApiDTO<String> body) {
                            if (body.getStatus().equals("200")) {
                                mInput.onRelativeRecordAdded();
                            }
                            super.handleApi(body);
                        }
                    });
                    break;
                }
                case AddMedicalRecordActivity.TYPE_SELF: {
                    api.setSelfMedicalRecord(param).enqueue(new ApiCallback<String>() {
                        @Override
                        protected void handleResponse(String response) {
                        }

                        @Override
                        protected void handleApi(ApiDTO<String> body) {
                            if(body.getStatus().equals("200")) {
                                mInput.onSelfRecordAdded();
                            }
                        }
                    });
                }
            }
        }
    }


    public interface MedicalRecordInput {
        HashMap<String, String> getParam();

        void onSelfRecordAdded();

        void onRelativeRecordAdded();
    }
}