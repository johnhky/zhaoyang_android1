package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Hospital;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.entity.Version;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.ResponseBody;

import java.util.List;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Url;

/**
 * Created by rick on 11/18/15.
 * 基础API
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/tool
 */
public interface ToolModule {

    @Multipart
    @POST("tool/upload")
    Call<ApiDTO<Photo>> uploadPhoto(@Part("photo\"; filename=\"photo\" ") RequestBody file);

    @GET("tool/recommendDoctors")
    Call<ApiDTO<List<Doctor>>> recommendDoctor();

    @GET("tool/doctorInfo/{doctorId}")
    Call<ApiDTO<Doctor>> doctorInfo(@Path("doctorId") int doctorId);

    @GET("tool/hospital/{hospitalId}")
    Call<ApiDTO<Hospital>> hospitalInfo(@Path("hospitalId") int hospitalId);

    @FormUrlEncoded
    @POST("appointment/collect")
    Call<ApiDTO<String>> likeDoctor(@Field("doctorId") int doctorId);

    @FormUrlEncoded
    @POST("appointment/un-collect")
    Call<ApiDTO<String>> unlikeDoctor(@Field("doctorId") int doctorId);

    @GET("profile/comments")
    Call<ApiDTO<PageDTO<Comment>>> comments(@Query("doctorId") int doctorId, @Query("page") String page);

    @GET("tool/version")
    Call<ApiDTO<Version>> getAppVersion(@Query("client") String client, @Query("version") String version);

    @GET()
    Call<ResponseBody> downloadFile(@Url() String path);
}
