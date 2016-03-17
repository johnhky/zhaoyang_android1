package com.doctor.sun.entity;

import android.app.TimePickerDialog;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.PickTimeDialog;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by rick on 12/22/15.
 */
public class ItemPickTime extends BaseItem {
    private final GregorianCalendar calendar = new GregorianCalendar();

    private String title;
    private int mBeginHour;
    private int mBeginMinute;
    private int mEndHour;
    private int mEndMinute;

    private String date;
    private int type;

    public ItemPickTime(int layoutId, String title) {
        super(layoutId);
        this.title = title;
        mBeginHour = calendar.get(Calendar.HOUR_OF_DAY);
        mBeginMinute = calendar.get(Calendar.MINUTE);
    }

    TimePickerDialog.OnTimeSetListener setBeginTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(final TimePicker view, int hourOfDay, int minute) {
            mBeginHour = hourOfDay;
            mBeginMinute = minute;

            Toast.makeText(view.getContext(), "请选择结束时间", Toast.LENGTH_SHORT).show();
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), setEndTime, mEndHour, mEndMinute, true);
                    timePickerDialog.show();
                }
            }, 1000);
        }
    };

    TimePickerDialog.OnTimeSetListener setEndTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mEndHour = hourOfDay;
            mEndMinute = minute;

            notifyChange();
        }
    };

    public int getItemLayoutId() {
        return R.layout.item_pick_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyChange();
    }

    public String getTime() {
        return String.format("%02d:%02d-%02d:%02d", mBeginHour, mBeginMinute, mEndHour, mEndMinute);
    }


    public OnItemClickListener pickTime() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                Toast.makeText(view.getContext(), "请选择开始时间", Toast.LENGTH_SHORT).show();
                TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), setBeginTime, mBeginHour, mBeginMinute, true);
                timePickerDialog.show();
            }
        };
    }

    public OnItemClickListener pickTime2() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter adapter, View view, BaseViewHolder vh) {
                final PickTimeDialog pickTimeDialog = new PickTimeDialog(view.getContext(), date, type) {
                    @Override
                    protected void onTimeSelected(String time) {
                        setTime(time);
                        notifyChange();
                        dismiss();
                    }
                };
                pickTimeDialog.show();
            }
        };
    }

    public void setTime(String time) {
        String[] times = time.split("-");
        String[] startTime = times[0].split(":");
        String[] endTime = times[1].split(":");

        mBeginHour = Integer.parseInt(startTime[0]);
        mBeginMinute = Integer.parseInt(startTime[1]);
        mEndHour = Integer.parseInt(endTime[0]);
        mEndMinute = Integer.parseInt(endTime[1]);
    }

    public int getmBeginHour() {
        return mBeginHour;
    }

    public void setmBeginHour(int mBeginHour) {
        this.mBeginHour = mBeginHour;
    }

    public int getmBeginMinute() {
        return mBeginMinute;
    }

    public void setmBeginMinute(int mBeginMinute) {
        this.mBeginMinute = mBeginMinute;
    }

    public int getmEndHour() {
        return mEndHour;
    }

    public void setmEndHour(int mEndHour) {
        this.mEndHour = mEndHour;
    }

    public int getmEndMinute() {
        return mEndMinute;
    }

    public void setmEndMinute(int mEndMinute) {
        this.mEndMinute = mEndMinute;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }
}
