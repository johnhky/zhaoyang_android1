package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.BillDetail;
import com.doctor.sun.entity.InComeOverView;
import com.doctor.sun.entity.SubsidyDetail;
import com.doctor.sun.immutables.Appointment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rick on 15/2/2017.
 */

public interface IncomeModule {


    @GET("income/month-list")
    Call<ApiDTO<ArrayList<String>>> monthList();

    @GET("income/appointment-list")
    Call<ApiDTO<PageDTO<Appointment>>> appointmentList(@Query("time") String time, @Query("type") int type, @Query("page") String page);

    @GET("income/overview")
    Call<ApiDTO<InComeOverView>> overview();

    @GET("income/subsidy-detail")
    Call<ApiDTO<SubsidyDetail>> subsidy(@Query("time") String time);

    @GET("income/bill")
    Call<ApiDTO<BillDetail>> bill(@Query("time") String time);

}
