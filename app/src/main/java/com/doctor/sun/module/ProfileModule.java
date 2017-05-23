package com.doctor.sun.module;

import android.test.mock.MockApplication;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.IsChanged;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.dto.PatientDTO;
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.Advice;
import com.doctor.sun.entity.Article;
import com.doctor.sun.entity.Comment;
import com.doctor.sun.entity.Coupon;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.DoctorIndex;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.NewDoctor;
import com.doctor.sun.entity.Patient;

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
import retrofit2.http.Query;

/**
 * Created by rick on 11/17/15.
 * https://gitlab.cngump.com/ganguo_web/zhaoyangyisheng_server/wikis/profile
 */
public interface ProfileModule {

    //获取收货地址列表
    @GET("shippingAddress/list")
    Call<ApiDTO<List<Address>>> getAddressList();

    //添加收货地址
    @FormUrlEncoded
    @POST("shippingAddress/store")
    Call<ApiDTO<Address>> uploadAddress(@FieldMap Map<String, String> map);

    //删除收货地址
    @FormUrlEncoded
    @POST("shippingAddress/destroy")
    Call<ApiDTO<String>> deleteAddress(@Field("shippingAddressId") int id);

    //修改收货地址
    @FormUrlEncoded
    @POST("shippingAddress/update")
    Call<ApiDTO<Address>> updateAddress(@FieldMap Map<String, String> map);

    //修改寄药单地址
    @FormUrlEncoded
    @POST("drug/modify-shipping")
    Call<ApiDTO<Address>> updateOrderAddress(@Field("address") String address, @Field("drugOrderId") String drugOrderId,
                                             @Field("phone") String phone,
                                             @Field("remark") String remark, @Field("to") String to);

    @FormUrlEncoded
    @POST("profile/patient-base")
    Call<ApiDTO<Patient>> editPatientInfo(@Field("name") String name, @Field("email") String email, @Field("birthday") String birthday, @Field("gender") int gender, @Field("avatar") String avatar);

    @FormUrlEncoded
    @POST("profile/patient-base")
    Call<ApiDTO<Patient>> editPatientInfo(@FieldMap Map<String, String> patientInfo);

    @GET("profile/patient-base")
    Call<ApiDTO<PatientDTO>> patientProfile();

    /**
     * @param medicalRecord age	必填。出生年月。e.g.1991-01	string
     *                      gender	必填。1男2女	string
     *                      medicalRecordId	必填。病历id	string
     *                      name	必填。病历名	string
     *                      relation	必填。关系	string
     *                      address	选填。用户地址	string
     * @return
     */
    @FormUrlEncoded
    @POST("profile/medical-record")
    Call<ApiDTO<MedicalRecord>> editMedicalRecord(@FieldMap Map<String, String> medicalRecord);

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
    Call<ApiDTO<MedicalRecord>> setSelfMedicalRecord(@FieldMap Map<String, String> medicalRecord);

    /**
     * @param medicalRecord relation    	string	是	关系
     *                      name        	string	是	姓名
     *                      age    	string	是	出生年月
     *                      gender  	    int	    是	1男 2女
     *                      province	    string	是	省
     *                      city        	string	是	市
     *                      address     	string	否	详细地址
     *                      identityNumber	string	否	身份证号
     * @return
     */
    @FormUrlEncoded
    @POST("profile/other-medical-record")
    Call<ApiDTO<MedicalRecord>> setRelativeMedicalRecord(@FieldMap Map<String, String> medicalRecord);

    @GET("profile/medical-records")
    Call<ApiDTO<List<MedicalRecord>>> medicalRecordList();

    @FormUrlEncoded
    @POST("profile/money")
    Call<ApiDTO<String>> setFee(@Field("money") String money, @Field("secondMoney") String secondMoney);

    @FormUrlEncoded
    @POST("profile/set-diag-money")
    Call<ApiDTO<String>> setFee(@Field("money") String money,@Field("networkMinute")
            String networkMinute,@Field("secondMoney") String secondMoney,@Field("surfaceMinute")
            String surfaceMinute,@Field("surfaceMoney") String surfaceMoney);
    @GET("profile/doctor-base")
    Call<ApiDTO<Doctor>> doctorProfile();

    @GET("profile/doctor-index")
    Call<ApiDTO<DoctorIndex>> doctorIndex();

    @FormUrlEncoded
    @POST("profile/doctor-base")
    Call<ApiDTO<IsChanged>> editDoctorProfile(@FieldMap Map<String, String> doctor);


    @FormUrlEncoded
    @POST("profile/new-password")
    Call<ApiDTO<String>> resetPassword(@Field("password") String password,
                                       @Field("newPassword") String newPassword,
                                       @Field("confirmPassword") String confirmPassword);

    @FormUrlEncoded
    @POST("profile/new-password")
    Call<ApiDTO<String>> resetPassword(@FieldMap Map<String, String> map);


    @GET("profile/comments")
    Call<ApiDTO<PageDTO<Comment>>> comments(@Query("doctorId") int doctorId, @Query("page") String page);

    @GET("profile/articles")
    Call<ApiDTO<PageDTO<Article>>> articles(@Query("doctorId") int doctorId, @Query("page") String page);

    @GET("profile/coupons")
    Call<ApiDTO<List<Coupon>>> coupons(@Query("type") String type);

    @GET("profile/coupons")
    Call<ApiDTO<List<Coupon>>> coupons(@Query("type") String type,
                                       @Query("scope") String Scope,
                                       @Query("originalMoney") double originalMoney);

    @GET("profile/qrcode")
    Call<ApiDTO<String>> barcode();


    @GET("profile/feedbacks")
    Call<ApiDTO<PageDTO<Advice>>> advice();

    @POST("profile/feedback")
    Call<ApiDTO<String>> sendAdvice(@Query("feedback") String feedback);

    @FormUrlEncoded
    @POST("profile/doctor-tags")
    Call<ApiDTO<String>> tags(@Field("tags") String tags);


    @GET("profile/update-search-status")
    Call<ApiDTO<String>> toggleSearchable();

    @GET("profile/recommend-doctors")
    Call<ApiDTO<List<Doctor>>> recommendDoctors();

    @GET("profile/recommend-doctors")
    Call<ApiDTO<ArrayList<NewDoctor>>> newRecommendDoctors();

    @FormUrlEncoded
    @POST("profile/tutorial-record")
    Call<ApiDTO<String>> setIsShow(@Field("step") int id);


    @GET("profile/tutorial-record")
    Call<ApiDTO<List<String>>> tutorialRecord();

    @FormUrlEncoded
    @POST("profile/config")
    Call<ApiDTO<HashMap<String, String>>> config(@FieldMap HashMap<String, String> fields);

    @GET("profile/coupons")
    Call<ApiDTO<List<Coupon>>> getAppointmentCoupons(@Query("type") String aType, @Query("currentScope") String type,
                                                 @Query("distinct") boolean distinct, @Query("originalMoney") String originalMoney,
                                                 @Query("scope")String scope,@Query("thresholdThreshold") String thresholdThreshold);
}
