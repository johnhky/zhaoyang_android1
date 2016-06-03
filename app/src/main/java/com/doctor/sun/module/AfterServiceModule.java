package com.doctor.sun.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.MedicalRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rick on 2/6/2016.
 * 随访相关api
 */
public interface AfterServiceModule {

    /**
     * @param type all代表全部 todo代表申请中状态 do代表除申请中状态的;不传默认为all
     * @return 用户随访订单列表
     */
    @GET("follow-up/patient-orders")
    Call<ApiDTO<PageDTO<AfterService>>> patientOrders(@AfterService.Status @Nullable @Query("followUpType") String type, @Query("page") String page);

    /**
     * @param id     随访id
     * @param action 操作类型 'accept'接受;'reject'拒绝
     * @return
     */
    @FormUrlEncoded
    @POST("follow-up/patient-action")
    Call<ApiDTO<Void>> perform(@NonNull @Field("followUpId") String id, @NonNull @AfterService.Actions @Field("followUpAction") String action);


    /**
     * @param id 病人id
     * @return 病历列表
     */
    @GET("follow-up/records")
    Call<ApiDTO<List<MedicalRecord>>> records(@NonNull @Query("patientId") int id);

    /**
     * @param id 病历的一组id，类似["1","2"]
     * @return
     */
    @FormUrlEncoded
    @POST("follow-up/doctor-request")
    Call<ApiDTO<Void>> requestService(@NonNull @Field("recordIds") String id);

    /**
     * @param type all代表全部 todo代表申请中状态 do代表除申请中状态的;不传默认为all
     * @return 用户随访订单列表
     */
    @GET("follow-up/doctor-orders")
    Call<ApiDTO<PageDTO<AfterService>>> doctorOrders(@AfterService.Status @Nullable @Query("followUpType") String type, @Query("page") String page);

    /**
     *
     * @param id 病历id
     * @param page
     * @return
     */
    @GET("follow-up/record-histories")
    Call<ApiDTO<PageDTO<AfterService>>> histories(@AfterService.Status @Nullable @Query("recordId") int id, @Query("page") String page);

}
