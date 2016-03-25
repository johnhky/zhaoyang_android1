package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.entity.VoipAccount;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by rick on 12/9/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/drug
 */
public interface DrugModule {

    @GET("drug/appointment-list")
    Call<ApiDTO<PageDTO<AppointMent>>> appointments(@Query("page") String page);

    @GET("drug/order-list")
    Call<ApiDTO<PageDTO<Drug>>> orderList(@Query("page") String page);

    @FormUrlEncoded
    @POST("drug/cancel")
    Call<ApiDTO<String>> cancelOrder(@Field("orderId") int orderId);

    @FormUrlEncoded
    @POST("drug/push-drug")
    Call<ApiDTO<String>> pushDrug(@Field("appointmentId") int appointmentId);

    @GET("drug/admin-im-account")
    Call<ApiDTO<VoipAccount>> serverAccount();

    @GET("drug/need-send-drug")
    Call<ApiDTO<NeedSendDrug>> needSendDrug(@Query("appointmentId")int appointmentId);
}
