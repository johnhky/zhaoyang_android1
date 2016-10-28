package com.doctor.sun.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.DialogPickTimeBinding;
import com.doctor.sun.entity.Time;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.adapter.PickTimeAdapter;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by rick on 18/1/2016.
 */
public class PickTimeDialog extends Dialog {
    private TimeModule api = Api.of(TimeModule.class);
    private DialogPickTimeBinding binding;
    private PickTimeAdapter mAdapter;
    private String date;
    private String time;
    private int type;

    public PickTimeDialog(Context context, String date, int type) {
        super(context);
        this.date = date;
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogPickTimeBinding.inflate(getLayoutInflater());

        GridLayoutManager layout = new GridLayoutManager(getContext(), 3);
        binding.recyclerView.setLayoutManager(layout);
        mAdapter = createAdapter();
        binding.recyclerView.setAdapter(mAdapter);


        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                api.getDaySchedule(getDoctorId(), getDate(), 2, "15").enqueue(new ListCallback<Time>(mAdapter));
            }
        });
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mAdapter.size()) {
                    return 3;
                }
                return 1;
            }
        });
        binding.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeSelected(mAdapter.getTime().replace(" ", ""));
            }
        });
        binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(binding.getRoot());
    }

    protected void onTimeSelected(String time) {

    }

    private int getDoctorId() {
        return Settings.getDoctorProfile().getId();
    }

    protected PickTimeAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    protected PickTimeAdapter createAdapter() {
        PickTimeAdapter simpleAdapter = new PickTimeAdapter( type, getDateTime());
        simpleAdapter.mapLayout(R.layout.item_time, R.layout.reserve_time);
        return simpleAdapter;
    }

    @Override
    public void show() {
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.2); // 高度设置为屏幕的0.5
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        super.show();
        getWindow().setAttributes(p);
    }

    public String getDate() {
        return date;
    }

    public long getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date parse = simpleDateFormat.parse(getDate());
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
