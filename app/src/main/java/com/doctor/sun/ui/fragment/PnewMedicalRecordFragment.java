package com.doctor.sun.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListPopupWindow;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.PNewMedicalRecordBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.activity.PMainActivity2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by heky on 17/6/28.
 */
public class PnewMedicalRecordFragment extends BaseFragmentActivity2 {
    PNewMedicalRecordBinding binding;
    private String[] relativeList;
    private List<String> items = new ArrayList<>();
    boolean isHas = false;
    private int id = 0;
    ProfileModule api = Api.of(ProfileModule.class);
    Patient patientProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_new_medical_record);
        relativeList = AppContext.me().getResources().getStringArray(R.array.relative_array);
        initView();
        initLisitener();
    }

    private void initView() {
        patientProfile = Settings.getPatientProfile();
        if (!patientProfile.getName().equals("")) {
            isHas = true;
            binding.llYourName.setVisibility(View.GONE);
        }
    }

    private void initLisitener() {
        items = Arrays.asList(relativeList);
        if (binding.radioMan.isChecked() == true) {
            id = 1;
        }
        binding.tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMain();
            }
        });
        binding.tvRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, PnewMedicalRecordFragment.this, items);
            }
        });
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.tvBirthday.setText(year + "-" + (month + 1));
            }
        },2017,8, 1);
        binding.tvBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(binding.tvRelation.getText().toString().trim())) {
                    Toast.makeText(PnewMedicalRecordFragment.this, "请先选择您与患者的关系!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!binding.tvRelation.getText().equals("本人")) {
                    setOtherRecord();
                } else {
                    setSelfRecord();
                }
            }
        });
        binding.radioGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_man:
                        id = 1;
                        break;
                    case R.id.radio_women:
                        id = 2;
                        break;
                }
            }
        });

    }


    public void showPopupWindow(View view, Context context, List<String> options) {
        final List<String> items = options;
        final ListPopupWindow listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items));
        listPopupWindow.setWidth(view.getWidth());
        listPopupWindow.setHeight(450);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.tvRelation.setText(items.get(position));
                if (!TextUtils.isEmpty(binding.tvRelation.getText().toString())) {
                    if (binding.tvRelation.getText().toString().equals("本人")) {
                        binding.etYourName.setText("");
                        binding.llYourName.setVisibility(View.GONE);
                    } else {
                        binding.llYourName.setVisibility(View.VISIBLE);
                    }
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setAnchorView(view);
        listPopupWindow.show();
    }

    public void setSelfRecord() {
        if (TextUtils.isEmpty(binding.tvRelation.getText().toString())) {
            Toast.makeText(this, "请选择您与患者关系!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(binding.etPatientName.getText().toString())) {
            Toast.makeText(this, "请输入患者姓名!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(binding.tvBirthday.getText().toString())) {
            Toast.makeText(this, "请输入患者的出生年月!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id == 0) {
            Toast.makeText(this, "请选择患者的性别!", Toast.LENGTH_SHORT).show();
        }
        api.uploadOtherMedicalRecord(binding.tvRelation.getText().toString(), "", binding.etPatientName.getText().toString(), binding.tvBirthday.getText().toString(), id + "").enqueue(new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                Toast.makeText(PnewMedicalRecordFragment.this, "新建病历成功!", Toast.LENGTH_SHORT).show();
                showMain();
            }
        });

    }

    public void setOtherRecord() {
        if (TextUtils.isEmpty(binding.tvRelation.getText().toString())) {
            Toast.makeText(this, "请选择您与患者关系!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isHas == true) {
            binding.llYourName.setVisibility(View.GONE);
        } else {
            if (TextUtils.isEmpty(binding.etYourName.getText().toString())) {
                Toast.makeText(this, "请输入您的姓名!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (TextUtils.isEmpty(binding.etPatientName.getText().toString())) {
            Toast.makeText(this, "请输入患者姓名!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(binding.tvBirthday.getText().toString())) {
            Toast.makeText(this, "请选择患者的出生年月!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id == 0) {
            Toast.makeText(this, "请选择患者的性别!", Toast.LENGTH_SHORT).show();
        }
        String name = "";
        if (isHas == true) {
            name = patientProfile.getName();
        } else {
            name = binding.etYourName.getText().toString();
        }
        api.uploadOtherMedicalRecord(binding.tvRelation.getText().toString(), name, binding.etPatientName.getText().toString(), binding.tvBirthday.getText().toString(), id + "").enqueue(new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                Toast.makeText(PnewMedicalRecordFragment.this, "新建病历成功!", Toast.LENGTH_SHORT).show();
                showMain();
            }
        });
    }

    public void showMain() {
        Intent i = PMainActivity2.makeIntent(PnewMedicalRecordFragment.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
        startActivity(i);
        finish();
    }

    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }
}
