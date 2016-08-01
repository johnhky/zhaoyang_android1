package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.patient.AllowAfterServiceActivity;
import com.doctor.sun.ui.activity.patient.DoctorDetailActivity;
import com.doctor.sun.ui.activity.patient.HospitalDetailActivity;
import com.doctor.sun.ui.adapter.SearchDoctorAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;

/**
 * Created by lucas on 1/8/16.
 */
public class DoctorHandler {
    private Doctor data;
    private boolean isSelected;

    public DoctorHandler(Doctor doctorDTO) {
        data = doctorDTO;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected() {
        return isSelected;
    }

    public void detail(View view) {
        Intent intent = DoctorDetailActivity.makeIntent(view.getContext(), data, AppointmentType.PREMIUM);
        view.getContext().startActivity(intent);
    }

    @JsonIgnore
    public String getLocate() {
        String locate;
        locate = data.getHospitalName() + "/" + data.getSpecialist() + "/" + data.getTitle();
        return locate;
    }

    @JsonIgnore
    public String getFee(@AppointmentType int type) {
        if (type == AppointmentType.PREMIUM) {
            return getDetailFee();
        } else {
            return getQuickFee();
        }
    }

    @JsonIgnore
    public String getDetailFee() {
        String fee = data.getMoney() + "元/次/15分钟";
        return fee;
    }

    @JsonIgnore
    public String getQuickFee() {
        String fee = data.getSecondMoney() + "元/次";
        return fee;
    }

    @JsonIgnore
    public String getSpecial() {
        String specialist;
        specialist = "专长病种:" + data.getSpecialist();
        return specialist;
    }


    public void itemClick(View v) {
        Intent intent = new Intent();
        intent.putExtra(Constants.DATA, data);
        Activity activity = (Activity) v.getContext();
        Messenger messenger = activity.getIntent().getParcelableExtra(Constants.HANDLER);
        if (messenger != null) {
            try {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.DATA, data);
                message.setData(bundle);
                message.what = Constants.DOCTOR_REQUEST_CODE;
                messenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public void viewDetail(BaseAdapter temp) {
        SearchDoctorAdapter adapter = (SearchDoctorAdapter) temp;
        viewDetail(adapter.getContext(), adapter.getType());
    }

    public void viewDetail(Context context, int type) {
        Intent intent = DoctorDetailActivity.makeIntent(context, data, type);
        context.startActivity(intent);
    }

    public void viewDetailIfIsPatient(Context context) {
        if (!Settings.isDoctor()) {
            Intent intent = DoctorDetailActivity.makeIntent(context, data, AppointmentType.PREMIUM);
            context.startActivity(intent);
        }
    }

//
//    public void pickDate(final Context context, final int type) {
//        SelectRecordDialog.showRecordDialog(context, new SelectRecordDialog.SelectRecordListener() {
//            @Override
//            public void onSelectRecord(SelectRecordDialog dialog, MedicalRecord record) {
//                data.setRecordId(String.valueOf(record.getMedicalRecordId()));
//                Intent intent = PickDateActivity.makeIntent(context, data, type);
//                context.startActivity(intent);
//                dialog.dismiss();
//            }
//        });
//    }

    private int getType(BaseAdapter temp) {
        SearchDoctorAdapter adapter = (SearchDoctorAdapter) temp;
        return adapter.getType();
    }


    @JsonIgnore
    public boolean getDetailVisible() {
        return !data.getDetail().equals("") && data.getDetail() != null;
    }

    public int getDefaultAvatar() {
        int result;
        if (data.getGender() == 0) {
            result = R.drawable.female_doctor_avatar;
        } else {
            result = R.drawable.male_doctor_avatar;
        }
        return result;
    }

//    public int money() {
//        switch (data.getType()) {
//            case AppointmentType.STANDARD:
//                return data.getSecondMoney();
//            case AppointmentType.PREMIUM:
//                int scalar = Integer.parseInt(data.getDuration()) / 15;
//                return data.getMoney() * scalar;
//            default:
//                return 0;
//        }
//    }

    public void toggleFav(final Context context, final Doctor doctor) {
        ToolModule api = Api.of(ToolModule.class);
        if ("1".equals(doctor.getIsFav())) {
            api.unlikeDoctor(doctor.getId()).enqueue(new SimpleCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    //改状态,不然下次点击还是会跑到这里,但是医生已经取消收藏了.
                    doctor.setIsFav("0");
                    doctor.notifyChange();
                    Toast.makeText(context, "取消收藏医生", Toast.LENGTH_SHORT).show();
                }

            });
        } else {
            api.likeDoctor(doctor.getId()).enqueue(new SimpleCallback<String>() {
                @Override
                protected void handleResponse(String response) {
                    //改状态,不然下次点击还是会跑到这里,但是医生已经收藏过了.
                    doctor.setIsFav("1");
                    doctor.notifyChange();
                    Toast.makeText(context, "成功收藏医生", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void hospital(Context context, Doctor data) {
        Intent intent = HospitalDetailActivity.makeIntent(context, data);
        context.startActivity(intent);
    }

    public View.OnClickListener allowAfterService(final BaseAdapter adapter, BaseViewHolder vh) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AllowAfterServiceActivity.intentFor(adapter.getContext(), data);
                adapter.getContext().startActivity(intent);
            }
        };
    }


    public boolean canWritePrescription() {
        return "执业医师认证".equals(data.getLevel());
    }

    public HashMap<String, String> toHashMap() {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("name", data.getName() == null ? "" : data.getName());
        result.put("email", data.getEmail() == null ? "" : data.getEmail());
        result.put("gender", String.valueOf(data.getGender()));
        result.put("avatar", data.getAvatar() == null ? "" : data.getAvatar());
        result.put("specialist", data.getSpecialist() == null ? "" : data.getSpecialist());
        result.put("title", data.getTitle() == null ? "" : data.getTitle());
        result.put("titleImg", data.getTitleImg() == null ? "" : data.getTitleImg());
        result.put("practitionerImg", data.getPractitionerImg() == null ? "" : data.getPractitionerImg());
        result.put("certifiedImg", data.getCertifiedImg() == null ? "" : data.getCertifiedImg());
        result.put("hospitalPhone", data.getHospitalPhone() == null ? "" : data.getHospitalPhone());
        result.put("detail", data.getDetail() == null ? "" : data.getDetail());
        result.put("hospital", data.getHospitalName() == null ? "" : data.getHospitalName());
        return result;
    }
}
