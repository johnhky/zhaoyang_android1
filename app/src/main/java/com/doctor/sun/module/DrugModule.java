package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.ImAccount;
import com.doctor.sun.entity.NeedSendDrug;
import com.doctor.sun.http.Api;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.immutables.Drug;
import com.doctor.sun.immutables.DrugOrderDetail;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.PrescriptionOrder;

import java.util.List;

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

    @GET("drug/prescription")
    Call<ApiDTO<List<Prescription>>> getPrescription(@Query("appointmentId") String appointment);

    @GET("drug/appointment-list")
    Call<ApiDTO<PageDTO<Appointment>>> appointments(@Query("page") String page);

    @GET("drug/my-prescriptions")
    Call<ApiDTO<PageDTO<PrescriptionOrder>>> myPrescriptions(@Query("page") String page);

    @GET("drug/order-list")
    Call<ApiDTO<PageDTO<Drug>>> orderList(@Query("page") String page,@Query("status") String status, @Query("keyword") String keyword);

    @GET("drug/order-detail")
    Call<ApiDTO<DrugOrderDetail>> drugDetail(@Query("drugOrderId") String drugOrderId);

    @FormUrlEncoded
    @POST("drug/cancel")
    Call<ApiDTO<String>> cancelOrder(@Field("orderId") String orderId);

    @FormUrlEncoded
    @POST("drug/push-drug")
    Call<ApiDTO<String>> pushDrug(@Field("appointmentId") String appointmentId);

    @GET("drug/admin-im-account")
    Call<ApiDTO<ImAccount>> serverAccount();

    @GET("drug/need-send-drug")
    Call<ApiDTO<NeedSendDrug>> needSendDrug(@Query("appointmentId") int appointmentId);

    @GET("drug/record-last")
    Call<ApiDTO<List<Prescription>>> recordLast(@Query("recordId") String recordId);

    //获取所有诊断名称
    @GET("list/drag-info")
    Call<ApiDTO<List<String>>> getRecordList(@Query("keyWord") String keyword);
}
