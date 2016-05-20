package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.ActivityMyQrCodeBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;
import com.doctor.sun.ui.activity.BaseActivity2;
import com.doctor.sun.ui.model.HeaderViewModel;

/**
 * Created by rick on 20/5/2016.
 */
public class MyQrCodeActivity extends BaseActivity2 {
    private ProfileModule api = Api.of(ProfileModule.class);

    private ActivityMyQrCodeBinding binding;

    public static Intent intentFor(Context context, Doctor doctor) {
        Intent intent = new Intent(context, MyQrCodeActivity.class);
        intent.putExtra(Constants.DATA, doctor);
        return intent;
    }

    public Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_qr_code);
        HeaderViewModel header = new HeaderViewModel(this);
        header.setMidTitle("我的二维码");
        header.setRightTitle("保存本地");
        binding.setHeader(header);
        binding.setData(getData());
        loadData();
    }

    public void loadData() {
        api.barcode().enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                binding.setQrcode(response);
            }
        });
    }

}
