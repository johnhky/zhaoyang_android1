package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.City;
import com.doctor.sun.bean.Province;
import com.doctor.sun.databinding.ActivityAddMedicalRecordBinding;
import com.doctor.sun.entity.ItemPickDate;
import com.doctor.sun.ui.activity.GetLocationActivity;
import com.doctor.sun.ui.activity.patient.handler.AddMedicalRecordHandler;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.CityPickerDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import io.ganguo.library.common.ToastHelper;
import io.realm.RealmList;
import io.realm.RealmResults;


/**
 * 建立病历
 * Created by rick on 10/23/15.
 */
public class AddMedicalRecordActivity extends GetLocationActivity implements View.OnClickListener, AddMedicalRecordHandler.MedicalRecordInput {
    public static final String RECORD_TYPE = "RECORD_TYPE";
    public static final int TYPE_SELF = 0;
    public static final int TYPE_OTHERS = 1;
    public static final String SELF_NAME = "selfName";
    public static final String EMAIL = "email";
    public static final String RELATION = "relation";
    public static final String NAME = "name";
    public static final String PROVINCE = "province";
    public static final String CITY = "city";
    public static final String ADDRESS = "address";
    public static final String IDENTITY_NUMBER = "identityNumber";
    public static final String BIRTHDAY = "birthday";
    public static final String GENDER = "gender";
    public static final String FIRST_TIME = "FIRST_TIME";

    private ActivityAddMedicalRecordBinding binding;
    private AddMedicalRecordHandler handler;
    private CityPickerDialog cityPickerDialog;

    public static Intent makeIntent(Context context, int type, boolean firstTime) {
        Intent i = new Intent(context, AddMedicalRecordActivity.class);
        i.putExtra(RECORD_TYPE, type);
        i.putExtra(FIRST_TIME, firstTime);
        return i;
    }

    public boolean isFirstTime() {
        return getIntent().getBooleanExtra(FIRST_TIME, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_medical_record);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("新建病历")
                .setRightTitle("完成");
        binding.setHeader(header);
        binding.setTime(new ItemPickDate(-1, ""));
        switch (getRecordType()) {
            case TYPE_OTHERS: {
                binding.othersRecord.root.setVisibility(View.VISIBLE);
                break;
            }
            case TYPE_SELF: {
                binding.myRecord.root.setVisibility(View.VISIBLE);
                break;
            }
        }
        binding.flLocation.setOnClickListener(this);
        binding.tvProvince.setOnClickListener(this);
        binding.tvCity.setOnClickListener(this);

        handler = new AddMedicalRecordHandler(this, getRecordType());
    }

    private int getRecordType() {
        return getIntent().getIntExtra(RECORD_TYPE, -1);
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();
        handler.done(null);
    }

    @Override
    public HashMap<String, String> getParam() {
        HashMap<String, String> result = new HashMap<>();
        switch (getRecordType()) {
            case TYPE_SELF: {
                result.put(SELF_NAME, binding.myRecord.etName.getText().toString());
                result.put(EMAIL, binding.myRecord.etEmail.getText().toString());
                break;
            }
            case TYPE_OTHERS: {
                result.put(SELF_NAME, binding.othersRecord.etSelfName.getText().toString());
                result.put(EMAIL, binding.othersRecord.etEmail.getText().toString());
                result.put(RELATION, binding.othersRecord.etRelation.getText().toString());
                result.put(NAME, binding.othersRecord.etPatientName.getText().toString());
                break;
            }
        }

        result.put(PROVINCE, binding.tvProvince.getText().toString());
        result.put(CITY, binding.tvCity.getText().toString());
        result.put(ADDRESS, binding.etAddress.getText().toString());
        result.put(IDENTITY_NUMBER, binding.etIdentityNumber.getText().toString());
        result.put(BIRTHDAY, binding.etBirthday.getText().toString());
        String gender;
        if (binding.rbMale.isChecked()) {
            gender = "1";
        } else if (binding.rbFemale.isChecked()) {
            gender = "2";
        } else {
            gender = "1";
        }
        // 性别:男:1,女:2
        result.put(GENDER, gender);

        Log.e(TAG, "getParam: " + result.toString());
        if (isValidRecord(result)) {
            return result;
        } else {
            return null;
        }
    }

