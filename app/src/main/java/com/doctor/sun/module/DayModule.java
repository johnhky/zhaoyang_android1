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
public interface DayModule {


    @FormUrlEncoded
    @POST("day/gettime")
    Call<ApiDTO<List<Time>>> getTime(@Field("type") int type);

    @POST("day/query-time")
    Call<ApiDTO<List<Time>>> getAllTime();

    @FormUrlEncoded
    @POST("day/set-time")
    Call<ApiDTO<Time>> setTime(@Field("week") int week);

    @FormUrlEncoded
    @POST("day/update-time")
    Call<ApiDTO<Time>> updateTime(@Field("id") int id, @Field("week") int week);

    @FormUrlEncoded
    @POST("day/del-time")
    Call<ApiDTO<String>> deleteTime(@Field("id") int id);

    @GET("day/month-schedule")
    Call<ApiDTO<List<ReserveDate>>> getDateSchedule(@Query("doctorId") int doctorId);

}
