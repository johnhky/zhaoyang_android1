package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Drug;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.immutables.Appointment;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rick on 12/9/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/drug
 */
public interface DrugModule {

    @GET("drug/appointment-list")
    Call<ApiDTO<PageDTO<Appointment>>> appointments(@Query("page") String page);

    @GET("drug/order-list")
    Call<ApiDTO<PageDTO<Drug>>> orderList(@Query("page") String page);

    @GET("drug/order-detail")
    Call<ApiDTO<Drug>> drugDetail(@Query("drugOrderId") String drugOrderId);

    @FormUrlEncoded
    @POST("drug/cancel")
    Call<ApiDTO<String>> cancelOrder(@Field("orderId") int orderId);

    @FormUrlEncoded
    @POST("drug/push-drug")
    Call<ApiDTO<String>> pushDrug(@Field("appointmentId") String appointmentId);

    @GET("drug/admin-im-account")
    Call<ApiDTO<ImAccount>> serverAccount();

    @GET("drug/need-send-drug")
    Call<ApiDTO<NeedSendDrug>> needSendDrug(@Query("appointmentId") int appointmentId);


}
