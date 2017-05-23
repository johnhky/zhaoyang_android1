package com.doctor.sun.ui.activity.patient.handler;


import android.content.Context;
import android.content.Intent;

import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Address;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.patient.AddressAddFragment;
import com.doctor.sun.ui.activity.patient.AddressManagerActivity;
import com.doctor.sun.util.PreferenceHelper;

import io.ganguo.library.common.LoadingHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by heky on 17/4/26.
 */

public class AddressHandler {

    static ProfileModule api = Api.of(ProfileModule.class);

    public static void deleteAddress(final Context context, int addressId) {
        LoadingHelper.showMaterLoading(context, "正在删除...");
        api.deleteAddress(addressId).enqueue(new Callback<ApiDTO<String>>() {
            @Override
            public void onResponse(Call<ApiDTO<String>> call, Response<ApiDTO<String>> response) {
                if (response.body().getStatus().equals("200")) {
                    Intent intent = new Intent();
                    intent.setAction("delete");
                    context.sendBroadcast(intent);
                    LoadingHelper.hideMaterLoading();
                }
            }

            @Override
            public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                LoadingHelper.hideMaterLoading();
            }
        });

    }

    public static void updateAddress(Context context, Address address) {
        Intent toUpdate = SingleFragmentActivity.intentFor(context, "修改收货地址", AddressAddFragment.update(address));
        context.startActivity(toUpdate);
    }

    public static void showChooseAddress(Context context, String id) {
        Intent toChoose = new Intent();
        toChoose.setClass(context, AddressManagerActivity.class);
        toChoose.putExtra(Constants.MOCK,"UPDATE");
        PreferenceHelper.wirteString(context,Constants.MOCK, id);
        context.startActivity(toChoose);
    }

    public static void showUpdateAddress(final Context context, final Address address) {
        if (PreferenceHelper.readString(context,Constants.MOCK)==null||PreferenceHelper.readString(context,Constants.MOCK).equals("")){
            return;
        }else{
            api.updateOrderAddress(address.getProvince() + "-" + address.getCity() + "-" + address.getArea() + "-" + address.getAddress(),
                    PreferenceHelper.readString(context,Constants.MOCK), address.getPhone(), address.getRemark(), address.getTo())
                    .enqueue(new SimpleCallback<Address>() {
                        @Override
                        protected void handleResponse(Address response) {
                            Intent intent = new Intent();
                            intent.setAction("updateSuccess");
                            context.sendBroadcast(intent);
                        }
                    });
        }

    }

}
