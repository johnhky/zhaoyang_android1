package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Avatar;
import com.doctor.sun.entity.Contact;
import com.doctor.sun.entity.ContactDetail;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.PatientMoney;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rick on 12/9/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/im
 */
public interface ImModule {

    @GET("im/doctor-contact-list")
    Call<ApiDTO<List<Contact>>> doctorContactList();

    @GET("im/patient-contact")
    Call<ApiDTO<ContactDetail>> patientContact(@Query("patientId") int patientId, @Query("recordId") int recordId);

    @GET("im/pContactList")
    Call<ApiDTO<List<Contact>>> pContactList();

    @GET("im/doctor-contact")
    Call<ApiDTO<ContactDetail>> doctorContact(@Query("doctorId") int doctorId);

    @GET("im/records")
    Call<ApiDTO<List<MedicalRecord>>> record(@Query("patientId") int patientId);

    @GET("im/records")
    Call<ApiDTO<List<MedicalRecord>>> recordDoctor(@Query("doctorId") int doctorId);

    @FormUrlEncoded
    @POST("im/patient-ban-status")
    Call<ApiDTO<HashMap<String, String>>> patientBan(@Field("patientId") String patientId, @Field("status") String status);

    @FormUrlEncoded
    @POST("im/patient-call-status")
    Call<ApiDTO<HashMap<String, String>>> patientCall(@Field("patientId") String patientId, @Field("status") String status);

    @GET("im/money")
    Call<ApiDTO<PatientMoney>> money();

    @FormUrlEncoded
    @POST("im/patient-nickname")
    Call<ApiDTO<HashMap<String, String>>> patientNickname(@Field("patientId") String patientId, @Field("nickname") String nickname);

    @FormUrlEncoded
    @POST("im/doctor-nickname")
    Call<ApiDTO<HashMap<String, String>>> doctorNickname(@Field("doctorId") int doctorId, @Field("nickname") String nickname);

    @FormUrlEncoded
    @POST("im/doctor-ban-status")
    Call<ApiDTO<HashMap<String, String>>> doctorBan(@Field("doctorId") String doctorId, @Field("status") String status);

    @FormUrlEncoded
    @POST("im/doctor-call-status")
    Call<ApiDTO<HashMap<String, String>>> doctorCall(@Field("doctorId") String doctorId, @Field("status") String status);

    @GET("im/avatar")
    Call<ApiDTO<Avatar>> avatar(@Query("voipAccount") String voipAccount, @Query("phone") String phone);

    @FormUrlEncoded
    @POST("im/phone-call")
    Call<ApiDTO<String>> makeYunXinPhoneCall(@Field("appointmentId") String appointmentId);
}
