package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Token;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by rick on 11/17/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/auth
 * 登录注册模块API
 */
public interface AuthModule {
    int PATIENT_TYPE = 1;
    int DOCTOR_TYPE = 2;
    int DOCTOR_PASSED = 4;
    int FORGOT_PASSWORD = 16;

    @FormUrlEncoded
    @POST("auth/register")
    Call<ApiDTO<Token>> register(@Field("type") int type, @Field("phone") String phone,
                                 @Field("captcha") String captcha, @Field("password") String password);


    @FormUrlEncoded
    @POST("auth/register")
    Call<ApiDTO<Token>> register(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("auth/captcha")
    Call<ApiDTO<String>> sendCaptcha(@Field("phone") String phone);


    /**
     * @param phone
     * @param type  change_phone or register
     * @return
     */
    @FormUrlEncoded
    @POST("auth/captcha")
    Call<ApiDTO<String>> sendCaptcha(@Field("phone") String phone, @Field("type") String type);


    @FormUrlEncoded
    @POST("profile/new-phone")
    Call<ApiDTO<String>> changePhone(@Field("captcha") String captcha, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("auth/login")
    Call<ApiDTO<Token>> login(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("auth/login")
    Call<ApiDTO<Token>> login(@Field("phone") String phone,
                              @Field("password") String password,
                              @Field("clientModel") String clientModel,
                              @Field("clientVersion") String clientVersion,
                              @Field("installVersion") String installVersion);

    @FormUrlEncoded
    @POST("auth/reset")
    Call<ApiDTO<String>> reset(@Field("phone") String phone,
                               @Field("password") String password, @Field("captcha") String captcha);

    @FormUrlEncoded
    @POST("auth/reset")
    Call<ApiDTO<String>> reset(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("auth/logout")
    Call<ApiDTO<String>> logout(@Field("token") String token);

}
