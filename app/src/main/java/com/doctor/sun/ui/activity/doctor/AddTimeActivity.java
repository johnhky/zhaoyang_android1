package com.doctor.sun.ui.activity.doctor;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityAddTimeBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.TimeType;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;

import io.ganguo.library.common.LoadingHelper;
import retrofit2.Call;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


/**
 * Created by lucas on 12/8/15.
 * 设置出诊时间
 */
public class AddTimeActivity extends BaseFragmentActivity2 {


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
        binding.setDescription(new Description(R.layout.item_description, "就诊周期"));
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

    public void onMenuClicked() {

        if (getType() != 0) {
            if (getSelectedWeeks() != 0) {
                String to = binding.tvEndTime.getText().toString() + ":00";
                String from = binding.tvBeginTime.getText().toString() + ":00";
                int interval = getInterval();
                if (getData().getId() != 0) {
               /*     api.updateTime(getData().getId(), getSelectedWeeks(), getType(), from, to, interval).enqueue(new SimpleCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            finish();
                        }
                    });*/
                    api.newUpdateTime(getData().getId(),getSelectedWeeks(),getType(),from,to,interval).enqueue(new SimpleCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ApiDTO<Time>> call, Throwable t) {
                            super.onFailure(call, t);
                        }
                    });
                } else {
                    api.newSetTime(getSelectedWeeks(),getType(),from,to,interval).enqueue(new SimpleCallback<Time>() {
                        @Override
                        protected void handleResponse(Time response) {
                            finish();
                        }
                    });
                }
            } else {
                makeText(this, "就诊周期不能为空", LENGTH_SHORT).show();
            }
        } else {
            makeText(AddTimeActivity.this, "问诊类型错误", LENGTH_SHORT).show();
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
        int type = TimeType.TYPE_UNDEFINE;
        if (binding.rbDetail.isChecked())
            type = TimeType.TYPE_DETAIL;
        if(binding.rbFace.isChecked())
            type = TimeType.TYPY_FACE;
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

    @Override
    public String getMidTitleString() {
        if (getData().getId() == 0) {
            return "添加出诊时间";
        } else {
            return "设置出诊时间";
        }
    }
}
