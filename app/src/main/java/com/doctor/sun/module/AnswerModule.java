package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Answer;
import com.doctor.sun.ui.handler.QCategoryHandler;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by rick on 11/24/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/question#%E8%8E%B7%E5%8F%96%E9%97%AE%E5%8D%B7
 * 用户问题模块API
 */
public interface AnswerModule {

    @GET("question/questionnaires/{appointmentId}")
    Call<ApiDTO<List<Answer>>> answers(@Path("appointmentId") int appointmentId);

    @FormUrlEncoded
    @POST("question/questionnaires/{appointmentId}")
    Call<ApiDTO<List<Answer>>> saveAnswers(
            @Path("appointmentId") int appointmentId,
            @Field("answer") String answer);


//    保存问卷答案
//
//    接口地址：http://域名/question/questionnaires/{预约单ID}

    @GET("question/questionnaires-scale/{appointmentId}/")
    Call<ApiDTO<List<QCategoryHandler>>> category(@Path("appointmentId") int appointmentId);

    @GET("question/questionnaires-scale/{appointmentId}/{categoryId}/show")
    Call<ApiDTO<List<Answer>>> categoryDetail(
            @Path("appointmentId") int appointmentId
            , @Path("categoryId") String categoryId);
}
