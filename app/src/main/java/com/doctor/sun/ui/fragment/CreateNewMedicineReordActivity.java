package com.doctor.sun.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PNewRecordBinding;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by heky on 17/6/30.
 */

public class CreateNewMedicineReordActivity extends BaseFragmentActivity2 {
    PNewRecordBinding binding;
    private String[] relativeList = AppContext.me().getResources().getStringArray(R.array.relative_array);
    private List<String> items = new ArrayList<>();
    private int id = 0;
    ProfileModule api = Api.of(ProfileModule.class);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(CreateNewMedicineReordActivity.this, R.layout.p_new_record);
        initLisitener();
    }


    private void initLisitener() {
        items.addAll(Arrays.asList(relativeList));
        if (hasSelfRecord(getRecords()) == true) {
            items.remove(0);
            binding.llYourName.setVisibility(View.GONE);
            binding.etYourName.setText(hasRecordName(getRecords()));
        }
        if (binding.radioMan.isChecked() == true) {
            id = 1;
        }
        binding.tvRelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow(v, CreateNewMedicineReordActivity.this, items);
            }
        });
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                binding.tvBirthday.setText(year + "-" + (month + 1));
            }
        }, year, month, day);
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
                    Toast.makeText(CreateNewMedicineReordActivity.this, "请先选择您与患者的关系!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (!binding.tvRelation.getText().equals("本人")) {
                    setOtherRecord();
                } else {
                    if (!binding.tvRelation.getText().equals("本人")) {
                        setOtherRecord();
                    } else {
                        setSelfRecord();
                    }

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
        listPopupWindow.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, items));
        listPopupWindow.setWidth(view.getWidth());
        listPopupWindow.setHeight(450);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                binding.tvRelation.setText(items.get(position));
                if (!TextUtils.isEmpty(binding.tvRelation.getText().toString())) {
                    if (binding.tvRelation.getText().toString().equals("本人")) {
                        binding.llYourName.setVisibility(View.GONE);
                    } else {
                        if (hasSelfRecord(getRecords()) == true) {
                            binding.llYourName.setVisibility(View.GONE);
                        } else {
                            if (TextUtils.isEmpty(Settings.getPatientProfile().getName())) {
                                binding.llYourName.setVisibility(View.VISIBLE);
                            } else {
                                binding.etYourName.setText("");
                                binding.llYourName.setVisibility(View.GONE);
                            }

                        }
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
                Toast.makeText(CreateNewMedicineReordActivity.this, "新建病历成功!", Toast.LENGTH_SHORT).show();
                createSuccess(response);
            }
        });

    }

    public void setOtherRecord() {
        if (TextUtils.isEmpty(binding.tvRelation.getText().toString())) {
            Toast.makeText(this, "请选择您与患者关系!", Toast.LENGTH_SHORT).show();
            return;
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
        api.uploadOtherMedicalRecord(binding.tvRelation.getText().toString(), binding.etYourName.getText().toString(), binding.etPatientName.getText().toString(), binding.tvBirthday.getText().toString(), id + "").enqueue(new SimpleCallback<MedicalRecord>() {
            @Override
            protected void handleResponse(MedicalRecord response) {
                Toast.makeText(CreateNewMedicineReordActivity.this, "新建病历成功!", Toast.LENGTH_SHORT).show();
                createSuccess(response);
            }
        });
    }

    public void createSuccess(MedicalRecord record) {
        if (getType() != 0) {
            Intent intent = new Intent();
            intent.setAction(Constants.CREATE_SUCCESS);
            intent.putExtra(Constants.DATA, record.getRecordName());
            sendBroadcast(intent);
        } else {
            Intent toRefresh = new Intent();
            toRefresh.setAction(Constants.REFRESH_RECORD);
            sendBroadcast(toRefresh);
        }
        finish();
    }

    public int getType() {
        return getIntent().getIntExtra(Constants.DATA, 0);
    }

    public List<MedicalRecord> getRecords() {
        return getIntent().getParcelableArrayListExtra(Constants.MOCK);
    }

    public static boolean hasSelfRecord(List<MedicalRecord> records) {
        if (records == null) {
            return false;
        }
        for (int i = 0; i < records.size(); i++) {
            if ("本人".equals(records.get(i).getRelation())) {
                return true;
            }
        }

        return false;
    }

    public static String hasRecordName(List<MedicalRecord> records) {
        if (records == null) {
            return null;
        }
        for (int i = 0; i < records.size(); i++) {
            if ("本人".equals(records.get(i).getRelation())) {
                return records.get(i).getRecordName();
            }
        }
        return "";
    }

    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    @Override
    public int getMidTitle() {
        return R.string.new_record;
    }
}
