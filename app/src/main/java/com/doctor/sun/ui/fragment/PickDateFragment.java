package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentPickDateBinding;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.doctor.sun.ui.activity.patient.ApplyAppointmentActivity;
import com.doctor.sun.ui.activity.patient.PickTimeActivity;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by rick on 8/1/2016.
 */
public class PickDateFragment extends BaseFragment {

    public static final String TAG = PickDateFragment.class.getSimpleName();
    public static final int ONE_DAY = 86400000;
    public static final int ONE_HOUR = 3600000;
    private TimeModuleWrapper api = TimeModuleWrapper.getInstance();

    private FragmentPickDateBinding binding;
    private SimpleDateFormat simpleDateFormat;
    private AppointmentBuilder builder;


    public static PickDateFragment newInstance(AppointmentBuilder builder) {

        Bundle args = new Bundle();
        args.putParcelable(Constants.DATA, builder);

        PickDateFragment fragment = new PickDateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        binding = FragmentPickDateBinding.inflate(inflater, container, false);
        final Calendar now = Calendar.getInstance();
        final Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 5);

        builder = getData();
        binding.calendarView.init(now.getTime(), nextMonth.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        binding.calendarView.setCellClickInterceptor(new OnDateClick());
        loadData();
        return binding.getRoot();
    }

    private void pickTime(String bookTime) {
        Time time = new Time();
        time.setDate(bookTime);
        builder.setTime(time);
        Intent intent = PickTimeActivity.makeIntent(getContext(), builder);
        startActivity(intent);
    }

    private void applyAppointment(String bookTime) {
        Time time = new Time();
        time.setDate(bookTime);
        builder.setTime(time);
        Intent intent = ApplyAppointmentActivity.makeIntent(getContext(), builder);
        startActivity(intent);
    }

    private int getDoctorId() {
        if (builder != null) {
            return builder.getDoctor().getId();
        }
        return -1;
    }

    private int getDuration() {
        if (builder != null) {
            return builder.getDuration();
        }
        return -1;
    }

    private void loadData() {
        api.getDateSchedule(getDoctorId(), getDuration(), getType()).enqueue(new ApiCallback<List<ReserveDate>>() {
            @Override
            protected void handleResponse(List<ReserveDate> reserveDates) {
                if (reserveDates == null) return;
                try {
                    Date minDate = simpleDateFormat.parse(reserveDates.get(0).getDate());
                    Date maxDate = simpleDateFormat.parse(reserveDates.get(reserveDates.size() - 1).getDate());
                    maxDate.setTime(maxDate.getTime() + ONE_DAY);

                    binding.calendarView.init(minDate, maxDate)
                            .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = reserveDates.size() - 1; i >= 0; i--) {
                    ReserveDate reserveDate = reserveDates.get(i);
                    String data = reserveDate.getDate();

                    try {
                        if (isEnable(reserveDate)) {
                            //选择了就是enable了
                            binding.calendarView.selectDate(simpleDateFormat.parse(data));
                        }
                    } catch (ParseException ignored) {
                    }
                }
            }
        });
    }

    private int getType() {
        return builder.getType();
    }

    private boolean isEnable(ReserveDate reserveDate) {
        if (builder.getType() == AppointmentType.DETAIL) {
            return reserveDate.getDetail() == 1;
        } else if (builder.getType() == AppointmentType.QUICK) {
            return reserveDate.getQuick() == 1;
        }
        return false;
    }

    private AppointmentBuilder getData() {
        return getArguments().getParcelable(Constants.DATA);
    }

    public long getMillisMidNight() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DAY_OF_MONTH, 1);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTimeInMillis();
    }

    public long getMillisUntilMidNight() {
        return getMillisMidNight() - System.currentTimeMillis();
    }

    public String getHourUntilMidNight() {
        return String.valueOf(getMillisUntilMidNight() / ONE_HOUR);
    }

    public void showDialog(MaterialDialog.SingleButtonCallback callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());
        builder.content(getDialogContent())
                .negativeText("另选合适日期")
                .positiveText("坚持继续预约今天")
                .neutralText("取消")
                .onPositive(callback);
        builder.show();
    }

    private String getDialogContent() {
        return String.format(getResources().getString(R.string.hours_until_midnight), getHourUntilMidNight());
    }

    private class OnDateClick implements CalendarPickerView.CellClickInterceptor {
        @Override
        public boolean onCellClicked(Date date) {

            if (binding.calendarView.getSelectedDates().contains(date)) {
                final String bookDate = simpleDateFormat.format(date);
                switch (builder.getType()) {
                    case AppointmentType.DETAIL: {
                        pickTime(bookDate);
                        break;
                    }
                    case AppointmentType.QUICK: {
                        if (date.getTime() <= getMillisMidNight() - ONE_DAY) {
                            showDialog(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    applyAppointment(bookDate);
                                }
                            });
                        } else {
                            applyAppointment(bookDate);
                        }
                        break;
                    }
                }
            }

            return true;
        }
    }
}
