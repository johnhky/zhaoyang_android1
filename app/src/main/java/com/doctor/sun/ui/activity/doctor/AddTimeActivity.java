package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAddTimeBinding;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.Time;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.common.ToastHelper;


/**
 * Created by lucas on 12/8/15.
 */
public class AddTimeActivity extends BaseActivity2 {


    private TimeModuleWrapper api = TimeModuleWrapper.getInstance();
    private ActivityAddTimeBinding binding;

    public static Intent makeIntent(Context context, Time data) {
        Intent i = new Intent(context, AddTimeActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }

    private Time getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        binding.setData(getData());
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_time);
        HeaderViewModel header = new HeaderViewModel(this);
        if (getData().getId() == 0) {
            header.setMidTitle("添加出诊时间").setRightTitle("保存");
        } else {
            header.setMidTitle("设置出诊时间").setRightTitle("保存");
        }
        binding.setHeader(header);
        binding.setDescription(new Description(R.layout.item_time_category, "就诊周期"));
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();

        if (getType() != 0) {
            if (getSelectedWeeks() != 0) {
                String to = binding.tvEndTime.getText().toString() + ":00";
                String from = binding.tvBeginTime.getText().toString() + ":00";
                int interval = getInterval();
                if (getData().getId() != 0) {
                    api.updateTime(getData().getId(), getSelectedWeeks(), getType(), from, to, interval).enqueue(new SimpleCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            finish();
                        }
                    });
                } else {
                    api.setTime(getSelectedWeeks(), getType(), from, to, interval).enqueue(new SimpleCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            finish();
                        }
                    });
                }
            } else {
                ToastHelper.showMessage(AddTimeActivity.this, "就诊周期不能为空");
            }
        } else {
            ToastHelper.showMessage(AddTimeActivity.this, "问诊类型错误");
        }
    }

    private int getInterval() {
        int interval;
        try {
            interval = Integer.parseInt(binding.etInterval.getText().toString());
        } catch (NumberFormatException e) {
            interval = 0;
        }
        return interval;
    }


    private int getType() {
        int type = Time.TYPE_UNDEFINE;
        if (!binding.rbQuick.isChecked() && !binding.rbDetail.isChecked())
            type = Time.TYPE_UNDEFINE;
        if (binding.rbQuick.isChecked())
            type = Time.TYPE_QUICK;
        if (binding.rbDetail.isChecked())
            type = Time.TYPE_DETAIL;
        return type;
    }

    private int getSelectedWeeks() {
        boolean[] isSelected = new boolean[7];
        int result = 0;
        for (int i = 0; i < binding.flyWeeks.getChildCount(); i++) {
            isSelected[i] = binding.flyWeeks.getChildAt(i).isSelected();
        }

        for (int i = 0; i < isSelected.length; i++) {
            if (isSelected[i]) {
                result += Math.pow(2, i);
            } else {
                result += 0;
            }
        }
        return result;
    }
}
