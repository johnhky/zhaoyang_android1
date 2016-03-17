package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.databinding.DialogRecordTypeBinding;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.ProfileModule;

import java.util.HashMap;

import io.ganguo.library.ui.dialog.BaseDialog;

/**
 * Created by rick on 4/1/2016.
 */
public class EditPatientInfoHandler extends BaseHandler{
    private ProfileModule api = Api.of(ProfileModule.class);

    private PatientInfoInput mInput;


    public EditPatientInfoHandler(Activity context) {
        super(context);
        try {
            mInput = (PatientInfoInput) context;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The host Activity must implement PatientInfoInput");
        }

    }

    public void done(View view) {
        HashMap<String, String> param = mInput.getParam();
        if (param != null) {
            api.editPatientProfile(param).enqueue(new ApiCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    Log.e("TAG", "handleResponse: " + response);
                    TokenCallback.checkToken(getContext());
                    DialogRecordTypeBinding binding = DialogRecordTypeBinding.inflate(LayoutInflater.from(getContext()));
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(binding.getRoot());
                    dialog.show();
                }
            });
        }
    }


    public interface PatientInfoInput {
        HashMap<String, String> getParam();
    }
}
