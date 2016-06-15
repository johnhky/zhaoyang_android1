package com.doctor.sun.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.doctor.sun.dto.AfterServiceDTO;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.AfterService;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.entity.ContactDetail;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Patient;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
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
     * @param id   病历id
     * @param page
     * @return
     */
    @GET("follow-up/record-histories")
    Call<ApiDTO<PageDTO<AfterService>>> histories(@AfterService.Status @Nullable @Query("recordId") int id, @Query("page") String page);


    @GET("tool/doctorInfo/{doctorId}")
    Call<ApiDTO<ContactDetail>> doctorInfo(@Path("doctorId") int doctorId, @Nullable @Query("append") String type);

    /**
     * @param id 病历的一组id，类似["1","2"]
     * @return
     */
    @FormUrlEncoded
    @POST("follow-up/apply-auth")
    Call<ApiDTO<Void>> allow(@NonNull @Field("doctorId") int id, @Field("followUpAuth") int allow, @Field("recordId") int recordId);

    @GET("follow-up/follow-up-question")
    Call<ApiDTO<AfterServiceDTO>> questions(@NonNull @Query("follow_order_id") String id, @Query("user_type") String userType);

    @FormUrlEncoded
    @POST("follow-up/follow-up-question")
    Call<ApiDTO<String>> saveAnswer(@NonNull @Field("follow_order_id") String id, @Field("answer") String answer, @Field("finished") int isFinished);


    //TODO:
        @GET("follow-up/doctor-list")
//    @GET("im/pContactList")
    Call<ApiDTO<List<Doctor>>> doctorList();

    //TODO:
        @GET("follow-up/patient-list")
//    @GET("im/doctor-contact-list")
    Call<ApiDTO<List<Patient>>> patientList();

    @GET("follow-up/doctor-feedback")
    Call<ApiDTO<List<Answer>>> feedback(@NonNull @Query("follow_order_id") String id);

    @FormUrlEncoded
    @POST("profile/updateMedicalRecord")
    Call<ApiDTO<String>> updateAddress(@Field("address") String address, @Field("medicalRecordId") int id);


}
