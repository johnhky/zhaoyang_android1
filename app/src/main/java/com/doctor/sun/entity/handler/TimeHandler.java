package com.doctor.sun.entity.handler;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.entity.Time;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.doctor.sun.ui.activity.doctor.AddTimeActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.BaseViewHolder;
import com.doctor.sun.vo.LayoutId;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.adapter.core.BaseListAdapter;
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
    long lastChangeTime = 0;

    private Time data;
    private int mHour;
    private int mMinute;
    public static final int FIVE_SECOND = 5000;

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


    public void upDateTime(View view) {
        Intent intent = AddTimeActivity.makeIntent(view.getContext(), data);
        view.getContext().startActivity(intent);
    }


    public void showDeleteDialog(final BaseListAdapter adapter, final BaseViewHolder vh) {
        final TimeModuleWrapper api = TimeModuleWrapper.getInstance();
        String questiion = "确定删除该出诊时间？";
        String cancel = "取消";
        String delete = "删除";
        TwoChoiceDialog.show(vh.itemView.getContext(), questiion, cancel, delete, new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(final MaterialDialog deleteDialog) {
                api.deleteTime(data.getId(), data.getType()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        deleteDialog.dismiss();
                        adapter.removeItem(data);
                        adapter.notifyItemRemoved(vh.getAdapterPosition());

                        LayoutId objectFace = (LayoutId) adapter.get(adapter.size() - 1);
                        if (objectFace.getItemLayoutId() == R.layout.item_description) {
                            adapter.removeItem(adapter.get(adapter.size() - 1));
                            adapter.notifyItemRemoved(adapter.size() - 1);
                        }
                        if (adapter.size() > 2) {
                            LayoutId objectNetwork = (LayoutId) adapter.get(1);
                            if (objectNetwork.getItemLayoutId() == R.layout.item_description) {
                                adapter.removeItem(adapter.get(0));
                                adapter.notifyItemRemoved(0);
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    public void showDeleteDisturb(final BaseListAdapter adapter, final BaseViewHolder vh) {
        final TimeModuleWrapper api = TimeModuleWrapper.getInstance();
        TwoChoiceDialog.show(vh.itemView.getContext(), "确定删除该免打扰时间？", "取消", "删除", new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(final MaterialDialog dialog) {
                api.deleteTime(data.getId(), data.getType()).enqueue(new SimpleCallback<String>() {
                    @Override
                    protected void handleResponse(String response) {
                        dialog.dismiss();
                        adapter.removeItem(data);
                        adapter.notifyItemRemoved(vh.getAdapterPosition());
                    }
                });
            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });
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

    public void constrainInterval(final Context context, Editable s) {
        boolean haveChanged = false;
        try {
            int interval = Integer.parseInt(s.toString());
            if (interval > 60) {
                haveChanged = true;
                s.clear();
                s.append("60");
            } else if (interval < 0) {
                haveChanged = true;
                s.clear();
                s.append("0");
            }
        } catch (NumberFormatException ignored) {

        }

        if (haveChanged) {
            if (System.currentTimeMillis() - lastChangeTime > FIVE_SECOND) {
                Toast.makeText(context, "时间间隔必须介于0-60之间", Toast.LENGTH_SHORT).show();
                lastChangeTime = System.currentTimeMillis();
            }
        }
    }

    public int isEditMode(SimpleAdapter adapter) {
        if (adapter.getBoolean(AdapterConfigKey.IS_EDIT_MODE)) {
            return View.VISIBLE;
        } else {
            return View.INVISIBLE;
        }
    }
}
