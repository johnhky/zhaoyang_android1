package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.dto.QuestionDTO;
import com.doctor.sun.entity.QTemplate2;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.Scales;
import com.doctor.sun.entity.ScalesResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by rick on 11/26/15.
 * 医生问题模块API
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/question#%E5%8C%BB%E7%94%9F%E9%97%AE%E9%A2%98%E6%A8%A1%E5%9D%97api
 */
public interface QuestionModule {
    /**
     * @param type          one of smartQuestionnaires, smartScales
     * @param appointmentId
     * @return
     */
    @GET("{type}/{id}")
    Call<ApiDTO<QuestionDTO>> questions2(@Path("type") String type, @Path("id") String appointmentId, @Query("questionnaires_type") String questionnaires_type, @QueryMap HashMap<String,String> params);

    @FormUrlEncoded
    @PUT("{type}/{id}")
    Call<ApiDTO<String>> saveQuestions2(@Path("type") String type, @Path("id") String appointmentId, @Field("answer") String answer, @Field("questionnaires_type") String questionnaires_type, @Field("finished") int i);

    @FormUrlEncoded
    @PUT("{type}/{id}")
    Call<ApiDTO<String>> refill2(@Path("type") String type, @Path("id") String appointmentId, @Field("refill_questions[]") ArrayList<String> need_refill);

    @GET("smartTemplate")
    Call<ApiDTO<PageDTO<QTemplate2>>> myTemplates(@Query("page") String page);

    @GET("smartSupplement")
    Call<ApiDTO<PageDTO<Scales>>> scales(@Query("appoint_id") String id, @Query("supplement_type") String type, @Query("page") String page);

    @GET("smartSupplement?supplement_type=3")
    Call<ApiDTO<PageDTO<Questions2>>> customQuestions(@Query("appoint_id") String appointmentId, @Query("page") String page);

    @GET("smartSupplement?supplement_type=1")
    Call<ApiDTO<PageDTO<Questions2>>> systemQuestions(@Query("appoint_id") String appointmentId, @Query("page") String page);

    @GET("{type}/{id}")
    Call<ApiDTO<List<ScalesResult>>> questionResult(@Path("type") String type, @Path("id") String id);


    @FormUrlEncoded
    @PUT("smartQuestionnaires/{id}")
    Call<ApiDTO<String>> addQuestionToAppointment(@Path("id") String appointmentId, @FieldMap Map<String, String> fieldMap);
}
