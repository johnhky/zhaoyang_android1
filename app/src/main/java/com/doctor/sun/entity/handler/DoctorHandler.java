package com.doctor.sun.entity.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.DialogPickDurationBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.ui.activity.doctor.ApplyAfterServiceActivity;
import com.doctor.sun.ui.activity.patient.AllowAfterServiceActivity;
import com.doctor.sun.ui.activity.patient.DoctorDetailActivity;
import com.doctor.sun.ui.activity.patient.PickDateActivity;
import com.doctor.sun.ui.adapter.SearchDoctorAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.BottomDialog;
import com.doctor.sun.ui.widget.SelectRecordDialog;
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

    public OnItemClickListener select() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                setSelected(!isSelected());
                view.setSelected(isSelected());
            }
        };
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected() {
        return isSelected;
    }

    public void detail(View view) {
        Intent intent = DoctorDetailActivity.makeIntent(view.getContext(), data, AppointmentType.DETAIL);
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
        if (type == AppointmentType.DETAIL) {
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
        String fee = data.getSecondMoney() + "元/次/15分钟";
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

    public void viewDetail(View view, int type) {
        Intent intent = DoctorDetailActivity.makeIntent(view.getContext(), data, type);
        view.getContext().startActivity(intent);
    }

    public void viewDetailIfIsPatient(Context context) {
        if (!AppContext.isDoctor()) {
            Intent intent = DoctorDetailActivity.makeIntent(context, data, AppointmentType.DETAIL);
            context.startActivity(intent);
        }
    }

    public OnItemClickListener viewDetail() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter temp, View view, BaseViewHolder vh) {
                SearchDoctorAdapter adapter = (SearchDoctorAdapter) temp;
                viewDetail(view, adapter.getType());
            }
        };
    }


    public OnItemClickListener pickDate() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter temp, final View v, BaseViewHolder vh) {

                SelectRecordDialog.showRecordDialog(v.getContext(), new SelectRecordDialog.SelectRecordListener() {
                    @Override
                    public void onSelectRecord(SelectRecordDialog dialog, MedicalRecord record) {
                        SearchDoctorAdapter adapter = (SearchDoctorAdapter) temp;
                        data.setRecordId(String.valueOf(record.getMedicalRecordId()));
                        Intent intent = PickDateActivity.makeIntent(v.getContext(), data, adapter.getType());
                        v.getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        };
    }

    public void pickDuration(final View root) {
        LayoutInflater inflater = LayoutInflater.from(root.getContext());
        final DialogPickDurationBinding binding = DialogPickDurationBinding.inflate(inflater, null, false);
        binding.setMoney(0);
        binding.rgDuration.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedItem = -1;
                for (int i = 0; i < binding.rgDuration.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) binding.rgDuration.getChildAt(i);
                    if (childAt.isChecked()) {
                        selectedItem = i;
                    }
                }
                binding.setMoney(data.getMoney() * (selectedItem + 1));
            }
        });
        binding.tvPickDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedItem = -1;
                for (int i = 0; i < binding.rgDuration.getChildCount(); i++) {
                    RadioButton childAt = (RadioButton) binding.rgDuration.getChildAt(i);
                    if (childAt.isChecked()) {
                        selectedItem = i;
                    }
                }

                if (selectedItem != -1) {
                    data.setDuration(String.valueOf((selectedItem + 1) * 15));
//                    pickDate(root);
                } else {
                    Toast.makeText(root.getContext(), "请选择时长", Toast.LENGTH_SHORT).show();
                }
            }
        });
        BottomDialog.showDialog((Activity) root.getContext(), binding.getRoot());
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

    public int money() {
        switch (data.getType()) {
            case AppointmentType.QUICK:
                return data.getSecondMoney();
            case AppointmentType.DETAIL:
                int scalar = Integer.parseInt(data.getDuration()) / 15;
                return data.getMoney() * scalar;
            default:
                return 0;
        }
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
        return data.getLevel().equals("执业医师认证");
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