    public boolean isValidRecord(HashMap<String, String> map) {

        if (isFirstTime()) {
            if (map.get(SELF_NAME).equals("")) {
                ToastHelper.showMessage(this, "请填写名字");
                return false;
            }

            if (map.get(EMAIL).equals("")) {
                ToastHelper.showMessage(this, "请填写邮箱");
                return false;
            }

            if (map.get(BIRTHDAY).equals("")) {
                ToastHelper.showMessage(this, "请填写生日");
                return false;
            }

            if (map.get(GENDER).equals("")) {
                ToastHelper.showMessage(this, "请填写性别");
                return false;
            }
        }

        if (map.get(PROVINCE).equals("")) {
            ToastHelper.showMessage(this, "请选择省份");
            return false;
        }

        if (map.get(CITY).equals("")) {
            ToastHelper.showMessage(this, "请选择城市");
            return false;
        }
        switch (getRecordType()) {
            case TYPE_SELF: {
                break;
            }
            case TYPE_OTHERS: {
                if (map.get(RELATION).equals("")) {
                    ToastHelper.showMessage(this, "请填写患者与您的关系");
                    return false;
                }
                if (map.get(NAME).equals("")) {
                    ToastHelper.showMessage(this, "请填写患者姓名");
                    return false;
                }
                break;
            }
        }

        return true;
    }

    @Override
    public void onSelfRecordAdded() {
        ToastHelper.showMessage(this, "新建成功");
        finish();
    }

    @Override
    public void onRelativeRecordAdded() {
        onSelfRecordAdded();
    }

    @Override
    protected void updateLocation(final Location location) {
        if (location == null) {
            ToastHelper.showMessage(this, "获取地址失败");
            return;
        }
        Province province = realm.where(Province.class).beginGroup().greaterThan("maxLon", location.getLongitude()).lessThan("minLon", location.getLongitude())
                .greaterThan("maxLat", location.getLatitude()).lessThan("minLat", location.getLatitude()).endGroup().findFirst();
        if (province != null) {
            RealmList<City> cities = province.getCities();
            if (!cities.isEmpty()) {
                ArrayList<City> copy = new ArrayList<>(cities);
                Collections.sort(copy, new Comparator<City>() {
                    @Override
                    public int compare(City lhs, City rhs) {
                        double ld = Math.sqrt(Math.pow(lhs.getLon() - location.getLongitude(), 2f) + Math.pow(lhs.getLat() - location.getLatitude(), 2f));
                        double rd = Math.sqrt(Math.pow(rhs.getLon() - location.getLongitude(), 2f) + Math.pow(rhs.getLat() - location.getLatitude(), 2f));
                        if (ld == rd) {
                            return 0;
                        } else if (ld < rd) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                });
                binding.tvCity.setText(copy.get(0).getCity());
            }
            binding.tvProvince.setText(province.getState());
        } else {
            Log.e(TAG, "updateLocation: not found");
        }
        onLocationGot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_location: {
                getLocation();
                break;
            }
            case R.id.tv_province: {
                //点这个跟点下面的按钮事件是相同的.所以没有break;
            }
            case R.id.tv_city: {
                if (cityPickerDialog == null) {
                    createCityPicker();
                }
                cityPickerDialog.show();
                break;
            }
        }
    }

    private void createCityPicker() {
        RealmResults<Province> provinces = realm.where(Province.class).findAll();
        String state = binding.tvProvince.getText().toString();
        String city = binding.tvCity.getText().toString();
        int provinceId = 0;
        int cityId = 0;
        for (int i = 0; i < provinces.size(); i++) {
            if (provinces.get(i).getState().equals(state)) {
                provinceId = i;
            }
        }
        RealmList<City> cities = provinces.get(provinceId).getCities();
        for (int i = 0; i < cities.size(); i++) {
            if (cities.get(i).getCity().equals(city)) {
                cityId = i;
            }
        }

        cityPickerDialog = new CityPickerDialog(this, provinces, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.tvCity.setText(cityPickerDialog.getCity());
                binding.tvProvince.setText(cityPickerDialog.getProvince());
                cityPickerDialog.dismiss();
            }
        });

        cityPickerDialog.setProvinceId(provinceId);
        cityPickerDialog.setCityId(cityId);
    }
}
