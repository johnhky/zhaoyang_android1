package com.doctor.sun.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.DialogSelectBinding;
import com.doctor.sun.entity.AppointMent;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.event.CloseDialogEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.AuthModule;
import com.doctor.sun.module.ImModule;
import com.doctor.sun.ui.adapter.RecordAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

import io.ganguo.library.Config;
import io.ganguo.library.core.drawable.MaterialProgressDrawable;
import io.ganguo.library.ui.dialog.BaseDialog;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;
import retrofit.Call;

/**
 * 选择病历列表
 * Created by Lynn on 12/30/15.
 */
public class SelectDialog extends BaseDialog implements View.OnClickListener {
    private Logger logger = LoggerFactory.getLogger(SelectDialog.class);
    private DialogSelectBinding binding;
    private ImModule api = Api.of(ImModule.class);
    private RecordAdapter mAdapter;
    private Context context;
    private MaterialProgressDrawable mFooterProgress;
    private boolean isHasContent;
    private AppointMent appointment;

    /**
     * patientId - 医生端
     * doctorId - 公众端
     */
    private int id;

    public SelectDialog(Context context, int id) {
        super(context);
        this.context = context;
        this.id = id;
    }

    public SelectDialog(Context context, AppointMent appointment) {
        this(context, appointment.getDoctor().getId());
        this.appointment = appointment;
    }

    @Override
    public void beforeInitView() {
        binding = DialogSelectBinding.inflate(LayoutInflater.from(context));
        setContentView(binding.getRoot());
    }

    @Override
    public void initView() {
        initProgressView();
        initLoadingView();
    }

    @Override
    public void initListener() {
        binding.tvCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (appointment == null) {
            mAdapter = new RecordAdapter(context);
        } else {
            mAdapter = new RecordAdapter(context, appointment);
        }
        AutoHeightLayoutManager layoutManager = new AutoHeightLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rvSelect.setLayoutManager(layoutManager);
        binding.rvSelect.setAdapter(mAdapter);

        isHasContent = false;
        getRecords();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                this.dismiss();
                break;
        }
    }

    private void getRecords() {
        Call<ApiDTO<List<MedicalRecord>>> call;
        if (Config.getInt(Constants.USER_TYPE, -1) == AuthModule.PATIENT_TYPE) {
            //病人端
            call = api.recordDoctor(id);
        } else {
            call = api.record(id);
        }
        call.enqueue(new ApiCallback<List<MedicalRecord>>() {
            @Override
            protected void handleResponse(List<MedicalRecord> response) {
                mAdapter.clear();
                mAdapter.addAll(response);
                mAdapter.notifyDataSetChanged();

                if (response.size() != 0) {
                    isHasContent = true;
                }
                showContentView();
            }

            @Override
            protected void handleApi(ApiDTO<List<MedicalRecord>> body) {
                super.handleApi(body);
                showContentView();

            }
        });
    }

    public static void showSelectDialog(Context context, final int id) {
        SelectDialog dialog = new SelectDialog(context, id);
        dialog.show();
    }

    public static void showSelectDialog(Context context, AppointMent appointment) {
        SelectDialog dialog = new SelectDialog(context, appointment);
        dialog.show();
    }

    private void initLoadingView() {
        //显示loading
        binding.ivLoading.setVisibility(View.VISIBLE);
        binding.ivLoading.setImageDrawable(mFooterProgress);
        mFooterProgress.start();

        binding.tvCancel.setVisibility(View.INVISIBLE);
        binding.rvSelect.setVisibility(View.INVISIBLE);
    }

    private void initProgressView() {
        mFooterProgress = new MaterialProgressDrawable(getContext(), binding.getRoot());
        mFooterProgress.setAlpha(255);
        mFooterProgress.setBackgroundColor(Color.TRANSPARENT);
        Resources resources = getContext().getResources();
        int color = resources.getColor(R.color.colorPrimaryDark);
        int blue = resources.getColor(R.color.colorPrimaryDark);
        int green = resources.getColor(R.color.colorPrimaryDark);
        mFooterProgress.setColorSchemeColors(color, blue, green);
    }

    private void showContentView() {
        binding.getRoot().setBackgroundResource(R.drawable.shape_dialog_bg);
        binding.ivLoading.setVisibility(View.GONE);
        binding.tvCancel.setVisibility(View.VISIBLE);
        mAdapter.onFinishLoadMore(true);

        if (mFooterProgress != null) {
            mFooterProgress.stop();
            binding.ivLoading.setImageDrawable(null);
        }

        if (isHasContent) {
            binding.rvSelect.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void onCloseDialog(CloseDialogEvent event) {
        if (event.isClose()) {
            dismiss();
        }
    }
}
