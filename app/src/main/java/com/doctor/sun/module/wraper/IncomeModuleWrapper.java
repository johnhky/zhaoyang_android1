package com.doctor.sun.module.wraper;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.BillDetail;
import com.doctor.sun.entity.InComeOverView;
import com.doctor.sun.entity.SubsidyDetail;
import com.doctor.sun.event.ConfigChangedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Appointment;
import com.doctor.sun.module.IncomeModule;
import com.doctor.sun.util.JacksonUtils;
import com.google.common.base.Strings;

import java.util.ArrayList;

import io.ganguo.library.Config;
import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;

/**
 * Created by rick on 15/2/2017.
 * 账单api wrapper 把部分api请求的返回结果保存到到本地
 */
public class IncomeModuleWrapper {

    private static IncomeModuleWrapper instance;

    public static IncomeModuleWrapper getInstance() {
        if (instance == null) {
            instance = new IncomeModuleWrapper();
        }
        return instance;
    }

    private IncomeModule api = Api.of(IncomeModule.class);

    public Call<ApiDTO<ArrayList<String>>> monthList() {
        return api.monthList();
    }

    public Call<ApiDTO<PageDTO<Appointment>>> appointmentList(String time, int type, String page) {
        return api.appointmentList(time, type, page);
    }

    public void refreshIncomeOverView() {
        api.overview().enqueue(new SimpleCallback<InComeOverView>() {
            @Override
            protected void handleResponse(InComeOverView response) {
                Config.putString(Constants.INCOME_OVERVIEW, JacksonUtils.toJson(response));
                EventHub.post(new ConfigChangedEvent(Constants.INCOME_OVERVIEW));
            }
        });
    }

    public InComeOverView getIncomeOverView() {
        String string = Config.getString(Constants.INCOME_OVERVIEW);
        if (!Strings.isNullOrEmpty(string)) {
            return JacksonUtils.fromJson(string, InComeOverView.class);
        } else {
            return new InComeOverView();
        }
    }

    //补贴
    public void refreshSubsidy(final String time) {
        api.subsidy(time).enqueue(new SimpleCallback<SubsidyDetail>() {
            @Override
            protected void handleResponse(SubsidyDetail response) {
                String key = time + Constants.SUBSIDY;
                Config.putString(key, JacksonUtils.toJson(response));
                EventHub.post(new ConfigChangedEvent(key));
            }
        });
    }

    public SubsidyDetail getSubsidy(String time) {
        String string = Config.getString(time + Constants.SUBSIDY);
        if (!Strings.isNullOrEmpty(string)) {
            return JacksonUtils.fromJson(string, SubsidyDetail.class);
        } else {
            return new SubsidyDetail();
       }
    }

    public void refreshBillDetail(final String time) {
        api.bill(time).enqueue(new SimpleCallback<BillDetail>() {
            @Override
            protected void handleResponse(BillDetail response) {
                String key = time + Constants.BILL_DETAIL;
                Config.putString(key, JacksonUtils.toJson(response));
                EventHub.post(new ConfigChangedEvent(key));
            }
        });
    }

    public BillDetail getBillDetail(String time) {
        String string = Config.getString(time + Constants.BILL_DETAIL);
        if (!Strings.isNullOrEmpty(string)) {
            return JacksonUtils.fromJson(string, BillDetail.class);
        } else {
            return new BillDetail();
        }
    }
}

