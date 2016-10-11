package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityDoctorInfoBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.entity.ContactDetail;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.doctor.ChattingRecordActivity;
import com.doctor.sun.ui.activity.doctor.HistoryRecordActivity;
import com.doctor.sun.ui.activity.doctor.ModifyNicknameActivity;
import com.doctor.sun.ui.widget.CancelHistoryDialog;
import com.doctor.sun.ui.widget.SelectDialog;
import com.doctor.sun.ui.widget.SelectRecordDialog;
import com.kyleduo.switchbutton.SwitchButton;

import java.util.HashMap;
import java.util.List;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit2.Call;

import static com.doctor.sun.bean.Constants.PARAM_NICKNAME;

/**
 * 个人信息
 * <p/>
 * Created by Lynn on 12/29/15.
 */
public class DoctorInfoActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(DoctorInfoActivity.class);
    private ActivityDoctorInfoBinding binding;
    private ImModule api = Api.of(ImModule.class);
    private Contact contact;
    private Appointment appointment;

    public static Intent makeIntent(Context context, Contact data) {
        Intent i = new Intent(context, DoctorInfoActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }


    private Contact getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_doctor_info);
        binding.blackList.setData("加入黑名单");
        binding.allowCall.setData("允许对方打电话");

        initListener();
        initData();
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.tv_check_history:
                startActivity(new Intent(DoctorInfoActivity.this, ChattingRecordActivity.class)
                        .putExtra(Constants.PARAM_APPOINTMENT, appointment));
                break;
            case R.id.tv_cancel_history:
                if (appointment != null) {
                    Call<ApiDTO<List<MedicalRecord>>> apiDTOCall = Api.of(ImModule.class).recordDoctor(Integer.parseInt(binding.getData().getId()));
                    SelectRecordDialog.showRecordDialog(v.getContext(), new SelectRecordDialog.SelectRecordListener() {
                        @Override
                        public void onSelectRecord(SelectRecordDialog dialog, MedicalRecord record) {
                            CancelHistoryDialog.showCancelHistoryDialog(DoctorInfoActivity.this, String.valueOf(record.getTid()));
                            dialog.dismiss();
                        }
                    }, apiDTOCall);
                }
                break;
            case R.id.rl_history_record:
                Intent intent = HistoryRecordActivity.intentFor(DoctorInfoActivity.this, binding.getData().getRecordId());
                startActivity(intent);
                break;
            case R.id.rl_modify_nickname:
                startActivityForResult(new Intent(DoctorInfoActivity.this, ModifyNicknameActivity.class)
                        .putExtra(Constants.PARAM_NICKNAME, contact.getNickname())
                        .putExtra(Constants.PARAM_DOCTOR_ID, contact.getDoctorId()), Constants.NICKNAME_REQUEST_CODE);
                break;
            case R.id.fl_send:
                if (appointment != null) {
                    SelectDialog.showSelectDialog(this, appointment);
                }
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.NICKNAME_REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_OK:
                    binding.tvModify.setText(data.getStringExtra(PARAM_NICKNAME));
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void initListener() {
        binding.tvCheckHistory.setOnClickListener(this);
        binding.rlModifyNickname.setOnClickListener(this);
        binding.tvCancelHistory.setOnClickListener(this);
        binding.flSend.setOnClickListener(this);
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
                        Call<ApiDTO<HashMap<String, String>>> call = api.doctorBan(contact.getDoctorId() + "", checked + "");
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
                Call<ApiDTO<HashMap<String, String>>> call = api.doctorCall(contact.getDoctorId() + "", checked + "");
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
        contact = getData();
        getContactDetail();
    }

    private void getContactDetail() {
        api.doctorContact(contact.getDoctorId()).enqueue(new ApiCallback<ContactDetail>() {
            @Override
            protected void handleResponse(ContactDetail response) {
                binding.setData(response);
                bindAppointment();
            }
        });
    }

    private void bindAppointment() {
        ToolModule tool = Api.of(ToolModule.class);
        tool.doctorInfo(contact.getDoctorId()).enqueue(new SimpleCallback<Doctor>() {
            @Override
            protected void handleResponse(Doctor response) {
                appointment = new Appointment();
                appointment.setVoipAccount(binding.getData().getVoipAccount());
                appointment.setPatientName(contact.getName());
                appointment.setAvatar(binding.getData().getAvatar());
                response.setVoipAccount(binding.getData().getVoipAccount());
                appointment.setYunxinAccid(response.getYunxinAccid());
                appointment.setDoctor(response);
            }
        });

    }

    public int getMidTitle() {
        return R.string.title_profile_info;
    }
}
