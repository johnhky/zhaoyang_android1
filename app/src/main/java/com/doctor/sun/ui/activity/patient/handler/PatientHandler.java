package com.doctor.sun.ui.activity.patient.handler;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.constans.RelationshipStatus;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AfterServiceModule;
import com.doctor.sun.ui.activity.doctor.ApplyAfterServiceActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.vm.ItemPickTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonthOfYear = monthOfYear + 1;
                    mDayOfMonth = dayOfMonth;
                    tvBirthday.setText(String.format(Locale.CHINA, "%04d-%02d", mYear, mMonthOfYear));
                }
            };
            DatePickerDialog dialog = new DatePickerDialog(view.getContext(), onDateSetListener, mYear, mMonthOfYear, mDayOfMonth);
            final DatePicker datePicker = dialog.getDatePicker();
            dialog.show();
            view.post(new Runnable() {
                @Override
                public void run() {
                    datePicker.setMinDate(System.currentTimeMillis() - ONE_HUNDRED_YEAR);
                    datePicker.setMaxDate(System.currentTimeMillis());
                }
            });
        }

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

    @JsonIgnore
    public String getRecordsLabel(Patient patient) {
        StringBuilder result = new StringBuilder();
        result.append("病历:  ");
        List<String> recordNames = patient.recordNames;
        if (recordNames != null && !recordNames.isEmpty()) {
            for (int i = 0; i < recordNames.size(); i++) {
                result.append(recordNames.get(i));
                if (i != recordNames.size() - 1) {
                    result.append("/");
                }
            }
        }
        return result.toString();
    }

    public boolean isUnApplied(String status) {
        return status != null && status.equals(RelationshipStatus.UNAPPLY);
    }

    public boolean isApplied(String status) {
        return status == null || status.equals(RelationshipStatus.APPLIED);
    }


    public View.OnClickListener applyAfterService(final BaseListAdapter adapter, BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ApplyAfterServiceActivity.intentFor(v.getContext(), data);
                v.getContext().startActivity(intent);
            }
        };
    }

    public void applyBuildRelation() {
        AfterServiceModule module = Api.of(AfterServiceModule.class);
        module.applyBuildRelation(data.getId()).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                Toast.makeText(AppContext.me(), "已向患者发送随访关系的建立申请\n请静候患者处理", Toast.LENGTH_SHORT).show();
                data.status = "applying";
                data.notifyChange();
            }
        });
    }

    public interface IEditPatient {
        boolean getIsEditMode();
    }
}
