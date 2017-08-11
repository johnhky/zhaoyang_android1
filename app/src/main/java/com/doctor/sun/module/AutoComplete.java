package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.DrugAutoComplete;
import com.doctor.sun.entity.DrugDetail;
import com.doctor.sun.entity.DrugInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rick on 21/5/2016.
 */
public interface AutoComplete {

    @GET("list/drug-names")
    Call<ApiDTO<List<DrugAutoComplete>>> drugNames();

    @GET("list/new-drug-names")
    Call<ApiDTO<List<DrugAutoComplete>>> newDrugNames();

    @GET("list/drug-info")
    Call<ApiDTO<List<DrugInfo>>> drugInfo(@Query("keyWord") String keyword);

    @GET("list/drug-detail")
    Call<ApiDTO<List<DrugDetail>>> drugDetail();
}
