package com.doctor.sun.ui.activity.patient.handler;

import android.app.DatePickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.doctor.sun.R;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.vo.ItemPickTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by lucas on 1/5/16.
 */
public class PatientHandler {
    private final static int CODE_IMAGE_REQUEST = 8;
    public static final long ONE_HUNDRED_YEAR = 3153600000000L;
    private Patient data;
    private final GregorianCalendar calendar = new GregorianCalendar();
    private int mYear;
    private int mMonthOfYear;
    private int mDayOfMonth;

    private ItemPickTime item;

    public PatientHandler(Patient patient) {
        data = patient;
        item = new ItemPickTime(0, "");
        mYear = calendar.get(Calendar.YEAR) - 18;
        mMonthOfYear = calendar.get(Calendar.MONTH);
        mDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
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
            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonthOfYear = monthOfYear + 1;
                    mDayOfMonth = dayOfMonth;
                    Log.e("TAG", "onDateSet: " + mYear + " " + mMonthOfYear + " " + mDayOfMonth);
                    tvBirthday.setText(String.format(Locale.CHINA, "%04d-%02d", mYear, mMonthOfYear));
                }
            };
            final DatePickerDialog dialog = new DatePickerDialog(view.getContext(), onDateSetListener, mYear, mMonthOfYear, mDayOfMonth);
            dialog.show();
            view.post(new Runnable() {
                @Override
                public void run() {
                    DatePicker datePicker = dialog.getDatePicker();
                    datePicker.setMinDate(System.currentTimeMillis() - ONE_HUNDRED_YEAR);
                    datePicker.setMaxDate(System.currentTimeMillis());
                }
            });
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
