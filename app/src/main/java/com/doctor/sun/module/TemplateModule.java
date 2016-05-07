package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.QTemplate;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by lucas on 12/4/15.
 */
public interface TemplateModule {
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
    @POST("question/template/")
    Call<ApiDTO<QTemplate>> addTemplate(@Field("template_name") String templateName, @Field("questions_id") String[] questionsId);

    /**
     * 修改医生模板
     *
     * @param templateName
     * @param questionsId
     * @return
     */
    @POST("question/template/")
    Call<ApiDTO<QTemplate>> updateTemplate(@Field("template_name") String templateName, @Field("questions_id") String[] questionsId);


    /**
     * 删除医生模板
     *
     * @param templateId
     * @return
     */
    @POST("question/template/{template_id}/default")
    Call<ApiDTO<QTemplate>> setDefaultTemplate(@Path("template_id") String templateId);

    /**
     * 设置默认模板
     *
     * @param templateId
     * @return
     */
    @POST("question/template/{template_id}/nodefault")
    Call<ApiDTO<QTemplate>> setNoDefaultTemplate(@Path("template_id") String templateId);

    /**
     * 取消默认模板
     *
     * @param templateId
     * @return
     */
    @GET("question/template/{template_id}/delete")
    Call<ApiDTO<String>> deleteTemplate(@Path("template_id") String templateId);
}
