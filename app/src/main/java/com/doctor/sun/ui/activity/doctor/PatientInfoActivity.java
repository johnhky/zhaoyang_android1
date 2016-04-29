package com.doctor.sun.ui.activity.doctor;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPatientInfoBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.ContactDetail;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.CancelHistoryDialog;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.HashMap;

import io.ganguo.library.common.ToastHelper;
import io.ganguo.library.core.image.GGlide;
import retrofit.Call;

/**
 * 个人信息
 * Created by Lynn on 12/29/15.
 */
public class PatientInfoActivity extends BaseActivity2 implements View.OnClickListener {
    private ActivityPatientInfoBinding binding;
    private ImModule api = Api.of(ImModule.class);
    private MedicalRecord patient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_patient_info);
        binding.setHeader(getHeaderViewModel());
        binding.blackList.setData("加入黑名单");
        binding.allowCall.setData("允许对方打电话");

        initListener();
        initData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_history:
                startActivity(new Intent(PatientInfoActivity.this, ChattingRecordActivity.class)
                        .putExtra(Constants.PARAM_APPOINTMENT, binding.getAppointment()));
                break;
            case R.id.tv_cancel_history:
                CancelHistoryDialog.showCancelHistoryDialog(PatientInfoActivity.this, binding.getData().getVoipAccount());
                break;
            case R.id.rl_history_record:
                startActivity(new Intent(PatientInfoActivity.this, HistoryRecordActivity.class)
                        .putExtra(Constants.PARAM_RECORD_ID, binding.getData().getRecordId()));
                break;
            case R.id.rl_modify_nickname:
                startActivityForResult(new Intent(PatientInfoActivity.this, ModifyNicknameActivity.class)
                        .putExtra(Constants.PARAM_NICKNAME, binding.getData().getNickname())
                        .putExtra(Constants.PARAM_PATIENT_ID, binding.getData().getId()), Constants.NICKNAME_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.NICKNAME_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    binding.tvModify.setText(data.getStringExtra(Constants.PARAM_NICKNAME));
                    ToastHelper.showMessageMiddle(this, "修改成功");
                    break;
            }
        }
    }

    private void initListener() {
        binding.rlHistoryRecord.setOnClickListener(this);
        binding.tvCheckHistory.setOnClickListener(this);
        binding.rlModifyNickname.setOnClickListener(this);
        binding.tvCancelHistory.setOnClickListener(this);
        ((SwitchButton) binding.blackList.getRoot().findViewById(R.id.switch_button)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int checked;
                        if (isChecked) {
                            checked = 1;
                        } else {
                            checked = 0;
                        }
                        Call<ApiDTO<HashMap<String, String>>> call = api.patientBan(patient.getPatientId() + "", checked + "");
                        call.enqueue(new ApiCallback<HashMap<String, String>>() {
                            @Override
                            protected void handleResponse(HashMap<String, String> response) {
//                                ToastHelper.showMessage(getBaseContext(), "修改成功");
                            }
                        });
                    }
                });
        ((SwitchButton) binding.allowCall.getRoot().findViewById(R.id.switch_button)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int checked;
                if (isChecked) {
                    checked = 1;
                } else {
                    checked = 0;
                }
                Call<ApiDTO<HashMap<String, String>>> call = api.patientCall(patient.getPatientId() + "", checked + "");
                call.enqueue(new ApiCallback<HashMap<String, String>>() {
                    @Override
                    protected void handleResponse(HashMap<String, String> response) {
//                        ToastHelper.showMessage(getBaseContext(), "修改成功");
                    }
                });
            }
        });
    }

    private void initData() {
        patient = getIntent().getParcelableExtra(Constants.PARAM_PATIENT);
        binding.setPatient(patient);
        getContactDetail();
    }

    private void getContactDetail() {
        api.patientContact(patient.getPatientId(),
                patient.getMedicalRecordId()).enqueue(new ApiCallback<ContactDetail>() {
            @Override
            protected void handleResponse(ContactDetail response) {
                binding.setData(response);

                GGlide.getGlide().with(PatientInfoActivity.this)
                        .load(response.getAvatar())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(getResources().getDrawable(R.drawable.default_avatar))
                        .into(new BitmapImageViewTarget(binding.ivPerson) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmap =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmap.setCircular(true);
                                binding.ivPerson.setImageDrawable(circularBitmap);
                            }
                        });

                bindAppointment();
            }
        });
    }

    private void bindAppointment() {
        Appointment appointment = new Appointment();
        //appointmentId 至少有一个
        appointment.setId(patient.getAppointmentId().get(patient.getAppointmentId().size() - 1));
        appointment.setVoipAccount(binding.getData().getVoipAccount());
        appointment.setPatientName(patient.getPatientName());
        appointment.setAvatar(binding.getData().getAvatar());
        appointment.setDoctor(TokenCallback.getDoctorProfile());
        appointment.setTid(patient.getTid());
        appointment.setYunxinAccid(patient.getYunxinAccid());
        binding.setAppointment(appointment);
    }

    @Nullable
    private HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this).setMidTitle("个人信息");
    }

}
