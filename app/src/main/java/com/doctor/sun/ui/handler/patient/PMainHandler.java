package com.doctor.sun.ui.handler.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Banner;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.SystemMsg;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.module.PushModule;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.SingleFragmentActivity;
import com.doctor.sun.ui.activity.SystemMsgListActivity;
import com.doctor.sun.ui.activity.patient.MedicineStoreActivity;
import com.doctor.sun.ui.activity.patient.MyOrderActivity;
import com.doctor.sun.ui.activity.patient.SearchDoctorActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.core.AdapterConfigKey;
import com.doctor.sun.ui.fragment.DrugListFragment;
import com.doctor.sun.ui.pager.BindingPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.ui.widget.AutoScrollViewPager;

/**
 * Created by kb on 21/12/2016.
 */

public class PMainHandler {
    public static final String LAST_VISIT_TIME = "LAST_VISIT_TIME";
    private String visitTimeKey = LAST_VISIT_TIME + Config.getString(Constants.VOIP_ACCOUNT);

    public void allDoctors(Context context) {
        Intent intent = SearchDoctorActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void myOrder(Context context) {
        Intent intent = MyOrderActivity.makeIntent(context);
        context.startActivity(intent);
    }

    public void myDrug(Context context) {
        Bundle bundle = DrugListFragment.getArgs();
        Intent intent = SingleFragmentActivity.intentFor(context, "寄药订单", bundle);
        context.startActivity(intent);
    }

    public void askForService(Context context) {
        Intent intent = MedicineStoreActivity.makeIntent(context);
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
        doctorApi.recommendDoctors().enqueue(new SimpleCallback<List<Doctor>>() {
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

    public void showDialog(Context context) {
        ToolModule api = Api.of(ToolModule.class);
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_view_pager, false)
                .build();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        if (window != null) {
            lp.copyFrom(window.getAttributes());
            lp.width = context.getResources().getDimensionPixelSize(R.dimen.dp_390);
            lp.height = context.getResources().getDimensionPixelSize(R.dimen.dp_480);
            window.setAttributes(lp);
        }

        final AutoScrollViewPager viewPager = (AutoScrollViewPager) dialog.getCustomView().findViewById(R.id.vp_banner);
        final BindingPagerAdapter adapter = new BindingPagerAdapter();
        api.patientBanner().enqueue(new SimpleCallback<List<Banner>>() {
            @Override
            protected void handleResponse(List<Banner> response) {
                adapter.setItems(response);
                adapter.notifyDataSetChanged();

                viewPager.setAdapter(adapter);
                dialog.show();
            }
        });
    }
}
