package com.doctor.sun.entity.handler;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Time;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.doctor.AddBreakTimeActivity;
import com.doctor.sun.ui.activity.doctor.AddTimeActivity;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.BaseAdapter;
import com.doctor.sun.ui.adapter.core.OnItemClickListener;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by lucas on 12/9/15.
 */
public class TimeHandler {
    public static final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
    public static final int ONE_HOUR = 3600000;

    private Time data;
    private int mHour;
    private int mMinute;

    public TimeHandler(Time time) {
        data = time;
    }

    public TimeHandler() {
    }

    public String getFrom() {
        String from = data.getFrom();
        if (from == null || from.length() < 5) {
            return "";
        }
        return from.substring(0, 5);
    }

    public boolean isWeekSelected(int week) {
        return ((data.getWeek() >> week) & 1) == 1;
    }


    public String getTo() {
        String to = data.getTo();
        if (to == null || to.length() < 5) {
            return "";
        }
        return to.substring(0, 5);
    }

    public interface GetIsEditMode {
        boolean getIsEditMode();
    }

    public void addTime(View view) {
        Intent intent = AddTimeActivity.makeIntent(view.getContext(), new Time());
        view.getContext().startActivity(intent);
    }

    public void addDisturb(View view) {
        Intent intent = AddBreakTimeActivity.makeIntent(view.getContext());
        view.getContext().startActivity(intent);
    }

    public void upDateTime(View view) {
        Intent intent = AddTimeActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }

    public OnItemClickListener deleteDialog() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, View view, final BaseViewHolder vh) {
                final TimeModule api = Api.of(TimeModule.class);
                String questiion = "确定删除该出诊时间？";
                String cancel = "取消";
                String delete = "删除";
                TwoChoiceDialog.show(view.getContext(), questiion, cancel, delete, new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(final TwoChoiceDialog deleteDialog) {
                        api.deleteTime(data.getId()).enqueue(new ApiCallback<String>() {
                            @Override
                            protected void handleResponse(String response) {

                            }

                            @Override
                            protected void handleApi(ApiDTO<String> body) {
                                deleteDialog.dismiss();
                                adapter.remove(data);
                                adapter.notifyItemRemoved(vh.getAdapterPosition());

                                LayoutId objectFace = (LayoutId) adapter.get(adapter.size() - 1);
                                if (objectFace.getItemLayoutId() == R.layout.item_time_category) {
                                    adapter.remove(adapter.size() - 1);
                                    adapter.notifyItemRemoved(adapter.size() - 1);
                                }
                                if (adapter.size() > 2) {
                                    LayoutId objectNetwork = (LayoutId) adapter.get(1);
                                    if (objectNetwork.getItemLayoutId() == R.layout.item_time_category) {
                                        adapter.remove(0);
                                        adapter.notifyItemRemoved(0);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelClick(TwoChoiceDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        };
    }

    public OnItemClickListener deleteDisturb() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(final BaseAdapter adapter, View view, final BaseViewHolder vh) {
                final TimeModule api = Api.of(TimeModule.class);
                TwoChoiceDialog.show(view.getContext(), "确定删除该免打扰时间？", "取消", "删除", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(final TwoChoiceDialog dialog) {
                        api.deleteTime(data.getId()).enqueue(new ApiCallback<String>() {
                            @Override
                            protected void handleResponse(String response) {

                            }

                            @Override
                            protected void handleApi(ApiDTO<String> body) {
                                dialog.dismiss();
                                adapter.remove(data);
                                adapter.notifyItemRemoved(vh.getAdapterPosition());
                            }
                        });

                    }

                    @Override
                    public void onCancelClick(TwoChoiceDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        };
    }


    public void beginTimeSet(View view) {
        final TextView tvBeginTime = (TextView) view.findViewById(R.id.tv_begin_time);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                tvBeginTime.setText(String.format(Locale.CHINA, "%02d:%02d", mHour, mMinute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), onTimeSetListener, mHour, mMinute, true);
        timePickerDialog.show();
    }


    public void endTimeSet(View view) {
        final TextView tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;
                tvEndTime.setText(String.format(Locale.CHINA, "%02d:%02d", mHour, mMinute));
            }
        };
        TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(), onTimeSetListener, mHour, mMinute, true);
        timePickerDialog.show();
    }


    public void select(View view) {
        view.setSelected(!view.isSelected());
    }

    public long getFromMillis() {
        try {
            return format.parse(data.getFrom()).getTime() + 8 * ONE_HOUR;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean isPast(long dateTime) {
        return getFromMillis() + dateTime < System.currentTimeMillis();
    }
}
