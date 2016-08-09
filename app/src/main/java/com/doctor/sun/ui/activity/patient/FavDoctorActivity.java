package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.doctor.sun.R;
import com.doctor.sun.databinding.PActivityFavDoctorBinding;
import com.doctor.sun.dto.PageDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.AppointmentModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;
import com.doctor.sun.ui.adapter.DocumentAdapter;
import com.doctor.sun.ui.model.HeaderViewModel;
import com.doctor.sun.ui.widget.TwoChoiceDialog;

import java.util.ArrayList;

/**
 * Created by lucas on 1/4/16.
 * 我收藏的医生
 */
public class FavDoctorActivity extends BaseFragmentActivity2 implements DocumentAdapter.GetEditMode {
    private boolean isEditMode;
    private DocumentAdapter mAdapter;
    private PActivityFavDoctorBinding binding;
    private AppointmentModule api = Api.of(AppointmentModule.class);

    public boolean isEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    @Override
    public boolean getEditMode() {
        return isEditMode();
    }

    public static Intent makeIntent(Context context) {
        Intent i = new Intent(context, FavDoctorActivity.class);
        return i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_fav_doctor);
//        binding.setHeader(header);
        mAdapter = new DocumentAdapter(this);
        binding.rvDocument.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDocument.setAdapter(mAdapter);
    }

    private void initListener() {
        binding.flDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoChoiceDialog.show(FavDoctorActivity.this, "确定要删除对该医生的收藏？", "取消", "删除", new TwoChoiceDialog.Options() {
                    @Override
                    public void onApplyClick(final MaterialDialog dialog) {
                        for (int i = 0; i < getDoctorId().size(); i++) {
                            api.unlikeDoctor(getDoctorId().get(i)).enqueue(new SimpleCallback<String>() {
                                @Override
                                protected void handleResponse(String response) {
                                    dialog.dismiss();
                                    loadData();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelClick(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        api.favoriteDoctors().enqueue(new ApiCallback<PageDTO<Doctor>>() {
            @Override
            protected void handleResponse(PageDTO<Doctor> response) {
                mAdapter.unSelectAll();
                mAdapter.clear();
                mAdapter.addAll(response.getData());
                mAdapter.notifyDataSetChanged();
                mAdapter.onFinishLoadMore(true);
                if (mAdapter.getItemCount() == 0) {
                    binding.flDelete.setVisibility(View.GONE);
                    binding.emptyIndicator.setText("当前我的收藏为空");
                    binding.emptyIndicator.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyIndicator.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isEditMode() && mAdapter.getItemCount() != 0) {
            setIsEditMode(!isEditMode());
            binding.flDelete.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        }else {
            super.onBackPressed();
        }
    }

    public void onMenuClicked() {
        setIsEditMode(!isEditMode());
        if (isEditMode()) {
            binding.flDelete.setVisibility(View.VISIBLE);
        } else {
            binding.flDelete.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<String> getDoctorId() {
        ArrayList<String> doctorId = new ArrayList<String>();
        doctorId.clear();
        for (int i = 0; i < mAdapter.size(); i++) {
            if (mAdapter.isSelected(i)) {
                Doctor doctor = (Doctor) mAdapter.get(i);
                doctorId.add(String.valueOf(doctor.getId()));
            }
        }
        return doctorId;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                onMenuClicked();
                return true;
            }
            case R.id.action_confirm: {
                onMenuClicked();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (isEditMode()) {
            getMenuInflater().inflate(R.menu.menu_confirm, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public int getMidTitle() {
        return R.string.title_favorite_doctor;
    }
}
