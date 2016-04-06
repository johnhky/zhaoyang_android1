package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.DoctorPageDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.dto.WeChatPayDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.EmergencyCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by rick on 11/20/15.
 */
public interface AppointmentModule {

    /**
     * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/appointment#%E8%8E%B7%E5%8F%96%E6%89%80%E6%9C%89%E5%8C%BB%E7%94%9F%E5%88%97%E8%A1%A8
     *
     * @param query
     * @param titleParam
     * @return
     */
    @GET("appointment/allDoctor/")
    Call<ApiDTO<DoctorPageDTO<Doctor>>> doctors(@Query("page") String page, @QueryMap HashMap<String, String> query, @Query("title[]") ArrayList<Integer> titleParam);

    @GET("appointment/recent-doctors")
    Call<ApiDTO<List<Doctor>>> recentDoctors(@Query("page") String page, @QueryMap HashMap<String, String> query, @Query("title[]") ArrayList<Integer> titleParam);

    @FormUrlEncoded
    @POST("appointment/collect")
    Call<ApiDTO<String>> likeDoctor(@Field("doctorId") String doctorId);

    @FormUrlEncoded
    @POST("appointment/un-collect")
    Call<ApiDTO<String>> unlikeDoctor(@Field("doctorId") String doctorId);

    @GET("appointment/collectionList")
    Call<ApiDTO<PageDTO<Doctor>>> favoriteDoctors();

    @FormUrlEncoded
    @POST("appointment/appointment")
    Call<ApiDTO<Appointment>> orderAppointment(@Field("doctorId") String doctorId, @Field("bookTime") String bookTime, @Appointment.Type @Field("type") int type, @Field("recordId") String recordId);

    @FormUrlEncoded
    @POST("appointment/appointment")
    Call<ApiDTO<Appointment>> orderAppointment(@Field("doctorId") String doctorId, @Field("bookTime") String bookTime, @Appointment.Type @Field("type") int type, @Field("recordId") String recordId, @Field("takeTime") String taketime);

    @GET("appointment/pAppointList")
    Call<ApiDTO<PageDTO<Appointment>>> pAppointments(@Query("page") String page);

    @GET("appointment/pAppointList")
    Call<ApiDTO<PageDTO<Appointment>>> pAppointments(@Query("page") String page, @Query("type") int type);

    @FormUrlEncoded
    @POST("appointment/patient-cancel")
    Call<ApiDTO<String>> pCancel(@Field("appointmentId") String appointmentId);

    @GET("appointment/dAppointList")
    Call<ApiDTO<PageDTO<Appointment>>> dAppointments(@Query("page") String page, @Query("paid") String paid);

    @FormUrlEncoded
    @POST("appointment/doctor-cancel")
    Call<ApiDTO<String>> dCancel(@Field("appointmentId") String appointmentId, @Field("reason") String reason);

    @FormUrlEncoded
    @POST("appointment/remind-answer")
    Call<ApiDTO<String>> remind(@Field("appointmentId") String appointmentId, @Field("patientId") String patientId);


    @FormUrlEncoded
    @POST("appointment/doing")
    Call<ApiDTO<String>> startConsulting(@Field("appointmentId") int appointmentId);


    @GET("diagnosis/return-list")
    Call<ApiDTO<PageDTO<Appointment>>> consultations(@Query("page") String page);

    @FormUrlEncoded
    @POST("diagnosis/accept-return")
    Call<ApiDTO<String>> acceptConsultation(@FieldMap Map<String, String> consultation);

    /**
     * @param id 会诊列表里的会诊ID returnListId
     * @return
     */
    @FormUrlEncoded
    @POST("diagnosis/refuse-return")
    Call<ApiDTO<String>> refuseConsultation(@Field("id") String id);

    @GET("appointment/dDoingList")
    Call<ApiDTO<PageDTO<Appointment>>> dDoingList(@Query("page") String page);

    @GET("appointment/dFinishList")
    Call<ApiDTO<PageDTO<Appointment>>> dFinishList(@Query("page") String page);

    @GET("appointment/pDoingList")
    Call<ApiDTO<PageDTO<Appointment>>> pDoingList(@Query("page") String page);

