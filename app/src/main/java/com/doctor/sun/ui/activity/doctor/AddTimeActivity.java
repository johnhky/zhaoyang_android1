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
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

import io.ganguo.library.common.ToastHelper;


/**
 * Created by lucas on 12/8/15.
 */
public class AddTimeActivity extends BaseActivity2 {


    private TimeModule api = Api.of(TimeModule.class);
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
    protected void onStart() {
        if (getData() != null)
            existTimeInit();
        super.onStart();
    }

    private void existTimeInit() {
        binding.tvBeginTime.setText(getData().getFrom().substring(0, 5));
        binding.tvEndTime.setText(getData().getTo().substring(0, 5));
        switch (getData().getType()) {
            case 2:
                binding.rbFace.setChecked(true);
                break;
            case 3:
                binding.rbNetwork.setChecked(true);
                break;
        }
        int week = getData().getWeek();
        for (int i = 64; i > 0; i /= 2) {
            if (week / i > 0) {
                selectedWeek(i);
                week -= i;
            }
        }
    }

    public void selectedWeek(double num) {
        binding.flyWeeks.getChildAt((int) (Math.log(num) / Math.log((double) 2))).setSelected(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        binding.setData(new Time());
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_time);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("添加出诊时间").setRightTitle("保存");
        binding.setHeader(header);
        binding.setDescription(new Description(R.layout.item_time_category, "就诊周期"));
    }

    @Override
    public void onMenuClicked() {
        super.onMenuClicked();

        if (getType() != 0) {
            if (getSelectedWeeks() != 0) {
                if (getData() != null) {
                    api.updateTime(getData().getId(), getSelectedWeeks(), getType(), binding.tvBeginTime.getText().toString() + ":00", binding.tvEndTime.getText().toString() + ":00").enqueue(new ApiCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            finish();
                        }
                    });
                } else {
                    api.setTime(getSelectedWeeks(), getType(), binding.tvBeginTime.getText().toString() + ":00", binding.tvEndTime.getText().toString() + ":00").enqueue(new ApiCallback<Time>() {
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


    private int getType() {
        int type = 0;
        if (!binding.rbFace.isChecked() && !binding.rbNetwork.isChecked())
            type = 0;
        if (binding.rbFace.isChecked())
            type = 2;
        if (binding.rbNetwork.isChecked())
            type = 3;
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
