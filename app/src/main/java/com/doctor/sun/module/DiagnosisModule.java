package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalHistory;
import com.doctor.sun.entity.SentDrugRecordInfo;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.SimpleAppointment;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rick on 12/23/15.
 */
public interface DiagnosisModule {
    @FormUrlEncoded
    @POST("diagnosis/set-diagnosis")
    Call<ApiDTO<String>> setDiagnosis(@FieldMap HashMap<String, String> query);

    //判断某个病历是否有过寄药记录
    @GET("diagnosis/sent-drug-status")
    Call<ApiDTO>getSentDrugRecord(@Query("recordId")String recordId);

    //将医生建议生成处方
    @POST("diagnosis/prescription-enable")
    Call<ApiDTO>createRecipe(@Query("appointmentId")String appointmentId,@Query("enable") String id);

    /*获取患者病历记录*/
    @GET("diagnosis/diagnosis-info")
    Call<ApiDTO<DiagnosisInfo>> diagnosisInfo(@Query("appointmentId") String appointmentId);
    /*获取患者病历记录*/
    @GET("diagnosis/diagnosis-info")
    Call<ApiDTO> getDiagnosisInfo(@Query("appointmentId") String appointmentId);

    @GET("diagnosis/search-doctors")
    Call<ApiDTO<PageDTO<Doctor>>> searchDoctor(@Query("page") String page, @Query("search") String search);

    @GET("diagnosis/patient-drug")
    Call<ApiDTO<List<Prescription>>> patientDrug(@Query("appointmentId") String appointmentId);
    //医生所有已完成的就诊记录［病历库］
    @GET("diagnosis/doctor-orders")
    Call<ApiDTO<PageDTO<Appointment>>> recordPool(@Query("page") String page, @Query("searchWord") String searchWord);
    //病历库病历列表
    @GET("diagnosis/doctor-case-records")
    Call<ApiDTO<PageDTO<MedicalHistory>>>CaseLibarayrList(@Query("page") String page, @Query("searchWord") String searchWord);
    //单个病人的病历记录
    @GET("diagnosis/single-record-histories")
    Call<ApiDTO<PageDTO<Appointment>>> singleHistory(@Query("recordId") String recordId);

    @GET("diagnosis/record-histories?type=detail")
    Call<ApiDTO<List<Appointment>>> recordHistory(@Query("recordId") int recordId);

    @GET("diagnosis/record-histories?type=detail")
    Call<ApiDTO<List<Appointment>>> recordHistoryDetail(@Query("recordId") int recordId);

    @GET("diagnosis/record-orders")
    Call<ApiDTO<PageDTO<Appointment>>> recordOrders(@Query("page") int page, @Query("recordId") int recordId, @Query("searchWord") String searchWord);
}
