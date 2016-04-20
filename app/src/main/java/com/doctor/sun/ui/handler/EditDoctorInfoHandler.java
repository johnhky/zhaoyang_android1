package com.doctor.sun.ui.handler;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.doctor.MainActivity;

/**
 * Created by rick on 11/18/15.
 */
public class EditDoctorInfoHandler extends BaseHandler {
    private Doctor data;

    private ProfileModule api = Api.of(ProfileModule.class);

    private DoctorInfoInput mInput;

    private int selectedTitle = -1;
    private String[] titles = new String[]{"主任医师", "副主任医师", "主治医师", "心理咨询师二级", "心理咨询师三级", "医师", "心理治疗师"};


    public EditDoctorInfoHandler(Activity context, Doctor doctor) {
        super(context);
        try {
            mInput = (DoctorInfoInput) context;
            data = doctor;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("The host Activity must implement PatientInfoInput");
        }
    }

    public void done(final View view) {
        Doctor doctorInfo = mInput.getDoctorInfo();
        if (doctorInfo != null) {
            if (data != null) {
                TextView et_title = (TextView) view.findViewById(R.id.et_title);
                switch (et_title.getText().toString()) {
                    case "主任医师":
                        selectedTitle = 0;
                        break;
                    case "副主任医师":
                        selectedTitle = 1;
                        break;
                    case "主治医师":
                        selectedTitle = 2;
                        break;
                    case "心理咨询师二级":
                        selectedTitle = 3;
                        break;
                    case "心理咨询师三级":
                        selectedTitle = 4;
                        break;
                    case "医师":
                        selectedTitle = 5;
                        break;
                    case "心理治疗师":
                        selectedTitle = 6;
                        break;
                }
            }

            if (selectedTitle == -1) {
                Toast.makeText(view.getContext(), "职称不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            doctorInfo.setTitle(String.valueOf(selectedTitle + 1));
            api.editDoctorProfile(doctorInfo.getHandler().toHashMap()).enqueue(new ApiCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    TokenCallback.checkToken(getContext());
                    Toast.makeText(view.getContext(), "保存成功,请耐心等待资料审核", Toast.LENGTH_SHORT).show();
                    Intent intent = MainActivity.makeIntent(view.getContext());
                    view.getContext().startActivity(intent);
                }

                @Override
                protected void handleApi(ApiDTO<String> body) {
                    super.handleApi(body);
                    TokenCallback.checkToken(getContext());
                    Toast.makeText(view.getContext(), "保存成功,请耐心等待资料审核", Toast.LENGTH_SHORT).show();
                    Intent intent = MainActivity.makeIntent(view.getContext());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    public void pickImage(View view) {
        mInput.pickImage(view.getId());
    }

    public void selectTitle(final View view) {
        new MaterialDialog.Builder(view.getContext())
                .title("职称")
                .items(titles)
                .itemsCallbackSingleChoice(selectedTitle, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View item, int which, CharSequence text) {
                        /**
                         * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                         * returning false here won't allow the newly selected radio button to actually be selected.
                         **/

                        selectedTitle = which;
                        TextView others = (TextView) view.findViewById(R.id.et_title);
                        others.setText(text);
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }

    public interface DoctorInfoInput {
        Doctor getDoctorInfo();

        void pickImage(int id);
    }

    public Doctor getData() {
        return data;
    }

    public void setData(Doctor data) {
        this.data = data;
    }
}
