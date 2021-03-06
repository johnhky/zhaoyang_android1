package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Area;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.Banner;
import com.doctor.sun.entity.BannerDetail;
import com.doctor.sun.entity.CallConfig;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Hospital;
import com.doctor.sun.entity.Photo;
import com.doctor.sun.entity.Version;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.immutables.UploadDrugData;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by rick on 11/18/15.
 * 基础API
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/tool
 */
public interface ToolModule {

    @Multipart
    @POST("tool/upload")
    Call<ApiDTO<Photo>> uploadPhoto(@Part("photo\"; filename=\"photo\" ") RequestBody file);


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


    @GET("tool/version")
    Call<ApiDTO<Version>> getAppVersion(@Query("client") String client, @Query("version") String version);

    @GET("tool/config")
    Call<ApiDTO<CallConfig>> getCallConfig();

    @GET()
    @Streaming
    Call<ResponseBody> downloadFile(@Url() String path);

    @GET
    Call<ApiDTO<List<Area>>> endemicAreaList(@Url String path);

    @GET("tool/homepage-display")
    Call<ApiDTO<List<Banner>>> patientBanner();

    @GET("tool/activity-detail")
    Call<ApiDTO<BannerDetail>> bannerDetail(@Query("activityId") String activityId);

    @GET()
    Call<ApiDTO<List<HashMap<String, String>>>> listOfItems(@Url String path);

    @GET()
    Call<ApiDTO<List<UploadDrugData>>> listOfDrugs(@Url String path);

    @GET("article/article-show")
    Call<ApiDTO<List<Article>>>getArticles();


}
