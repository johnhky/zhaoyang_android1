package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.DoctorIndex;
import com.doctor.sun.entity.Fee;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.Patient;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rick on 11/17/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/profile
 */
public interface ProfileModule {

    @FormUrlEncoded
    @POST("profile/patient-base")
    Call<ApiDTO<Patient>> editPatientInfo(@Field("name") String name, @Field("email") String email, @Field("birthday") String birthday, @Field("gender") int gender, @Field("avatar") String avatar);

    @GET("profile/patient-base")
    Call<ApiDTO<PatientDTO>> patientProfile();

    @FormUrlEncoded
    @POST("profile/patient-base")
    Call<ApiDTO<String>> editPatientProfile(@FieldMap Map<String, String> patientInfo);


    /**
     * @param medicalRecord province	    string	是	省
     *                      city	        string	是	市
     *                      address	        string	否	详细地址
     *                      identityNumber	string	否	身份证号
     * @return
     */
    @FormUrlEncoded
//    @POST("profile/setSelfMedicalRecord")
    @POST("profile/self-medical-record")
    Call<ApiDTO<String>> setSelfMedicalRecord(@FieldMap Map<String, String> medicalRecord);

    /**
     * @param medicalRecord relation    	string	是	关系
     *                      name        	string	是	姓名
     *                      birthday    	string	是	出生年月
     *                      gender  	    int	    是	1男 2女
     *                      province	    string	是	省
     *                      city        	string	是	市
     *                      address     	string	否	详细地址
     *                      identityNumber	string	否	身份证号
     * @return
     */
    @FormUrlEncoded
    @POST("profile/other-medical-record")
    Call<ApiDTO<String>> setRelativeMedicalRecord(@FieldMap Map<String, String> medicalRecord);

    //    @GET("profile/medicalRecordList")
    @GET("profile/medical-records")
    Call<ApiDTO<List<MedicalRecord>>> medicalRecordList();

    @FormUrlEncoded
    @POST("profile/setPatientFeedback")
    Call<ApiDTO<String>> setPatientFeedback(@Field("feedback") String feedback);

    @GET("profile/histories")
    Call<ApiDTO<PageDTO<Appointment>>> histories(@Query("page") int page);

    @GET("profile/record-detail")
    Call<ApiDTO<MedicalRecord>> recordDetail(@Query("recordId") String recordId);

    @FormUrlEncoded
    @POST("profile/setDoctorFeedback")
    Call<ApiDTO<String>> setDoctorFeedback(@Field("feedback") String feedback);

    @GET("profile/money")
    Call<ApiDTO<Fee>> fee();

    @FormUrlEncoded
    @POST("profile/money")
    Call<ApiDTO<String>> setFee(@Field("money") String money, @Field("secondMoney") String secondMoney, @Field("commission") String commission);

    @GET("profile/doctor-base")
    Call<ApiDTO<Doctor>> doctorProfile();

    @GET("profile/doctor-index")
    Call<ApiDTO<DoctorIndex>> doctorIndex();

    @FormUrlEncoded
    @POST("profile/doctor-base")
    Call<ApiDTO<String>> editDoctorProfile(@FieldMap Map<String, String> doctor);


    @FormUrlEncoded
    @POST("profile/new-password")
    Call<ApiDTO<String>> resetPassword(@Field("password") String password, @Field("newPassword") String newPassword, @Field("confirmPassword") String confirmPassword);


    @GET("profile/comments")
    Call<ApiDTO<PageDTO<Comment>>> comments(@Query("doctorId") int doctorId, @Query("page") String page);

    @GET("profile/articles")
    Call<ApiDTO<PageDTO<Article>>> articles(@Query("doctorId") int doctorId, @Query("page") String page);

    @GET("profile/coupons")
    Call<ApiDTO<List<Coupon>>> coupons(@Query("type") String type);

    @GET("profile/qrcode")
    Call<ApiDTO<String>> barcode();

    @GET("im/doctor-contact")
    Call<ApiDTO<Doctor>> doctorContact(@Query("doctorId") int doctorId);
}
