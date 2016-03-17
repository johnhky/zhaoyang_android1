package com.doctor.sun.ui.widget;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.doctor.sun.bean.City;
import com.doctor.sun.bean.Province;
import com.doctor.sun.databinding.ItemCityPickerBinding;
import com.doctor.sun.ui.activity.BaseActivity2;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by rick on 11/13/15.
 */
public class CityPickerDialog {
    private final BaseActivity2 context;
    private final View.OnClickListener confirm;
    private RealmResults<Province> mProvince;

    private int mProvinceId;
    private Dialog dialog;
    private RealmList<City> mCity;
    private int mCityId;

    public CityPickerDialog(BaseActivity2 context, RealmResults<Province> province, View.OnClickListener confirm) {
        mProvince = province;
        mCity = province.get(0).getCities();
        this.context = context;
        this.confirm = confirm;
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
            return;
        }

        ItemCityPickerBinding binding = ItemCityPickerBinding.inflate(LayoutInflater.from(context));
        if (confirm != null) {
            binding.confirm.setOnClickListener(confirm);
        }
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final NumberPicker provincePicker = binding.provincePicker;
        final NumberPicker cityPicker = binding.cityPicker;
        provincePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        cityPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        provincePicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return mProvince.get(value).getState();
            }
        });
        cityPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {

                return mProvince.get(mProvinceId).getCities().get(value).getCity();
            }
        });


        provincePicker.setMinValue(0);
        provincePicker.setMaxValue(mProvince.size() - 1);
        provincePicker.setValue(mProvinceId);
        provincePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mProvinceId = newVal;
                mCityId = 0;

                mCity = mProvince.get(mProvinceId).getCities();
                cityPicker.setMaxValue(mCity.size() - 1);
                cityPicker.setValue(1);
                changeValueByOne(cityPicker);
            }
        });


        cityPicker.setMinValue(0);
        cityPicker.setMaxValue(mCity.size() - 1);
        cityPicker.setValue(mCityId);
        cityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                mCityId = newVal;
            }
        });


        changeValueByOne(provincePicker);
        changeValueByOne(cityPicker);

        dialog = BottomDialog.showDialog(context, binding.getRoot());
    }

    private void changeValueByOne(NumberPicker picker) {
        try {
            Method method = picker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(picker, true);
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String getProvince() {
        return mProvince.get(mProvinceId).getState();
    }


    public String getCity() {
        return mCity.get(mCityId).getCity();
    }

    public void setCityId(int mCityId) {
        this.mCityId = mCityId;
    }

    public void setProvinceId(int mProvinceId) {
        mCity = mProvince.get(mProvinceId).getCities();
        this.mProvinceId = mProvinceId;
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
