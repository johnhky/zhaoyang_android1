package com.doctor.sun.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.FragmentPickDateBinding;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.activity.patient.PickTimeActivity;
import com.doctor.sun.ui.pager.PickDatePagerAdapter;
import com.squareup.timessquare.CalendarPickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by rick on 8/1/2016.
 */
public class PickDateFragment extends Fragment {

    public static final String TAG = PickDateFragment.class.getSimpleName();
    public static final int ONE_DAY = 86400000;
    private TimeModule api = Api.of(TimeModule.class);

    private FragmentPickDateBinding binding;
    private SimpleDateFormat simpleDateFormat;
    private Doctor doctor;
    private String type;
    private String recordId;


    public static PickDateFragment newInstance(Doctor doctor, String type) {

        Bundle args = new Bundle();
        args.putString(Constants.POSITION, type);
        args.putParcelable(Constants.PARAM_DOCTOR_ID, doctor);
        args.putString(Constants.PARAM_RECORD_ID, doctor.getRecordId());

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

        type = getType();
        doctor = getDoctor();
        recordId = getRecordId();
        binding.calendarView.init(now.getTime(), nextMonth.getTime())
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        binding.calendarView.setCellClickInterceptor(new CalendarPickerView.CellClickInterceptor() {
            @Override
            public boolean onCellClicked(Date date) {

                if (binding.calendarView.getSelectedDates().contains(date)) {
                    Intent intent = PickTimeActivity.makeIntent(getContext(), doctor, simpleDateFormat.format(date), recordId, getType());
                    startActivity(intent);
                }

                return true;
            }
        });
        loadData();
        return binding.getRoot();
    }

    private String getRecordId() {
        return getArguments().getString(Constants.PARAM_RECORD_ID);
    }

    private int getDoctorId() {
        if (doctor != null) {
            return doctor.getId();
        }
        return -1;
    }

    private int getDuration() {
        // debug : error:doctor.getDuration() == null
        if (doctor.getDuration() == null){
            return 15;//prx －－2016.3.23 －－若为空，返回默认值15分钟
        }else {
            return Integer.parseInt(doctor.getDuration());//点击再次预约报错(原因是：获取到的预约时长为空)
        }
    }

    private void loadData() {
        api.getDateSchedule(getDoctorId(), getDuration()).enqueue(new Callback<ApiDTO<List<ReserveDate>>>() {
            @Override
            public void onResponse(Response<ApiDTO<List<ReserveDate>>> response, Retrofit retrofit) {
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
                            binding.calendarView.selectDate(simpleDateFormat.parse(data));
                        }
                    } catch (ParseException e) {
                        continue;
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private String getType() {
        return getArguments().getString(Constants.POSITION);
    }

    private boolean isEnable(ReserveDate reserveDate) {
        if (type.equals(PickDatePagerAdapter.TYPE_NET)) {
            return reserveDate.getDetail() == 1;
        } else if (type.equals(PickDatePagerAdapter.TYPE_FACE)) {
            return reserveDate.getQuick() == 1;
        }
        return false;
    }

    private Doctor getDoctor() {
        return getArguments().getParcelable(Constants.PARAM_DOCTOR_ID);
    }
}
