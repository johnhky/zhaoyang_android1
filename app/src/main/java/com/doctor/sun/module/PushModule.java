package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.SystemMsg;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rick on 12/9/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/push-message
 */
public interface PushModule {
    @GET("pushMessage/messages")
    Call<ApiDTO<PageDTO<SystemMsg>>> systemMsg(@Query("page") String page);

    @FormUrlEncoded
    @POST("pushMessage/read-mark")
    Call<ApiDTO> markMessageAsRead(@Field("messageIds") String messageIds);
}
