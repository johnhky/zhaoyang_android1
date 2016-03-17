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
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Time;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.adapter.PickTimeAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.pager.PickDatePagerAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by rick on 11/1/2016.
 */
public class PickTimeActivity extends BaseActivity2 {

    private TimeModule api = Api.of(TimeModule.class);
    private ActivityPickTimeBinding binding;
    private PickTimeAdapter mAdapter;
    String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};

    public static Intent makeIntent(Context context, Doctor data, String date, String recordId, String consultType) {
        Intent i = new Intent(context, PickTimeActivity.class);
        i.putExtra(Constants.DATA, data);
        i.putExtra(Constants.DATE, date);
        i.putExtra(Constants.PARAM_RECORD_ID, recordId);
        i.putExtra(Constants.CONSULT_TYPE, consultType);
        return i;
    }


    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    private String getDate() {
        return getIntent().getStringExtra(Constants.DATE);
    }

    public String getType() {
        return getIntent().getStringExtra(Constants.CONSULT_TYPE);
    }

    private String getRecordId() {
        return getIntent().getStringExtra(Constants.PARAM_RECORD_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pick_time);

        binding.setHeader(getHeaderViewModel());

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
        if (getType().equals(PickDatePagerAdapter.TYPE_FACE)) {
            return "预约类型：简捷复诊";
        } else if (getType().equals(PickDatePagerAdapter.TYPE_NET)) {
            return "预约类型：详细咨询";
        }
        return "";
    }

    private String getDateImpl() {
        return getDate();
    }

    private String getWeek() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date parse = simpleDateFormat.parse(getDate());
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
        Doctor data = getData();
        api.getDaySchedule(data.getId(), getDate(), getType(), data.getDuration()).enqueue(new ListCallback<Time>(mAdapter));
    }

    private void makeAppointment() {
        String selectedTime = mAdapter.getSelectedTime();
        if (selectedTime.equals("")) {
            Toast.makeText(PickTimeActivity.this, "请选择一个时间", Toast.LENGTH_SHORT).show();
            return;
        }
        String bookTime = getDate() + " " + selectedTime.replace(" ", "");
        Intent intent = ApplyAppointmentActivity.makeIntent(this, getData(), bookTime, getType(), getRecordId());
        startActivity(intent);
    }

    @NonNull
    protected HeaderViewModel getHeaderViewModel() {
        return new HeaderViewModel(this).setMidTitle("选择时间");
    }


    protected PickTimeAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    protected PickTimeAdapter createAdapter() {
        PickTimeAdapter simpleAdapter = new PickTimeAdapter(this, Integer.valueOf(getType()));
        simpleAdapter.mapLayout(R.layout.item_time, R.layout.reserve_time);
        return simpleAdapter;
    }

}