    @GET("appointment/pFinishList")
    Call<ApiDTO<PageDTO<Appointment>>> pFinishList(@Query("page") String page);

    @FormUrlEncoded
    @POST("appointment/evaluate-patient")
    Call<ApiDTO<String>> evaluatePatient(@Field("point") String point, @Field("appointmentId") String appointmentId, @Field("detail") String detail);

    @GET("urgent/doctor-list")
    Call<ApiDTO<PageDTO<Appointment>>> urgentCalls(@Query("page") String page);

    @GET("urgent/patient-list")
    Call<ApiDTO<PageDTO<EmergencyCall>>> pUrgentCalls(@Query("page") String page);

    @FormUrlEncoded
    @POST("urgent/receive")
    Call<ApiDTO<String>> acceptUrgentCall(@Field("ucId") int ucId);

    @GET("appointment/record-histories")
    Call<ApiDTO<PageDTO<Appointment>>> Patient(@Query("recordId") String recordId);

    @FormUrlEncoded
    @POST("appointment/evaluate-patient")
    Call<ApiDTO<String>> evaluatePatient(@Field("point") String point
            , @Field("appointmentId") int appointmentId
            , @Field("detail") String detail
            , @Field("content") String content);

    @FormUrlEncoded
    @POST("appointment/evaluate-doctor")
    Call<ApiDTO<String>> evaluateDoctor(@Field("point") String point
            , @Field("appointmentId") int appointmentId
            , @Field("detail") String detail
            , @Field("content") String content);

    @FormUrlEncoded
    @POST("appointment/pay")
    Call<ApiDTO<String>> pay(@Field("appointmentId") String appointmentId);


    /**
     * @param type 'alipay'或'wechat'(微信app支付)或'wechat_js'(微信公众号支付)。不传默认值为'alipay'
     * @return
     */
    @FormUrlEncoded
    @POST("pay/info")
    Call<ApiDTO<String>> buildOrder(@Field("appointmentId") int id,
                                    @Field("type") String type);

    /**
     * @param type 'alipay'或'wechat'(微信app支付)或'wechat_js'(微信公众号支付)。不传默认值为'alipay'
     * @return
     */
    @FormUrlEncoded
    @POST("pay/info")
    Call<ApiDTO<WeChatPayDTO>> buildWeChatOrder(@Field("appointmentId") int id,
                                                @Field("type") String type);

    /**
     * @param type 'alipay'或'wechat'(微信app支付)或'wechat_js'(微信公众号支付)。不传默认值为'alipay'
     * @return
     */
    @FormUrlEncoded
    @POST("pay/build-order")
    Call<ApiDTO<String>> rechargeOrderWithAlipay(@Field("totalFee") int totalFee, @Field("body") String body,
                                                 @Field("type") String type);

    @FormUrlEncoded
    @POST("pay/build-order")
    Call<ApiDTO<WeChatPayDTO>> rechargeOrderWithWechat(@Field("totalFee") int totalFee, @Field("body") String body,
                                                       @Field("type") String type);

    /**
     * @param type 'alipay'或'wechat'(微信app支付)或'wechat_js'(微信公众号支付)。不传默认值为'alipay'
     * @return
     */
    @FormUrlEncoded
    @POST("pay/build-order")
    Call<ApiDTO<String>> drugOrder(@Field("totalFee") int totalFee, @Field("body") String body,
                                   @Field("type") String type, @Field("drugOrderId") int drugOrderId);

    @FormUrlEncoded
    @POST("pay/build-order")
    Call<ApiDTO<WeChatPayDTO>> drugOrderWithWechat(@Field("totalFee") int totalFee, @Field("body") String body,
                                                   @Field("type") String type, @Field("drugOrderId") int drugOrderId);

    @GET("appointment/doctor-appoint-list")
    Call<ApiDTO<PageDTO<Appointment>>> doctorAppointment(@Query("page") String page, @Query("orderType") String orderType);

    @GET("appointment/patient-appoint-list")
    Call<ApiDTO<PageDTO<Appointment>>> patientAppointment(@Query("page") String page);

    @FormUrlEncoded
    @POST("appointment/appointment-by-tid")
    Call<ApiDTO<PageDTO<Appointment>>> appointmentInTid(@Field("tidArray") String tid, @Field("page") String page);

}
