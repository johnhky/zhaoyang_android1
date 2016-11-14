package com.doctor.sun.module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.ContactDetail;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.immutables.Appointment;

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
    Call<ApiDTO<PageDTO<Appointment>>> patientOrders(@Nullable @Query("followUpType") String type, @Query("page") String page);


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
    Call<ApiDTO<PageDTO<Appointment>>> doctorOrders(@Nullable @Query("followUpType") String type, @Query("keyword") String keyword, @Query("page") String page);

    /**
     * @param id   病历id
     * @param page
     * @return
     */
    @GET("follow-up/record-histories")
    Call<ApiDTO<PageDTO<Appointment>>> histories(@Nullable @Query("recordId") int id, @Query("page") String page);


    @GET("tool/doctorInfo/{doctorId}")
    Call<ApiDTO<ContactDetail>> doctorInfo(@Path("doctorId") int doctorId, @Nullable @Query("append") String type);

    /**
     * @param id 病历的一组id，类似["1","2"]
     * @return
     */
    @FormUrlEncoded
    @POST("follow-up/apply-auth")
    Call<ApiDTO<Void>> allow(@NonNull @Field("doctorId") int id, @Field("followUpAuth") int allow, @Field("recordId") int recordId);


    //TODO:
    @GET("follow-up/doctor-list")
    Call<ApiDTO<List<Doctor>>> doctorList();

    @GET("follow-up/new-doctor-list")
    Call<ApiDTO<PageDTO<Doctor>>> applyingDoctorList(@Query("search") String keyword, @Query("type") String type, @Query("page") String page);

    //TODO:
    @GET("follow-up/patient-list")
    Call<ApiDTO<PageDTO<Patient>>> patientList();

    @GET("follow-up/new-patient-list")
    Call<ApiDTO<PageDTO<Patient>>> newPatientList(@Query("search") String keyword, @Query("type") String type);

    @FormUrlEncoded
    @POST("follow-up/apply-build-relation")
    Call<ApiDTO<String>> applyBuildRelation(@Field("patientId") int patientId);

    @FormUrlEncoded
    @POST("follow-up/agree-apply")
    Call<ApiDTO<String>> acceptBuildRelation(@Field("applyId") String patientId);

}
