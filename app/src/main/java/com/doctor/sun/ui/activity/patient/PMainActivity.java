package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityMainBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Patient;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ListCallback;
import com.doctor.sun.http.callback.TokenCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.activity.patient.handler.MainActivityHandler;
import com.doctor.sun.ui.adapter.SearchDoctorAdapter;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.ui.adapter.core.LoadMoreListener;
import com.doctor.sun.ui.model.FooterViewModel;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.model.PatientFooterView;
import com.doctor.sun.ui.widget.AddMedicalRecordDialog;
import com.doctor.sun.util.PermissionUtil;
import com.doctor.sun.util.ShowCaseUtil;
import com.doctor.sun.util.UpdateUtil;
import com.squareup.otto.Subscribe;

import io.ganguo.library.util.Tasks;


/**
 * Created by rick on 10/23/15.
 */
public class PMainActivity extends BaseActivity2 implements SwipeRefreshLayout.OnRefreshListener {

    private PActivityMainBinding binding;
    private SimpleAdapter mAdapter;
    private ToolModule api = Api.of(ToolModule.class);
    private ListCallback<Doctor> callback;

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, PMainActivity2.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_main);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("昭阳医生");
        binding.setHeader(header);
        FooterViewModel footer = getFooter();
        binding.setFooter(footer);

        final LinearLayoutManager layout = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(layout);
        mAdapter = createAdapter();
        callback = new ListCallback<Doctor>(mAdapter) {
            @Override
            public void onFinishRefresh() {
                super.onFinishRefresh();
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showShowCase();
                    }
                }, 250);
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.refreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        };
        binding.recyclerView.setAdapter(mAdapter);

        mAdapter.setLoadMoreListener(new LoadMoreListener() {
            @Override
            protected void onLoadMore() {
                loadMore();
            }
        });
        Patient patientProfile = TokenCallback.getPatientProfile();
        if (patientProfile == null || "".equals(patientProfile.getName())) {
            new AddMedicalRecordDialog(this, true).show();
        }

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstCompletelyVisibleItemPosition = layout.findFirstCompletelyVisibleItemPosition();

                if (firstCompletelyVisibleItemPosition > 1) {
                    binding.llyLabelHeader.setVisibility(View.VISIBLE);
                } else {
                    binding.llyLabelHeader.setVisibility(View.GONE);
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimaryDark));
        binding.refreshLayout.setOnRefreshListener(this);
    }

    private FooterViewModel getFooter() {
        PatientFooterView mView = new PatientFooterView(this);
        return FooterViewModel.getInstance(mView, R.id.tab_one);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (shouldCheck()) {
            UpdateUtil.checkUpdate(this);
        }
    }

    protected void loadMore() {
        binding.refreshLayout.setRefreshing(true);
        mAdapter.clear();
        mAdapter.add(new MainActivityHandler(this));
        mAdapter.add(new LayoutId() {
            @Override
            public int getItemLayoutId() {
                return R.layout.header_recommand_doctor;
            }
        });
        api.recommendDoctor().enqueue(callback);
    }

    protected SimpleAdapter getAdapter() {
        return mAdapter;
    }

    @NonNull
    protected SimpleAdapter createAdapter() {
        SimpleAdapter simpleAdapter = new SearchDoctorAdapter(this, AppointmentType.DETAIL);
        simpleAdapter.mapLayout(R.layout.item_doctor, R.layout.item_recommand_doctor);
        return simpleAdapter;
    }

    @Override
    public void onStart() {
        super.onStart();
        getRealm().addChangeListener(getFooter());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UpdateUtil.STORE_REQUEST) {
            boolean isGranted = PermissionUtil.verifyPermissions(grantResults);
            if (isGranted) {
                if (shouldCheck()) {
                    UpdateUtil.checkUpdate(this);
                }
            }
        }
    }

    @Subscribe
    public void onShowCaseFinished(ShowCaseFinishedEvent e) {
        if (getIntent().getBooleanExtra(Constants.IS_SHOWCASE, false)) {
            if (e.id.equals(TAG)) {
                finish();
            }
        }
    }

    public void showShowCase() {
        if (ShowCaseUtil.isShow(TAG)) {
            return;
        }
        if (!AppContext.isDoctor()) {
            View view1 = binding.recyclerView.findViewById(R.id.tv_sync_consultation);
            View view2 = binding.recyclerView.findViewById(R.id.tv_async_consultation);
            View view3 = binding.recyclerView.findViewById(R.id.tv_after_service);
            View view4 = binding.recyclerView.findViewById(R.id.lly_recent_appointment);
            View view5 = binding.includeFooter.flTabTwo;


            ShowCaseUtil.showCase(view1, "点击这里可以找到适合您的医生", TAG, 5, 0, false);
            ShowCaseUtil.showCase(view2, "点击这里预约您咨询过的医生进行便捷复诊和寄药", TAG, 5, 1, false);
            ShowCaseUtil.showCase(view3, "点击这里向随访医生反馈诊后/出院后病情恢复情况", TAG, 5, 2, false);
            ShowCaseUtil.showCase(view4, "点这里可以查询所有类型的订单", TAG, 5, 3, true);
            ShowCaseUtil.showCase(view5, "您可以在这里与医生通过文字信息或者电话进行沟通", TAG, 5, 4, false);
        }
    }


    @Override
    public void onRefresh() {
        binding.refreshLayout.setRefreshing(false);
    }
}
