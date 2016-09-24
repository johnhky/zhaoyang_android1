package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Prescription;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

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

    @GET("diagnosis/last-drug")
    Call<ApiDTO<List<Prescription>>> lastDrug(@Query("appointmentId") int appointmentId);

    @GET("diagnosis/patient-drug")
    Call<ApiDTO<List<Prescription>>> patientDrug(@Query("appointmentId") int appointmentId);


    /**
     * @param path path
     *             <p>
     *             drug/record-last?recordId=
     *             diagnosis/patient-drug?id=
     *             diagnosis/last-drug?id=
     */
    @GET()
    Call<ApiDTO<List<Prescription>>> drugs(@Url() String path);

    /**
     * 病历库
     * @param page
     * @param searchWord
     * @return
     */
    @GET("diagnosis/doctor-orders")
    Call<ApiDTO<PageDTO<Appointment>>> recordPool(@Query("page") String page, @Query("searchWord") String searchWord);
}
