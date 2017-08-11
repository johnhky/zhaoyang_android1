package com.doctor.sun.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.databinding.FragmentPickDateBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.module.wraper.TimeModuleWrapper;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rick on 18/1/2016.
 */
public class PickDateDialog extends Dialog {
    public static final String TAG = PickDateDialog.class.getSimpleName();
    public static final int ONE_DAY = 86400000;
    private TimeModuleWrapper api = TimeModuleWrapper.getInstance();
    private FragmentPickDateBinding binding;
    private SimpleDateFormat simpleDateFormat;
    private int doctorId = -1;
    private int type;
    public static TimeModule module = Api.of(TimeModule.class);

    public PickDateDialog(Context context, int type) {
        super(context);
        this.doctorId = Settings.getDoctorProfile().getId();
        this.type = type;
        View view = onCreateView(getLayoutInflater(), null, null);
        setContentView(view);
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_pick_date, container, false);
        final Calendar now = Calendar.getInstance();
        final Calendar nextMonth = Calendar.getInstance();
        nextMonth.add(Calendar.MONTH, 5);

        binding.calendarView.init(now.getTime(), nextMonth.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        loadData();
        return binding.getRoot();
    }

    public boolean isContains(Date date) {
        return binding.calendarView.getSelectedDates().contains(date);
    }

    private int getDoctorId() {
        return doctorId;
    }

    private void loadData() {
        Log.e(TAG, "loadData: " + getDoctorId());
        if (type==2){
            List<Date>dates = new ArrayList<>();
            for(int i = 0 ; i < 31; i++){
                Date date = new Date();
                date.setDate(date.getDate()+i);
                dates.add(date);
            }
            try {
                Date minDate = simpleDateFormat.parse(simpleDateFormat.format(dates.get(1).getTime()));
                Date maxDate = simpleDateFormat.parse(simpleDateFormat.format(dates.get(dates.size()-1).getTime()));
                maxDate.setTime(maxDate.getTime()+ONE_DAY);
                binding.calendarView.init(minDate,maxDate).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(dates.size()>0){
                for (int i = dates.size()-1 ; i >=1;i--){
                    try {
                        Date date = simpleDateFormat.parse(simpleDateFormat.format(dates.get(i).getTime())+"");
                        binding.calendarView.selectDate(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }

        }else{
            module.getDate(getDoctorId(), 15, type).enqueue(new Callback<ApiDTO<List<Time>>>() {
                @Override
                public void onResponse(Call<ApiDTO<List<Time>>> call, Response<ApiDTO<List<Time>>> response) {
                    List<Time> reserveDates = response.body().getData();
                    if (reserveDates == null) return;
                    if (reserveDates.size() == 0) {
                        return;
                    }

                    try {
                        Date minDate = simpleDateFormat.parse(reserveDates.get(0).getDate());
                        Date maxDate = simpleDateFormat.parse(reserveDates.get(reserveDates.size() - 1).getDate());
                        maxDate.setTime(maxDate.getTime() + ONE_DAY);
                        binding.calendarView.init(minDate, maxDate)
                                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    for (int i = response.body().getData().size() - 1; i >= 0; i--) {
                        Time reserveDate = reserveDates.get(i);
                        String data = reserveDate.getDate();

                        try {
                            if (isEnable(reserveDate)) {
                                //选择了就是enable了
                                Log.e(TAG, "onResponse: " + data);
                                binding.calendarView.selectDate(simpleDateFormat.parse(data));
                            }
                        } catch (ParseException e) {
                            continue;
                        }
                    }

                }

                @Override
                public void onFailure(Call call, Throwable t) {

                }
            });
        }


    }


    private boolean isEnable(Time reserveDate) {
        if (type == AppointmentType.PREMIUM) {
            return reserveDate.getOptional() == 1;
        } else if (type == AppointmentType.STANDARD) {
            return reserveDate.getOptional() == 1;
        } else if (type == AppointmentType.FACE) {
            return reserveDate.getOptional() == 1;
        }
        return false;
    }

    public void setCellClickInterceptor(CalendarPickerView.CellClickInterceptor cellClickInterceptor) {
        binding.calendarView.setCellClickInterceptor(cellClickInterceptor);
    }
}