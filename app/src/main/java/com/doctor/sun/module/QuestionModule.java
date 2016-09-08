package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.dto.QuestionDTO;
import com.doctor.sun.entity.QTemplate;
import com.doctor.sun.entity.QTemplate2;
import com.doctor.sun.entity.Question;
import com.doctor.sun.entity.QuestionCategory;
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
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by rick on 11/26/15.
 * 医生问题模块API
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/question#%E5%8C%BB%E7%94%9F%E9%97%AE%E9%A2%98%E6%A8%A1%E5%9D%97api
 */
public interface QuestionModule {

    /**
     * 获取问题库列表
     *
     * @param appointmentId
     * @return
     */
    @GET("question/library/{id}")
    Call<ApiDTO<PageDTO<Question>>> library(@Path("id") String appointmentId);

    /**
     * 获取医生自编问题列表
     *
     * @param appointmentId
     * @return
     */
    @GET("question/customs/{id}")
    Call<ApiDTO<PageDTO<Question>>> customs(@Path("id") String appointmentId);

    /**
     * 添加医生自编题
     *
     * @param questionContent
     * @param questionType
     * @return
     */
    @FormUrlEncoded
    @POST("question/custom/")
    Call<ApiDTO<PageDTO<Question>>> addQuestion(@Field("question_content") String questionContent
            , @Field("question_type") String questionType
            , @FieldMap() Map<String, String> options);

    /**
     * 获取医生模板列表
     *
     * @return
     */
    @GET("question/templates/")
    Call<ApiDTO<PageDTO<QTemplate>>> templates();

    /**
     * 添加医生模板
     *
     * @param templateName
     * @param questionsId
     * @return
     */
    @FormUrlEncoded
    @POST("question/template/")
    Call<ApiDTO<QTemplate>> addTemplate(@Field("template_name") String templateName, @Field("questions_id") ArrayList<String> questionsId);

    /**
     * 修改医生模板
     *
     * @param templateName
     * @param questionsId
     * @return
     */
    @FormUrlEncoded
    @POST("question/template/{template_id}")
    Call<ApiDTO<QTemplate>> updateTemplate(@Path("template_id") String templateId, @Field("template_name") String templateName, @Field("questions_id[]") ArrayList<String> questionsId);


    /**
     * 取消默认模板删除医生模板
     *
     * @param templateId
     * @return
     */
    @GET("question/template/{template_id}/default")
    Call<ApiDTO<QTemplate>> setDefaultTemplate(@Path("template_id") String templateId);

    /**
     * 设置默认模板
     *
     * @param templateId
     * @return
     */
    @GET("question/template/{template_id}/nodefault")
    Call<ApiDTO<QTemplate>> setNoDefaultTemplate(@Path("template_id") String templateId);

    /**
     * 删除医生模板
     *
     * @param templateId
     * @return
     */
    @GET("question/template/{template_id}/delete")
    Call<ApiDTO<String>> deleteTemplate(@Path("template_id") String templateId);

    /**
     * 获取模板详情
     *
     * @param templateId
     * @return
     */
    @GET("question/template/{template_id}")
    Call<ApiDTO<QTemplate>> getTemplate(@Path("template_id") String templateId);

//    /**
//     * 保存问卷需要重填的题目
//     *
//     * @param appointmentId
//     * @param need_refill
//     * @return
//     */
//    @FormUrlEncoded
//    @POST("question/refill/{id}")
//    Call<ApiDTO<List<>>> refill(@Path("id") String appointmentId, @Field("need_refill[]") ArrayList<String> need_refill);

    /**
     * 追加模板问题到问卷
     *
     * @param appointmentId
     * @param template_id
     * @return
     */
    @GET("question/append/{id}/template/{template_id}")
    Call<ApiDTO<List<String>>> appendTemplate(@Path("id") String appointmentId, @Path("template_id") String template_id);

//    /**
//     * 追加问题到问卷
//     *
//     * @param appointmentId
//     * @param questionId
//     * @return
//     */
//    @GET("question/append/{id}/question/{questionId}")
//    Call<ApiDTO<List<Answer>>> appendQuestion(@Path("id") String appointmentId, @Path("questionId") String questionId);
//

    /**
     * 追加量表到问卷
     *
     * @param appointmentId
     * @param questionId
     * @return
     */
    @GET("question/append/{id}/scale/{questionId}")
    Call<ApiDTO<List<String>>> appendScale(@Path("id") String appointmentId, @Path("questionId") String questionId);

//    ### 删除问卷里单个问题
//    ##### 接口地址:http://域名/question/append/{问卷ID}/delete/{问题ID}
//            ##### 请求方法:get
//    ##### 请求示例:
//    http://zhaoyang.dev.ganguo.hk:8088/question/append/1/question/1
//            ##### JSON返回示例:

//    @GET("question/append/{id}/delete/{questionId}")
//    Call<ApiDTO<List<Answer>>> deleteQuestion(@Path("id") int appointmentId, @Path("questionId") int questionId);
//

    @GET("question/scale/{id}")
    Call<ApiDTO<List<QuestionCategory>>> scaleCategory(@Path("id") String appointmentId);


    /**
     * @param type          one of smartQuestionnaires, smartScales
     * @param appointmentId
     * @return
     */
    @GET("{type}/{id}")
    Call<ApiDTO<QuestionDTO>> questions2(@Path("type") String type, @Path("id") String appointmentId, @Query("questionnaires_type") String questionnaires_type, @Query("template_type") String template_type);

    @FormUrlEncoded
    @PUT("{type}/{id}")
    Call<ApiDTO<String>> saveQuestions2(@Path("type") String type, @Path("id") String appointmentId, @Field("answer") String answer, @Field("questionnaires_type") String questionnaires_type, @Field("finished") int i);

    @FormUrlEncoded
    @PUT("{type}/{id}")
    Call<ApiDTO<String>> refill2(@Path("type") String type, @Path("id") String appointmentId, @Field("refill_questions[]") ArrayList<String> need_refill);


    @GET("smartTemplate")
    Call<ApiDTO<PageDTO<QTemplate2>>> templates2(@Query("page") String page);

    @GET("smartSupplement")
    Call<ApiDTO<List<HashMap<String, String>>>> templateType();

    @GET("{type}/{id}")
    Call<ApiDTO<List<ScalesResult>>> questionResult(@Path("type") String type, @Path("id") String id);


}
