package com.doctor.sun.ui.handler.patient;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Banner;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicineStore;
import com.doctor.sun.entity.NewDoctor;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.SystemMsgListActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.activity.patient.MyDrugOrderActivity;
import com.doctor.sun.ui.activity.patient.MyOrderActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.ui.pager.BindingPagerAdapter;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.ui.widget.AutoScrollViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kb on 21/12/2016.
 */

public class PMainHandler {
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private String visitTimeKey = LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT);
    private MedicineStore medicineStore = new MedicineStore();

    public void allDoctors(Context context) {
        Intent intent = SearchDoctorActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void myOrder(Context context) {
        Intent intent = MyOrderActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void myDrug(Context context) {
        Intent intent = MyDrugOrderActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void askForService(Context context) {
        Intent intent = MedicineStoreActivity.intentForCustomerService(context);
        context.startActivity(intent);
    }

    public void allMyMessages(Context context) {
        Intent intent = SystemMsgListActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public int getSearchDoctorBackground() {
        return R.drawable.search_doctor;
    }

    public int getMyOrderBackground() {
        return R.drawable.ic_my_order;
    }

    public int getDrugOrderBackground() {
        return R.drawable.ic_drug_order;
    }

    public int getServiceBackground() {
        return R.drawable.ic_service;
    }

    public SimpleAdapter getMessageAdapter() {
        PushModule messageApi = Api.of(PushModule.class);
        final SimpleAdapter adapter = new SimpleAdapter();
        adapter.putLong(AdapterConfigKey.LAST_VISIT_TIME, Config.getLong(visitTimeKey, System.currentTimeMillis()));
        messageApi.systemMsg("1").enqueue(new SimpleCallback<PageDTO<SystemMsg>>() {
            @Override
            protected void handleResponse(PageDTO<SystemMsg> response) {
                // 只显示两条信息
                if (response == null || response.getData() == null) {
                    return;
                }
                if (response.getData().size() > 0) {
                    adapter.insert(response.getData().get(0));
                }
                if (response.getData().size() > 1) {
                    adapter.insert(response.getData().get(1));
                }
                adapter.notifyDataSetChanged();
            }
        });
        adapter.mapLayout(R.layout.p_item_system_msg, R.layout.item_new_message);

        return adapter;
    }

    public SimpleAdapter getDoctorAdapter() {
        ProfileModule doctorApi = Api.of(ProfileModule.class);
        final SimpleAdapter adapter = new SimpleAdapter();
        doctorApi.recommendDoctors("1").enqueue(new SimpleCallback<List<Doctor>>() {
            @Override
            public void onFailure(Call<ApiDTO<List<Doctor>>> call, Throwable t) {
                super.onFailure(call, t);
            }

            @Override
            protected void handleResponse(List<Doctor> response) {
                // 由于每次到首页要重新刷新，disable掉首页的推荐医生点击跳转动画
                for (Doctor doctor : response) {
                    doctor.disableSharedTransition();
                }
                adapter.insertAll(response);
                adapter.notifyDataSetChanged();
            }

        });
        adapter.mapLayout(R.layout.item_doctor, R.layout.item_search_doctor);
        return adapter;
    }

    private HashMap<String, String> getQueryParams() {
        HashMap<String, String> hashMap = new HashMap<>();


        return hashMap;
    }

    private ArrayList<Integer> getTitleParams() {
        ArrayList<Integer> result = new ArrayList<>();


        return result;
    }

    public void showPromotion(final Context activity, final boolean fromUserAction) {
        ToolModule api = Api.of(ToolModule.class);
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(activity);
        View inflate = inflater.inflate(R.layout.dialog_view_pager, null, false);
        dialog.setContentView(inflate);
        WindowManager manager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            lp.copyFrom(window.getAttributes());
            int screenHeight = manager.getDefaultDisplay().getHeight();
            lp.width = activity.getResources().getDimensionPixelSize(R.dimen.dp_350);
            lp.height = (int) (screenHeight * 0.5);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        final AutoScrollViewPager viewPager = (AutoScrollViewPager) inflate.findViewById(R.id.vp_banner);
        final BindingPagerAdapter adapter = new BindingPagerAdapter();
        api.patientBanner().enqueue(new SimpleCallback<List<Banner>>() {
            @Override
            protected void handleResponse(List<Banner> response) {
                if (response != null && response.size() > 0) {
                    Banner banner = response.get(0);
                    if (fromUserAction || !banner.showed()) {
                        adapter.setItems(response);
                        adapter.notifyDataSetChanged();
                        viewPager.setAdapter(adapter);
                        dialog.show();
                        banner.markShowed();
                    }
                }
            }
        });
    }


    public MedicineStore getMedicineStore() {
        return medicineStore;
    }
}
