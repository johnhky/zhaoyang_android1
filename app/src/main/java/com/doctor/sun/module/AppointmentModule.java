package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.dto.WeChatPayDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.entity.constans.CommunicationType;
import com.doctor.sun.immutables.Appointment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    Call<ApiDTO<PageDTO<Doctor>>> allDoctors(@Query("page") String page,
                                             @QueryMap HashMap<String, String> query,
                                             @Query("title[]") ArrayList<Integer> titleParam);

    @GET("appointment/recent-doctors")
    Call<ApiDTO<List<Doctor>>> recentDoctors(@Query("page") String page,
                                             @QueryMap HashMap<String, String> query,
                                             @Query("title[]") ArrayList<Integer> titleParam);

    @FormUrlEncoded
    @POST("appointment/un-collect")
    Call<ApiDTO<String>> unlikeDoctor(@Field("doctorId") String doctorId);

    @GET("appointment/collectionList")
    Call<ApiDTO<PageDTO<Doctor>>> favoriteDoctors();


    @FormUrlEncoded
    @POST("appointment/appointment")
    Call<ApiDTO<Appointment>> orderAppointment(@Field("doctorId") int doctorId,
                                               @Field("bookTime") String bookTime,
                                               @AppointmentType @Field("type") int type,
                                               @Field("recordId") String recordId,
                                               @Field("couponId") String couponId,
                                               @Field("select_tags[]") ArrayList<String> tagsId,
                                               @FieldMap HashMap<String, String> otherParam);

    @FormUrlEncoded
    @POST("appointment/patient-cancel")
    Call<ApiDTO<String>> pCancel(@Field("appointmentId") String appointmentId);


    @FormUrlEncoded
    @POST("appointment/doctor-cancel")
    Call<ApiDTO<String>> dCancel(@Field("appointmentId") String appointmentId, @Field("reason") String reason);

    @FormUrlEncoded
    @POST("appointment/remind-answer")
    Call<ApiDTO<String>> remind(@Field("appointmentId") String appointmentId, @Field("patientId") int patientId);


    @FormUrlEncoded
    @POST("appointment/doing")
    Call<ApiDTO<String>> startConsulting(@Field("appointmentId") String appointmentId);


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


    @FormUrlEncoded
    @POST("urgent/receive")
    Call<ApiDTO<String>> acceptUrgentCall(@Field("ucId") int ucId);

    @GET("appointment/record-histories")
    Call<ApiDTO<PageDTO<Appointment>>> Patient(@Query("recordId") String recordId);

    @FormUrlEncoded
    @POST("appointment/evaluate-patient")
    Call<ApiDTO<String>> evaluatePatient(@Field("point") String point
            , @Field("appointmentId") String appointmentId
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
    Call<ApiDTO<String>> buildAliPayOrder(@Field("appointmentId") String id,
                                          @Field("type") String type, @Field("couponId") String couponId);


    /**
     * @param type 'alipay'或'wechat'(微信app支付)或'wechat_js'(微信公众号支付)。不传默认值为'alipay'
     * @return
     */
    @FormUrlEncoded
    @POST("pay/info")
    Call<ApiDTO<WeChatPayDTO>> buildWeChatOrder(@Field("appointmentId") String id,
                                                @Field("type") String type, @Field("couponId") String couponId);


    /**
     * @param type 'alipay'或'wechat'(微信app支付)或'wechat_js'(微信公众号支付)。不传默认值为'alipay'
     * @return
     */
    @FormUrlEncoded
    @POST("pay/build-order")
    Call<ApiDTO<String>> buildAlipayGoodsOrder(@Field("totalFee") double totalFee,
                                               @Field("type") String type,
                                               @FieldMap HashMap<String, String> extraField);

    @FormUrlEncoded
    @POST("pay/build-order")
    Call<ApiDTO<WeChatPayDTO>> buildWeChatGoodsOrder(@Field("totalFee") double totalFee,
                                                     @Field("type") String type,
                                                     @FieldMap HashMap<String, String> extraField);


    @GET("appointment/doctor-appoint-list")
    Call<ApiDTO<PageDTO<Appointment>>> searchAppointment(@Query("page") String page,
                                                         @Query("keyword") String keyword,
                                                         @Query("displayStatus") String displayStatus);

    @GET("appointment/patient-appoint-list")
    Call<ApiDTO<PageDTO<Appointment>>> patientAppointment(@Query("page") String page);

    @FormUrlEncoded
    @POST("im/list-info")
    Call<ApiDTO<PageDTO<Appointment>>> appointmentInTid(@Field("tidArray") String tid,
                                                        @Field("page") String page);


    /**
     * @param appointmentId 预约单id
     * @param type          1表示电话；2表示视频
     * @return
     */
    @GET("appointment/communicate-avaliable")
    Call<ApiDTO<String>> canUse(@CommunicationType @Query("type") long type
            , @Query("appointmentId") int appointmentId);

    /**
     * @param appointmentId 预约单id
     * @param type          1表示电话；2表示视频
     * @return
     */
    @FormUrlEncoded
    @POST("appointment/communicate-unable")
    Call<ApiDTO<String>> rejectCommunication(@CommunicationType @Field("type") int type
            , @Field("appointmentId") int appointmentId);


    @GET("appointment/status?type=detail")
    Call<ApiDTO<Appointment>> appointmentDetail(@Query("appointmentId") String appointmentId);

}
