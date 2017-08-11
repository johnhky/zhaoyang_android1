package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityPickTimeBinding;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.PickTimeAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by rick on 11/1/2016.
 */
public class PickTimeActivity extends BaseFragmentActivity2 {

    private TimeModule api = Api.of(TimeModule.class);
    private ActivityPickTimeBinding binding;
    private PickTimeAdapter mAdapter;
    String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    private AppointmentBuilder data;

    public static Intent makeIntent(Context context, AppointmentBuilder builder) {
        Intent i = new Intent(context, PickTimeActivity.class);
        i.putExtra(Constants.DATA, builder);
        return i;
    }


    private AppointmentBuilder getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        data = getData();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_time);


        binding.setType(getTypeImpl());
        binding.setDate(getDateImpl() + " " + getWeek());
        initAdapter();
        initRecyclerView();

        binding.setData(getData());
        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAppointment();
            }
        });
    }

    private String getTypeImpl() {
        if (data.getType() == AppointmentType.STANDARD) {
            return "预约类型:简易复诊";
        } else if (data.getType() == AppointmentType.PREMIUM) {
            return "预约类型:VIP网诊";
        }
        return "";
    }

    private String getDateImpl() {
        return data.getTime().getDate();
    }

    private String getWeek() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date parse = simpleDateFormat.parse(data.getTime().getDate());
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(parse);
            int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            if (w < 0) {
                w = 0;
            }
            return weekOfDays[w];
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void initAdapter() {
        mAdapter = createAdapter();
        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadData();
            }
        });
    }

    private void initRecyclerView() {
        GridLayoutManager layout = new GridLayoutManager(this, 3);
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.size()) {
                    return 3;
                }
                return 1;
            }
        });
        binding.recyclerView.setLayoutManager(layout);
        binding.recyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        api.getDaySchedule(data.getDoctor().getId(), data.getTime().getDate(), data.getType(), String.valueOf(data.getDuration())).enqueue(new ListCallback<Time>(mAdapter));
    }

    private void makeAppointment() {
        Time selectedTime = mAdapter.getSelectedItem();
        if (selectedTime == null) {
            Toast.makeText(PickTimeActivity.this, "请选择一个时间", Toast.LENGTH_SHORT).show();
            return;
        }
        AppointmentBuilder data = getData();
        selectedTime.setDate(data.getTime().getDate());
        data.setTime(selectedTime);
        Intent intent = ApplyAppointmentActivity.makeIntent(this, data);
        startActivity(intent);
    }


    protected PickTimeAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    protected PickTimeAdapter createAdapter() {
        PickTimeAdapter simpleAdapter = new PickTimeAdapter( data.getType(), getDateTime());
        simpleAdapter.mapLayout(R.layout.item_time, R.layout.reserve_time);
        return simpleAdapter;
    }

    public long getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        try {
            Date parse = simpleDateFormat.parse(data.getTime().getDate() + "-00:00:00");
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_pick_time;
    }
}
