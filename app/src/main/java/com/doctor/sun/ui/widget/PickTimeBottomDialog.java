package com.doctor.sun.ui.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.databinding.BottomDialogPickTimeBinding;
import com.doctor.sun.entity.AppointmentBuilder;
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
public class PickTimeBottomDialog extends BottomSheetDialog {
    private final AppointmentBuilder builder;
    private TimeModule api = Api.of(TimeModule.class);
    private BottomDialogPickTimeBinding binding;
    private PickTimeAdapter mAdapter;


    public PickTimeBottomDialog(Context context, AppointmentBuilder builder) {
        super(context);
        this.builder = builder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.bottom_dialog_pick_time, null, false);
        binding.setData(getDateLabel());

        GridLayoutManager layout = new GridLayoutManager(getContext(), 3);
        binding.recyclerView.setLayoutManager(layout);
        mAdapter = createAdapter();
        binding.recyclerView.setAdapter(mAdapter);

        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                api.getDaySchedule(getDoctorId(), getDate(), builder.getType(), String.valueOf(builder.getDuration())).enqueue(new ListCallback<Time>(mAdapter));
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

        binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Time selectedItem = mAdapter.getSelectedItem();
                onItemSelected(selectedItem);
            }
        });
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        setContentView(binding.getRoot());
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setPeekHeight(getContext().getResources().getDimensionPixelSize(R.dimen.dp_500));
    }

    public void onItemSelected(Time selectedItem) {
        if (selectedItem != null) {
            selectedItem.setDate(builder.getTime().getDate());
            builder.setTime(selectedItem);
            builder.reviewAppointment(getContext());
        } else {
            Toast.makeText(getContext(), "请选择时间", Toast.LENGTH_SHORT).show();
        }
    }

    private int getDoctorId() {
        return builder.getDoctor().getId();
    }

    protected PickTimeAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    protected PickTimeAdapter createAdapter() {
        PickTimeAdapter simpleAdapter = new PickTimeAdapter(builder.getType(), getDateTime());
        simpleAdapter.mapLayout(R.layout.item_time, R.layout.reserve_time);
        return simpleAdapter;
    }

    @Override
    public void show() {
        super.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public String getDate() {
        return builder.getTime().getDate();
    }

    public String getDateLabel() {
        Date date = new Date(getDateTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return simpleDateFormat.format(date);
    }

    public long getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.CHINA);
        try {
            Date parse = simpleDateFormat.parse(builder.getTime().getDate() + "-00:00:00");
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
