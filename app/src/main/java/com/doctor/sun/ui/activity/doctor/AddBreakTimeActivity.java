package com.doctor.sun.ui.activity.doctor;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAddBreakTimeBinding;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.handler.TimeHandler;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import io.ganguo.library.common.ToastHelper;

/**
 * Created by lucas on 12/9/15.
 */
public class AddBreakTimeActivity extends BaseFragmentActivity2 {
    public static final int ADDDISTURB = 1;
    private TimeModule api = Api.of(TimeModule.class);
    private ActivityAddBreakTimeBinding binding;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, AddBreakTimeActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_break_time);
        binding.setDescription(new Description(R.layout.item_description, "就诊周期"));
        binding.setHandler(new TimeHandler());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save: {
                onMenuClicked();
            }
        }
        return true;
    }


    private int getWeekSelected() {
        boolean[] isSelected = new boolean[7];
        int result = 0;
        for (int i = 0; i < binding.llyWeeks.getChildCount(); i++) {
            isSelected[i] = binding.llyWeeks.getChildAt(i).isSelected();
        }
        for (int i = 0; i < binding.llyWeeks2.getChildCount() - 1; i++) {
            isSelected[i + 4] = binding.llyWeeks2.getChildAt(i).isSelected();
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

    public void selected(int column, int num) {
        if (column == 1)
            binding.llyWeeks.getChildAt(num).setSelected(true);
        if (column == 2)
            binding.llyWeeks2.getChildAt(num).setSelected(true);
    }

    public void onMenuClicked() {
        if (getWeekSelected() != 0) {
            api.setTime(getWeekSelected(), Time.TYPE_BREAK, binding.tvBeginTime.getText().toString() + ":00", binding.tvEndTime.getText().toString() + ":00", 0).enqueue(new ApiCallback<Time>() {
                @Override
                protected void handleResponse(Time response) {
                    Intent intent = new Intent();
                    intent.putExtra(Constants.DATA, response);
                    setResult(ADDDISTURB, intent);
                    finish();
                }
            });
        } else {
            ToastHelper.showMessage(this, "免打扰周期不能为空");
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_add_break_time;
    }
}
