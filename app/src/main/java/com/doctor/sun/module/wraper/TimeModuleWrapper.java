package com.doctor.sun.module.wraper;

import android.util.Log;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.Settings;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.AllTime;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.DayModule;
import com.doctor.sun.module.TimeModule;
import com.doctor.sun.vm.ItemSwitch;
import com.doctor.sun.vm.LayoutId;
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
    private Description detailDescription = new Description(R.layout.item_description, "VIP网诊");
    private Description netDescription = new Description(R.layout.item_description, "诊所面诊" + "\n" + Settings.getDoctorProfile().getClinicAddress().getAddress());

    private TimeModule time = Api.of(TimeModule.class);
    private DayModule day = Api.of(DayModule.class);
    boolean isShow  = false;
    private static TimeModuleWrapper instance;

    public static TimeModuleWrapper getInstance() {
        if (instance == null) {
            instance = new TimeModuleWrapper();
        }
        return instance;
    }

    public Call<ApiDTO<List<Time>>> getTime(int type) {
        if (type == AppointmentType.STANDARD) {
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
        Response<ApiDTO<AllTime>> surface = null;
        Response<ApiDTO<AllTime>> simple = null;
        try {
            detail = time.getTime(1).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            simple = time.getNewTime().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            surface = time.getNewTime().execute();
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
        if (surface != null && surface.isSuccessful()) {
            List<Time> body = surface.body().getData().surface;
            if (body != null && body!= null) {
                if (!body.isEmpty()) {
                    result.add(netDescription);
                    result.addAll(body);
                }
            }
        }
        final ItemSwitch itemSwitch = new ItemSwitch(R.layout.item_switch_simple);

        if (simple != null && simple.isSuccessful()) {
                List<Time> body = simple.body().getData().simple;
            if (body != null && body != null) {
                if (!body.isEmpty()) {
                    itemSwitch.setChecked(true);
                    itemSwitch.setContent("简易复诊(开启)");
                    isShow = true;
                } else {
                    itemSwitch.setChecked(false);
                    itemSwitch.setContent("简易复诊(关闭)");
                    isShow = false;
                }
            }
        }
        itemSwitch.setListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                time.setSimple().enqueue(new SimpleCallback<Time>() {
                    @Override
                    protected void handleResponse(Time response) {
                        if (isShow==false){
                            itemSwitch.setChecked(true);
                            itemSwitch.setContent("简易复诊(开启)");
                            Toast.makeText(v.getContext(), "简易复诊已开启", Toast.LENGTH_SHORT).show();
                            isShow = true;
                        }else{
                            itemSwitch.setChecked(false);
                            itemSwitch.setContent("简易复诊(关闭)");
                            Toast.makeText(v.getContext(), "简易复诊已关闭", Toast.LENGTH_SHORT).show();
                            isShow = false;
                        }
                    }
                });
            }
        });
        if (Settings.getDoctorProfile().getSpecialistCateg() == 0) {
            result.add(itemSwitch);
        }
        return result;
    }

    public Call<ApiDTO<Time>> newUpdateTime(@Field("drTimeId") int id, @Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, int interval) {
        return time.newUpdateTime(id, week, type, from, to, interval);
    }

    public Call<ApiDTO<Time>> newSetTime(@Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, int interval) {
        return time.newSetTime(week, type, from, to, interval);
    }

    public Call<ApiDTO<Time>> setTime(@Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, int interval) {
        if (type == AppointmentType.STANDARD) {
            return day.setTime(week);
        } else {
            return time.setTime(week, type, from, to, interval);
        }
    }


    public Call<ApiDTO<Time>> updateTime(@Field("id") int id, @Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, int interval) {
        if (type == AppointmentType.STANDARD) {
            return day.updateTime(id, week);
        } else {
            return time.updateTime(id, week, type, from, to, interval);
        }
    }

    public Call<ApiDTO<String>> deleteTime(@Field("id") int id, int type) {
        if (type == AppointmentType.STANDARD) {
            return day.deleteTime(id);
        } else {
            return time.deleteTime(id);
        }
    }

    public Call<ApiDTO<List<ReserveDate>>> getDateSchedule(@Query("doctorId") int doctorId, @Query("takeTime") int takeTime, int type) {
        if (type == AppointmentType.STANDARD) {
            return day.getDateSchedule(doctorId);
        } else {
            return time.getDateSchedule(doctorId, takeTime);
        }
    }
}
