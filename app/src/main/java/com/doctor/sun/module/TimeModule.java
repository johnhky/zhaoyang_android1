package com.doctor.sun.module;

import com.doctor.sun.dto.AllDateDTO;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.DateDTO;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.entity.Time;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by lucas on 12/1/15.
 */
public interface TimeModule {

    @FormUrlEncoded
    @POST("time/settime")
    Call<ApiDTO<Time>> setTime(@Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to);

    @FormUrlEncoded
    @POST("time/gettime")
    Call< DateDTO> getDontDisturbTime(@Field("type") int type);

    @POST("time/gettime")
    Call<ApiDTO<List<Time>>> getAllTime();

    @FormUrlEncoded
    @POST("time/update-time")
    Call<ApiDTO<Time>> updateTime(@Field("id") int id, @Field("week") int week, @Field("type") int type, @Field("from") String from, @Field("to") String to);

    @FormUrlEncoded
    @POST("time/deltime")
    Call<ApiDTO<String>> deleteTime(@Field("id") int id);

    //    @FormUrlEncoded
//    @POST("time/getdatalist")
//    Call<> getDate(@Field("doctorId")int doctorId,@Field("data")String data);
//
    @FormUrlEncoded
    @POST("time/getresrvedate")
    Call<ApiDTO<List<ReserveDate>>> getResrveDate(@Field("doctorId") int doctorId, @Field("is_referral") String is_referral);


    @FormUrlEncoded
    @POST("time/getdatalist")
    Call<DateDTO> reserveTime(@Field("doctorId") int doctorId, @Field("data") String data);

    @GET("time/day-schedule")
    Call<ApiDTO<List<Time>>> getDaySchedule(@Query("doctorId") int doctorId, @Query("date") String date , @Query("type") String type, @Query("takeTime") String takeTime);

    @GET("time/date-schedule")
    Call<ApiDTO<List<ReserveDate>>> getDateSchedule(@Query("doctorId") int doctorId, @Query("takeTime") int takeTime);

}
