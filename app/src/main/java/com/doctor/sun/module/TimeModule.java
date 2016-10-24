package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.entity.Time;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by lucas on 12/1/15.
 */
public interface TimeModule {


    @GET("time/time")
    Call<ApiDTO<List<Time>>> getTime(@Query("type") int type);

    @FormUrlEncoded
    @POST("time/settime")
    Call<ApiDTO<Time>> setTime(@Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, @Field("interval") int interval);

    @FormUrlEncoded
    @POST("time/update-time")
    Call<ApiDTO<Time>> updateTime(@Field("id") int id, @Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to, @Field("interval") int interval);

    @FormUrlEncoded
    @POST("time/deltime")
    Call<ApiDTO<String>> deleteTime(@Field("id") int id);

    @GET("time/day-schedule")
    Call<ApiDTO<List<Time>>> getDaySchedule(@Query("doctorId") int doctorId, @Query("date") String date, @Query("type") int type, @Query("takeTime") String takeTime);

    @GET("time/date-schedule")
    Call<ApiDTO<List<ReserveDate>>> getDateSchedule(@Query("doctorId") int doctorId, @Query("takeTime") int takeTime);

    @Deprecated
    @GET("013/time/latest-avaliable-time")
    Call<ApiDTO<Time>> latestAvailableTime(@Query("doctorId") int doctorId, @Query("takeTime") int data, @Query("date") String date);

}
