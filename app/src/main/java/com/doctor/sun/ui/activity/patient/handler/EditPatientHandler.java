package com.doctor.sun.ui.activity.patient.handler;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by lucas on 1/5/16.
 */
public class EditPatientHandler {
    private final static int CODE_IMAGE_REQUEST = 8;
    private Patient data;
    private int myear;
    private int mmothOfYear;
    private int mdayOfMonth;

    public EditPatientHandler(Patient patient) {
        data = patient;
    }

    public void setAvatar(final View view) {
        IEditPatient isEditMode = (IEditPatient) view.getContext();
        boolean editStatus = isEditMode.getIsEditMode();
        if (editStatus) {
            PickImageDialog pickImageDialog = new PickImageDialog(view.getContext(), CODE_IMAGE_REQUEST);
            pickImageDialog.show();
//            PickImageDialog.((Activity) view.getContext(), CODE_IMAGE_REQUEST);
        }
    }

    public void setBirthday(View view) {
        IEditPatient isEditMode = (IEditPatient) view.getContext();
        boolean editStatus = isEditMode.getIsEditMode();
        if (editStatus) {
            final TextView tvBirthday = (TextView) view.findViewById(R.id.tv_birthday);
            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    myear = year;
                    mmothOfYear = monthOfYear + 1;
                    mdayOfMonth = dayOfMonth;
                    Log.e("TAG", "onDateSet: " + myear + " " + mmothOfYear + " " + mdayOfMonth);
                    tvBirthday.setText(String.format("%04d-%02d", myear, mmothOfYear));
                }
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), onDateSetListener, myear, mmothOfYear, mdayOfMonth);
            datePickerDialog.show();
        }

    }

    public int getDefaultAvatar(Patient patient) {
        int result;
        if (patient.getGender() == 0) {
            result = R.drawable.female_doctor_avatar;
        } else {
            result = R.drawable.male_doctor_avatar;
        }
        return result;
    }

    @JsonIgnore
    public String getGenderResult(Patient patient) {
        String result = "";
        switch (patient.getGender()) {
            case 1:
                result = "男";
                break;
            case 2:
                result = "女";
                break;
        }
        return result;
    }

    public interface IEditPatient {
        boolean getIsEditMode();
    }
}
