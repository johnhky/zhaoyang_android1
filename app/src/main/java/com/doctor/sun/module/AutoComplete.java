package com.doctor.sun.module;

import com.doctor.sun.dto.ApiDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by rick on 21/5/2016.
 */
public interface AutoComplete {

    @GET("list/drug-names")
    Call<ApiDTO<List<String>>> drugNames();

}
