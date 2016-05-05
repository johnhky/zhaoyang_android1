package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.WeChatPayDTO;
import com.doctor.sun.entity.EmergencyCall;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by lucas on 1/20/16.
 */
public interface EmergencyModule {
    @FormUrlEncoded
    @POST("urgent/publish")
    Call<ApiDTO<EmergencyCall>> publish(@Field("recordId") String recordId, @Field("title[]") ArrayList<String> title, @Field("city") String city, @Field("gender") int gender, @Field("money") int money, @Field("waitTime") String waitTime);

    @FormUrlEncoded
    @POST("urgent/cancel")
    Call<ApiDTO<HashMap<String, String>>> cancel(@Field("ucId") int ucId);

    @FormUrlEncoded
    @POST("urgent/pay")
    Call<ApiDTO<String>> pay(@Field("ucId") int ucId);

    @FormUrlEncoded
    @POST("urgent/add-money")
    Call<ApiDTO<String>> addMoney(@Field("ucId") int ucId, @Field("money") String money);

    /**
     * @param ucId
     * @param time 增加等待时间(秒)
     * @return
     */
    @FormUrlEncoded
    @POST("urgent/add-time")
    Call<ApiDTO<String>> addTime(@Field("ucId") int ucId, @Field("time") int time);

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
}
