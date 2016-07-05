package com.doctor.sun.module.wraper;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.module.DayModule;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.util.Function0;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.ganguo.library.util.Tasks;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Query;

/**
 * Created by rick on 23/6/2016.
 */
public class TimeModuleWrapper {
    private Description detailDescription = new Description(R.layout.item_description, "专属咨询");
    private Description quickDescription = new Description(R.layout.item_description, "留言咨询(全天)");

    private TimeModule time = Api.of(TimeModule.class);
    private DayModule day = Api.of(DayModule.class);

    private static TimeModuleWrapper instance;

    public static TimeModuleWrapper getInstance() {
        if (instance == null) {
            instance = new TimeModuleWrapper();
        }
        return instance;
    }

    public Call<ApiDTO<List<Time>>> getTime(int type) {
        if (type == AppointmentType.QUICK) {
            return day.getTime(type);
        } else {
            return time.getTime(type);
        }
    }

    public void getAllTimeAsync(final Function0<List<LayoutId>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<LayoutId> allTime = getAllTime();
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.apply(allTime);
                    }
                });
            }
        }).start();
    }

    private List<LayoutId> getAllTime() {
        ArrayList<LayoutId> result = new ArrayList<>();
        Response<ApiDTO<List<Time>>> detail = null;
        Response<ApiDTO<List<Time>>> quick = null;
        try {
            detail = time.getTime(1).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            quick = day.getAllTime().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (detail != null && detail.isSuccessful()) {
            ApiDTO<List<Time>> body = detail.body();
            if (body != null && body.getData() != null) {
                if (!body.getData().isEmpty()) {
                    result.add(detailDescription);
                    result.addAll(body.getData());
                }
            }
        }
        if (quick != null && quick.isSuccessful()) {
            ApiDTO<List<Time>> body = quick.body();
            if (body != null && body.getData() != null) {
                if (!body.getData().isEmpty()) {
                    result.add(quickDescription);
                    result.addAll(body.getData());
                }
            }
        }
        return result;
    }

    public Call<ApiDTO<Time>> setTime(@Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, int interval) {
        if (type == AppointmentType.QUICK) {
            return day.setTime(week);
        } else {
            return time.setTime(week, type, from, to, interval);
        }
    }

    public Call<ApiDTO<Time>> updateTime(@Field("id") int id, @Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, int interval) {
        if (type == AppointmentType.QUICK) {
            return day.updateTime(id, week);
        } else {
            return time.updateTime(id, week, type, from, to,interval);
        }
    }

    public Call<ApiDTO<String>> deleteTime(@Field("id") int id, int type) {
        if (type == AppointmentType.QUICK) {
            return day.deleteTime(id);
        } else {
            return time.deleteTime(id);
        }
    }

//    public Call<ApiDTO<List<Time>>> getDaySchedule(@Query("doctorId") int doctorId, @Query("date") String date, @Query("type") int type, @Query("takeTime") String takeTime) {
//        if (type == AppointmentType.QUICK) {
//            return day.getDaySchedule(doctorId, date, type, takeTime);
//        } else {
//            return time.getDaySchedule(doctorId, date, type, takeTime);
//        }
//    }

    public Call<ApiDTO<List<ReserveDate>>> getDateSchedule(@Query("doctorId") int doctorId, @Query("takeTime") int takeTime, int type) {
        if (type == AppointmentType.QUICK) {
            return day.getDateSchedule(doctorId);
        } else {
            return time.getDateSchedule(doctorId, takeTime);
        }
    }
//
//    public Call<ApiDTO<List<ReserveDate>>> getResrveDate(@Field("doctorId") int doctorId, @Field("is_referral") String is_referral) {
//        return day.getResrveDate(doctorId, is_referral);
//    }
//
//    public Call<ApiDTO<List<Time>>> reserveTime(@Field("doctorId") int doctorId, @Field("data") String data) {
//        return day.reserveTime(doctorId, data);
//    }
}
