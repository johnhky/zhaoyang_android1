package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;

import java.util.HashMap;

import retrofit.Call;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by rick on 12/23/15.
 */
public interface DiagnosisModule {
    @FormUrlEncoded
    @POST("diagnosis/set-diagnosis")
    Call<ApiDTO<String>> setDiagnosis(@FieldMap HashMap<String, String> query);


    @GET("diagnosis/diagnosis-info")
    Call<ApiDTO<DiagnosisInfo>> diagnosisInfo(@Query("appointmentId") int appointmentId);

    @GET("diagnosis/search-doctors")
    Call<ApiDTO<PageDTO<Doctor>>> searchDoctor(@Query("page") String page, @Query("search") String search);
}
