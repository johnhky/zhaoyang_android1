package com.doctor.sun.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.databinding.FragmentPickDateBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.fragment.DiagnosisFragment;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private TimeModule api = Api.of(TimeModule.class);
    private FragmentPickDateBinding binding;
    private SimpleDateFormat simpleDateFormat;
    private int doctorId = -1;
    private String type;

    public PickDateDialog(Context context, String type) {
        super(context);
        this.doctorId = TokenCallback.getDoctorProfile().getId();
        this.type = type;
        View view = onCreateView(getLayoutInflater(), null, null);
        setContentView(view);
    }


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        binding = FragmentPickDateBinding.inflate(inflater, container, false);
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
        api.getDateSchedule(getDoctorId(), 15).enqueue(new Callback<ApiDTO<List<ReserveDate>>>() {
            @Override
            public void onResponse(Call<ApiDTO<List<ReserveDate>>> call, Response<ApiDTO<List<ReserveDate>>> response) {
                List<ReserveDate> reserveDates = response.body().getData();
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
                for (int i = response.body().getData().size() - 1; i >= 0; i--) {
                    ReserveDate reserveDate = reserveDates.get(i);
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

    private String getType() {
        return type;
    }

    private boolean isEnable(ReserveDate reserveDate) {
        if (type.equals(DiagnosisFragment.TYPE_NET)) {
            return reserveDate.getDetail() == 1;
        } else if (type.equals(DiagnosisFragment.TYPE_FACE)) {
            return reserveDate.getQuick() == 1;
        }
        return false;
    }

    public void setCellClickInterceptor(CalendarPickerView.CellClickInterceptor cellClickInterceptor) {
        binding.calendarView.setCellClickInterceptor(cellClickInterceptor);
    }
}