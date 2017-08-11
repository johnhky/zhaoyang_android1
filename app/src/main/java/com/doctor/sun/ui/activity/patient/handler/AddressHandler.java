package com.doctor.sun.ui.activity.patient.handler;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Address;
import com.doctor.sun.entity.DiagnosisInfo;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.patient.AddressAddFragment;
import com.doctor.sun.ui.activity.patient.AddressManagerActivity;
import com.doctor.sun.ui.widget.TwoChoiceDialog;
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

    public static void deleteAddress(final Context context, final int addressId) {
        TwoChoiceDialog.show(context, "确定删除该地址吗?", "取消", "确定", new TwoChoiceDialog.Options() {
            @Override
            public void onApplyClick(final MaterialDialog dialog) {
                LoadingHelper.showMaterLoading(context, "正在删除...");
                api.deleteAddress(addressId).enqueue(new Callback<ApiDTO<String>>() {
                    @Override
                    public void onResponse(Call<ApiDTO<String>> call, Response<ApiDTO<String>> response) {
                        if (response.body().getStatus().equals("200")) {
                            Intent intent = new Intent();
                            intent.setAction("delete");
                            context.sendBroadcast(intent);
                            LoadingHelper.hideMaterLoading();
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiDTO<String>> call, Throwable t) {
                        LoadingHelper.hideMaterLoading();
                        dialog.dismiss();
                    }
                });

            }

            @Override
            public void onCancelClick(MaterialDialog dialog) {
                dialog.dismiss();
            }
        });

    }
    public static void UpdateMockAddress(final Context context, Address address){
        if (address.getDefaults().equals("1")){
            api.updateMockAddress(address.getId()+"","0").enqueue(new Callback<ApiDTO>() {
                @Override
                public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                    Intent intent = new Intent();
                    intent.setAction("addAddress");
                    context.sendBroadcast(intent);
                }

                @Override
                public void onFailure(Call<ApiDTO> call, Throwable t) {

                }
            });

        }else{
            api.updateMockAddress(address.getId()+"","1").enqueue(new Callback<ApiDTO>() {
                @Override
                public void onResponse(Call<ApiDTO> call, Response<ApiDTO> response) {
                    Intent intent = new Intent();
                    intent.setAction("addAddress");
                    context.sendBroadcast(intent);
                }

                @Override
                public void onFailure(Call<ApiDTO> call, Throwable t) {

                }
            });
        }
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
