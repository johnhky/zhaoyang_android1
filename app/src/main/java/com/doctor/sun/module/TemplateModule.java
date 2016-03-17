package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.MTemplate;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

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
    Call<ApiDTO<PageDTO<MTemplate>>> templates();

    /**
     * 添加医生模板
     *
     * @param templateName
     * @param questionsId
     * @return
     */
    @POST("question/template/")
    Call<ApiDTO<MTemplate>> addTemplate(@Field("template_name") String templateName, @Field("questions_id") String[] questionsId);

    /**
     * 修改医生模板
     *
     * @param templateName
     * @param questionsId
     * @return
     */
    @POST("question/template/")
    Call<ApiDTO<MTemplate>> updateTemplate(@Field("template_name") String templateName, @Field("questions_id") String[] questionsId);


    /**
     * 删除医生模板
     *
     * @param templateId
     * @return
     */
    @POST("question/template/{template_id}/default")
    Call<ApiDTO<MTemplate>> setDefaultTemplate(@Path("template_id") String templateId);

    /**
     * 设置默认模板
     *
     * @param templateId
     * @return
     */
    @POST("question/template/{template_id}/nodefault")
    Call<ApiDTO<MTemplate>> setNoDefaultTemplate(@Path("template_id") String templateId);

    /**
     * 取消默认模板
     *
     * @param templateId
     * @return
     */
    @GET("question/template/{template_id}/delete")
    Call<ApiDTO<String>> deleteTemplate(@Path("template_id") String templateId);
}
