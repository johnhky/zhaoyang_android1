package com.doctor.sun.ui.activity.patient.handler;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.ui.widget.PickImageDialog;

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
            PickImageDialog.chooseImage((Activity) view.getContext(), CODE_IMAGE_REQUEST);
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

    public interface IEditPatient {
        boolean getIsEditMode();
    }
}
