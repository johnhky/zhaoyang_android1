package com.doctor.sun.ui.activity.patient;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.bean.Constants;
import com.doctor.sun.databinding.PActivityHospitalBinding;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Hospital;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.ApiCallback;
import com.doctor.sun.module.ToolModule;
import com.doctor.sun.ui.activity.BaseFragmentActivity2;


/**
 * Created by lucas on 1/26/16.
 */
public class HospitalDetailActivity extends BaseFragmentActivity2 implements View.OnClickListener {
    private PActivityHospitalBinding binding;
    private ToolModule api = Api.of(ToolModule.class);
    private Hospital data;

    public static Intent makeIntent(Context context, Doctor data) {
        Intent i = new Intent(context, HospitalDetailActivity.class);
        i.putExtra(Constants.DATA, data);
        return i;
    }


    private Doctor getData() {
        return getIntent().getParcelableExtra(Constants.DATA);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.p_activity_hospital);
        api.hospitalInfo(getData().getHospitalId()).enqueue(new ApiCallback<Hospital>() {
            @Override
            protected void handleResponse(Hospital response) {
                binding.setData(response);
                data = response;
            }
        });
        binding.tvLocate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_locate:
                Uri mUri = Uri.parse("geo:"+data.getLatitude()+","+data.getLongitude());
                Intent intent = new Intent(Intent.ACTION_VIEW,mUri);
                startActivity(intent);
                break;
        }
    }

    @Override
    public int getMidTitle() {
        return R.string.title_hospital_detail;
    }
}